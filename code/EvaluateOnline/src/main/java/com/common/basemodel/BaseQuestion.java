package com.common.basemodel;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseQuestion<M extends BaseQuestion<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return getInt("id");
	}

	public void setTitle(java.lang.String title) {
		set("title", title);
	}

	public java.lang.String getTitle() {
		return getStr("title");
	}

	public void setContent(java.lang.String content) {
		set("content", content);
	}

	public java.lang.String getContent() {
		return getStr("content");
	}

	public void setTemplate(java.lang.String template) {
		set("template", template);
	}

	public java.lang.String getTemplate() {
		return getStr("template");
	}

	public void setKeywords(java.lang.String keywords) {
		set("keywords", keywords);
	}

	public java.lang.String getKeywords() {
		return getStr("keywords");
	}

	public void setInput(java.lang.String input) {
		set("input", input);
	}

	public java.lang.String getInput() {
		return getStr("input");
	}

	public void setOutput(java.lang.String output) {
		set("output", output);
	}

	public java.lang.String getOutput() {
		return getStr("output");
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
