package com.fortune.redis;

import java.util.List;

import org.slf4j.Logger;

import com.fortune.redis.queue.ChannelMessage;
import com.fortune.redis.queue.ChannelResQueue;
import com.fortune.redis.test.ScannerTest;
import com.fortune.redis.utils.LoggerUtil;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisClient {
	public static Logger  log = LoggerUtil.getClassLogger(JedisClient.class);
	
	//此处可做成配置文件配置
	String redisIp = "192.168.58.128";
	
	//此处可做成配置文件配置
	int port = 6379;
	
	String clientId;
	
	JedisPoolConfig config = new JedisPoolConfig();
	
	ChannelResQueue queue = new ChannelResQueue();
	
	InitSubsribeThread subThread = null;
	
	Publisher publisher = null;
	
	public JedisClient(String jedisClientId){
			
		if(jedisClientId != null)
			clientId = jedisClientId;
		else{
			
			log.error("jedis clientId is null can not connect to server！");
			return ;
		}
		config.setMaxIdle(10);
		config.setMaxTotal(100);
		config.setMaxWaitMillis(10000);
		config.setMinIdle(0);
		
		JedisPool jedisPool = new JedisPool(config,redisIp,port);
		
		log.info("redis pool is start, ip:"+redisIp+",port:"+port);

		Subscriber subscriber = new Subscriber(jedisPool.getResource(),clientId);
		
		subThread = new InitSubsribeThread(jedisPool.getResource(),subscriber,clientId);
		
		publisher = new Publisher(jedisPool, queue);
			
	}
	public void initSubPub() {
		
		subThread.start();
		
		publisher.start();
		
	}
	//发送消息和指定的频道
	public void publishMsgByChannel(String channel,String msg) {
		ChannelMessage cm = new ChannelMessage();
		cm.setChannel(channel);
		cm.setMessage(msg);
		//向消息发送对象的资源队列放进要发送的消息和频道信息
		publisher.putRes(cm);
		
	}
	
	
	//增加单个订阅频道
	public void addSubscribeChannel(String channel) {
		
		subThread.addInitSubcribeChannel(channel);
	}
	
	//增加多个订阅频道
	public void addSubscribeChannels(List<String> channels) {
		//向订阅线程的队列中添加频道消息
		subThread.addInitSubcribeChannels(channels);
	}
}
