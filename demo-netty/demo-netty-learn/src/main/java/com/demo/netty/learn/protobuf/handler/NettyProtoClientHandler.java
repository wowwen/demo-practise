package com.demo.netty.learn.protobuf.handler;

import com.demo.netty.learn.protobuf.MyDataInfo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Random;

/**
 * @author juven
 * @date 2025/10/5 15:25
 * @description
 */
public class NettyProtoClientHandler extends ChannelInboundHandlerAdapter {
    // 当通道就绪，就会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 随机的发送 Student 或者 Worker 对象
        final int random = new Random().nextInt(3);
        System.out.println("随机数" + random);
        MyDataInfo.MyMessage myMessage = null;
        if (0 == random){
            myMessage = MyDataInfo.MyMessage.newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.StudentType)
                    .setStudent(MyDataInfo.Student.newBuilder().setId(100).setName("李四").build())
                    .build();
        }else{
            myMessage = MyDataInfo.MyMessage.newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.WorkerType)
                    .setWorker(MyDataInfo.Worker.newBuilder().setAge(20).setName("老李").build())
                    .build();
        }
        ctx.writeAndFlush(myMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("服务器回复的消息：" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址： " + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
