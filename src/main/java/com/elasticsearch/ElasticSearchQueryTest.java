package com.elasticsearch;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ElasticSearchQueryTest {
	public static void main(String[] args) throws IOException{
		search();
	}
	public static void search() throws IOException {
        // 自定义集群结点名称
        String clusterName = "rain_es";
        
        //程序中更改集群结点名称 并且设置client.transport.sniff为true来使客户端去嗅探整个集群的状态
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", clusterName).put("client.transport.sniff", true).build();   
        
        //创建客户端对象
        TransportClient client = new TransportClient(settings);
        client.addTransportAddress(new InetSocketTransportAddress("192.168.1.123", 9300));
        client.addTransportAddress(new InetSocketTransportAddress("192.168.1.143", 9300));
        // 创建查询索引,参数indexdemo表示要查询的索引库为indexdemo
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("indexdemo");

        // 设置查询索引类型,setTypes("typetest", "typetest2","typetest3");
        // 用来设定在多个类型中搜索
        searchRequestBuilder.setTypes("typetest");

        // 设置查询类型 1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询 2.SearchType.SCAN = 扫描查询,无序
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);

        // 设置查询关键词
        searchRequestBuilder.setQuery(QueryBuilders.matchQuery("function", "银花"));

        // 查询过滤器过滤价格在1-10内 这里范围为[1,10]区间闭包含,搜索结果包含价格为1和价格为10的数据
        searchRequestBuilder.setPostFilter(FilterBuilders.rangeFilter("id").from(6).to(8));

        // 分页应用
        searchRequestBuilder.setFrom(0).setSize(60);

        // 设置是否按查询匹配度排序
        searchRequestBuilder.setExplain(true);
        
        //设置高亮显示
        searchRequestBuilder.addHighlightedField("function");
        searchRequestBuilder.setHighlighterPreTags("<span style=\"color:red\">");
         searchRequestBuilder.setHighlighterPostTags("</span>");
        // 执行搜索,返回搜索响应信息
        SearchResponse response = searchRequestBuilder.execute().actionGet();
        
        //获取搜索的文档结果
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            //将文档中的每一个对象转换json串值
            String json = hit.getSourceAsString();
            //将json串值转换成对应的实体对象
            Medicine medicine = mapper.readValue(json, Medicine.class);  
            
            //获取对应的高亮域
            Map<String, HighlightField> result = hit.highlightFields();    
            //从设定的高亮域中取得指定域
            HighlightField titleField = result.get("function");  
            //取得定义的高亮标签
            Text[] titleTexts =  titleField.fragments();    
            //为title串值增加自定义的高亮标签
            String function = "";  
            for(Text text : titleTexts){    
            	function += text;  
            }
            //将追加了高亮标签的串值重新填充到对应的对象
            medicine.setFunction(function);
            //打印高亮标签追加完成后的实体对象
            System.out.println(function);
        }
        System.out.println("search success ..");

    }
}
