package com.atlp.netty.utils;

import com.atlp.netty.common.Cache;
import com.atlp.netty.common.Header;
import com.atlp.netty.common.NettyMessage;
import io.netty.channel.Channel;

public class ClientUtils {

    private static NettyMessage buildNettyMsg(Object sendObj, Byte type) {
        NettyMessage nettyMessage = new NettyMessage();
        nettyMessage.setBody(sendObj);
        Header header = new Header();
        header.setCrcCode(0XFE);
        header.setStation((short)0);
        header.setSource("C660000188");
        header.setDestination("M000000000");
        header.setComponent((short)0);
        header.setType(type);
        nettyMessage.setHeader(header);
        return nettyMessage;
    }

    private static void sendInfoMiddle(NettyMessage message) {
        Channel channel = Cache.LINK_CHANNEL;
        if(channel != null) {
            channel.writeAndFlush(message);
        }
    }

    public static void sendInfo(Object sendObj, Byte type) {
        sendInfoMiddle(buildNettyMsg(sendObj, type));
    }
}
