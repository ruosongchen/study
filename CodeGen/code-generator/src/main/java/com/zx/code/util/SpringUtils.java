package com.zx.code.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SpringUtils {
	
	private static ApplicationContext context;

	@Autowired
	public SpringUtils(ApplicationContext context) { this.context = context; }

    public static <T> T getBean(String name) {
    	return (T) context.getBean(name);
    }
    
    public static <T> T getBean(Class<?> clazz) {
    	return (T) context.getBean(clazz);
    }
}
