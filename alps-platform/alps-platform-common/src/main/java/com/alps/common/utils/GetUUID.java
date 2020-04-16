package com.alps.common.utils;

import java.util.UUID;

public class GetUUID {
	
	//获取UUID
	public static String getGUID() { 
		 UUID uuid = UUID.randomUUID(); 
		 return uuid.toString().replace("-", "");
	} 

}
