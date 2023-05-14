//package com.demo.practise.stream.streamtest;
//
//import java.math.BigInteger;
//
//public class FibonacciUtil {
//    /**
//     * 计算第n位上的斐波那契值
//     * @param n
//     * @return 返回第n位上的斐波那契值
//     */
//    public static long calculate(int n){
//        if (n < 2){
//            return Math.max(n ,0);
//        }else {
//            return calculate(n -1) + calculate(n -2);
//        }
//    }
//
//    public static BigInteger fibonacci(int n){
//        if (n < 2){
//            return BigInteger.valueOf(n);
//        }else {
//            BigInteger[] ans = new BigInteger[n];
//            ans[0] = BigInteger.valueOf(1);
//            ans[1] = BigInteger.valueOf(2);
//            for (int i = 2; i < n ; i++) {
//                ans[i] = ans[i - 1].add(ans[i - 2]);
//            }
//            return ans[n - 1];
//        }
//    }
//}
