package com.slu.persistence;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.slu.domain.HeartVO;

@Repository
public class HeartDAOImpl implements HeartDAO{
	
	@Inject
	private SqlSession sqlSession;
	
	private static final String namespace =
			"com.slu.mappers.HeartMapper";

	@Override
	public void sendHeart(HeartVO vo) {
		// TODO Auto-generated method stub
		sqlSession.insert(namespace + ".sendHeart", vo);
	}

	@Override
	public List<HeartVO> getHeartHistory(String id) {
		// TODO Auto-generated method stub
		List<HeartVO> arr = sqlSession.selectList(namespace + ".getHeartHistory", id); 
		return arr;
	}
	
	
	
	
	
}
