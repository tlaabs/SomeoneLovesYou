package com.slu.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	
	@Value("${jwt.header}")
	private String tokenHeader;
	
	@RequestMapping(value="/user", method = RequestMethod.GET)
	public String user(){
		return tokenHeader;
	}
}
