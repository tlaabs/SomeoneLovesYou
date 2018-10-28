package com.slu.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.slu.domain.MemberVO;
import com.slu.dto.SignupDTO;
import com.slu.persistence.MemberDAO;
import com.slu.exception.*;

@Service
public class MemberServiceImpl implements MemberService{

	@Inject
	private MemberDAO dao;

	@Override
	public void insertMember(SignupDTO dto){
		// TODO Auto-generated method stub
		//이미 존재하면 런타임 예외 발생
		MemberVO member = dao.read(dto.getUserid());
		if(member != null){
			throw new UserAlreadySignedUpException();
		}else{
			//없는 아이디면 등록 
			dao.insert(dto);
		}
	}

	@Override
	public MemberVO readMember(String userid) {
		// TODO Auto-generated method stub
		return dao.read(userid);
	}

	@Override
	public MemberVO readWithPWD(String userid, String userpwd) throws Exception {
		// TODO Auto-generated method stub
		return dao.readWithPWD(userid, userpwd);
	}

	@Override
	public void updateMember(String userid, String usernpwd1, String usernpwd2) throws Exception{
//		 TODO Auto-generated method stub
		
		//pwd1와 pwd2가 맞으면 일단 패스함.
		if(compareToPasswords(usernpwd1, usernpwd2)){
			dao.update(userid, usernpwd1);
		}else{
			throw new Exception("비번 불일치");
		}	
	}
	
	private boolean compareToPasswords(String usernpwd1, String usernpwd2){
		if(usernpwd1.equals(usernpwd2)) return true;
		return false;
	}

	@Override
	public void updateMemberWithNoPWD(MemberVO vo) {
		// TODO Auto-generated method stub
		dao.updateWithNoPWD(vo);
	}
	
	
}