package com.demo.netty.learn.asyncpool;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.nio.charset.Charset;

/**
 * @author owen
 * @date 2025/4/9 0:07
 * @description 处理耗时业务的第二种方式--Context 中添加线程池
 */
public final class EchoServer {

    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));

    // 创建业务线程池
    // 创建2个子线程
    static final EventExecutorGroup group = new DefaultEventExecutorGroup(2);

    public static void main(String[] args) throws Exception {
        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(ch.alloc()));
                            }
                            p.addLast(new LoggingHandler(LogLevel.INFO));
//                     p.addLast(new EchoServerHandlerW1());
                            // 说明：如果在 addLast 添加 handler，前面有指定 EventExecutorGroup，那么该 handler 会优先加入到该线程池中
                            p.addLast(group, new EchoServerHandler());
                        }
                    });

            // Start the server.
            ChannelFuture f = b.bind(PORT).sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

@ChannelHandler.Sharable
class EchoServerHandler extends ChannelInboundHandlerAdapter {
    // group 就是充当业务线程池，可以将任务提交到该线程池
    // 创建了 16 个线程
    static final EventExecutorGroup group = new DefaultEventExecutorGroup(16);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InterruptedException {
        System.out.println("EchoServerHandlerW1 的线程是：" + Thread.currentThread().getName());

        /*ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5 * 1000);
                    // 输出线程名
                    System.out.println("EchoServerHandlerW1 execute 的线程是：" + Thread.currentThread().getName());
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端~ 喵2", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    System.out.println("发生异常 " + e.getMessage());
                    e.printStackTrace();
                }

            }
        });*/

        // 将任务提交到 group 线程池
        /*group.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                // 接收客户端信息
                ByteBuf buf = (ByteBuf) msg;
                byte[] bytes = new byte[buf.readableBytes()];
                buf.readBytes(bytes);
                String body = new String(bytes, Charset.forName("utf-8"));
                // 休眠10秒
                Thread.sleep(10 * 1000);
                System.out.println("group.submit 的 call 线程是" + Thread.currentThread().getName());

                ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端~ 2喵喵喵喵", CharsetUtil.UTF_8));

                return null;
            }
        });*/

        // 普通方式
        // 接收客户端信息
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String body = new String(bytes, Charset.forName("utf-8"));
        // 休眠10秒
        Thread.sleep(10 * 1000);
        System.out.println("普通调用方式的线程是" + Thread.currentThread().getName());
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端~ 2喵喵喵喵", CharsetUtil.UTF_8));
        System.out.println("go on.....");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
