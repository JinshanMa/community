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
	String redisIp = "192.168.8.40";
	
	//此处可做成配置文件配置
	int port = 6379;
	
	JedisPoolConfig config = new JedisPoolConfig();
	
	ChannelResQueue queue = new ChannelResQueue();
	
	SubsribeThread subThread = null;
	
	Publisher publisher = null;
	
	public JedisClient(){
			
		config.setMaxIdle(10);
		config.setMaxTotal(100);
		config.setMaxWaitMillis(10000);
		config.setMinIdle(0);
		
		JedisPool jedisPool = new JedisPool(config,redisIp,port);
		
		log.info("redis pool is start, ip:"+redisIp+",port:"+port);

		subThread = new SubsribeThread(jedisPool,new Subscriber());
		
		publisher = new Publisher(jedisPool, queue);
			
	}
	public void initSubPub() {
		
		subThread.start();
		
		publisher.start();
		
	}
	
	public void publishMsgByChannel(String channel,String msg) {
		ChannelMessage cm = new ChannelMessage();
		cm.setChannel(channel);
		cm.setMessage(msg);
		publisher.putRes(cm);
		
	}
	
	public void addSubscribeChannel(String channel) {
		
		subThread.addSubcribeChannel(channel);
	}
	public void addSubscribeChannels(List<String> channels) {
		
		subThread.addSubcribeChannels(channels);
	}
}
