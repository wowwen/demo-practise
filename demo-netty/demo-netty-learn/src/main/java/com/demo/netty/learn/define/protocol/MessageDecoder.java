package com.demo.netty.learn.define.protocol;

import com.demo.netty.learn.define.protocol.Constants;
import com.demo.netty.learn.define.protocol.Message;
import com.demo.netty.learn.define.protocol.MessageTypeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        Message message = new Message();
        //读取魔数
        message.setMagicNumber(byteBuf.readInt());
        //读取主版本号
        message.setMainVersion(byteBuf.readByte());
        //读取次版本号
        message.setSubVersion(byteBuf.readByte());
        //读取修订版本号
        message.setModifyVersion(byteBuf.readByte());
        //读取sessionId
        CharSequence sessionId = byteBuf.readCharSequence(Constants.SESSION_ID_LENGTH, Charset.defaultCharset());
        message.setSessionId((String)sessionId);
        //读取当前的消息类型
        byte b = byteBuf.readByte();
        MessageTypeEnum messageTypeEnum = MessageTypeEnum.get(b);
        message.setMessageType(MessageTypeEnum.get(byteBuf.readByte()));
        //读取附件长度
        short attachmentSize = byteBuf.readShort();
        for (int i = 0; i < attachmentSize ; i++) {
            //读取键长度和数据
            int keyLength = byteBuf.readInt();
            CharSequence key = byteBuf.readCharSequence(keyLength, Charset.defaultCharset());
            //读取值长度和数据
            int valueLength = byteBuf.readInt();
            CharSequence value = byteBuf.readCharSequence(valueLength, Charset.defaultCharset());
            message.addAttachment(key.toString(), value.toString());
        }

        //读取消息体长度和数据
        int bodyLength = byteBuf.readInt();
        CharSequence body = byteBuf.readCharSequence(bodyLength, Charset.defaultCharset());
        message.setBody(body.toString());
        out.add(message);
    }
}
