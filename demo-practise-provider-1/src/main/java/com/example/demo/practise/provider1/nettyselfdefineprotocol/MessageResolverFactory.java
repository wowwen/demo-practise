package com.example.demo.practise.provider1.nettyselfdefineprotocol;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 我们看到，对于具体类型消息的处理，
 * 我们是通过一个工厂类来获取对应的消息处理器，然后处理相应的消息
 */

public final class MessageResolverFactory {

    //创建一个工厂类实例
    private static final MessageResolverFactory resolverFactory = new MessageResolverFactory();

    private static final List<Resolver> resolvers = new CopyOnWriteArrayList<>();


    private MessageResolverFactory(){

    }
    //使用单例模式实例化当前工厂类实例
    public static MessageResolverFactory getInstance(){
        return resolverFactory;
    }

    public void registerResolver(Resolver resolver){
        resolvers.add(resolver);
    }
    //根据解码后的消息，在工厂类处理器中查找可以处理当前消息的处理器
    public Resolver getMessageResolver(Message message){
        for (Resolver resolver : resolvers) {
            if (resolver.support(message)){
                return resolver;
            }
        }
        //当所有的resolver都找遍了之后还没有，则抛异常
        throw new RuntimeException("cannot find resolver, message type:" + message.getMessageType());
    }
}
