package com.demo.practise.common.helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author jiang
 */
public class Md5Helper {

    /**
     * 生成MD5
     *
     * @param value 数据
     * @return String
     */
    public static String genSignature(String value) {
        try {
            //获取消息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(value.getBytes());
            StringBuffer buffer = new StringBuffer();
            //把每一个byte和0xff做一个与运算
            for (byte b : result) {
                //加盐
                int num = b & 0xff;
                String str = Integer.toHexString(num);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }
            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

    }
}
