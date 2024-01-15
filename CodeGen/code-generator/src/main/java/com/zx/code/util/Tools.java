package com.zx.code.util;

import org.slf4j.LoggerFactory;

public class Tools {

	private static final String logName = "GE";

	public static void error(Throwable e) {
		error(e.getMessage(), e);
	}

	public static void error(String msg, Throwable e) {
		LoggerFactory.getLogger(logName).error(msg, e);
	}

	public static void error(String msg) {
		LoggerFactory.getLogger(logName).error(msg);
	}

	public static void info(String msg) {
		LoggerFactory.getLogger(logName).info(msg);
	}

}
