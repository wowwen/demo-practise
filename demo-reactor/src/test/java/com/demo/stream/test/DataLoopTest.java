package com.demo.stream.test;

/**
 *
 * @FileName: DataLoopTest
 * @Author: jiangyw8
 * @Date: 2020-10-9 8:29
 * @Description: 可以看到在百万数据下做简单数据循环处理，对于普通for(for\foreach)循环或stream(并行、非并行)下，几者的效率差异并不明显，
 * 注意: 在百万数据下，普通for、foreach循环处理可能比stream的方式快许多，对于这点效率的损耗，其实lambda表达式对代码的简化更大！
 * 另外,在并行流的循环下速度提升了一倍之多，当单个循环耗时较多时，会拉大与前几者的循环效率 (以上测试仅对于循环而言，其他类型业务处理,比如排序、求和、最大值等未做测试，个人猜测与以上测试结果相似)
 */

import lombok.Data;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Stream(流)的效率
 * 模拟非耗时简单业务逻辑
 */
public class DataLoopTest {
    private static final Logger LOG = LoggerFactory.getLogger(DataLoopTest.class);
    private static final List<Staff> staffs = new ArrayList<>();

    //此处为静态代码块，Person类作为StreamPractice的内部类，是非静态的，不能在此处new出来，故重新定义一个Staff类
    static {
        for (int i = 0; i <= 1000000; i++) {
            Staff staff = new Staff();
            staff.setAge(i);
            staff.setName("张三");
            staff.setJoinDate(new Date());

            staffs.add(staff);
        }
    }

    /**
     * for 循环耗时 ===> 1.221
     * for 循环耗时 ===> 1.234
     * for 循环耗时 ===> 1.202
     */

    @Test
    public void forTest() {
        Instant date_start = Instant.now();
        int size = staffs.size();
        for (int i = 0; i < size; i++) {
            staffs.get(i).setLabel(staffs.get(i).getName().concat("-" + staffs.get(i).getAge())
                                                          .concat("-" + staffs.get(i).getJoinDate().getTime()));
        }
        Instant date_end = Instant.now();
        LOG.info("for 循环耗时 ===> {}", Duration.between(date_start,date_end).toMillis()/1000.0);
    }

    /**
     * for 循环耗时 ===> 1.237
     * for 循环耗时 ===> 1.213
     * for 循环耗时 ===> 1.367
     */
    @Test
    public void forEachTest(){
        Instant date_start = Instant.now();
        for (Staff staff : staffs) {
            staff.setLabel(staff.getName().concat("-" + staff.getAge()).concat("-" + staff.getJoinDate().getTime()));
        }
        Instant date_end = Instant.now();
        LOG.info("for 循环耗时 ===> {}", Duration.between(date_start,date_end).toMillis()/1000.0);
    }

    /**
     * streamForeach 循环耗时 ===> 1.237
     * streamForeach 循环耗时 ===> 1.359
     * streamForeach 循环耗时 ===> 1.263
     */
    @Test
    public void streamForEach(){
        Instant date_start  = Instant.now();
        staffs.stream().forEach(staff -> {
            staff.setLabel(staff.getName().concat("-" + staff.getAge())
                    .concat("-" + staff.getJoinDate().getTime()));
        });
        Instant date_end = Instant.now();
        LOG.info("streamForeach 循环耗时 ===> {}", Duration.between(date_start,date_end).toMillis()/1000.0);
    }

    /**
     * parallelStreamForeach 循环耗时 ===> 1.014
     * parallelStreamForeach 循环耗时 ===> 1.126
     * parallelStreamForeach 循环耗时 ===> 1.128
     * 并行方式parallelStream
     * 串行方式Stream
     *
     */
    @Test
    public void parallelStreamForEach(){
        Instant date_start = Instant.now();
        staffs.parallelStream().forEach(staff -> {
            staff.setLabel(staff.getName().concat("-" + staff.getAge())
                    .concat("-" + staff.getJoinDate().getTime()));
        });
        Instant date_end = Instant.now();
        LOG.info("parallelStreamForeach 循环耗时 ===> {}", Duration.between(date_start,date_end).toMillis()/1000.0);
    }

    /**
     * 模拟耗时简单业务逻辑
     */
    @Test
    public void forTestBlock(){
        Instant date_start = Instant.now();
        int size = staffs.size();
        for (int i = 0; i < size; i++) {
            try {
                Thread.sleep(1);
                staffs.get(i).setLabel(staffs.get(i).getName().concat("-" + staffs.get(i).getAge())
                    .concat("-" + staffs.get(i).getJoinDate().getTime()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Instant date_end = Instant.now();
        LOG.info("forBlock 循环耗时 ===> {}", Duration.between(date_start,date_end).toMillis()/1000.0);
    }

    @Test
    public void forEachTestBlock(){
        Instant date_start = Instant.now();
        for (Staff staff : staffs) {
            try {
                Thread.sleep(1);
                staff.setLabel(staff.getName().concat("-" + staff.getAge()).concat("-" + staff.getJoinDate().getTime()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Instant date_end = Instant.now();
        LOG.info("forEachBlock 循环耗时 ===> {}", Duration.between(date_start,date_end).toMillis()/1000.0);
    }

    @Test
    public void streamForEachTestBlock(){
        Instant date_start  = Instant.now();
        staffs.stream().forEach(staff -> {
            try {
                Thread.sleep(1);
                staff.setLabel(staff.getName().concat("-" + staff.getAge())
                        .concat("-" + staff.getJoinDate().getTime()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Instant date_end = Instant.now();
        LOG.info("streamForeachBlock 循环耗时 ===> {}", Duration.between(date_start,date_end).toMillis()/1000.0);
    }

    @Test
    public void parallelStreamForeachTestBlock(){
        Instant date_start = Instant.now();
        staffs.parallelStream().forEach(staff -> {
            try {
                Thread.sleep(1);
                staff.setLabel(staff.getName().concat("-" + staff.getAge())
                        .concat("-" + staff.getJoinDate().getTime()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Instant date_end = Instant.now();
        LOG.info("parallelStreamForeachBlock 循环耗时 ===> {}", Duration.between(date_start,date_end).toMillis()/1000.0);
    }

    @Data
    public static class Staff {
        private String name;
        private int age;
        private Date joinDate;
        private String label;
    }

}
