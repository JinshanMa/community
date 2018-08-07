package com.fortune.redis;

import org.slf4j.Logger;

import com.fortune.redis.queue.ChannelMessage;
import com.fortune.redis.queue.ChannelResQueue;
import com.fortune.redis.utils.LoggerUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Publisher extends Thread {
	
	private static Logger log;
	static {
		if(log == null)
			log = LoggerUtil.getClassLogger(SubsribeThread.class);
	}
	
	private ChannelResQueue resPool;
	
	private final JedisPool jedisPool;
	
	private final Jedis jedis;
	
	public Publisher(JedisPool pool,ChannelResQueue queue) {
	
		if(pool != null) {
			jedisPool = pool;
			jedis = jedisPool.getResource();
		}else {
			jedisPool = null;
			jedis = null;
		}
		if(queue != null) {
			resPool = queue;
		}else {
			resPool = null;
		}
	}
	
	public void run() {
		while(!isInterrupted()) {
			ChannelMessage channelMsg = null;
			try {
				
				channelMsg = getResPool().getChannelRes();
				System.out.println("redis send---,");
				jedis.publish(channelMsg.getChannel(), channelMsg.getMessage());
				if(log != null)
				log.info("publish Channel:"+channelMsg.getChannel()+",message:"+channelMsg.getMessage());
			} catch (InterruptedException e) {
				e.printStackTrace();
				log.info("jedis publish message throws error:"+e.getMessage());
			}
			
		}
	}
	
	public void putRes(ChannelMessage channelMsg) {
		getResPool().putChannelRes(channelMsg);
	}
	
	private ChannelResQueue getResPool() {
		return resPool;
	}
	
}
