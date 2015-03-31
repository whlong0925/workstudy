package com.elasticsearch;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ElasticSearchTest{

	public static void main(String[] args) {

		// 当你启动一个节点,它会自动加入同网段的es集群,一个前提就是es的集群名(cluster.name)这个参数要设置一致。
		String clusterName = "rain_es"; // 集群结点名称

		nodeTest(clusterName);
		transportTest(clusterName);
	}
	/**
	 * 1.通过在程序中创建一个嵌入es节点（Node），使之成为es集群的一部分，然后通过这个节点来与es集群通信.
	 * @param clusterName
	 */
	public static void nodeTest(String clusterName){
		/**
		 * 默认的话启动一个节点,es集群会自动给它分配一些索引的分片,如果你想这个节点仅仅作为一个客户端而不去保存数据,
		 * 你就可以设置把node.data设置成false或 node.client设置成true。
		 */
		Node node = NodeBuilder.nodeBuilder().clusterName(clusterName).client(true).node();

		// 启动结点,加入到指定集群
		node.start();

		// 获取节点搜索端,使用prepareGet搜索indexdemo索引库中 索引类型为typetest,的索引记录唯一_id值为"AUxuyNjOSrjZO9"的记录
		GetResponse response = node.client().prepareGet("indexdemo", "typetest", "AUxuyNjOSrjZO9-CUYlY").execute().actionGet();

		// 对象映射模型
		ObjectMapper mapper = new ObjectMapper();
		// 将搜索结果response中的值转换成指定的对象模型,Datum是自己建立的一个咨询Model对象
		System.out.println(response.getSource());
		Medicine medicine = mapper.convertValue(response.getSource(),Medicine.class);

		System.out.println("Node search result:NAME=" + medicine.getName());
		// 关闭结点
		node.close();
	}
	/**
	 * 2.用TransportClient这个接口和es集群通信.
	 * @param clusterName
	 */
	public static void transportTest(String clusterName){
		//程序中更改集群结点名称
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", clusterName).build();
        
        //创建集群,绑定集群内的机器
        TransportClient client = new TransportClient(settings);
        client.addTransportAddress(new InetSocketTransportAddress("192.168.1.123", 9300));
        client.addTransportAddress(new InetSocketTransportAddress("192.168.1.143", 9300));
        
        //搜索
        GetResponse response = client.prepareGet("indexdemo", "typetest", "AUxuyNjOSrjZO9-CUYlY").execute() .actionGet();
        
        ObjectMapper mapper = new ObjectMapper();
        Medicine medicine= mapper.convertValue(response.getSource(), Medicine.class);
        
        System.out.println("Transport search result:NAME=" + medicine.getName());
        
        //关闭结点
        client.close();    
	}
}
