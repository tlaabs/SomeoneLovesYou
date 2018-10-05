package com.slu.rest.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.slu.rest.response.Response;
import com.slu.rest.response.ResponseFactory;
import com.slu.security.JwtAuthenticationRequest;
import com.slu.security.JwtTokenUtil;
import com.slu.security.service.JwtAuthenticationResponse;
import com.slu.service.HeartService;
import com.slu.service.MemberService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("heart")
public class HeartRestController {
	private static final Logger logger = LoggerFactory.getLogger(HeartRestController.class);

	@Value("${jwt.header}")
	private String tokenHeader;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	//	DB의 유저정보를 불러오는 역할을 한다.
	@Autowired
	@Qualifier("jwtUserDetailsService")
	private UserDetailsService userDetailsService;

	@Inject
	private HeartService memberService;
	
	@RequestMapping(value = "send", method = RequestMethod.POST)
	public ResponseEntity<?> sendHeart(){
		try{

			// Return the token
			//200, 성공
			return new ResponseEntity<Response>(
					ResponseFactory.create(
							ResponseFactory.SUCCESS,"",new JwtAuthenticationResponse(token)),
					HttpStatus.OK
					);
		}catch(Exception e){
			//인증 실패시 401 에러 출력
			return new ResponseEntity<Response>(
					ResponseFactory.create(ResponseFactory.FAIL,"실패"),HttpStatus.UNAUTHORIZED);
		}
	}

}
