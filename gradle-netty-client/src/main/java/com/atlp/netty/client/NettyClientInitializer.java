package com.atlp.netty.client;

import com.atlp.netty.common.Cache;
import com.atlp.netty.utils.NettyMessageDecoder;
import com.atlp.netty.utils.NettyMessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new NettyMessageDecoder(10240, 4, 4));
        pipeline.addLast(new NettyMessageEncoder());
        pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
        pipeline.addLast(new NettyClientHandler());
        Cache.LINK_CHANNEL = ch;
    }
}
