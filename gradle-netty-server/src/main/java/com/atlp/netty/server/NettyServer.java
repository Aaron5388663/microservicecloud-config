package com.atlp.netty.server;

import com.atlp.netty.common.Constants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.ArrayList;
import java.util.List;

public class NettyServer {

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            bootstrap.group(bossGroup, workerGroup).
                    channel(NioServerSocketChannel.class).
                    handler(new LoggingHandler(LogLevel.INFO)).
                    childHandler(new NettyServerInitializer());
            List<ChannelFuture> futures = new ArrayList<>();
            futures.add(bootstrap.bind(Constants.HTTP_PORT));
            futures.add(bootstrap.bind(Constants.AI_PORT));
            for (ChannelFuture f : futures) {
                f.channel().closeFuture().sync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
