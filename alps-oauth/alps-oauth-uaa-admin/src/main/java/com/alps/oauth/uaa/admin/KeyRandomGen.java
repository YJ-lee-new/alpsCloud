/**
 *@date:2019年12月11日
 *@author:Yujie.Lee
 *TODO
 */
package com.alps.oauth.uaa.admin;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.alps.common.oauth2.utils.RandomValueUtils;

/**
 * @author:Yujie.lee
 * Date:2019年12月11日
 * Todo 手动生成 appKey,appSecretKey以及password
 */
public class KeyRandomGen {

	/**
	 *date:2019年12月11日
	 *Author:Yujie.lee
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String appKey = RandomValueUtils.randomAlphanumeric(32);
        String appSecretKey = RandomValueUtils.randomAlphanumeric(32);
        String password  = encryptPassword(appSecretKey);
        System.out.println("appKey === > " +appKey);
        System.out.println("appSecretKey === > " +appSecretKey);
        System.out.println("password === > " +password);
        

	}
	
	  public static PasswordEncoder passwordEncoder() {
	      return new BCryptPasswordEncoder();
	    }
	  
	  public static String encryptPassword(String str)
	    {
	    	return passwordEncoder().encode(str);
	    }

}
