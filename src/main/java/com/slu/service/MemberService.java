package com.slu.service;

import com.slu.domain.MemberVO;
import com.slu.dto.SignupDTO;

public interface MemberService {
	
	public void insertMember(SignupDTO dto) throws Exception;
	
	public MemberVO readMember(String userid);
	
	public MemberVO readWithPWD(String userid, String userpwd)throws Exception;
	
	public void updateMember(String userid, String usernpwd1, String usernpwd2) throws Exception;
	
	public void updateMemberWithNoPWD(MemberVO vo);
}
