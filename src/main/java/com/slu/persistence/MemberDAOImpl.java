package com.slu.persistence;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.slu.domain.MemberVO;

@Repository
public class MemberDAOImpl implements MemberDAO{
	
	@Inject
	private SqlSession sqlSession;
	
	private static final String namespace =
			"com.slu.mapper.MemberMapper";
	@Override
	public String getTime() {
		// TODO Auto-generated method stub
		sqlSession.selectOne(namespace+".getTime");
		return null;
	}

	@Override
	public void insertMember(MemberVO vo) {
		// TODO Auto-generated method stub
		sqlSession.insert(namespace+".insertMember", vo);
	}

	@Override
	public MemberVO readMember(String userid) throws Exception {
		// TODO Auto-generated method stub
		return (MemberVO)
				sqlSession.selectOne(namespace+".selectMember", userid);
	}
	
	
}
