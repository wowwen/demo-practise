package com.demo.practise.nettyselfdefineprotocol;

public interface Resolver {

    public boolean support(Message message);

    public Message resolve(Message message);

}
