package com.luo.project.entity;

/**
 * ApiMsg
 * <p/>
 * Created by luoyingxing on 16/10/10.
 */
public class ApiMsg {
    private int errNum;
    private String errMsg;

    public ApiMsg() {
    }

    public ApiMsg(int errNum, String errMsg) {
        this.errNum = errNum;
        this.errMsg = errMsg;
    }

    public int getErrNum() {
        return errNum;
    }

    public void setErrNum(int errNum) {
        this.errNum = errNum;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {
        return "ApiMsg{" +
                "errNum=" + errNum +
                ", errMsg='" + errMsg + '\'' +
                '}';
    }

    public static boolean isApiMsg(String json) {
        String msgRegExp = "\\{\"errNum\":-?\\d*,\"errMsg\":\"[\\s\\S]*\"\\}";
        return json != null && json.matches(msgRegExp);
    }
}