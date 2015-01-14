package com.design.mode.decorator;
/**
 * 山寨机装饰类
 * @author rain
 *
 */
public class ShanZhaiPhone extends PhoneDecorator {
    public ShanZhaiPhone(Phone phone) {
    	super(phone);
    }
    @Override
    public void feature() {
        super.feature();
        System.out.println("可以双卡双待！");
    }
}