package com.common.util;



import java.util.Map;


import com.fasterxml.jackson.databind.ObjectMapper;



public class JSONUtil {
	private static ObjectMapper mapper = null;
	public static ObjectMapper getMapper(){
		if(mapper == null){
			mapper = new ObjectMapper();
		}
		return mapper;
	}
	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(String json) throws Exception{
		return getMapper().readValue(json, Map.class);
	}
	
	
	

}
