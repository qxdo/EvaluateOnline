package com.core.system.teacher.course.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.core.common.model.Chapter;
import com.core.common.model.Course;
import com.core.common.model.Experiment;
import com.core.common.model.Question;
import com.core.common.model.Teacher;
import com.core.common.model.Teachercourseclazz;
import com.core.common.vo.ObjectVo;
import com.core.system.teacher.course.service.CourseService;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

/**
 * 教师进入课程管理
 * @author Hang
 *
 */
public class CourseController extends Controller {
	
	private CourseService courseService = new CourseService();
	
	/**
	 * 教师进入课程列表页面
	 */
	public void index(){
		setAttr("type", "course");
		render("list.html");
	}
	
	/**
	 * 以JSON形式显示教师所有课程列表
	 */
	public void list(){
		// 获取教师的Session值
		Teacher teacher = getSessionAttr("teacher");
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String sql = Db.getSql("teacher.findCourseByTeacher");
		// 查询所有与教师有关系的课程
		List<Teachercourseclazz> teachercourseclazzs = Teachercourseclazz.dao.find(sql, teacher.getNumber());
		for (Teachercourseclazz teachercourseclazz : teachercourseclazzs) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", teachercourseclazz.getId());   // ID
			map.put("courseid", teachercourseclazz.getCourseid());   // 课程ID
			map.put("coursenumber", teachercourseclazz.getCoursenumber());  // 课程编号
			map.put("coursename", teachercourseclazz.getCourse().getName());  // 课程名称
			map.put("classid", teachercourseclazz.getClazz().getId());  // 班级名称
			map.put("classname", teachercourseclazz.getClazz().getName());  // 班级名称
			map.put("modifyby", teachercourseclazz.getCourse().getModifyby());  // 修改人
			map.put("modifytime", teachercourseclazz.getCourse().getModifytime());  // 修改时间
			list.add(map);
		}
		renderJson(list);
	}
	
	/**
	 * 点击课程
	 */
	public void info(){
		// 获取课程编号 
		long courseid = this.getParaToLong("courseid");
		Course course = Course.dao.findById(courseid);
		Long id = this.getParaToLong("id");
		// 查询老师是否有修改实验的权限
		Teachercourseclazz teachercourseclazz = Teachercourseclazz.dao.findById(id);
		setAttr("course", course);
		setAttr("teachercourseclazz", teachercourseclazz);
		render("course.html");
	}
	
	/**
	 * 获取Tree列表
	 */
	public void tree(){
		// 目录以层级不能为空
		long courseid = this.getParaToLong("courseid");
				
		// 根据课程ID显示Tree结构树
		String sql = Db.getSql("teacher.findChapterByCourse");
		List<Chapter> chapters = Chapter.dao.find(sql, courseid);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Chapter chapter : chapters) {
			Map<String , Object> map = new HashMap<String, Object>();
			map.put("id", "chapterid"+chapter.getId());      // 设置节点ID
			map.put("text", chapter.getName());  // 设置节点Name
			map.put("level", 1);  // 设置节点level
			map.put("url", "/teacher/course/experiment?courseid="+courseid+"&chapterid="+chapter.getId()+"&level=1");
			map.put("children", courseService.getChapterChildren(courseid, chapter.getId()));
			list.add(map);
		}
		renderJson(list);
	}
	
	/**
	 * 获取各个章节的实验
	 */
	public void experiment(){
		// 获取目录层次
		Long courseid = this.getParaToLong("courseid"); // 课程ID
		Long chapterid = this.getParaToLong("chapterid");
		Integer level = this.getParaToInt("level");
		String sql = "";
		List<Experiment> experiments = new ArrayList<Experiment>();
		if (level == 1) {
			// 获取本章实验
			sql = Db.getSql("teacher.findExperimentByChapter");
			experiments = Experiment.dao.find(sql, courseid, chapterid);
		}else if (level == 2) {
			// 获取本节实验
			Long sectionid = this.getParaToLong("sectionid");
			sql = Db.getSql("teacher.findExperimentBySectionid");
			experiments = Experiment.dao.find(sql, courseid, chapterid, sectionid);
		}else if (level == 3) {
			// 获取本小节实验
			Long sectionid = this.getParaToLong("sectionid");
			Long partid = this.getParaToLong("partid");
			sql = Db.getSql("teacher.findExperimentBypartid");
			experiments =Experiment.dao.find(sql, courseid, chapterid, sectionid, partid);
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Experiment experiment : experiments) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", experiment.getId());
			map.put("questionid", experiment.getQuestion().getId());
			map.put("title", experiment.getQuestion().getTitle());
			map.put("score", experiment.getScore());
			map.put("date", experiment.getCreatetime());
			list.add(map);
		}
		renderJson(list);
	}
	
	/**
	 * 进入添加实页面
	 */
	public void addexperiment(){
		Long courseid = this.getParaToLong("courseid");
		Long chapterid = this.getParaToLong("chapterid");
		Long sectionid = this.getParaToLong("sectionid");
		Long partid = this.getParaToLong("partid");
		
		setAttr("courseid", courseid);
		setAttr("chapterid", chapterid);
		setAttr("sectionid", sectionid);
		setAttr("partid", partid);
		render("question.html");
	}
	
	/**
	 * 添加实验
	 */
	public void saveexperiment(){
		Long courseid = this.getParaToLong("courseid");
		Long chapterid = this.getParaToLong("chapterid");
		Long sectionid = this.getParaToLong("sectionid");
		Long partid = this.getParaToLong("partid");
		
		// 分数
		int score = this.getParaToInt("score");
		
		String title = this.getPara("title");
		String content = this.getPara("content");
		String keywords = this.getPara("keywords");
		String input = this.getPara("input");
		String output = this.getPara("output");
		String template = this.getPara("template");

		ObjectVo vo = new ObjectVo();
		// 内容不能为空
		if (title.equals("") || content.equals("") || keywords.equals("") || input.equals("") || output.equals("") || template.equals("")) {
			vo.setCode(0);
			vo.setMsg("参数内容不能为空");
		}else{
			Teacher teacher = getSessionAttr("teacher");
			boolean result = Db.tx(new IAtom() {
								@Override
								public boolean run() throws SQLException {
									// TODO Auto-generated method stub
									// 保存题库
									Question question = new Question().set("title", title)
																  .set("content", content)
																  .set("keywords", keywords)
																  .set("input", input)
																  .set("output", output)
																  .set("template", template)
																  .set("createtime", new Date())
																  .set("createby", teacher.getNumber());
									boolean flag = question.save();
									// 建立关系表
									Experiment experiment = new Experiment().set("courseid", courseid)
																	 .set("chapterid", chapterid)
																	 .set("sectionid", sectionid)
																	 .set("partid", partid)
																	 .set("questionid", question.getId())
																	 .set("score", score)
																	 .set("createtime", new Date())
																	 .set("createby", teacher.getNumber());
									boolean result = experiment.save();
									return (result && flag);
								}
							});
			if (result) {
				// 添加成功
				vo.setCode(1);
				vo.setMsg("实验添加成功");
			}else{
				// 添加失败
				vo.setCode(0);
				vo.setMsg("实验添加失败");
			}
		}
		renderJson(vo);
	}
	
	
	/**
	 * 删除实验
	 */
	public void deleteexperiment(){
		
		// 获取实验关系ID
		Long experimentid = this.getParaToLong("id");
		
		boolean flag = Experiment.dao.findById(experimentid).set("flag", 0).update();
		ObjectVo vo = new ObjectVo();
		if (flag) {
			// 删除实验成功
			vo.setCode(1);
			vo.setMsg("实验删除成功");
		}else {
			// 删除实验失败
			vo.setCode(0);
			vo.setMsg("实验删除失败");
		}
		renderJson(vo);
	}
	
	/**
	 * 修改实验
	 */
	public void updateexperiment(){
		// 获取实验关系ID
		Long experimentid = this.getParaToLong("id");
		Experiment experiment = new Experiment();
		if (null != experimentid) {
			experiment = Experiment.dao.findById(experimentid);
		}
		setAttr("experiment", experiment);
		render("question.html");
	}
	
	/**
	 * 保存实验修改
	 */
	public void saveupdateexperiment(){
		Long questionid = this.getParaToLong("id");
		
		// 分数
		int score = this.getParaToInt("score");
		
		String title = this.getPara("title");
		String content = this.getPara("content");
		String keywords = this.getPara("keywords");
		String input = this.getPara("input");
		String output = this.getPara("output");
		String template = this.getPara("template");

		ObjectVo vo = new ObjectVo();
		// 内容不能为空
		if (title.equals("") || content.equals("") || keywords.equals("")) {
			vo.setCode(0);
			vo.setMsg("参数内容不能为空");
		}else{
			Teacher teacher = getSessionAttr("teacher");
			Question question = Question.dao.findById(questionid);
			boolean flag = question.set("title", title)
								   .set("content", content)
								   .set("keywords", keywords)
								   .set("input", input)
								   .set("output", output)
								   .set("template", template)
								   .set("createtime", new Date())
								   .set("createby", teacher.getNumber())
								   .update();
			if (flag) {
				// 修改成功
				vo.setCode(1);
				vo.setMsg("实验修改成功");
			}else{
				// 添加失败
				vo.setCode(0);
				vo.setMsg("实验修改失败");
			}
		}
		renderJson(vo);
	}
	
}
