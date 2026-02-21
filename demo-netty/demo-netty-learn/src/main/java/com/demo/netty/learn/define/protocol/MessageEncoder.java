package com.demo.netty.learn.define.protocol;

import com.demo.netty.learn.define.protocol.Constants;
import com.demo.netty.learn.define.protocol.Message;
import com.demo.netty.learn.define.protocol.MessageTypeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.catalina.util.StandardSessionIdGenerator;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;

public class MessageEncoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out) throws Exception {
        // 这里会判断消息类型是不是EMPTY类型，如果是EMPTY类型，则表示当前消息不需要写入到管道中
        if (message.getMessageType() != MessageTypeEnum.EMPTY){
            //写入当前的魔数
            out.writeInt(Constants.MAGIC_NUMBER);
            out.writeByte(Constants.MAIN_VERSION);
            out.writeByte(Constants.SUB_VERSION);
            out.writeByte(Constants.MODIFY_VERSION);

            if (!StringUtils.hasText(message.getSessionId())){
                //生成一个sessionId，并将其写入到字节序列中
                    //SessionIdGenerator为interface--》SessionIdGeneratorBase为abstract抽象类--》StandardSessionIdGenerator为实现类
                    //需要实例化实现类才能调用generateSessionId()方法
                StandardSessionIdGenerator standardSessionIdGenerator = new StandardSessionIdGenerator();
                String sessionId = standardSessionIdGenerator.generateSessionId();
                message.setSessionId(sessionId);
                out.writeCharSequence(sessionId, Charset.defaultCharset());
            }

            //写入当前消息的类型
            out.writeByte(message.getMessageType().getType());
            //写入当前消息的附加参数数量
            out.writeShort(message.getAttachments().size());
            message.getAttachments().forEach((key, value) -> {
                Charset charset = Charset.defaultCharset();
                //写入键长
                out.writeInt(key.length());
                //写入键值
                out.writeCharSequence(key, charset);
                //写入希尔值的长度
                out.writeInt(value.length());
                //写入值数据
                out.writeCharSequence(value, charset);
            });

            if (null == message.getBody()){
                //如果消息体为空，则写入0，表示消息体长度为0
                out.writeInt(0);
            }else {
                out.writeInt(message.getBody().length());
                out.writeCharSequence(message.getBody(), Charset.defaultCharset());
            }
        }
    }
}
