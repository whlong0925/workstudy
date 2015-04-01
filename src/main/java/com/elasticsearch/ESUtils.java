package com.elasticsearch;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

/**
 * ES工具类
 */
public class ESUtils {

	private static Client clients;

	private ESUtils() {
	}

	public static void flush(Client client, String indexName, String indexType)  throws Exception{
		client.admin().indices().flush(new FlushRequest(indexName.toLowerCase(), indexType)).actionGet();
	}

	/**
	 * 根据默认系统默认配置初始化库,如果已经有连接则使用该连接
	 * 
	 * @return
	 */
	public static Client getClient()  throws Exception{

		if (clients != null) {
			return clients;
		}
		synchronized (ESUtils.class) {
			if (clients != null) {
				return clients;
			}
			clients = newClient();
		}
		return clients;
	}

	/**
	 * 初始化并连接elasticsearch集群，返回连接后的client
	 * 
	 * @return 返回连接的集群的client
	 */
	public static Client newClient()  throws Exception{
		String clusterName = "rain_es"; //集群名称
		boolean clientTransportSniff = true;//设置client.transport.sniff为true来使客户端去嗅探整个集群的状态
		int port = 9300;
		String hostname = "192.168.1.123,192.168.1.143";
		String hostnames[] = hostname.split(",");
		return newClient(clusterName, clientTransportSniff, port, hostnames);

	}

	/**
	 * 初始化并连接elasticsearch集群，返回连接后的client
	 * 
	 * @param clusterName  中心节点名称
	 * @param clientTransportSniff 是否自动发现新加入的节点
	 * @param port 节点端口
	 * @param hostname  集群节点所在服务器IP，支持多个
	 * @return 返回连接的集群的client
	 */
	public static Client newClient(String clusterName,boolean clientTransportSniff, int port, String... hostname) {
		
		Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", clusterName).put("client.transport.sniff", clientTransportSniff).build();
		TransportClient transportClient = new TransportClient(settings);
		if (hostname != null) {
			for (String host : hostname) {
				transportClient.addTransportAddress(new InetSocketTransportAddress(host, port));
			}
		}
		return transportClient;
	}

	public static boolean indicesExists(Client client, String indexName) {
		IndicesExistsRequest ier = new IndicesExistsRequest();
		ier.indices(new String[] { indexName.toLowerCase() });
		return client.admin().indices().exists(ier).actionGet().isExists();
	}

	public static boolean typesExists(Client client, String indexName,String indexType) {
		if (indicesExists(client, indexName)) {
			TypesExistsRequest ter = new TypesExistsRequest(new String[] { indexName.toLowerCase() }, indexType);
			return client.admin().indices().typesExists(ter).actionGet().isExists();
		}
		return false;
	}

	/**
	 * 根据索引数据id删除索引
	 * 
	 * @param indexName  索引名称
	 * @param indexType 索引类型
	 * @param id  对应数据ID
	 */
	public static void deleteIndex(Client client, String indexName,String indexType, String id)  throws Exception{
		client.prepareDelete(indexName.toLowerCase(),indexType.toLowerCase(), id).execute().actionGet();
	}

	/**
	 * 根据索引名称删除索引
	 * @param indexName 索引名称
	 */
	public static void deleteIndex(String indexName) throws Exception{
		IndicesExistsRequest ier = new IndicesExistsRequest();
		ier.indices(new String[] { indexName.toLowerCase() });
		boolean exists = getClient().admin().indices().exists(ier).actionGet().isExists();
		if (exists) {
			getClient().admin().indices().prepareDelete(indexName.toLowerCase()).execute().actionGet();
		}
	}
}
