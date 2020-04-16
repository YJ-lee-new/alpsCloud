/**
 *@date:2019年10月23日
 *@author:Yujie.Lee
 *TODO
 */
package com.alps.zuul.service;

import org.springframework.stereotype.Component;

/**
 * @author:Yujie.lee
 * Date:2019年10月23日
 * TodoTODO
 */
@Component
public class FeignWebIterfaceImp  implements FeignWebInterface {

	/* (non-Javadoc)
	 * @see com.FeignServiceInterface#callClient()
	 */
	@Override
	public  String callClient() {
		return "alps web is  down!";
		// TODO Auto-generated method stub
	}

}
