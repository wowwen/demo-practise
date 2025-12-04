package com.demo.netty.proxy.init;

import com.demo.netty.proxy.config.ClientNode;
import com.demo.netty.proxy.handler.ClientMsgDecoder;
import com.demo.netty.proxy.handler.HeartBeatServerHandler;
import com.demo.netty.proxy.handler.LoginServerHandler;
import com.demo.netty.proxy.handler.PositionHandler;
import com.demo.netty.proxy.service.AccountService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author owen
 * @date 2024/9/3 0:23
 * @description
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private AccountService accountService;
    private KafkaTemplate kfkProducer;
    private String topic;

    private static final EventExecutorGroup EVENT_EXECUTOR_GROUP = new DefaultEventExecutorGroup(32);

    public ServerChannelInitializer(AccountService accountService, String topic, KafkaTemplate kfkProducer) {
        this.accountService = accountService;
        this.topic = topic;
        this.kfkProducer = kfkProducer;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        /*
         * ChannelInboundHandler按照注册的先后顺序执行，ChannelOutboundHandler按照注册的先后顺序逆序执行。
         * HttpRequestDecoder、HttpObjectAggregator、HttpHandler为InboundHandler
         * HttpContentCompressor、HttpResponseEncoder为OutboundHandler
         * 在使用Handler的过程中，需要注意：
         * 1、ChannelInboundHandler之间的传递，通过调用 ctx.fireChannelRead(msg) 实现；调用ctx.write(msg) 将传递到ChannelOutboundHandler。
         * 2、ctx.write()方法执行后，需要调用flush()方法才能令它立即执行。
         * 3、ChannelOutboundHandler 在注册的时候需要放在最后一个ChannelInboundHandler之前，否则将无法传递到ChannelOutboundHandler。
         * 4、Handler的消费处理放在最后一个处理。
         */
        ChannelPipeline pipeline = socketChannel.pipeline();
        //netty的空闲状态处理器只会检测连接的空闲情况，而不会处理情况。具体情况如何处理，我们可以通过自定义心跳处理器来实现处理逻辑，比如断开，或者重连。
        pipeline.addLast("idleStateHandler", new IdleStateHandler(3, 0, 0));
        //通过上面的IdleStateHandler，当超时时，IdleStateHandler会触发一个IdleStateEvent 事件，向下传递，可以在自定义handler中通过重写userEventTriggered
        // 方法来处理IdleStateEvent 事件(如关闭连接或发送心跳包)
        pipeline.addLast("heartbeatServerHandler", new HeartBeatServerHandler());
        //粘包拆包处理器,用指定标识符分隔
        pipeline.addLast("delimiterBasedFrameDecoder", new DelimiterBasedFrameDecoder(1024,
               new ByteBuf[]{
                       Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("$", CharsetUtil.UTF_8)),
                       Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(new byte[] { '\r', '\n' })),
                       Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(new byte[] { '\n' }))
               } ));
        //将ByteBuf转成字符串，并传递给下一个ChannelInboundHandler
        pipeline.addLast("msgDecoder", new ClientMsgDecoder());
        pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
        //这里采用处理耗时业务的第二种方式。
        // 说明：如果在 addLast 添加 handler，前面有指定 EventExecutorGroup，那么该 handler 会优先加入到该线程池中
        pipeline.addLast(EVENT_EXECUTOR_GROUP, "loginHandler", new LoginServerHandler(accountService));
        pipeline.addLast(EVENT_EXECUTOR_GROUP, "positionHandler", new PositionHandler(topic, kfkProducer));
    }
}
