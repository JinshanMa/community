package com.fortune.redis.test;

import java.util.ArrayList;
import java.util.List;

import com.fortune.redis.PubSubClient;

import junit.framework.TestCase;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class SubscribeTest extends TestCase{

	public void pSubscribeTest(){
		List<String> list = new ArrayList<String>();
		
		JedisPoolConfig  config = new JedisPoolConfig();
		
		config.setMaxIdle(5);
		config.setMaxTotal(10);
		config.setMaxWaitMillis(10000);
		
		JedisPool pool = new JedisPool(config,"192.168.107.129", 6379);
		Jedis jedis = pool.getResource();
		
		PubSubClient client = new PubSubClient(jedis);
		client.multiSubscribeChannel(list);
		
	}
	
}
