package com.luo.project.retrofit;

import java.io.Serializable;
import java.util.List;

/**
 * Focus
 * <p/>
 * Created by luoyingxing on 16/10/19.
 */
public class Focus implements Serializable {
    private String reason;
    private Result result;
    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public  class Result {
        private String stat;
        private List<FocusDetails> data;

        public String getStat() {
            return stat;
        }

        public void setStat(String stat) {
            this.stat = stat;
        }

        public List<FocusDetails> getData() {
            return data;
        }

        public void setData(List<FocusDetails> data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "stat='" + stat + '\'' +
                    ", data=" + data +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Focus{" +
                "reason='" + reason + '\'' +
                ", result=" + result +
                ", error_code=" + error_code +
                '}';
    }
}