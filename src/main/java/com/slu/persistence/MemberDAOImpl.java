package com.slu.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.slu.domain.MemberVO;
import com.slu.dto.SignupDTO;

@Repository
public class MemberDAOImpl implements MemberDAO{
	
	@Inject
	private SqlSession sqlSession;
	
	private static final String namespace =
			"com.slu.mappers.MemberMapper";

	@Override
	public void insertMember(SignupDTO dto) {
		// TODO Auto-generated method stub
		sqlSession.insert(namespace+".insertMember", dto);
	}
	
	

	@Override
	public MemberVO readMember(String userid) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(namespace + ".readMember", userid);
	}



	@Override
	public MemberVO readWithPWD(String userid, String userpwd) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userid", userid);
		paramMap.put("userpwd", userpwd);
		return (MemberVO)
				sqlSession.selectOne(namespace+".readWithPWD", paramMap);
	}



	@Override
	public void updateMember(String userid, String usernpwd) throws Exception{
		// TODO Auto-generated method stub
		Map<String,Object> paramMap = new HashMap<String, Object>();
		System.out.println("inner 1: " + userid);
		System.out.println("inner 2: " + usernpwd);
//		
		paramMap.put("userid", userid);
		paramMap.put("usernpwd", usernpwd);
		
		sqlSession.update(namespace + ".updateMember",paramMap);
	}
	
	
	
	
}
