package com.slu.rest.controller;

import java.util.Objects;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.slu.domain.MemberVO;
import com.slu.dto.SignupDTO;
import com.slu.dto.UpdateMemberDTO;
import com.slu.rest.response.Response;
import com.slu.rest.response.ResponseFactory;
import com.slu.security.JwtAuthenticationRequest;
import com.slu.security.JwtTokenUtil;
import com.slu.security.JwtUser;
import com.slu.security.RespJwtUser;
import com.slu.security.service.JwtAuthenticationResponse;
import com.slu.service.MemberService;

import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("user")
public class UserRestController {

	private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

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

	@ApiOperation(value = "아이디/패스워드로 로그인")
	@RequestMapping(value = "login/idpwd", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {

		try{
			//아이디 비번일치 확인
			authenticate(authenticationRequest.getUserid(), authenticationRequest.getUserpwd());


			// Reload password post-security so we can generate the token
			final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserid());
			final String token = jwtTokenUtil.generateToken(userDetails);

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

	private JwtUser authenticateByToken(HttpServletRequest request) throws Exception{
		try{
			String token = request.getHeader(tokenHeader).substring(7);
			String username = jwtTokenUtil.getUsernameFromToken(token);
			JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
			return user;
		}catch(Exception e){
			throw new Exception("토큰 에러");
		}
	}

	@ApiOperation(value = "토큰으로 로그인")	
	@RequestMapping(value = "login/token", method = RequestMethod.POST)
	public ResponseEntity<?> getAuthenticatedUser(HttpServletRequest request) {
		try{
			JwtUser user = authenticateByToken(request);
			RespJwtUser ruser = new RespJwtUser(
					user.getUsername(),
					user.getUserid(),
					user.getEmail()
					);
			return new ResponseEntity<Response>(
					ResponseFactory.create(ResponseFactory.SUCCESS,"성공",ruser),HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<Response>(
					ResponseFactory.create(ResponseFactory.FAIL,e.getMessage()),HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value="회원가입")
	@ApiImplicitParams({
	})
	@RequestMapping(value = "signup", method = RequestMethod.POST)
	public ResponseEntity signUp(@RequestBody SignupDTO userInfo){
		try{
			memberService.insertMember(userInfo);
		}catch(Exception e){
			return new ResponseEntity<Response>(
					ResponseFactory.create(ResponseFactory.FAIL,"아이디 중복"),HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Response>(
				ResponseFactory.create(ResponseFactory.SUCCESS,"가입완료"),HttpStatus.OK);
	}

	@ApiOperation(value="아이디 중복확인")
	@ApiImplicitParams({
		//		@ApiImplicitParam(name = "userid", value = "아이디", required = true, dataType = "string", paramType = "query")
	})
	//	@ApiResponses({
	//		@ApiResponse(code=200, message = "회원가입 성공")
	//	})
	@RequestMapping(value="checkid", method= RequestMethod.GET)
	public ResponseEntity<?> validateUserId(@RequestParam(value="userid") String userid){
		MemberVO member = memberService.readMember(userid);
		if(member != null){
			return new ResponseEntity<Response>(
					ResponseFactory.create(ResponseFactory.FAIL,"중복"),HttpStatus.CONFLICT);
		}

		return new ResponseEntity<Response>(
				ResponseFactory.create(ResponseFactory.SUCCESS,"사용가능"),HttpStatus.OK);
	}

	@ApiOperation(value="회원정보 수정")
	@RequestMapping(value="update", method=RequestMethod.POST)
	public ResponseEntity<Response> updateMember(
			HttpServletRequest request,
			@RequestBody UpdateMemberDTO dto)
	{
		try{
			System.out.println("udpate start");
			JwtUser user = authenticateByToken(request);

			RespJwtUser ruser = new RespJwtUser(
					user.getUsername(),
					user.getUserid(),
					user.getEmail()
					);
			System.out.println("inner : " + ruser.getUserid());
			System.out.println("pwd : " + dto.getUsernpwd1());
			memberService.updateMember(ruser.getUserid(),dto.getUsernpwd1(),dto.getUsernpwd2());
			
			return new ResponseEntity<Response>(
					ResponseFactory.create(ResponseFactory.SUCCESS,"업데이트 완료"),HttpStatus.OK);

		}catch(Exception e){
			return new ResponseEntity<Response>(
					ResponseFactory.create(ResponseFactory.FAIL,e.getMessage()),HttpStatus.BAD_REQUEST);
		}
	}

}