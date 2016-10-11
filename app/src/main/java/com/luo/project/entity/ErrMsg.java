package com.luo.project.entity;

/**
 * ErrMsg
 * <p/>
 * Created by luoyingxing on 16/10/11.
 */
public class ErrMsg {

    private static final int PARSE_ERROR = 60001;
    private static final String PARSE_ERROR_MSG = "数据类型解析错误";

    public static ApiMsg parseError() {
        return new ApiMsg(PARSE_ERROR, PARSE_ERROR_MSG);
    }
}