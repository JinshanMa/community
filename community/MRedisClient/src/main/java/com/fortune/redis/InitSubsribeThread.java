package com.fortune.redis;



import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.fortune.redis.utils.LoggerUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class InitSubsribeThread extends Thread {

	private static Logger log;
	static {
		if(log == null)
			log = LoggerUtil.getClassLogger(InitSubsribeThread.class);
	}	
	//实现消息处理的类
	private final Subscriber subsriber;
	
	//jedis 客户端资源
	Jedis jedis;
	
	
	//该客户端所订阅的所有频道列表
	private final List<String> subsList;
	
	private boolean isFireRun = false;
	
	private String jedisId = null;
	
	private final String serverSubscriberSetKey = "fortuneSubcrbeKey";
	
	/**
	 * 唯一构造函数，设置jedis资源池和订阅消息处理类
	 * @param pool
	 * @param sub
	 */
	public InitSubsribeThread(Jedis jedis,Subscriber sub,String jedisId) {
		
		
		subsriber = sub;
		this.jedisId = jedisId;
		subsList = new ArrayList<String>();
		this.jedis = jedis;
	
		initJedisParam();
	}
	
	private void initJedisParam(){
		
		//此处向redis服务器添加元素，所有的redis客户端的jedisI的，都将成为 key为"fortuneSubcrbeKey"的集合元素
		jedis.sadd("fortuneSubcrbeKey", jedisId);
		//jedis.lpush(jedisId, "ping");
	}
	
	
	/**
	 * 添加单个订阅的频道
	 * @param channel
	 * @return
	 */
	public boolean addInitSubcribeChannel(String channel) {
		
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
	public boolean addInitSubcribeChannels(List<String> channels) {
		
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
		isFireRun = true;
		
		
		if(log != null)
			log.info(String.format("subsribe channels: %s,thread will be blocked", subsList.toString()));
		
		if(subsriber != null) {
			if(jedis != null) {
				
				if(subsList.isEmpty()) {
					if(log != null)
						log.info("substribeChannelList is empty!");
					return ;
				}
				
				try {
					
					for(String subch:subsList){
						
						//开始订阅频道时，先查看是否存在此频道的相关键值信息
						if(jedis.exists(jedisId+".channel."+subch)){
							//如果存在此频道，则说明还有消息未及时取出
							String msg = jedis.rpop(jedisId+".channel."+subch);
							//把消息取出丢给消息处理类去处理
							subsriber.onMessage(subch, msg);
						}
						//向redis服务器中添加频道的相关键值，此处与消息的发布进行配合使用
						jedis.sadd(jedisId+".channel", subch);
						
					}
					//订阅频道，并设置相关的处理类
					jedis.subscribe(subsriber, subsList.toArray(new String[]{}));
					
					//isFireRun = true;
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
