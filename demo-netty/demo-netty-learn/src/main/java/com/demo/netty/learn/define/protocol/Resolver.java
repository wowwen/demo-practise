package com.demo.netty.learn.define.protocol;

public interface Resolver {

    public boolean support(Message message);

    public Message resolve(Message message);

}
