package com.atlp.netty.server;

import com.atlp.netty.common.Constants;
import com.atlp.netty.utils.NettyMessageDecoder;
import com.atlp.netty.utils.NettyMessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
        if(channel.localAddress().getPort() == Constants.HTTP_PORT) {
            pipeline.addLast(new HttpServerCodec());
            pipeline.addLast(new HttpServerHandler());
        } else if(channel.localAddress().getPort() == Constants.AI_PORT) {
            pipeline.addLast(new NettyMessageDecoder(10240, 4, 4));
            pipeline.addLast(new NettyMessageEncoder());
            pipeline.addLast(new AIServerHandler());
        }
    }
}
