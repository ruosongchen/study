package com.zx.code.rest;


import java.io.File;
import java.io.IOException;

import com.zx.code.service.CreateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zx.code.Application;
import com.zx.code.util.Tools;

@RestController
@RequestMapping("/test")
public class CreateCodeResource{
	
	@Autowired
	private CreateCodeService service;
	
	@GetMapping("/createCode")
    public boolean createCode() {
		service.createCode();
        return true;
    }
	
	@GetMapping("/restart")
	public void restart() {
		String currentPath = System.getProperty("user.dir");
		Tools.info(currentPath);
		try {
			Runtime.getRuntime().exec("cmd /k start " + currentPath + File.separator + "restart.bat");
		} catch (IOException e) {
			Tools.error(e);
		}
		try {
			Tools.info("我要退出了。。。。");
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Tools.error(e);
		}
		Application.ctx.close();
	}

}
