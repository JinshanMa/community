package com.daemon.redisclient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fortune.redis.JedisClient;

public class AppJedisClient {


	JedisClient jedis = null;
	
	public AppJedisClient(String jedisClientId) {
		
		jedis = new JedisClient(jedisClientId);
//		jedis.initSubPub();
	}
	//订阅频道，此处只在redis客户端初始化的时候调用，同时进行频道的订阅
	public void subscribeChannels(String ...strings) {
		
		if(strings.length > 1) {
			String[] channels = strings.clone();
			List<String> listChannels = new ArrayList<String>();
			for(String s:channels) {
				listChannels.add(s);
			}
			//调用MRedisClient工程模块的接口进行频道订阅
			jedis.addSubscribeChannels(listChannels);
		}
		if(strings.length == 1) {
			String channel = strings[0];
			jedis.addSubscribeChannel(channel);
		}
		
	}

	
	public void start() {
		jedis.initSubPub();
	}
	public void publishChannelMsg(String channel,String msg) {
		jedis.publishMsgByChannel(channel, msg);
	}
	
}
