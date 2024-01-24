
/**
 * Hello world!
 *
 */
package com.zx.code;

import com.zx.code.config.Configs;
import com.zx.code.service.CreateCodeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.zx.code.util.SpringUtils;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(Configs.class)
public class Application {
	public static ConfigurableApplicationContext ctx;

	public static void main(String[] args) {
		try {
			ctx = SpringApplication.run(Application.class, args);
			startCreateCode();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static void startCreateCode() {
		CreateCodeService createCodeService = SpringUtils.getBean(CreateCodeService.class);
		createCodeService.createCode();
		if (ctx != null) {
			ctx.close();
		}
	}
}
