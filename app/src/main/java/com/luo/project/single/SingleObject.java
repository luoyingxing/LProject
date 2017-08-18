package com.luo.project.single;

/**
 * SingleObject
 * <p>
 * Created by luoyingxing on 2017/8/17.
 */

public class SingleObject {
    private long time = System.currentTimeMillis();

    private SingleObject() {
    }

    public static SingleObject getInstance() {
        return SingleHolder.singleObject;
    }

    private static class SingleHolder {
        private static SingleObject singleObject = new SingleObject();
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}