package com.atlp.netty.server;

import com.alibaba.fastjson.JSON;
import com.atlp.netty.common.Cache;
import com.atlp.netty.common.Constants;
import com.atlp.netty.common.NettyInfoDao;
import com.atlp.netty.common.NettyMessageTypeEnum;
import com.atlp.netty.utils.DesUtil;
import com.atlp.netty.utils.ServerUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.util.Random;

public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        DesUtil desUtil = new DesUtil("TKH6YWtBk10RmEB0");
        if(msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest)msg;
            if(request.getMethod() == HttpMethod.GET) {
                URI uri = new URI(request.getUri());
                String path = uri.getPath();
                String[] split = path.split("/");
                String serialNo = ServerUtils.getSerialNo();
                if("open".equals(split[2])) {
                    String deviceId = split[3].substring(1, split[3].length());
                    NettyInfoDao nettyInfoDao = new NettyInfoDao();
                    nettyInfoDao.setCmd(Constants.OPEN_DOOR_CMD);
                    nettyInfoDao.setValue("{\n" +
                            "\t\"sysSource\": 0,\n" +
                            "\t\"deviceId\": \"" + deviceId + "\",\n" +
                            "\t\"serialNo\": \"" + serialNo + "\"\n" +
                    "}\n");
                    String sendStr = JSON.toJSONString(nettyInfoDao);
                    String str = desUtil.encryptUTF8(sendStr);
                    ServerUtils.sendCommandClient(str, NettyMessageTypeEnum.BUSINESS_REQ, split[3]);

                    Thread.sleep(3000);
                    String sendMsg = Cache.NETTY_HTTP_MAP.get(split[3]);
                    ByteBuf buf = Unpooled.copiedBuffer(sendMsg, CharsetUtil.UTF_8);
                    FullHttpResponse response = new DefaultFullHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK, buf);
                    response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");
                    response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, buf.readableBytes());
                    ctx.writeAndFlush(response);
                }
            }
        }
    }
}
