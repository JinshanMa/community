package com.fortune.redis;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class PubSubClient {

	
	Jedis jedis;
	
	JedisPubSub jedisPubSub;
	
	public PubSubClient(Jedis jedis){
		if(jedis !=	null){
			this.jedis = jedis;
			this.jedisPubSub = new EventImplment();
		}
	}
	
	public PubSubClient(Jedis jedis,JedisPubSub jedisPubSub){
		if(jedis !=	null && jedisPubSub != null){
			this.jedis = jedis;
			this.jedisPubSub = jedisPubSub;
		}
		
	}
	
	
	public void subscribeChannel(String channel){
		if(jedisPubSub != null){
			jedis.subscribe(jedisPubSub,channel);
		}
		else{
			
		}
		
	}
	
	public void multiSubscribeChannel(List<String> channelList){
		if(jedisPubSub != null){
		
			jedis.psubscribe(jedisPubSub,"helll111");
		}
		else{
			
		}
	}
	
}
