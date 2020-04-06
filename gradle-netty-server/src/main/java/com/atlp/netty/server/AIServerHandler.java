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
        System.out.println("===receive client message from remoteAddr===" + remoteAdde);
        DesUtil desUtil = new DesUtil("TKH6YWtBk10RmEB0");
        Cache.NETTY_CHANNEL_MAP.put(message.getHeader().getSource(), ctx.channel());

        if(message.getHeader().getType() == NettyMessageTypeEnum.LOGIN_REQ.getCode()) {
            System.out.println("===receive client login message info===" + JSON.toJSONString(message));
            ServerUtils.sendCommandClient((byte)0, NettyMessageTypeEnum.LOGIN_RESP, message.getHeader().getSource());
        } else if(message.getHeader().getType() == NettyMessageTypeEnum.HEART_BEAT_REQ.getCode()) {
            System.out.println("===receive client heartbeat message info===" + JSON.toJSONString(message));
            ServerUtils.sendCommandClient(null, NettyMessageTypeEnum.HEART_BEAT_RESP, message.getHeader().getSource());
        }

        if(message.getBody() != null) {
            String decryptInfo = desUtil.decryptUTF8((String) message.getBody());
            System.out.println("===receive client body message info===" + decryptInfo);
            NettyInfoDao nettyInfoDao = JSON.parseObject(decryptInfo, NettyInfoDao.class);
            if(nettyInfoDao.getCmd() == Constants.OPEN_DOOR_CMD) {
                String sendHttpStr = JSON.toJSONString(nettyInfoDao.getValue());
                Cache.NETTY_HTTP_MAP.put(message.getHeader().getSource(), sendHttpStr);
            } else if(nettyInfoDao.getCmd() == Constants.ADD_CART_CMD) {
                nettyInfoDao = new NettyInfoDao();
                nettyInfoDao.setCmd(Constants.ADD_CART_CMD);
                nettyInfoDao.setValue("1");
                String sendStr = JSON.toJSONString(nettyInfoDao);
                String encryptStr = desUtil.encryptUTF8(sendStr);
                ServerUtils.sendCommandClient(encryptStr, NettyMessageTypeEnum.BUSINESS_RESP, message.getHeader().getSource());
            }
        }
    }

    private void handleCallback(NettyInfoDao nettyInfo) {
        Integer cmd = nettyInfo.getCmd();
        String value = nettyInfo.getValue();
        System.out.println("----------cmd---------" + cmd);
        System.out.println("-----------value------" + JSON.toJSONString(value));
        if(cmd == null) {
            return;
        }
    }
}
