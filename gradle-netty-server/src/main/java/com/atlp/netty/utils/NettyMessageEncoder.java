package com.atlp.netty.utils;

import com.alibaba.fastjson.JSON;
import com.atlp.netty.common.Header;
import com.atlp.netty.common.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyMessageEncoder extends MessageToByteEncoder<NettyMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf out) throws Exception {
        if(msg == null || msg.getHeader() == null) {
            throw new Exception("encode message is null...");
        }

        Header header = msg.getHeader();
        out.writeInt(header.getCrcCode());
        out.writeInt(0);
        out.writeShort(header.getStation());
        out.writeBytes(JSON.toJSONBytes(header.getSource()), 1, 10);
        out.writeBytes(JSON.toJSONBytes(header.getDestination()), 1, 10);
        out.writeShort(header.getComponent());
        out.writeByte(header.getType());

        if(msg.getBody() != null) {
            out.writeBytes(JSON.toJSONString(msg.getBody()).getBytes());
        }
        out.setInt(4, out.readableBytes() - 8);
    }
}
