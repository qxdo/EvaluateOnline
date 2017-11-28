package com.core.common.vo;

public class CommonCourse {
	private long id;
	private String coursename;
	private String teachername;
	private int questionsum;
	private int questionsuccess;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCoursename() {
		return coursename;
	}
	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}
	public String getTeachername() {
		return teachername;
	}
	public void setTeachername(String teachername) {
		this.teachername = teachername;
	}
	public int getQuestionsum() {
		return questionsum;
	}
	public void setQuestionsum(int questionsum) {
		this.questionsum = questionsum;
	}
	public int getQuestionsuccess() {
		return questionsuccess;
	}
	public void setQuestionsuccess(int questionsuccess) {
		this.questionsuccess = questionsuccess;
	}
	@Override
	public String toString() {
		return "CommonCourse [id=" + id + ", coursename=" + coursename + ", teachername=" + teachername
				+ ", questionsum=" + questionsum + ", questionsuccess=" + questionsuccess + "]";
	}
	
	
}
