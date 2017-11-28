package com.common.basemodel;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseAdmin<M extends BaseAdmin<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Long id) {
		set("id", id);
	}

	public java.lang.Long getId() {
		return getLong("id");
	}

	public void setNumber(java.lang.String number) {
		set("number", number);
	}

	public java.lang.String getNumber() {
		return getStr("number");
	}

	public void setPassword(java.lang.String password) {
		set("password", password);
	}

	public java.lang.String getPassword() {
		return getStr("password");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return getStr("name");
	}

	public void setType(java.lang.Integer type) {
		set("type", type);
	}

	public java.lang.Integer getType() {
		return getInt("type");
	}

	public void setSex(java.lang.Integer sex) {
		set("sex", sex);
	}

	public java.lang.Integer getSex() {
		return getInt("sex");
	}

	public void setEmail(java.lang.String email) {
		set("email", email);
	}

	public java.lang.String getEmail() {
		return getStr("email");
	}

	public void setPhone(java.lang.String phone) {
		set("phone", phone);
	}

	public java.lang.String getPhone() {
		return getStr("phone");
	}

	public void setFlag(java.lang.Integer flag) {
		set("flag", flag);
	}

	public java.lang.Integer getFlag() {
		return getInt("flag");
	}

	public void setCreatetime(java.util.Date createtime) {
		set("createtime", createtime);
	}

	public java.util.Date getCreatetime() {
		return get("createtime");
	}

	public void setCreateby(java.lang.String createby) {
		set("createby", createby);
	}

	public java.lang.String getCreateby() {
		return getStr("createby");
	}

	public void setModifytime(java.util.Date modifytime) {
		set("modifytime", modifytime);
	}

	public java.util.Date getModifytime() {
		return get("modifytime");
	}

	public void setModifyby(java.lang.String modifyby) {
		set("modifyby", modifyby);
	}

	public java.lang.String getModifyby() {
		return getStr("modifyby");
	}

}
