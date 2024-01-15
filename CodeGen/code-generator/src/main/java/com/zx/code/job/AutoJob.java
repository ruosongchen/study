package com.zx.code.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AutoJob{

	@Scheduled(cron = "0 0/1 * * * ?")
	public void close() {
//		Tools.info("我要退出了。。。。");
		//Application.ctx.close();
	}

}
