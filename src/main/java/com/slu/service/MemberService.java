package com.slu.service;

import com.slu.domain.MemberVO;

public interface MemberService {
	
	public String getTime();
	
	public void insertMember(MemberVO vo);
	
	public MemberVO readMember(String userid)throws Exception;
	
}
