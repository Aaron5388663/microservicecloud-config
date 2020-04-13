package com.atlp.netty.common;

public enum NettyMessageTypeEnum {

    BUSINESS_REQ((byte)0, "业务请求消息"),
    BUSINESS_RESP((byte)1, "业务响应消息"),
    BUSINESS_ONE_WAY((byte)2, "业务one way消息"),
    LOGIN_REQ((byte)3, "握手请求消息"),
    LOGIN_RESP((byte)4, "握手响应消息"),
    HEART_BEAT_REQ((byte)5, "心跳请求消息"),
    HEART_BEAT_RESP((byte)6, "心跳响应消息");

    private Byte code;
    private String msg;

    NettyMessageTypeEnum(Byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Byte getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static String getMsgByCode(Byte code) {
        NettyMessageTypeEnum status[] = NettyMessageTypeEnum.values();
        for(NettyMessageTypeEnum statu : status) {
            if(code.equals(statu.getCode())) {
                return statu.getMsg();
            }
        }
        return null;
    }

    public static NettyMessageTypeEnum getEnumByCode(Byte code) {
        NettyMessageTypeEnum status[] = NettyMessageTypeEnum.values();
        for(NettyMessageTypeEnum statu : status) {
            if(code.equals(statu.getCode())) {
                return statu;
            }
        }
        return null;
    }
}
