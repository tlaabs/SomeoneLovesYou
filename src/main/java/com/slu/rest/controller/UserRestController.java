package com.slu.rest.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.slu.domain.MemberVO;
import com.slu.dto.SignupDTO;
import com.slu.rest.response.Response;
import com.slu.rest.response.ResponseFactory;
import com.slu.security.JwtTokenUtil;
import com.slu.security.JwtUser;
import com.slu.service.MemberService;

import io.jsonwebtoken.ExpiredJwtException;
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

	//	DB�� ���������� �ҷ����� ������ �Ѵ�.
	@Autowired
	@Qualifier("jwtUserDetailsService")
	private UserDetailsService userDetailsService;

	@Inject
	private MemberService memberService;
	
	@ApiOperation(value = "�α��� ����")	
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
	
	@ApiOperation(value="ȸ������")
	@ApiImplicitParams({
	})
	@RequestMapping(value = "signup", method = RequestMethod.POST)
	public ResponseEntity signUp(@RequestBody SignupDTO userInfo){
		try{
		memberService.insertMember(userInfo);
		}catch(Exception e){
			return new ResponseEntity<Response>(
					ResponseFactory.create(ResponseFactory.FAIL,"���̵� �ߺ�"),HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Response>(
				ResponseFactory.create(ResponseFactory.SUCCESS,"���ԿϷ�"),HttpStatus.OK);
	}
	
	@ApiOperation(value="���̵� �ߺ�Ȯ��")
	@ApiImplicitParams({
//		@ApiImplicitParam(name = "userid", value = "���̵�", required = true, dataType = "string", paramType = "query")
	})
//	@ApiResponses({
//		@ApiResponse(code=200, message = "ȸ������ ����")
//	})
	@RequestMapping(value="checkid", method= RequestMethod.GET)
	public ResponseEntity<?> validateUserId(@RequestParam(value="userid") String userid){
		MemberVO member = memberService.readMember(userid);
		if(member != null){
			return new ResponseEntity<Response>(
					ResponseFactory.create(ResponseFactory.FAIL,"�ߺ�"),HttpStatus.CONFLICT);
		}

		return new ResponseEntity<Response>(
				ResponseFactory.create(ResponseFactory.SUCCESS,"��밡��"),HttpStatus.OK);
	}

}