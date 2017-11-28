package com.core.system.admin.teacher.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class TeacherPojo {

	@Excel(name = "教师工号", needMerge = true)
	private String number;
	@Excel(name = "教师姓名", needMerge = true)
	private String name;
	@Excel(name = "教师性别", needMerge = true)
	private Integer sex;
	@Excel(name = "教师邮箱", needMerge = true)
	private String email;
	@Excel(name = "教师电话", needMerge = true)
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
