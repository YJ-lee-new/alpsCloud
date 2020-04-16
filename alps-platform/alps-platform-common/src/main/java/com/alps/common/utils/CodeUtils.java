/**
 *@date:2019年11月20日
 *@author:Yujie.Lee
 *TODO
 */
package com.alps.common.utils;

/**
 * @author:Yujie.lee
 * Date:2019年11月20日
 * TodoTODO
 */
public enum CodeUtils {
	 SUCCESS(0),
	    ERROR(1);

    private Integer code;
    CodeUtils(Integer code){
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
