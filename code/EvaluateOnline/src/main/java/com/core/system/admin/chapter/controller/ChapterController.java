package com.core.system.admin.chapter.controller;

import java.util.List;

import com.core.common.model.Chapter;
import com.core.common.model.Course;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;

public class ChapterController extends Controller {
	
	/**
	 * 进入课程的章节
	 */
	public void index(){
		// 获取课程编号
		String number = this.getPara("number");

		String coursesql = Db.getSql("admin.findCourseByNumber");
		Course course = Course.dao.findFirst(coursesql, number);
		// 通过课程ID查询章目录
		String chaptersql = Db.getSql("admin.findChapterByCourse");
		List<Chapter> chapters = Chapter.dao.find(chaptersql, course.getId());

		// 设置课程
		setAttr("course", course);
		setAttr("chapters", chapters);

		render("course_tree.html");
	}
	
}
