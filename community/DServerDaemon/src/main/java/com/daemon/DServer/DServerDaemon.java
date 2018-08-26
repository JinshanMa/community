package com.daemon.DServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.daemon.redisclient.DJedisClient;

public class DServerDaemon {

	DJedisClient redisClient = null;

	BufferedReader reader=null;
	
	public DServerDaemon() {
		redisClient = new DJedisClient("devId1");
		reader = new BufferedReader(new InputStreamReader(System.in));
	}
	public void start() {
		boolean isInitSubs = false;
		//redisClient.start();
		System.out.println("Please input subscribe channel (or channels):");
		while(true) {
			
			
			String line = null;
			
			try {
				line = reader.readLine();
				String[] msgs = line.split(":");
				if(msgs.length<=1)
					continue;
				String cmd = msgs[0];
				String content = msgs[1];
				
				String[] cons =  content.split(",");
				
				switch(cmd) {
					case "subch":
						if(!isInitSubs){
							
							if(cons.length > 0){
								redisClient.subscribeChannels(cons);
								isInitSubs = true;
								redisClient.start();
								
								continue;
							}
							else{
								System.out.println("subscribe channels is empty!");
							}
						}else{
							System.out.println("has been inited channels!");
							continue;
						}
					break;
					case "pubmsg":
						if(cons.length == 2) {
							String channel = cons[0];
							String msg = cons[1];
							redisClient.publishChannelMsg(channel, msg);
						}
						
					break;
				}
				
//				System.out.println("channel:"+msgs[0]+",message:"+msgs[1]);
//				
//				redisClient.publishChannelMsg(channel, message);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public static void main(String[] args) {
		DServerDaemon daemon = new DServerDaemon();
		daemon.start();
	}
	
}
