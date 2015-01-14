package com.design.mode.decorator;
/**
 * 智能机修饰类
 * @author rain
 *
 */
public class AdvancedPhone extends PhoneDecorator{
	
    public AdvancedPhone(Phone phone){
    	super(phone);
    }
    @Override
    public void feature()  {
        super.feature();
        System.out.println("可以玩愤怒的小鸟！");
    }

}