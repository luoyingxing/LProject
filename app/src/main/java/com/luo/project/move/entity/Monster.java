package com.luo.project.move.entity;

/**
 * Monster
 * <p/>
 * Created by luoyingxing on 2018/7/15.
 */
public class Monster {
    private int speed; //速度(像素/s)
    private int HP;  //生命值

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }
}