package com.core.system.teacher.clazz.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.core.common.model.Chapter;
import com.core.common.model.Clazz;
import com.core.common.model.Course;
import com.core.common.model.Part;
import com.core.common.model.Question;
import com.core.common.model.Section;
import com.core.common.model.Student;
import com.core.common.model.Studentexpertiment;
import com.core.common.model.Teacher;
import com.core.common.model.Teachercourseclazz;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;

/**
 * 教师进行班级管理
 * @author Hang
 *
 */
public class ClazzController extends Controller {
	
	/**
	 * 教师进入班级列表
	 */
	public void index(){
		Teacher teacher = getSessionAttr("teacher");
		// 显示教师对应的所有班级及所有课程
		String sql = "";
		List<Teachercourseclazz> teachercourseclazzs = Teachercourseclazz.dao.find(Db.getSql("teacher.findClazzByTeacher"), teacher.getNumber());
		List<Clazz> clazzs = new ArrayList<Clazz>();
		List<Course> courses = new ArrayList<Course>();
		for (Teachercourseclazz teachercourseclazz : teachercourseclazzs) {
			clazzs.add(teachercourseclazz.getClazz());
			courses.add(teachercourseclazz.getCourse());
		}
		setAttr("clazzs", clazzs);
		setAttr("courses", courses);
		setAttr("type", "class");
		render("list.html");
	}
	
	/**
	 * 获取班级学生信息
	 */
	public void list(){
		// 获取班级ID
		String clazzid = this.getPara("clazzid");
		List<Student> students = new ArrayList<Student>();
		if (!clazzid.equals("")) {
			// 通过班级ID获取班级学生信息
			String sql = Db.getSql("teacher.findStudentByClazzid");
			students = Student.dao.find(sql, clazzid);
		}
		renderJson(students);
	}
	
	/**
	 * 进入学生课程信息列表页面（默认第一章第一节第一小节的实验）
	 */
	public void info(){
		// 获取课程ID
		String courseid = this.getPara("courseid");
		// 获取班级ID
		String studentid = this.getPara("studentid");
		System.out.println("studentid:"+studentid);
		// 获取课程章节
		List<Chapter> chapters = Chapter.dao.find("select * from chapter where courseid = ? and flag = 1 ORDER BY sort ASC", courseid);
		setAttr("chapters", chapters);
		Chapter chapter = new Chapter();
		if (null != chapters) {
			chapter = chapters.get(0);
		}
		
		// 获取第一章的小节
		List<Section> sections = Section.dao.find("select * from section where courseid = ? and chapterid = ? and flag = 1 ORDER BY sort ASC", courseid, chapter.getId());
		setAttr("sections", sections);
		Section section = new Section();
		if (null != sections) {
			section = sections.get(0);
		}
		
		List<Part> parts = Part.dao.find("select * from part where courseid =? and chapterid = ? and sectionid = ? and flag = 1 ORDER BY sort ASC", courseid, chapter.getId(), section.getId());
		setAttr("parts", parts);
		setAttr("studentid", studentid);
		setAttr("courseid", courseid);
		render("class.html");
	}
	
	
	
	/**
	 * 加载学生实验信息(加载学生完成的)
	 */
	public void listexperiment(){
		// 获取课程ID
		Long courseid = this.getParaToLong("courseid");
		Long chapterid = this.getParaToLong("chapterid");
		Long studentid = this.getParaToLong("studentid");
		System.out.println(courseid+":"+chapterid+":"+studentid);
		// 设置第一章
		Chapter chapter = new Chapter();
		
		if (null == chapterid) {
			List<Chapter> chapters = Chapter.dao.find("select * from chapter where courseid = ? and flag = 1 ORDER BY sort ASC", courseid);
			if (null != chapters) {
				chapter = chapters.get(0);
				chapterid = chapter.getId();
			}
		}

		
		
		// 通过班级ID获取班级学生信息(完成实验的情况)
		String sql = "select * from studentexpertiment where courseid = ? and chapterid = ? and studentid = ? and flag = 1";
		List<Studentexpertiment> studentexpertiments = Studentexpertiment.dao.find(sql, courseid, chapterid, studentid);
		
		List<Map<String,Object>> list = new ArrayList<>();
		for (Studentexpertiment studentexpertiment : studentexpertiments) {
			Map<String,Object> map = new HashMap<>();
			map.put("id", studentexpertiment.getId());
			map.put("experiment", studentexpertiment.getExperiment().getQuestion().getTitle());
			map.put("score", studentexpertiment.getScore());
			map.put("status", studentexpertiment.getStatus());
			list.add(map);
		}
		renderJson(list);
	}
	
}
