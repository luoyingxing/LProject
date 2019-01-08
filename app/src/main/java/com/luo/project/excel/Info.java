package com.luo.project.excel;

/**
 * author:  luoyingxing
 * date: 2019/1/8.
 */
public class Info {
    public String provider;
    public String card;
    public String number;
    public String information;

    public Info() {
    }

    public Info(String provider, String card, String number, String information) {
        this.provider = provider;
        this.card = card;
        this.number = number;
        this.information = information;
    }

    @Override
    public String toString() {
        return provider + "   " + card + "   " + number + "   " + information + "   ";
    }
}
