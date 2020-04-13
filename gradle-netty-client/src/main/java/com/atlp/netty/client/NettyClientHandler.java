package com.atlp.netty.client;

import com.alibaba.fastjson.JSON;
import com.atlp.netty.common.Constants;
import com.atlp.netty.common.NettyInfoDao;
import com.atlp.netty.common.NettyMessage;
import com.atlp.netty.common.NettyMessageTypeEnum;
import com.atlp.netty.utils.ClientUtils;
import com.atlp.netty.utils.DesUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyClientHandler extends SimpleChannelInboundHandler<NettyMessage> {

    public NettyClientHandler() {
        super();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ClientUtils.sendInfo(null, NettyMessageTypeEnum.LOGIN_REQ.getCode());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NettyMessage msg) throws Exception {
        if(msg.getHeader().getType() == NettyMessageTypeEnum.LOGIN_RESP.getCode()) {
            System.out.println("===server send login message info===" + JSON.toJSONString(msg));
//            System.out.println("服务器端响应的注册成功消息:" + JSON.toJSONString(msg));
            if(msg.getHeader() != null) {
                ClientUtils.sendInfo(null, NettyMessageTypeEnum.HEART_BEAT_REQ.getCode());
            }
        } else if(msg.getHeader().getType() == NettyMessageTypeEnum.HEART_BEAT_RESP.getCode()) {
            System.out.println("===server send heartbeat message info===" + JSON.toJSONString(msg));
//            System.out.println("服务器端响应的心跳连接消息:" + JSON.toJSONString(msg));
        } else if(msg.getHeader().getType() == NettyMessageTypeEnum.BUSINESS_REQ.getCode()) {
            DesUtil desUtil = new DesUtil("TKH6YWtBk10RmEB0");
            NettyInfoDao nettyInfoDao = null;
            if(msg.getBody() != null) {
                String decryptInfo = desUtil.decryptUTF8((String) msg.getBody());
                nettyInfoDao = JSON.parseObject(decryptInfo, NettyInfoDao.class);
                if(nettyInfoDao.getCmd() == Constants.OPEN_DOOR_CMD) {
                    System.out.println("===server send open door message info===" + decryptInfo);
//                    System.out.println("服务器端发送的开门请求信息:" + decryptInfo);
                    nettyInfoDao = new NettyInfoDao();
                    nettyInfoDao.setCmd(Constants.OPEN_DOOR_CMD);
                    nettyInfoDao.setValue("{\n" +
                            "\t\"sysSource\": 0,\n" +
                            "\t\"deviceId\": \"660000188\",\n" +
                            "\t\"serialNo\": \"2431515461\",\n" +
                            "\t\"openResult\": 1,\n" +
                            "\t\"openResultMsg\": \"成功\",\n" +
                            "\t\"deviceStatusCode\": 1,\n" +
                            "\t\"deviceStatusMsg\": \"门打开\"\n" +
                            "}\n");
                    String sendStr = JSON.toJSONString(nettyInfoDao);
                    String str = desUtil.encryptUTF8(sendStr);
                    ClientUtils.sendInfo(str, NettyMessageTypeEnum.BUSINESS_RESP.getCode());

                    long closeTime = System.currentTimeMillis();
                    nettyInfoDao = new NettyInfoDao();
                    nettyInfoDao.setCmd(Constants.CLOSE_DOOR_CMD);
                    nettyInfoDao.setValue("{\n" +
                            "\t\"closeResult\": 1,\n" +
                            "\t\"closeTime\": " + closeTime + ",\n" +
                            "\t\"deviceId\": \"660000188\",\n" +
                            "\t\"serialNo\": \"2431515461\",\n" +
                            "\t\"sysSource\": 100\n" +
                            "}\n");
                    sendStr = JSON.toJSONString(nettyInfoDao);
                    str = desUtil.encryptUTF8(sendStr);
                    ClientUtils.sendInfo(str, NettyMessageTypeEnum.BUSINESS_REQ.getCode());

                    nettyInfoDao = new NettyInfoDao();
                    nettyInfoDao.setCmd(Constants.ADD_CART_CMD);
                    nettyInfoDao.setValue("{\n" +
                            "\t\"sysSource\": 100,\n" +
                            "\t\"deviceId\": \"660000188\",\n" +
                            "\t\"serialNo\": \"2431515461\",\n" +
                            "\t\"diffWeight\": 330.0,\n" +
                            "\t\"skuList\": [{\n" +
                            "\t\t\"number\": 2,\n" +
                            "\t\t\"sku\": \"60213\",\n" +
                            "\t},{\n" +
                            "\t\t\"number\": 1,\n" +
                            "\t\t\"sku\": \"60214\",\n" +
                            "\t}]\n" +
                            "}\n");
                    sendStr = JSON.toJSONString(nettyInfoDao);
                    str = desUtil.encryptUTF8(sendStr);
                    ClientUtils.sendInfo(str, NettyMessageTypeEnum.BUSINESS_REQ.getCode());
                } else if(nettyInfoDao.getCmd() == Constants.SEND_QR_CMD) {
                    System.out.println("===server send qrcode message info===" + decryptInfo);
//                    System.out.println("服务器端发送的推送二维码信息:" + decryptInfo);
                    nettyInfoDao = new NettyInfoDao();
                    nettyInfoDao.setCmd(Constants.SEND_QR_CMD);
                    nettyInfoDao.setValue("{\n" +
                            "\t\"deviceId\": \"660000188\",\n" +
                            "\t\"pushResult\": 1,\n" +
                            "\t\"pushResultMsg\": \"成功\",\n" +
                            "\t\"serialNo\": \"2431515461\",\n" +
                            "\t\"sysSource\": 100\n" +
                            "}\n");
                    String sendStr = JSON.toJSONString(nettyInfoDao);
                    String str = desUtil.encryptUTF8(sendStr);
                    ClientUtils.sendInfo(str, NettyMessageTypeEnum.BUSINESS_RESP.getCode());
                } else if(nettyInfoDao.getCmd() == Constants.SEND_ALL_CMD) {
                    System.out.println("===server send all message info===" + decryptInfo);
//                    System.out.println("服务器端发送的推送全量信息:" + decryptInfo);
                    nettyInfoDao = new NettyInfoDao();
                    nettyInfoDao.setCmd(Constants.SEND_ALL_CMD);
                    nettyInfoDao.setValue("{\n" +
                            "\t\"deviceId\": \"660000188\",\n" +
                            "\t\"pushResult\": 1,\n" +
                            "\t\"pushResultMsg\": \"成功\",\n" +
                            "\t\"serialNo\": \"2431515461\",\n" +
                            "\t\"sysSource\": 100\n" +
                            "}\n");
                    String sendStr = JSON.toJSONString(nettyInfoDao);
                    String str = desUtil.encryptUTF8(sendStr);
                    ClientUtils.sendInfo(str, NettyMessageTypeEnum.BUSINESS_RESP.getCode());
                }
            }
        } else if(msg.getHeader().getType() == NettyMessageTypeEnum.BUSINESS_RESP.getCode()) {
            DesUtil desUtil = new DesUtil("TKH6YWtBk10RmEB0");
            NettyInfoDao nettyInfoDao = null;
            if(msg.getBody() != null) {
                String decryptInfo = desUtil.decryptUTF8((String) msg.getBody());
                nettyInfoDao = JSON.parseObject(decryptInfo, NettyInfoDao.class);
                if (nettyInfoDao.getCmd() == Constants.ADD_CART_CMD) {
                    System.out.println("===server send add cart message info===" + decryptInfo);
//                    System.out.println("服务器端响应的上传购物车信息:" + desUtil.decryptUTF8((String) msg.getBody()));
                }
            }
        }
    }
}