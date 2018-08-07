package com.fortune.redis;



import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.fortune.redis.utils.LoggerUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class SubsribeThread extends Thread {

	private static Logger log;
	static {
		if(log == null)
			log = LoggerUtil.getClassLogger(SubsribeThread.class);
	}

	//jedis 客户端资源池
	private final JedisPool jedisPool;
	
	//实现消息处理的类
	private final Subscriber subsriber;
	
	//jedis 客户端资源
	Jedis jedis;
	
	//该客户端所订阅的所有频道列表
	private final List<String> subsList;
	
	
	/**
	 * 唯一构造函数，设置jedis资源池和订阅消息处理类
	 * @param pool
	 * @param sub
	 */
	public SubsribeThread(JedisPool pool,Subscriber sub) {
		
		jedisPool = pool;
		subsriber = sub;
		subsList = new ArrayList<String>();
	}
	
	/**
	 * 添加当个订阅的频道
	 * @param channel
	 * @return
	 */
	public boolean addSubcribeChannel(String channel) {
		
		if(subsList != null) {
			subsList.add(channel);
			return true;
		}else {
			if(log != null)
				log.info("subsList is not initial!");
			return false;
		}
	}
	
	/**
	 * 添加多个订阅频道
	 * @param channels
	 * @return
	 */
	public boolean addSubcribeChannels(List<String> channels) {
		
		if(subsList != null) {
			subsList.addAll(channels);
			
			return true;
		}else {
			if(log != null)
				log.info("subsList is not initial!");
			return false;
		}
		
	}
	
	
	/**
	 * 在执行前请确定已初始化各个必要的参数
	 */
	public void run() {
		
		if(subsList.isEmpty()) {
			if(log != null)
				log.info("substribeChannelList is empty! exiting thread!");
			return ;
		}
		
		if(log != null)
			log.info(String.format("subsribe channels: %s,thread will be blocked", subsList.toString()));
		
		if(jedisPool != null && subsriber != null) {
			jedis = jedisPool.getResource();
			if(jedis != null) {
				
				try {
					jedis.subscribe(subsriber, subsList.toArray(new String[]{}));
				}catch(Exception e) {
					
					if(log != null)
						log.info("jedis subscribe interface is throws error:"+e.getMessage());
				}finally {
					if(jedis != null) {
						if(log != null)
							log.info("jedis closing...");
						jedis.close();
					}
				}
			}else {
				if(log != null)
					log.error("get jedis resource from jedispool error!");
			}
			
		}else {
			if(log != null)
				log.error("jedispool or subsriber is null!");
		}
		
	
	}
	
	
}
