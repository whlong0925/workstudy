package com.design.mode.decorator;
/**
 * 一般的手机类
 * @author rain
 *
 */
public class CommonPhone implements Phone  {
    public void feature()  {
        System.out.println("能发短信和打电话.");
    }
}