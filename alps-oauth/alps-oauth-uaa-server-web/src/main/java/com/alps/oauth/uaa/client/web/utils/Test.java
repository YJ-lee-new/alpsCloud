/**  
* <p>Title: Test.java</p>  

* <p>Description: </p>  

* <p>Copyright: Copyright (c) YJ.lee 2020</p>  

* @author YJ.lee  

* @date 2020年5月29日  

* @version 1.0  

*/  
package com.alps.oauth.uaa.client.web.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**  
* <p>Title: Test</p>  

* <p>Description: </p>  

* @author YJ.lee  

* @date 2020年5月29日  

*/
public class Test {

	public static void main(String[] args) {
		
		String hashPass = passwordEncoder().encode("123456");
		String hashPass2 = "$2a$10$nB60AgnhpNNor7w2FQlcF.ZpXfz9xMJGr.oeo6YX7qn/jQ29M9XG2";
        System.out.println(hashPass);
 
        boolean f = passwordEncoder().matches("123456",hashPass);
        System.out.println(f);
        boolean f2 = passwordEncoder().matches("EZOhaZ77fnxIu3JnxVxPp3susWM8G4Xs",hashPass2);
        System.out.println(f2);


	}
	
	  public static PasswordEncoder passwordEncoder(){
	        return new  BCryptPasswordEncoder();
	    }

}
