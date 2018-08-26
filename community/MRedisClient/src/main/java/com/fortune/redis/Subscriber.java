package com.fortune.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class Subscriber extends JedisPubSub{

	private Jedis jedis;
	private String jedisId;
	
	public Subscriber(Jedis jedisClient,String jedisId){
		jedis = jedisClient;
		this.jedisId = jedisId;
	}
	
	 public void onMessage(String channel, String message) {
		 //断线时,查看对应的key值是否存在，如存在则，取其消息
		 if(jedis.exists(jedisId+".channel."+channel)){
			 
			 //此处可视为一个队列，从右端弹出数据
			 jedis.rpop(jedisId+".channel."+channel);
			 System.out.println("has remove jedis list message!");
		 }
		 System.out.println("onMessage method:-channel:"+channel+",message:"+message);
	 }
	 

	  public void onPMessage(String pattern, String channel, String message) {
		  
		  System.out.println("onPMessage method:-channel:"+channel+",message:"+message);
	  }

	  public void onSubscribe(String channel, int subscribedChannels) {
		  
		  System.out.println("onSubscribe method:-channel:"+channel+",subscribedChannles:"+subscribedChannels);
	  }

	  public void onUnsubscribe(String channel, int subscribedChannels) {
		  System.out.println("onUnSubscribe method:-channel:"+channel+",subscribedChannles:"+subscribedChannels);
	  }

	  public void onPUnsubscribe(String pattern, int subscribedChannels) {
		  System.out.println("onPSubscribe method:-pattern:"+pattern+",subscribedChannles:"+subscribedChannels);
	  }

	  public void onPSubscribe(String pattern, int subscribedChannels) {
		  System.out.println("onPSubscribe method:-pattern:"+pattern+",subscribedChannles:"+subscribedChannels);
	  }

	  public void onPong(String pattern) {
		  System.out.println("onSubscribe method:-pattern:"+pattern);
	  }
}
