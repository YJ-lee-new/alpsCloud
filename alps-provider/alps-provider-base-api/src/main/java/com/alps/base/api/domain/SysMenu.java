package com.alps.base.api.domain;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class SysMenu implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int menuId;
	private String menuName;
	private int parentId;
	private int orderNum;
	private String url;
	private List<SysMenu> children;
	
	
	
	
	

}
