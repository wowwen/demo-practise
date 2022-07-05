package com.example.demo.designmodel.strategymodel;

import com.example.demo.designmodel.strategymodel.v1.*;
import com.example.demo.designmodel.strategymodel.v2.StrategyFactory;
import com.example.demo.designmodel.strategymodel.v2.StrategyV2;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ©Copyright ©1968-2020 Midea Group,IT
 *
 * @FileName: StrategyTest
 * @Author: jiangyw8
 * @Date: 2020-11-26 16:25
 * @Description: 策略模式优化if-else
 */
//假设有这么一个需求：
//
//        一个电商系统，当用户消费满1000 金额，可以根据用户VIP等级，享受打折优惠。根据用户VIP等级，计算出用户最终的费用。
//
//        普通会员 不打折
//        白银会员 优惠50元
//        黄金会员 8折
//        白金会员 优惠50元，再打7折
//-------------------- 现实中的 if-else  --------------------
//
//        if (money >= 1000) {
//        if (type == UserType.SILVER_VIP.getCode()) {
//
//        System.out.println("白银会员 优惠50元");
//        result = money - 50;
//        } else if (type == UserType.GOLD_VIP.getCode()) {
//
//        System.out.println("黄金会员 8折");
//        result = money * 0.8;
//        } else if (type == UserType.PLATINUM_VIP.getCode()) {
//
//        System.out.println("白金会员 优惠50元，再打7折");
//        result = (money - 50) * 0.7;
//        } else {
//        System.out.println("普通会员 不打折");
//        result = money;
//        }
//        }


public class StrategyTest {
//    public static void main(String[] args) {
//        String aa = "A00003";
//        String[] s = aa.split("_");
//        System.out.println(s[0]);
//        System.out.println(s[1]);
//    }

    public static void main(String[] args) {


        Long timeStamp = System.currentTimeMillis();
        System.out.println(timeStamp);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String format = sdf.format(timeStamp);
        System.out.println("格式化结果：" + format);
        String sd = sdf.format(new Date(timeStamp + 10));      // 时间戳转换成时间
        System.out.println("格式化结果：" + sd);
    }


//    public static void main(String[] args) {
//        long money = 1500;
//        int type = 1;
//        double result = getResultV2(money, type);
//        System.out.println(result);
//    }
    //原始编码V0.0
    public static double getResult(long money, int type){
        double result = money;

        if (money >= 1000) {
            if (type == UserType.SILVER_VIP.getCode()) {

                System.out.println("白银会员 优惠50元");
                result = money - 50;
            } else if (type == UserType.GOLD_VIP.getCode()) {

                System.out.println("黄金会员 8折");
                result = money * 0.8;
            } else if (type == UserType.PLATINUM_VIP.getCode()) {

                System.out.println("白金会员 优惠50元，再打7折");
                result = (money - 50) * 0.7;
            } else {
                System.out.println("普通会员 不打折");
                result = money;
            }
        }
        return result;
    }
    //策略模式V1.0
    private static double getResultV1(long money, int type) {

        double result = money;

        if (money >= 1000) {
            if (type == UserType.SILVER_VIP.getCode()) {

                result = new SilverStrategyV1().compute(money);
            } else if (type == UserType.GOLD_VIP.getCode()) {

                result = new GoldStrategyV1().compute(money);
            } else if (type == UserType.PLATINUM_VIP.getCode()) {

                result = new PlatinumStrategyV1().compute(money);
            } else {
                result = new OrdinaryStrategyV1().compute(money);
            }
        }

        return result;
    }
    //策略模式V1.1
    //上面代码上出现了重复的调用 compute ，我们可以尝试进一步优化。
    private static double getResultV11(long money, int type) {
        //反逻辑：把money < 1000的情况提前return，减少缩进，专注 >=1000的情况
        if (money < 1000) {
            return money;
        }

        StrategyV1 strategyV1;

        if (type == UserType.SILVER_VIP.getCode()) {
            strategyV1 = new SilverStrategyV1();
        } else if (type == UserType.GOLD_VIP.getCode()) {
            strategyV1 = new GoldStrategyV1();
        } else if (type == UserType.PLATINUM_VIP.getCode()) {
            strategyV1 = new PlatinumStrategyV1();
        } else {
            strategyV1 = new OrdinaryStrategyV1();
        }

        return strategyV1.compute(money);
    }
    //策略模式V2.0  简单工厂 + 策略模式
//    后续代码优化上，若是 Java 项目，可以尝试使用自定义注解，注解 Strategy 实现类。
//
//    这样可以简化原来需在工厂类 List 添加一个 Stratey 策略。
    private static double getResultV2(long money, int type){
        if(money < 1000){
            return  money;
        }
        StrategyV2 strategyV2 = StrategyFactory.getInstance().get(type);
        if (strategyV2 == null){
            throw new IllegalArgumentException("输入会员类型不正确");
        }
    //精髓
        return strategyV2.compute(money);
    }
}
