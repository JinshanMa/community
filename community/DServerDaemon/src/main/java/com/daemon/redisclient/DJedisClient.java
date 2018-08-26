package com.daemon.redisclient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fortune.redis.JedisClient;

public class DJedisClient {

	
	

	JedisClient jedis = null;
	
	//创建JedisClient客户端
	public DJedisClient(String jedisClientId) {
		
		jedis = new JedisClient(jedisClientId);
//		jedis.initSubPub();
	}
	
	//订阅频道
	public void subscribeChannels(String ...strings) {
		
		if(strings.length > 1) {
			String[] channels = strings.clone();
			List<String> listChannels = new ArrayList<String>();
			for(String s:channels) {
				listChannels.add(s);
			}
			jedis.addSubscribeChannels(listChannels);
		}
		if(strings.length == 1) {
			String channel = strings[0];
			jedis.addSubscribeChannel(channel);
		}
		
	}

	//启动jedis
	public void start() {
		jedis.initSubPub();
	}
	
	//发送向特定的频道发送消息
	public void publishChannelMsg(String channel,String msg) {
		jedis.publishMsgByChannel(channel, msg);
	}
	
}
