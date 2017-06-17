package com.gopush.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;


/**
 * go-push
 *
 * @类功能说明： Gson 工具类
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/17 下午10:22
 * @VERSION：
 */
public class GsonUtil {

    private static Gson gson = null;

    static {
        if (gson == null){
            gson =
                    new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .enableComplexMapKeySerialization()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();
        }
    }

    public static String gson(Object object){
        return gson.toJson(object);
    }

    public static <T> T gson(String json,Class<T> cls){
        return gson.fromJson(json,cls);
    }


    public static <T> List<T> gsonToList(String json, Class<T> cls){
        return gson.fromJson(json,new TypeToken<List<T>>(){}.getType());
    }


}
