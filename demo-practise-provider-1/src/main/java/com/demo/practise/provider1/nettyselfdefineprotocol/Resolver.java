package com.demo.practise.provider1.nettyselfdefineprotocol;

public interface Resolver {

    public boolean support(Message message);

    public Message resolve(Message message);

}
