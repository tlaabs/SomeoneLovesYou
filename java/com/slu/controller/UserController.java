package com.slu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	
	@Value("${jwt.header}")
	private String tokenHeader;
	
//	DB�� ���������� �ҷ����� ������ �Ѵ�.
	@Autowired
	@Qualifier("jwtUserDetailsService")
	private UserDetailsService userDetailsService;
	
	@RequestMapping(value="/user", method = RequestMethod.GET)
	public String user(){
		return tokenHeader;
	}
}
