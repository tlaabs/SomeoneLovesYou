package com.slu.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.slu.domain.MemberVO;
import com.slu.security.JwtTokenUtil;
import com.slu.security.JwtUser;
import com.slu.service.MemberService;

import io.jsonwebtoken.ExpiredJwtException;

@RestController
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Value("${jwt.header}")
	private String tokenHeader;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	//	DB의 유저정보를 불러오는 역할을 한다.
	@Autowired
	@Qualifier("jwtUserDetailsService")
	private UserDetailsService userDetailsService;

	@Inject
	private MemberService memberService;

	@RequestMapping(value = "user", method = RequestMethod.GET)
	public ResponseEntity<?> getAuthenticatedUser(HttpServletRequest request) {
		try{
			String token = request.getHeader(tokenHeader).substring(7);

			String username = jwtTokenUtil.getUsernameFromToken(token);
			JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
			return new ResponseEntity<JwtUser>(user,HttpStatus.ACCEPTED);
		}catch(ExpiredJwtException e){
			return new ResponseEntity<String>("Token expired", HttpStatus.BAD_REQUEST); 
		}
	}

	@RequestMapping(value = "users", method = RequestMethod.GET)
	public String time()throws Exception{
		//		return memberService.getTime();
		MemberVO user = memberService.readMember("devsim");
		return user.getEmail();
	}

}