package com.demo.netty.learn.protobuf.handler;

import com.demo.netty.learn.protobuf.MyDataInfo;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author juven
 * @date 2025/10/5 3:03
 * @description 说明：
 * 1.自定义一个 Handler 需要继承 netty 规定好的某个 handlerAdapter
 * 2.这是我们自定义的 Handler，
 */
public class NettyProtoServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {

    /**
     * 1. ChannelHandlerContext ctx：上下文对象，含有管道 pipeline，通道channel，地址
     * 2. Object msg：就是客户端发送的数据 默认Object
     */
    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // writeAndFlush 是 write + flush
        // 将数据写入到缓存，并刷新
        // 一般讲，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端~", CharsetUtil.UTF_8));
    }

    // 处理异常，一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
        // 根据 dataType 来显示不同的信息
        final MyDataInfo.MyMessage.DataType dataType = msg.getDataType();
        if (dataType == MyDataInfo.MyMessage.DataType.StudentType) {
            final MyDataInfo.Student student = msg.getStudent();
            System.out.println("学生id=" + student.getId() + " 学生名字=" + student.getName());
        } else if (dataType == MyDataInfo.MyMessage.DataType.WorkerType) {
            MyDataInfo.Worker worker = msg.getWorker();
            System.out.println("工人age=" + worker.getAge() + " 工人名字=" + worker.getName());
        } else {
            System.out.println("传输的类型不正确");
        }
    }
}
