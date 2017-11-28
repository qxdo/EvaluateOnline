package com.core.system.admin.index.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class AdminPojo {

	@Excel(name = "负责人工号", needMerge = true)
	private String number;
	@Excel(name = "负责人姓名", needMerge = true)
	private String name;
	@Excel(name = "负责人性别", needMerge = true)
	private Integer sex;
	@Excel(name = "负责人邮箱", needMerge = true)
	private String email;
	@Excel(name = "负责人电话", needMerge = true)
	private Integer phone;
	
	
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
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getPhone() {
		return phone;
	}
	public void setPhone(Integer phone) {
		this.phone = phone;
	}
	
}
