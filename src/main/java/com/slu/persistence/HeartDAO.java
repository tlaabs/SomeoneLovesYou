package com.slu.persistence;

import java.util.List;

import com.slu.domain.HeartVO;

public interface HeartDAO {
	public void sendHeart(HeartVO vo);
	public List<HeartVO> getHeartHistory(String id);
}
