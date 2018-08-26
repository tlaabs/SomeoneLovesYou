package com.slu.web;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//테스트 코드를 실행할 때 스프링이 로딩되도록 하는 부분
@RunWith(SpringJUnit4ClassRunner.class)
//이 경로의 xml 파일을 이용해서 스프링이 로딩됨.
@ContextConfiguration(
		locations = {"file:src/main/webapp/WEB-INF/spring/**/*.xml"})
public class MyBatisTest {
	
	@Inject 
	SqlSessionFactory sqlFactory;
	
	@Test 
	public void testFactory(){
		System.out.println(sqlFactory);
	}
	
	@Test
	public void testSession() throws Exception{
		try(SqlSession session = sqlFactory.openSession()){
			System.out.println(session);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
