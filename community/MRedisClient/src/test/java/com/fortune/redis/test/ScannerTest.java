package com.fortune.redis.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.fortune.redis.Publisher;
import com.fortune.redis.queue.ChannelMessage;

public class ScannerTest{

	private Publisher publishClient = null;
	
	public ScannerTest(Publisher pub){
	
		publishClient = pub;
		
	}
	
	
	public void start() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		while(true) {
			
			String line = null;
			
			try {
				line = reader.readLine();
				String[] msgs = line.split(":");
				System.out.println("msgs[0]:"+msgs[0]+",msgs[1]:"+msgs[1]);
				ChannelMessage cm = new ChannelMessage();
				cm.setChannel(msgs[0]);
				cm.setMessage(msgs[1]);
				publishClient.putRes(cm);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
}
