package com.gopush.protocol.node;

import com.gopush.protocol.exceptions.NodeProtocolException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * go-push
 *
 * @类功能说明：节点服务消息基类
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */

@Slf4j
public abstract class NodeMessage {

    //消息 Type Key
    protected static final String N_TYPE_KEY = "T";

    /**
     * 消息类型
     * 1)心跳请求
     * 2)心跳响应
     * 3)设备上线请求
     * 4)设备上线响应
     * 5)设备断线请求
     * 6)设备断线响应
     * 7)多条消息发送给一个设备请求
     * 8)多条消息发送给一个设备响应
     * 9)单条消息发送给多个设备请求
     * 10)单条消息发送给多个设备响应
     * 11)节点服务信息请求
     * 12)节点服务信息响应
     */
    protected enum Type{
        PING,
        PONG,
        DEVICE_DOCKED_REQ,
        DEVICE_DOCKED_RESP,
        DEVICE_DISCON_REQ,
        DEVICE_DISCON_RESP,
        MULTI_MSG_TO_ONE_DEVICE_REQ,
        MULTI_MSG_TO_ONE_DEVICE_RESP,
        ONE_MSG_TO_MULTI_DEVICE_REQ,
        ONE_MSG_TO_MULTI_DEVICE_RESP,
        NODE_INFO_REQ,
        NODE_INFO_RESP,
    }

    /**
     * 获取节点消息类型
     * @return
     */
    protected abstract Type type();

    /**
     * 节点消息转换
     * @return
     * @throws JSONException
     */
    protected abstract JSONObject toEncode() throws JSONException;


    /**
     * 节点消息编码
     * @return
     */
    public String encode(){
        try{
            JSONObject jsonObject = toEncode();
            if (jsonObject == null){
                jsonObject = new JSONObject();
            }
            jsonObject.putOnce(N_TYPE_KEY,type());
            return jsonObject.toString();
        }catch (JSONException e){
            throw new NodeProtocolException(e);
        }
    }


    /**
     * 节点消息转换
     * @param jsonObject
     * @throws JSONException
     */
    protected abstract void toDecode(JSONObject jsonObject) throws JSONException;


    /**
     * 节点消息解码
     * @param json
     * @return
     * @throws NodeProtocolException
     */
    public NodeMessage decode(String json) throws NodeProtocolException{
        try{
            JSONObject jsonObject = new JSONObject(json);
            NodeMessage message;
            switch (Type.valueOf(jsonObject.getString(N_TYPE_KEY))){
                case PING:
                    message = new Ping();
                    break;
                case PONG:
                    message = new Pong();
                    break;
                case DEVICE_DOCKED_REQ:
                    message = new DeviceDockedReq();
                    break;
                case DEVICE_DOCKED_RESP:
                    message = new DeviceDockedResp();
                    break;
                case DEVICE_DISCON_REQ:
                    message = new DeviceDisconReq();
                    break;
                case DEVICE_DISCON_RESP:
                    message = new DeviceDisconResp();
                    break;
                case MULTI_MSG_TO_ONE_DEVICE_REQ:
                    message = new MultiMessageToOneDeviceReq();
                    break;
                case MULTI_MSG_TO_ONE_DEVICE_RESP:
                    message = new MultiMessageToOneDeviceResp();
                    break;
                case ONE_MSG_TO_MULTI_DEVICE_REQ:
                    message = new OneMessageToMultiDeviceReq();
                    break;
                case ONE_MSG_TO_MULTI_DEVICE_RESP:
                    message = new OneMessageToMultiDeviceResp();
                    break;
                case NODE_INFO_REQ:
                    message = new NodeInfoReq();
                    break;
                case NODE_INFO_RESP:
                    message = new NodeInfoResp();
                    break;
                default:
                    throw new NodeProtocolException("Unknown Node type " + jsonObject.getString(N_TYPE_KEY));

            }
            message.toDecode(jsonObject);
            return message;
        } catch (JSONException e){
            throw  new NodeProtocolException("Exception occur,Message is "+json, e);
        }
    }

}
