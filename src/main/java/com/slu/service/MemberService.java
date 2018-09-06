package com.slu.service;

import com.slu.domain.MemberVO;

public interface MemberService {
	
	public void insertMember(MemberVO vo);
	
	public MemberVO readMember(String userid);
	
	public MemberVO readWithPWD(String userid, String userpwd)throws Exception;
	
}
