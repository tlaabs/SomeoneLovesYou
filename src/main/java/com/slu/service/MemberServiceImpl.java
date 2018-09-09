package com.slu.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.slu.domain.MemberVO;
import com.slu.dto.SignupDTO;
import com.slu.persistence.MemberDAO;

@Service
public class MemberServiceImpl implements MemberService{

	@Inject
	private MemberDAO dao;

	@Override
	public void insertMember(SignupDTO dto) throws Exception{
		// TODO Auto-generated method stub
		MemberVO member = dao.readMember(dto.getUserid());
		if(member != null){
			throw new Exception();
		}else{
			dao.insertMember(dto);
		}
	}

	@Override
	public MemberVO readMember(String userid) {
		// TODO Auto-generated method stub
		return dao.readMember(userid);
	}

	@Override
	public MemberVO readWithPWD(String userid, String userpwd) throws Exception {
		// TODO Auto-generated method stub
		return dao.readWithPWD(userid, userpwd);
	}
}