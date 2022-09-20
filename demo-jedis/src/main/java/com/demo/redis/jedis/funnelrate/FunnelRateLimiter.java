package com.demo.redis.jedis.funnelrate;

import java.util.HashMap;
import java.util.Map;

/**
 * 漏斗限流
 * 本方法有原子性问题
 * 但是有个问题，我们无法保证整个过程的原子性。从 hash 结构中取值，然后在内存里
 * 运算，再回填到 hash 结构，这三个过程无法原子化，意味着需要进行适当的加锁控制。而
 * 一旦加锁，就意味着会有加锁失败，加锁失败就需要选择重试或者放弃。
 * 如果重试的话，就会导致性能下降。如果放弃的话，就会影响用户体验。同时，代码的
 * 复杂度也跟着升高很多。这真是个艰难的选择，我们该如何解决这个问题呢？Redis-Cell 救
 * 星来了！
 */
public class FunnelRateLimiter {
    //内部类
    static class Funnel{
        int capacity; //漏斗容量
        float leakingRate;
        int leftQuota;
        long leakingTs;

        //构造函数
        public Funnel(int capacity, float leakingRate) {
            this.capacity = capacity;
            this.leakingRate = leakingRate;
            this.leftQuota = capacity;
            this.leakingTs = System.currentTimeMillis();
        }
        void makeSpace(){
            long nowTs = System.currentTimeMillis();
            long deltaTs = nowTs - leakingTs; //距离上一次漏出过了多长时间
            int deltaQuota =(int)(deltaTs * leakingRate);
            if (deltaQuota < 0){ //距离上一次漏出等待的时间过长，导致deltaQuota整型数值过大，引起了范围溢出
                this.leftQuota = capacity;
                this.leakingTs = nowTs;
                return;
            }
            if (deltaQuota < 1){ //腾出的空间太小，不够1个，最小的单位是1
                return;
            }
            this.leftQuota += deltaQuota; //上一次剩的加这次漏出的
            this.leakingTs = nowTs;
            if (this.leftQuota > this.capacity){//如果漏出超过最大容量，则剩余的容量为最大容量
                this.leftQuota = this.capacity;
            }
        }

        /**
         * 灌水
         * @param quota
         * @return
         */
        boolean watering(int quota){
            makeSpace();
            if (this.leftQuota >= quota){
                this.leftQuota -= quota;
                return true;
            }
            return false;
        }
    }
    //定义容器存放所有的桶
    private Map<String, Funnel> funnels = new HashMap<String, Funnel>();

    public boolean isActionAllowed(String userId, String actionKey, int capacity, float leakingRate){
        String key = String.format("%s:%s", userId, actionKey);
        Funnel funnel = funnels.get(key);
        if (null == funnel){
            funnel = new Funnel(capacity, leakingRate);
            funnels.put(key, funnel);
        }
        return funnel.watering(1); //灌水,需要 1 个 quota
    }

}
