package com.atlp.netty.common;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    public static Map<String, Channel> NETTY_CHANNEL_MAP = new ConcurrentHashMap<>();
    public static volatile Channel LINK_CHANNEL;
    public static Map<String, String> NETTY_HTTP_MAP = new HashMap<>();
}
