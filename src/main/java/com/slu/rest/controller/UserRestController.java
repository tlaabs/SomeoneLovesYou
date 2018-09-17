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

	//	DB�� ���������� �ҷ����� ������ �Ѵ�.
	@Autowired
	@Qualifier("jwtUserDetailsService")
	private UserDetailsService userDetailsService;

	@Inject
	private MemberService memberService;

	@ApiOperation(value = "���̵�/�н������ �α���")
	@RequestMapping(value = "login/idpwd", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {

		try{
			//���̵� �����ġ Ȯ��
			authenticate(authenticationRequest.getUserid(), authenticationRequest.getUserpwd());


			// Reload password post-security so we can generate the token
			final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserid());
			final String token = jwtTokenUtil.generateToken(userDetails);

			// Return the token
			//200, ����
			return new ResponseEntity<Response>(
					ResponseFactory.create(
							ResponseFactory.SUCCESS,"",new JwtAuthenticationResponse(token)),
					HttpStatus.OK
					);
		}catch(Exception e){
			//���� ���н� 401 ���� ���
			return new ResponseEntity<Response>(
					ResponseFactory.create(ResponseFactory.FAIL,"����"),HttpStatus.UNAUTHORIZED);
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
			throw new Exception("��ū ����");
		}
	}

	@ApiOperation(value = "��ū���� �α���")	
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
					ResponseFactory.create(ResponseFactory.SUCCESS,"����",ruser),HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<Response>(
					ResponseFactory.create(ResponseFactory.FAIL,e.getMessage()),HttpStatus.BAD_REQUEST);
		}
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

	@ApiOperation(value="ȸ������ ����")
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
					ResponseFactory.create(ResponseFactory.SUCCESS,"������Ʈ �Ϸ�"),HttpStatus.OK);

		}catch(Exception e){
			return new ResponseEntity<Response>(
					ResponseFactory.create(ResponseFactory.FAIL,e.getMessage()),HttpStatus.BAD_REQUEST);
		}
	}

}