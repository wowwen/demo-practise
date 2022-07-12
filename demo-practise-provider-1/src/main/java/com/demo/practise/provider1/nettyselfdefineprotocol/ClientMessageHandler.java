package com.demo.practise.provider1.nettyselfdefineprotocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 由于客户端和服务端需要处理的消息类型是完全一样的，因而客户端处理类继承了服务端处理类。但是对于客户端而言，
 * 其还需要定时向服务端发送心跳消息，用于检测客户端与服务器的连接是否健在，因而客户端还会实现userEventTriggered()方法，
 * 在该方法中定时向服务器发送心跳消息。userEventTriggered()方法主要是在客户端被闲置一定时间后，
 * 其会根据其读取或者写入消息的限制时长来选择性的触发读取或写入事件。
 *
 * 客户端消息处理器
 * @author jiangyw8
 */
public class ClientMessageHandler extends ServerMessageHandler {

    /**
     * 创建一个线程，模拟用户发送消息
     */
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        //对于客户端，在建立链接之后，在一个独立线程中模拟用户发送数据给服务端
        executor.execute(new MessageSender(ctx));

    }

    /**
     * 这里userEventTrigger()主要是在一些用户事件触发时被调用，这里我们定义的事件是进行心跳检测的ping和pong消息，当前触发器会在
     * 指定的触发器指定的时间内返回，如果客户端没有被读取消息或者写入消息到管道，则会触发当前方法。
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                //一定时间内，当前服务没有发送读取事件，也即没有消息发送到当前服务来时，
                // 其会发送一条ping消息到服务器，以等待其响应pong消息
                Message message = new Message();
                message.setMessageType(MessageTypeEnum.PING);
                ctx.writeAndFlush(message);
            } else if (event.state() == IdleState.WRITER_IDLE) {
                // 如果当前服务在指定时间内没有写入消息到管道，则关闭当前管道
                ctx.close();
            }
        }

    }

    //私有化的静态内部类
    private static final class MessageSender implements Runnable {

        private static final AtomicLong counter = new AtomicLong(1);

        private volatile ChannelHandlerContext ctx;

        public MessageSender(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    //模拟随机发送消息的过程
                    TimeUnit.SECONDS.sleep(new Random().nextInt(3));
                    Message message = new Message();
                    message.setMessageType(MessageTypeEnum.REQUEST);
                    message.setBody("this is my " + counter.getAndIncrement() + "message.");
                    message.addAttachment("name", "xufeng");
                    ctx.writeAndFlush(message);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
