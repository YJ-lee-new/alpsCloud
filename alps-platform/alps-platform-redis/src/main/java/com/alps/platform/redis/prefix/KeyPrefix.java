package com.alps.platform.redis.prefix;

/**
 * @author:Yujie.lee
 * Date:2019年11月21日
 * TodoTODO
 */
public interface KeyPrefix {
		
	public int expireSeconds();
	
	public String getPrefix();
	
}
