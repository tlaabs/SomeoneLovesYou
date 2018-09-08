package com.slu.rest.response;

import java.util.HashMap;

//¼³°è : https://github.com/yhbyun/money/wiki/API-%EC%84%A4%EA%B3%84
public class ResponseFactory {
	
	public final static int SUCCESS = 1; 

	public final static int FAIL = 0;
	
	private ResponseFactory(){}
	
	private static ResponseFactory getInstance(){
		return new ResponseFactory();
	}
	
	public static Response create(int status){
		Response res = new Response();
		if(status == SUCCESS){
			res.setStatus("SUCCESS");
		}else{
			res.setStatus("FAIL");
		}
		
		res.setMessage("");
		res.setResults(new HashMap());
		
		return res;
	}
	
	public static Response create(int status, String message){
		Response res = ResponseFactory.create(status);
		res.setMessage(message);
		res.setResults(new HashMap());
		
		return res;
	}
	
	public static Response create(int status, String message, Object results){
		Response res = ResponseFactory.create(status, message);
		res.setResults(results);
		
		return res;
	}

}
