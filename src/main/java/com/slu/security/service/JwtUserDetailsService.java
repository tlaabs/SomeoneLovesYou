package com.slu.security.service;

import javax.inject.Inject;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.slu.domain.MemberVO;
import com.slu.security.JwtUserFactory;
import com.slu.service.MemberService;

@Service
public class JwtUserDetailsService implements UserDetailsService{

	//	@Autowired
	//	private UserRepository userRepository;
	@Inject
	private MemberService memberService;

	//메서드명은 Username이지만 실체는 userid를 받음.
	@Override
	public UserDetails loadUserByUsername(String userid){
		MemberVO user = null;
		try{  
			user = memberService.readMember(userid);
		}catch(Exception e){}
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", userid));
		} else {
			return JwtUserFactory.create(user);
		}

	}

}
