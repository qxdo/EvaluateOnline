package com.core.system.admin.student.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class StudentPojo {

	@Excel(name = "学生学号", needMerge = true)
	private String number;
	@Excel(name = "学生姓名", needMerge = true)
	private String name;
	@Excel(name = "学生性别", needMerge = true)
	private Integer sex;
	@Excel(name = "学生邮箱", needMerge = true)
	private String email;
	@Excel(name = "学生电话", needMerge = true)
	private Integer phone;
	@Excel(name = "班级ID", needMerge = true)
	private Long classid;
	@Excel(name = "班级编号", needMerge = true)
	private String classnumber;
	
	
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
	public Long getClassid() {
		return classid;
	}
	public void setClassid(Long classid) {
		this.classid = classid;
	}
	public String getClassnumber() {
		return classnumber;
	}
	public void setClassnumber(String classnumber) {
		this.classnumber = classnumber;
	}
	
}
