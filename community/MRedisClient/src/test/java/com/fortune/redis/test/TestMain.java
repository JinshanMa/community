package com.fortune.redis.test;

import org.slf4j.Logger;

import com.fortune.redis.Publisher;
import com.fortune.redis.Subscriber;
import com.fortune.redis.SubsribeThread;
import com.fortune.redis.queue.ChannelMessage;
import com.fortune.redis.queue.ChannelResQueue;
import com.fortune.redis.utils.LoggerUtil;

import junit.framework.TestCase;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class TestMain extends TestCase{

	public static Logger  log = LoggerUtil.getClassLogger(TestMain.class);
	
	
	
	public static void main(String[] args) {
		
		String redisIp = "192.168.8.40";
		int port = 6379;
		
		JedisPoolConfig config = new JedisPoolConfig();
		
		config.setMaxIdle(10);
		config.setMaxTotal(100);
		config.setMaxWaitMillis(10000);
		config.setMinIdle(0);
		
		JedisPool jedisPool = new JedisPool(config,redisIp,port);
		
		ChannelResQueue queue = new ChannelResQueue();
		
		log.info("redis pool is start, ip:"+redisIp+",port:"+port);
		
		
		SubsribeThread subThread = new SubsribeThread(jedisPool,new Subscriber());
		
		Publisher publisher = new Publisher(jedisPool, queue);
		
		subThread.addSubcribeChannel("testChannel");
		
		subThread.start();
		
		publisher.start();
	
		ScannerTest t = new ScannerTest(publisher);
		t.start();
		
				
	}
	
	
	
	
}
