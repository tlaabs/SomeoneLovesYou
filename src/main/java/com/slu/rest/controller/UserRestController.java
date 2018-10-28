package com.slu.rest.controller;

import java.util.ArrayList;
import java.util.List;
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

import com.slu.analyzer.AnalyzeResult;
import com.slu.analyzer.EmotionAnalyzer;
import com.slu.domain.MemberVO;
import com.slu.dto.SignupDTO;
import com.slu.dto.UpdateMemberDTO;
import com.slu.exception.UserAlreadySignedUpException;
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
			//아이디 비번일치 확인, 없는 계정이거나 정보가 올바르지 않으면 예외 발생
			authenticate(authenticationRequest.getUserid(), authenticationRequest.getUserpwd());

			// Reload password post-security so we can generate the token
			final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserid());
			//유저 정보를 가져와 토큰을 생성.
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
			//토큰 분리
			String token = request.getHeader(tokenHeader).substring(7);
			//토큰을 통해 userid 가져옴. 메서드명 오류
			String userid = jwtTokenUtil.getUsernameFromToken(token);
			JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(userid);
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
			/**
			 * RespJwtUser에는 아이디가 name, name이 아이디로 초기화되어있다.
			 * 상속문제로 문제 미해결
			 * 일단 RespJwtUser에 임시로 담아두자.  
			 */
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
		}catch(UserAlreadySignedUpException e){
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

	@ApiOperation(value="회원 패스워드 변경")
	@RequestMapping(value="update", method=RequestMethod.POST)
	public ResponseEntity<Response> updateMember(
			HttpServletRequest request,
			@RequestBody UpdateMemberDTO dto)
	{
		try{
			//토큰을 통해 사용자 정보를 가져옴.
			JwtUser user = authenticateByToken(request);

			RespJwtUser ruser = new RespJwtUser(
					user.getUsername(),
					user.getUserid(),
					user.getEmail()
					);
			
			memberService.updateMember(ruser.getUserid(),dto.getUsernpwd1(),dto.getUsernpwd2());
			
			return new ResponseEntity<Response>(
					ResponseFactory.create(ResponseFactory.SUCCESS,"업데이트 완료"),HttpStatus.OK);

		}catch(Exception e){
			return new ResponseEntity<Response>(
					ResponseFactory.create(ResponseFactory.FAIL,e.getMessage()),HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value="패스워드 없이 회원 정보 변경")
	@RequestMapping(value="update/nopwd", method=RequestMethod.POST)
	public ResponseEntity<Response> updateWithNoPwd(
			HttpServletRequest request,
			@RequestBody MemberVO vo){
		try{
			//토큰 인증
			JwtUser user = authenticateByToken(request);
			//갱신될 정보로 MemberVO 재구성 
			MemberVO userVO = new MemberVO();
			userVO.setUserid(user.getUsername());
			userVO.setState(vo.getState());
			userVO.setEmotion(vo.getEmotion());
			memberService.updateMemberWithNoPWD(userVO);
			
			MemberVO readUser = memberService.readMember(userVO.getUserid());
			
			return new ResponseEntity<Response>(
					ResponseFactory.create(ResponseFactory.SUCCESS,"성공",readUser),HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<Response>(
					ResponseFactory.create(ResponseFactory.FAIL,e.getMessage()),HttpStatus.BAD_REQUEST);
		}
	}
	
	/*
	 * 유저의 전화 번호(userid)들을 받아서 slu 서비스에 가입되어있는 친구들을 리스트로 반환
	 */
	@ApiOperation(value="모든 친구목록 불러오기")
	@RequestMapping(value="get/friends", method=RequestMethod.POST)
	public List<MemberVO> getFriends(@RequestBody List<MemberVO> vos){
		ArrayList result = new ArrayList<MemberVO>();
		for(int i = 0 ; i < vos.size(); i++){
			MemberVO item = vos.get(i); 
			String user_id = item.getUserid();
			memberService.readMember(user_id);
			MemberVO existMember = memberService.readMember(item.getUserid().trim());

			if(existMember != null){
				//비밀번호를 유출하면 안되니까 하이픈 처리
				existMember.setUserpwd("-");
				result.add(existMember);
			}
		}
		
		return result;
	}
		
	@ApiOperation(value="평문 감정 분석")
	@RequestMapping(value = "emotion", method = RequestMethod.GET)
	public ResponseEntity<Response> emotionAnalyze(@RequestParam(value="profile") String profile) throws Exception{
		AnalyzeResult rs = EmotionAnalyzer.run(profile);
		
		return new ResponseEntity<Response>(
				ResponseFactory.create(ResponseFactory.SUCCESS,"성공",rs),HttpStatus.OK);
	}
}