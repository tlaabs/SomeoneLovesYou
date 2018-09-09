package com.slu.rest.controller;

import java.util.Objects;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.slu.domain.MemberVO;
import com.slu.security.JwtAuthenticationRequest;
import com.slu.security.JwtTokenUtil;
import com.slu.security.service.JwtAuthenticationResponse;
import com.slu.service.MemberService;

@RestController
public class AuthenticationRestController {
	
	@Value("${jwt.header}")
	private String tokenHeader;

	//    @Autowired
	//    private AuthenticationManager authenticationManager;

	@Inject
	private MemberService memberService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	@Qualifier("jwtUserDetailsService")
	private UserDetailsService userDetailsService;
	
	

	@RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
		
		try{
		//아이디 비번일치 확인
		authenticate(authenticationRequest.getUserid(), authenticationRequest.getUserpwd());


		// Reload password post-security so we can generate the token
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserid());
		final String token = jwtTokenUtil.generateToken(userDetails);

		// Return the token
		//200, 성공
		return ResponseEntity.ok(new JwtAuthenticationResponse(token));
		}catch(Exception e){
			//인증 실패시 401 에러 출력
			return new ResponseEntity<String>("no",HttpStatus.UNAUTHORIZED);
		}
	}


	@ExceptionHandler({AuthenticationException.class})
	public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	private void authenticate(String userid, String userpwd) throws Exception{
		Objects.requireNonNull(userid);
		Objects.requireNonNull(userpwd);


		MemberVO user = memberService.readWithPWD(userid, userpwd);
		if(user == null) throw new Exception();
	}


}
