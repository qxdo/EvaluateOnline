package com.core.system.admin.clazz.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class ClazzPojo {
	
	@Excel(name = "班级编号", needMerge = true)
	private String number;
	@Excel(name = "班级名称", needMerge = true)
	private String name;
	
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
