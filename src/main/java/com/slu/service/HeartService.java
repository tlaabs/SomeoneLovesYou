package com.slu.service;

import java.util.List;

import com.slu.domain.HeartVO;

public interface HeartService {
	public void send(HeartVO vo);
	public List<HeartVO> getHistory(String id);
}
