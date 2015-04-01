package com.elasticsearch;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
public class ElasticSearchBulkAndSearchTest {
	
	public static void insert() throws Exception{
		Client client = ESUtils.getClient(); 
		BulkRequestBuilder bulkRequest = client.prepareBulk(); 
        for(int i=500;i<1000;i++){ 
            //业务对象 
            String json = JsonUtil.obj2JsonData(new Medicine(i,"name"+i,"function"+i)); 
            IndexRequestBuilder indexRequest = ESUtils.getClient().prepareIndex("index-bulktest", "type-bulktest") 
            //指定不重复的ID       
            .setSource(json).setId(String.valueOf(i)); 
            //添加到builder中 
            bulkRequest.add(indexRequest); 
        } 
           
        BulkResponse bulkResponse = bulkRequest.execute().actionGet(); 
        if (bulkResponse.hasFailures()) { 
            // process failures by iterating through each bulk response item 
            System.out.println(bulkResponse.buildFailureMessage()); 
        }
        client.close();
	}
	
	public static void search() throws Exception{
		Client client = ESUtils.getClient(); 
        BoolQueryBuilder query = QueryBuilders.boolQuery(); 
        //function为字段名称，1为字段值 
        query.mustNot(QueryBuilders.termQuery("function", "function501")); 
        SearchResponse response = client.prepareSearch("index-bulktest").setTypes("type-bulktest")
        //设置查询条件和排序条件
        .addSort("id", SortOrder.DESC).setQuery(query).setFrom(0).setSize(600).execute().actionGet(); 
        /**
         * SearchHits是SearchHit的复数形式，表示这个是一个列表
         */ 
        SearchHits shs = response.getHits(); 
        for(SearchHit hit : shs){ 
            System.out.println(hit.getSourceAsString()); 
        } 
        client.close();
	}
	
	public static void filter() throws Exception{
		Client client = ESUtils.getClient(); 
		SearchResponse response = client.prepareSearch("index-bulktest").setTypes("type-bulktest")
        //设置查询条件, 
        .setPostFilter(FilterBuilders.matchAllFilter()).addSort("id", SortOrder.ASC).setFrom(0).setSize(1000).execute().actionGet(); 
        /**
         * SearchHits是SearchHit的复数形式，表示这个是一个列表
         */ 
        SearchHits shs = response.getHits(); 
        for(SearchHit hit : shs){ 
            System.out.println("id:"+hit.getId()+":"+hit.getSourceAsString()); 
        } 
        client.close();
	}
	
	public static void main(String[] args)  throws Exception{
//		insert();
//		search();
//		filter();
//		ESUtils.getClient().prepareDelete("index-bulktest","type-bulktest","503").execute().actionGet();//根据id进行删除
		ESUtils.deleteIndex("index-bulktest");//删除整个index
	}
}
