package com.slu.persistence;

import com.slu.domain.MemberVO;
import com.slu.dto.SignupDTO;

public interface MemberDAO {
	public void insert(SignupDTO dto);
	
	public MemberVO read(String userid);
	
	public MemberVO readWithPWD(String userid, String userpwd)throws Exception;
	
	public void update(String userid, String usernpwd) throws Exception;
	
	public void updateWithNoPWD(MemberVO dto);
}
