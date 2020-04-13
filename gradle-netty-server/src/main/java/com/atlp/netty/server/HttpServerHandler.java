package com.atlp.netty.server;

import com.alibaba.fastjson.JSON;
import com.atlp.netty.common.Constants;
import com.atlp.netty.common.NettyInfoDao;
import com.atlp.netty.common.NettyMessageTypeEnum;
import com.atlp.netty.utils.DesUtil;
import com.atlp.netty.utils.ServerUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.net.URI;

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
                String deviceId = split[3].substring(1, split[3].length());
                if("open".equals(split[2])) {
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
                    ServerUtils.httpResponse(split[3], request, ctx);
                } else if("sendqr".equals(split[2])) {
                    NettyInfoDao nettyInfoDao = new NettyInfoDao();
                    nettyInfoDao.setCmd(Constants.SEND_QR_CMD);
                    nettyInfoDao.setValue("{\n" +
                            "\t\"sysSource\": 0,\n" +
                            "\t\"deviceId\": \"" + deviceId + "\",\n" +
                            "\t\"serialNo\": \"" + serialNo + "\",\n" +
                            "\t\"qrCodeUrl\": \"www.atlp.com\"\n" +
                            "}\n");
                    String sendStr = JSON.toJSONString(nettyInfoDao);
                    String str = desUtil.encryptUTF8(sendStr);
                    ServerUtils.sendCommandClient(str, NettyMessageTypeEnum.BUSINESS_REQ, split[3]);
                    ServerUtils.httpResponse(split[3], request, ctx);
                } else if("sendall".equals(split[2])) {
                    long timestamp = System.currentTimeMillis();
                    NettyInfoDao nettyInfoDao = new NettyInfoDao();
                    nettyInfoDao.setCmd(Constants.SEND_ALL_CMD);
                    nettyInfoDao.setValue("{\n" +
                            "\t\"sysSource\": 0,\n" +
                            "\t\"deviceId\": \"" + deviceId + "\",\n" +
                            "\t\"serialNo\": \"" + serialNo + "\",\n" +
                            "\t\"timestamp\": \"" + timestamp + "\",\n" +
                            "\t\"skuDatas\": [{\n" +
                            "\t\t\"sku\": \"601213\",\n" +
                            "\t\t\"name\": \"ラーメン\",\n" +
                            "\t\t\"sku\": \"100\",\n" +
                            "\t\t\"sku\": 1.00,\n" +
                            "\t\t\"sku\": 1.00,\n" +
                            "\t\t\"sku\": 1.00,\n" +
                            "\t\t\"sku\": 100.00,\n" +
                            "\t\t\"sku\": 10.00,\n" +
                            "\t\t\"sku\": \"https://test.jd.com/1.jpg\",\n" +
                            "\t\t\"sku\": 1,\n" +
                            "\t\t\"sku\": \"1,2\"\n" +
                            "\t}]\n" +
                            "}\n");
                    String sendStr = JSON.toJSONString(nettyInfoDao);
                    String str = desUtil.encryptUTF8(sendStr);
                    ServerUtils.sendCommandClient(str, NettyMessageTypeEnum.BUSINESS_REQ, split[3]);
                    ServerUtils.httpResponse(split[3], request, ctx);
                }
            }
        }
    }
}
