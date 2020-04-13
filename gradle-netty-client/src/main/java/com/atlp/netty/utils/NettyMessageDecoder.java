package com.atlp.netty.utils;

import com.alibaba.fastjson.JSON;
import com.atlp.netty.common.Header;
import com.atlp.netty.common.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.charset.Charset;

public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf buf = (ByteBuf) super.decode(ctx, in);
        if(buf == null) {
            return null;
        }

        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setCrcCode(buf.readInt());
        header.setLen(buf.readInt());
        header.setStation(buf.readShort());
        header.setSource(buf.readBytes(10).toString(Charset.forName("UTF-8")));
        header.setDestination(buf.readBytes(10).toString(Charset.forName("UTF-8")));
        header.setComponent(buf.readShort());
        header.setType(buf.readByte());

        Object body = null;
        if(buf.readableBytes() > 0) {
            String bodyStr = buf.toString(Charset.forName("utf-8"));
            body = JSON.parseObject(bodyStr, Object.class);
            message.setBody(body);
        }

        message.setHeader(header);
        message.setBody(body);

        return message;
    }
}
