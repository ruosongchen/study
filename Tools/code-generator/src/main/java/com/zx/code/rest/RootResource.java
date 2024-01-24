package com.zx.code.rest;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootResource {
	
	@GetMapping("/")
    public String noCrawler() {
        return "User-agent: *\nDisallow: /";
    }
}
