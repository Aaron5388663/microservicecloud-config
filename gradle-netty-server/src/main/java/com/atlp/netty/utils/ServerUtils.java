package com.atlp.netty.utils;

import com.alibaba.fastjson.JSON;
import com.atlp.netty.common.Cache;
import com.atlp.netty.common.Header;
import com.atlp.netty.common.NettyMessage;
import com.atlp.netty.common.NettyMessageTypeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.Random;

public class ServerUtils {

    public static void sendCommandClient(Object sendObj, NettyMessageTypeEnum typeEnum, String processId) {
        Channel channel = Cache.NETTY_CHANNEL_MAP.get(processId);
        if(channel == null) {
            System.out.println("send cmd to fridge error, not found channel");
            return;
        }
        channel.writeAndFlush(buildNettyMsg(sendObj, typeEnum, processId));
    }

    private static NettyMessage buildNettyMsg(Object sendObj, NettyMessageTypeEnum typeEnum, String processId) {
        NettyMessage message = new NettyMessage();
        message.setBody(sendObj);
        Header header = new Header();
        header.setCrcCode(0XFE);
        header.setStation((short)0);
        header.setSource("M000000000");
        header.setDestination(processId);
        header.setComponent((short)0);
        header.setType(typeEnum.getCode());
        message.setHeader(header);
        return message;
    }

    public static String getSerialNo() {
        String serialNo = "";
        Random random = new Random();
        for(int i = 0; i < 10; i++) {
            serialNo += String.valueOf(random.nextInt(10));
        }
        return serialNo;
    }

    public static void httpResponse(String deviceId, HttpRequest request, ChannelHandlerContext ctx) throws Exception {
        Thread.sleep(3000);
        String sendMsg = Cache.NETTY_HTTP_MAP.get(deviceId);
        ByteBuf buf = Unpooled.copiedBuffer(sendMsg, CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK, buf);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, buf.readableBytes());
        ctx.writeAndFlush(response);
    }
}
