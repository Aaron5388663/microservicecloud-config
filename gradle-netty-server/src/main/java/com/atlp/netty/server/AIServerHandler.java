package com.atlp.netty.server;

import com.alibaba.fastjson.JSON;
import com.atlp.netty.common.*;
import com.atlp.netty.utils.DesUtil;
import com.atlp.netty.utils.ServerUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class AIServerHandler extends SimpleChannelInboundHandler<NettyMessage> {

    public AIServerHandler() {
        super();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NettyMessage message) throws Exception {
        String remoteAdde = ctx.channel().remoteAddress().toString();
//        System.out.println("===client send remoteAddr message info===" + remoteAdde);
//        System.out.println("客户端连接的远程地址:" + remoteAdde);
        DesUtil desUtil = new DesUtil("TKH6YWtBk10RmEB0");
        Cache.NETTY_CHANNEL_MAP.put(message.getHeader().getSource(), ctx.channel());

        if(message.getHeader().getType() == NettyMessageTypeEnum.LOGIN_REQ.getCode()) {
            System.out.println("===client send login message info===" + JSON.toJSONString(message));
//            System.out.println("客户端发送的注册请求信息:" + JSON.toJSONString(message));
            ServerUtils.sendCommandClient((byte)0, NettyMessageTypeEnum.LOGIN_RESP, message.getHeader().getSource());
        } else if(message.getHeader().getType() == NettyMessageTypeEnum.HEART_BEAT_REQ.getCode()) {
            System.out.println("===client send heartbeat message info===" + JSON.toJSONString(message));
//            System.out.println("客户端发送的心跳连接信息:" + JSON.toJSONString(message));
            ServerUtils.sendCommandClient(null, NettyMessageTypeEnum.HEART_BEAT_RESP, message.getHeader().getSource());
        }

        if(message.getBody() != null) {
            String decryptInfo = desUtil.decryptUTF8((String) message.getBody());
            NettyInfoDao nettyInfoDao = JSON.parseObject(decryptInfo, NettyInfoDao.class);
            if(nettyInfoDao.getCmd() == Constants.OPEN_DOOR_CMD) {
                String sendHttpStr = JSON.toJSONString(nettyInfoDao.getValue());
                System.out.println("===client send open door message info===" + sendHttpStr);
//                System.out.println("客户端响应的开门请求信息:" + sendHttpStr);
            } else if(nettyInfoDao.getCmd() == Constants.CLOSE_DOOR_CMD) {
                String sendHttpStr = JSON.toJSONString(nettyInfoDao.getValue());
                System.out.println("===client send close door message info===" + sendHttpStr);
//                System.out.println("客户端发送的关门请求信息:" + sendHttpStr);
            } else if(nettyInfoDao.getCmd() == Constants.ADD_CART_CMD) {
                String sendHttpStr = JSON.toJSONString(nettyInfoDao.getValue());
                System.out.println("===client send add cart message info===" + sendHttpStr);
//                System.out.println("客户端发送的上传购物车信息:" + sendHttpStr);
                nettyInfoDao.setCmd(Constants.ADD_CART_CMD);
                nettyInfoDao.setValue("1");
                String sendStr = JSON.toJSONString(nettyInfoDao);
                String encryptStr = desUtil.encryptUTF8(sendStr);
                ServerUtils.sendCommandClient(encryptStr, NettyMessageTypeEnum.BUSINESS_RESP, message.getHeader().getSource());
                Cache.NETTY_HTTP_MAP.put(message.getHeader().getSource(), sendHttpStr);
            } else if(nettyInfoDao.getCmd() == Constants.SEND_QR_CMD) {
                String sendHttpStr = JSON.toJSONString(nettyInfoDao.getValue());
                System.out.println("===client send qrcode message info===" + sendHttpStr);
//                System.out.println("客户端响应的推送二维码信息:" + sendHttpStr);
                Cache.NETTY_HTTP_MAP.put(message.getHeader().getSource(), sendHttpStr);
            } else if(nettyInfoDao.getCmd() == Constants.SEND_ALL_CMD) {
                String sendHttpStr = JSON.toJSONString(nettyInfoDao.getValue());
                System.out.println("===client send all message info===" + sendHttpStr);
//                System.out.println("客户端响应的推送全量信息:" + sendHttpStr);
                Cache.NETTY_HTTP_MAP.put(message.getHeader().getSource(), sendHttpStr);
            }
        }
    }
}
