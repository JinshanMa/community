package com.fortune.redis.queue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

public class ChannelResQueue {
	
	LinkedBlockingDeque<ChannelMessage> res = new LinkedBlockingDeque<ChannelMessage>();
	
	public ChannelMessage getChannelRes()throws InterruptedException  {
		return res.take();
	}
	
	public void putChannelRes(ChannelMessage channelMsg) {
		res.add(channelMsg);
	}
	
	public void removeRes(ChannelMessage channelMsg) {
		res.remove(channelMsg);
	}
	
}
