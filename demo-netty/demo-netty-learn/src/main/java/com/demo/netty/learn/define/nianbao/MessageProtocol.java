package com.demo.netty.learn.define.nianbao;

/**
 * @author juven
 * @date 2025/10/7 13:05
 * @description 协议包
 */
public class MessageProtocol {
    private int len;
    private byte[] content;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
