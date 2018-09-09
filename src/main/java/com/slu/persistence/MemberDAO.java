package com.slu.persistence;

import com.slu.domain.MemberVO;
import com.slu.dto.SignupDTO;

public interface MemberDAO {
	public void insertMember(SignupDTO dto);
	
	public MemberVO readMember(String userid);
	
	public MemberVO readWithPWD(String userid, String userpwd)throws Exception;

}
