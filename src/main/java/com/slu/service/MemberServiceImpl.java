package com.slu.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.slu.domain.MemberVO;
import com.slu.persistence.MemberDAO;

@Service
public class MemberServiceImpl implements MemberService{

	@Inject
	private MemberDAO dao;

	@Override
	public void insertMember(MemberVO vo) {
		// TODO Auto-generated method stub
		dao.insertMember(vo);
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