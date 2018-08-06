package com.fortune.redis;

import redis.clients.jedis.JedisPubSub;

public class EventImplment extends JedisPubSub{

	
	 public void onMessage(String channel, String message) {
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
