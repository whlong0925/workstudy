package com.elasticsearch;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.fasterxml.jackson.databind.ObjectMapper;


public class ESClient {

    
    /**在运行该测试实例时,已经在本地建立了对应的索引库datum*/
    public static void main(String[] args) {
        
        
        //自定义集群结点名称
        String clusterName = "rain_es"; 
        
        //程序中更改集群结点名称 并且设置client.transport.sniff为true来使客户端去嗅探整个集群的状态
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", clusterName).put("client.transport.ping_timeout", "5s").build();   
        
        //创建客户端对象
        TransportClient client = new TransportClient(settings);
        
        //客户端对象初始化集群内结点,绑定多个ip
        client.addTransportAddress(new InetSocketTransportAddress("192.168.1.123", 9300));
        client.addTransportAddress(new InetSocketTransportAddress("192.168.1.166", 9300));
        
        
        //搜索,根据Id查询
        GetResponse response = client.prepareGet("indexdemo", "typetest", "AUxuyNjOSrjZO9-CUYlY").execute().actionGet();
        
        //查询结果映射成对象类
        ObjectMapper mapper = new ObjectMapper();
        Medicine medicine= mapper.convertValue(response.getSource(), Medicine.class);
        
        System.out.println("编号:" + medicine.getId() +"\tfunction:"+medicine.getFunction()  );
        
        //构造查询器查询,第一个参数为要查询的关键字,第二个参数为要检索的索引库中的对应索引类型的域
        QueryBuilder query = QueryBuilders.multiMatchQuery("银花", "function");  
        //第一个参数indexdemo表示索引库,第二个参数typetest表示索引类型,from表示开始的位置 size表示查询的条数 ,类似mysql中的limit3,5
        SearchResponse searchResponse = client.prepareSearch("indexdemo").setTypes("typetest").setQuery(query).setFrom(0).setSize(5).execute().actionGet(); 
        
 
        //将搜索结果转换为list集合对象
        List<Medicine> lists  = getBeans(searchResponse);
        
        System.out.println("查询出来的结果数:" + lists.size());
        for(Medicine dtm: lists){
            System.out.println("编号:" + dtm.getId() +"\tfunction:"+dtm.getFunction());
        }
        
        //关闭客户端
        client.close();    

    }
    
    public static Medicine getResponseToObject(GetResponse response){
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(response.getSource(), Medicine.class);
    }
    
    
    public static List<Medicine> getBeans(SearchResponse response) {
        SearchHits hits = response.getHits();
        ObjectMapper mapper = new ObjectMapper();
        List<Medicine> medicineList = new ArrayList<Medicine>();
        for (SearchHit hit : hits) {  
            String json = hit.getSourceAsString();
            Medicine dtm = new Medicine();
           
            try {
                dtm = mapper.readValue(json, Medicine.class);
                medicineList.add(dtm);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        return medicineList;
    }
    
}