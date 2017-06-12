package com.gopush.protocol.device;

import com.gopush.protocol.exceptions.DeviceProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * go-push
 *
 * @类功能说明：设备消息基类
 * @作者：chenxiangqi
 * @创建时间：2017/6/9
 * @VERSION：
 */
public abstract class DeviceMessage {

    //消息 Type Key
    protected static final String D_TYPE_KEY = "T";

    /**
     * 消息类型
     * 1)握手请求
     * 2)握手响应
     * 3)心跳请求
     * 4)心跳响应
     * 5)消息推送
     * 6)推送反馈
     */
    public enum Type {
        HANDSHAKE_REQ,
        HANDSHAKE_RESP,
        PING,
        PONG,
        PUSH_REQ,
        PUSH_RESP
    }

    /**
     * 获取设备消息类型
     * @return
     */
    protected abstract Type type();



    /**
     * 消息转换 JSONObject
     * @return
     * @throws JSONException
     */
    protected abstract JSONObject toEncode() throws JSONException;


    /**
     * 消息编码
     * @return
     */
    public String encode() throws DeviceProtocolException{
        try {
            JSONObject jsonObject = toEncode();
            if(jsonObject == null){
                //直接抛出空指针异常，确保 消息是必须要解码的
                //throw new NullPointerException("Message is empty");
                //前端不传 直接新建一个消息json空
                jsonObject = new JSONObject();
            }
            jsonObject.putOnce(D_TYPE_KEY,type());
            return jsonObject.toString();
        } catch (JSONException e) {
            throw new DeviceProtocolException(e);
        }
    }

    /**
     * 消息转换 JSONObject
     * @return
     * @throws JSONException
     */
    protected abstract void toDecode(JSONObject jsonObject) throws JSONException;


    /**
     * 消息编码
     * @return
     */
    public DeviceMessage decode(String json) throws DeviceProtocolException {
        try {
            JSONObject jsonObject = new JSONObject(json);
            DeviceMessage message;
            switch (Type.valueOf(jsonObject.getString(D_TYPE_KEY))){
                case HANDSHAKE_REQ:
                    message = new HandShakeReq();
                    break;
                case HANDSHAKE_RESP:
                    message = new HandShakeResp();
                    break;
                case PING:
                    message = new Ping();
                    break;
                case PONG:
                    message = new Pong();
                    break;
                case PUSH_REQ:
                    message = new PushReq();
                    break;
                case PUSH_RESP:
                    message = new PushResp();
                    break;
                default:
                    throw new DeviceProtocolException("Unknown Device type " + jsonObject.getString(D_TYPE_KEY));
            }
            message.toDecode(jsonObject);
            return message;
        } catch (JSONException e) {
            throw new DeviceProtocolException("Exception occur,Message is " + json, e);
        }
    }

}
