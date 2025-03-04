package com.api.domain.main.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/")
public class MainController {
	
	@GetMapping("/")
	public String rootUrl() {
	    return "알빠몬 API 서버(60085)";
	}

}
