package com.demo.designmodel.strategymodel.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @FileName: StrategyFactory
 * @Author: jiangyw8
 * @Date: 2020-11-27 20:23
 * @Description:
 * 静态内部类单例，单例模式实现的一种，不是本文重点，如不了解，可以自行 google
 * 我们再着手创建一个 StrategyFactory 工厂类。StrategyFactory 这里我使用的是静态内部类单例，在构造方法的时候，初始化好 需要的 Strategy，并把 list 转化为 map。
 * 这里 转化就是“灵魂”所在
 */
public class StrategyFactory {

    private Map<Integer, StrategyV2> map;

    public StrategyFactory(){
        List<StrategyV2> strategies  = new ArrayList<>();

        strategies.add(new OrdinaryStrategyV2());
        strategies.add(new SilverStrategyV2());
        strategies.add(new GoldStrategyV2());
        strategies.add(new PlatinumStrategyV2());

        map = strategies.stream().collect(Collectors.toMap(StrategyV2::getType, strategyV2 -> strategyV2 ));

        //等同于上面的java8写法
//        map = new HashMap<>();
//        for (StrategyV2 strategy : strategies) {
//            map.put(strategy.getType(), strategy);
//        }
    }

    public static class Holder{
        public static StrategyFactory instance = new StrategyFactory();
    }

    public static StrategyFactory getInstance(){
        return Holder.instance;
    }

    public StrategyV2 get(Integer type) {
        return map.get(type);
    }
}
