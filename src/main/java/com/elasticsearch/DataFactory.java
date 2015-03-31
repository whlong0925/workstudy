package com.elasticsearch;
import java.util.ArrayList;
import java.util.List;

public class DataFactory {
    
    public static DataFactory dataFactory = new DataFactory();
    
    private DataFactory(){
        
    }
    
    public DataFactory getInstance(){
        return dataFactory;
    }
    
    public static List<String> getInitJsonData(){
        List<String> list = new ArrayList<String>();
        String data1  = JsonUtil.obj2JsonData(new Medicine(1,"a b c","test abc"));
        String data2  = JsonUtil.obj2JsonData(new Medicine(2,"d e f","test def"));
        String data3  = JsonUtil.obj2JsonData(new Medicine(3,"h i j","test h i j"));
        String data4  = JsonUtil.obj2JsonData(new Medicine(4,"k l m","test k l m"));
        String data5  = JsonUtil.obj2JsonData(new Medicine(5,"n o p","test n o p"));
        String data6  = JsonUtil.obj2JsonData(new Medicine(6,"银花 感冒 颗粒","功能主治：银花感冒颗粒 ，头痛,清热，解表，利咽。"));
        String data7  = JsonUtil.obj2JsonData(new Medicine(7,"感冒  止咳糖浆","功能主治：感冒止咳糖浆,解表清热，止咳化痰。"));
        String data8  = JsonUtil.obj2JsonData(new Medicine(8,"感冒灵颗粒","功能主治：解热镇痛。头痛 ,清热。"));
        String data9  = JsonUtil.obj2JsonData(new Medicine(9,"感冒  灵胶囊","功能主治：银花感冒颗粒 ，头痛,清热，解表，利咽。"));
        String data10  = JsonUtil.obj2JsonData(new Medicine(10,"仁和 感冒 颗粒","功能主治：疏风清热，宣肺止咳,解表清热，止咳化痰。"));
        
        list.add(data1);
        list.add(data2);
        list.add(data3);
        list.add(data4);
        list.add(data5);
        list.add(data6);
        list.add(data7);
        list.add(data8);
        list.add(data9);
        list.add(data10);
        return list;
    }
}