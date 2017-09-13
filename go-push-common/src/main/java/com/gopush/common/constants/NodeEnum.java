package com.gopush.common.constants;


import lombok.Getter;

import java.util.Arrays;

/**
 * @author chenxiangqi
 * @date 2017/9/14 上午12:13
 */
@Getter
public enum  NodeEnum {

    OK(200,"OK"),
    FAIL(500,"FAIL")
    ;


    private int code;
    private String descri;

    NodeEnum(int code,String descri){
        this.code = code;
        this.descri = descri;
    }

    public static NodeEnum fromCode(int code) {
        if (code <= 0) {
            return null;
        }
        return Arrays.stream(NodeEnum.values())
                .filter(a -> code == a.getCode())
                .findFirst()
                .orElse(null);
    }
}
