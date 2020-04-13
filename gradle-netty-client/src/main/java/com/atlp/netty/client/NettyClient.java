package com.atlp.netty.client;

import com.atlp.netty.common.Constants;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap().
                    group(group).
                    channel(NioSocketChannel.class).
                    option(ChannelOption.TCP_NODELAY, true);
            synchronized (b) {
                b.handler(new NettyClientInitializer());
            }
            b.connect("127.0.0.1", Constants.AI_PORT).sync();
        } catch (Exception e) {
            group.shutdownGracefully();
        }
    }
}
