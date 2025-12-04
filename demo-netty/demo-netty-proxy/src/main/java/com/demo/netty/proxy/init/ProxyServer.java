package com.demo.netty.proxy.init;

import com.demo.netty.proxy.config.ClientNode;
import com.demo.netty.proxy.service.AccountService;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author owen
 * @date 2024/8/25 16:32
 * @description
 */
@Service
@Slf4j
public class ProxyServer {

    @Resource
    private ClientNode clientNode;
    @Resource
    private AccountService accountService;
    @Resource
    private KafkaTemplate kfkProducer;

    @Value("${spring.kafka.topics}")
    private String topic;

    @PostConstruct
    public void run(){
        //获取代理信息
        clientNode.getClients().forEach(client -> {
            //启动代理服务
            initProxyServer(client.getPort());
        });
    }

    /**
     * 代理服务器服务端
     * @param key
     */
    private void initProxyServer(Object key){
        NioEventLoopGroup bossGroup;
        NioEventLoopGroup workGroup;

        if (key instanceof Integer) {
            int serverPort = (Integer) key;
            //默认线程CPU * 2(IO密集型：核心线程数 = CPU核数 × 2  - CPU密集型：核心线程数 = CPU核数 + 1
            // 混合型（即同时包含CPU密集型和I/O密集型操作的任务），可以使用类似“核心线程数=(线程等待时间/线程CPU时间+1)*CPU核心数”)
            //http-nio-8212
            bossGroup = new NioEventLoopGroup(1);
            //nioEventLoopGroup---编号在主线程后一个
            //不指定大小，默认是CPU*2
            workGroup = new NioEventLoopGroup();
            //此处是将netty服务作为服务端，所以采用ServerBootstrap；如果是作为客户端，则new Bootstrap();
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    //如果队列满了，server将发送一个拒绝连接的错误信息到client。java.net.SocketTimeoutException: connect timed out
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //如果不配置，则linux下默认是PooledByteBufAllocator.DEFAULT.但是显示配置能提升性能（少了些判断）
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    //true:禁用Nagle算法，小包即发，升高网络负载，降低延迟
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    //启用TCP keepalive机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ServerChannelInitializer(accountService, topic, kfkProducer));
            try {
                //绑定服务端口
                ChannelFuture future = bootstrap.bind(serverPort).sync();

                future.addListener((ChannelFutureListener) channelFuture -> {
                    if (channelFuture.isSuccess()) {
                        log.info("代理服务端成功");
                    } else {
                        log.info("代理服务端失败");
                    }
                });
            } catch (Exception e) {
                log.error("init proxy server error:", e);
                bossGroup.shutdownGracefully();
                workGroup.shutdownGracefully();
            }
        }
    }
}
