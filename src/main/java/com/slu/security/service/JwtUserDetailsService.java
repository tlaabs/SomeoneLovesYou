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

	@Override
	public UserDetails loadUserByUsername(String username){
		MemberVO user = null;
		try{  
			user = memberService.readMember(username);
		}catch(Exception e){}
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		} else {
			return JwtUserFactory.create(user);
		}

	}

}
