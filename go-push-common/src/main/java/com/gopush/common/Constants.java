package com.gopush.common;


import io.netty.util.AttributeKey;

/**
 * go-push
 *
 * @类功能说明：全局常量定义
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/18 下午3:02
 * @VERSION：
 */
public class Constants {



    public static final int READ_IDLE = 10;

    public static final int WRITE_IDLE = 30;

    public static final int ALL_IDLE = 50;



    /*****
     *
     * Device 和 NodeServer 之间
     *
     * ***/
    //绑定设备
    public static final AttributeKey<String> CHANNEL_ATTR_DEVICE = AttributeKey.newInstance("device");

    //绑定的握手状态
    public static final AttributeKey<Boolean> CHANNEL_ATTR_HANDSHAKE = AttributeKey.newInstance("handshake");

    //绑定的心跳间隔 // read ,write, all
    public static final AttributeKey<Integer[]> CHANNEL_ATTR_IDLE = AttributeKey.newInstance("idle");


    /**
     * redis 中存储设备Token的键
     */
    public static final String R_DEVICE_TOKEN_KEY ="D:T:";

    public static final String R_DEIVCE_TOKEN_FIELD = "token";



    /*****
     *
     * NodeServer 与 DataCenter 之间
     *
     * ***/
    public static final AttributeKey<String> CHANNEL_ATTR_DATACENTER = AttributeKey.newInstance("datacenter");









}
