package com.slu.persistence;

import com.slu.domain.MemberVO;

public interface MemberDAO {
	public void insertMember(MemberVO vo);
	
	public MemberVO readMember(String userid);
	
	public MemberVO readWithPWD(String userid, String userpwd)throws Exception;

}
