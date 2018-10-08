package com.slu.rest.controller;

import java.util.ArrayList;
import java.util.List;

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

import com.slu.domain.HeartVO;
import com.slu.domain.MemberVO;
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

	//	DB�� ���������� �ҷ����� ������ �Ѵ�.
	@Autowired
	@Qualifier("jwtUserDetailsService")
	private UserDetailsService userDetailsService;

	@Inject
	private HeartService heartService;
	
	@RequestMapping(value = "send", method = RequestMethod.POST)
	public ResponseEntity<Response> sendHeart(@RequestBody HeartVO vo){
		
		try{
			heartService.send(vo);
			
			//200, ����
			return new ResponseEntity<Response>(
					ResponseFactory.create(ResponseFactory.SUCCESS,"����"),HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<Response>(
					ResponseFactory.create(ResponseFactory.FAIL,"����"),HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "gets", method = RequestMethod.POST)
	public ResponseEntity<Response> getHeartHistory(@RequestBody MemberVO vo){
		try{
			System.out.println(vo.getUserid());
			List<HeartVO> arr = heartService.getHistory(vo.getUserid());
			System.out.println("size : " + arr.size());
			//200, ����
			return new ResponseEntity<Response>(
					ResponseFactory.create(ResponseFactory.SUCCESS,"����",arr),HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<Response>(
					ResponseFactory.create(ResponseFactory.FAIL,"����"),HttpStatus.BAD_REQUEST);
		}
	}
	
	

}
