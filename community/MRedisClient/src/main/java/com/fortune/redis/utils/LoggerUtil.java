package com.fortune.redis.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

	public static Logger getClassLogger(Class<?> clazz) {
		return LoggerFactory.getLogger(clazz);
	}
	
	
}
