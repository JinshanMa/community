package com.daemon.AppServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.daemon.redisclient.AppJedisClient;

public class AppServerDaemon {

	AppJedisClient redisClient = null;

	BufferedReader reader=null;
	
	public AppServerDaemon() {
		redisClient = new AppJedisClient();
		reader = new BufferedReader(new InputStreamReader(System.in));
	}
	public void start() {
		while(true) {
			
			
			String line = null;
			
			try {
				line = reader.readLine();
				String[] msgs = line.split(":");
				if(msgs.length<1)
					continue;
				String cmd = msgs[0];
				String content = msgs[1];
				
				String[] cons =  content.split(",");
				
				switch(cmd) {
					case "subch":
						if(cons.length > 0)
						redisClient.subscribeChannels(cons);
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
		AppServerDaemon daemon = new AppServerDaemon();
		daemon.start();
	}
	
}
