package com.slu.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.slu.domain.HeartVO;
import com.slu.persistence.HeartDAO;
import com.slu.persistence.MemberDAO;

@Service
public class HeartServiceImpl implements HeartService{

	@Inject
	private HeartDAO dao;

	@Override
	public void send(HeartVO vo) {
		// TODO Auto-generated method stub
		dao.sendHeart(vo);
	}
	
	

}	
