package com.daemon.redisclient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fortune.redis.JedisClient;

public class DJedisClient {


	JedisClient jedis = null;
	
	public DJedisClient() {
		
		jedis = new JedisClient();
//		jedis.initSubPub();
	}
	
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

	
	public void start() {
		jedis.initSubPub();
	}
	public void publishChannelMsg(String channel,String msg) {
		jedis.publishMsgByChannel(channel, msg);
	}
	
}
