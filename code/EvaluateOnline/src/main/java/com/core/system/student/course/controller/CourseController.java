package com.core.system.student.course.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.core.common.model.Chapter;
import com.core.common.model.Course;
import com.core.common.model.Experiment;
import com.core.common.model.Question;
import com.core.common.model.Student;
import com.core.common.model.Studentexpertiment;
import com.core.common.model.Teacher;
import com.core.common.vo.CommonCourse;
import com.core.common.vo.CommonQuestion;
import com.core.common.vo.ObjectVo;
import com.core.system.student.course.service.CourseService;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;

public class CourseController extends Controller {
	private CourseService courseService = new CourseService();

	
	/**
	 * 登录后获取到 课程的 状态
	 */
	public void getCourseAndStatus(){
		//从session 中获取到登录到的student
		Student student = (Student)getSessionAttr("student");
		
		//获取到班级ID
		long clazzId = student.getClassid();
		
		System.out.println("班级ID"+ clazzId);
		
		String sql = Db.getSql("student.getCourseByclazzId");
		//获取班级 对应的课程
		List<Course> courses = Course.dao.find(sql,clazzId);
		//System.out.println(courses.get(0).getName());
		
		//通过课程 ID 获取到对应老师的信息
		String getteacher = Db.getSql("student.getTeacherBycourseId");
		
		String getQuestions = Db.getSql("student.getQuestionBycourseId");
		String getSuccess = Db.getSql("student.getQuestionSuccessByStudentIdAndCourseId");
		List<CommonCourse> commonCourses = new ArrayList<CommonCourse>();
		CommonCourse commonCourse;
		Teacher teacher ;
		for (Course course : courses) {
			commonCourse = new CommonCourse();
			commonCourse.setId(course.getId());
			commonCourse.setCoursename(course.getName());
			teacher = Teacher.dao.find(getteacher,course.getId()).get(0);
			System.out.println("老师名称"+ teacher.getName());
			commonCourse.setTeachername(teacher.getName());
			List<Question> find = Question.dao.find(getQuestions,course.getId());
			int size;
			if(find.isEmpty()){
				size = 0;
			}else{
				size = find.size();
			}
			
			System.out.println("全部实验"+ size);
			commonCourse.setQuestionsum(size);
			System.out.println("查找已经完成的实验");
			int succsee ;
			List<Studentexpertiment> find2 = Studentexpertiment.dao.find(getSuccess,student.getId(),course.getId());
			if(find2.isEmpty())
			{
				succsee = 0;
			}else{
				succsee = find2.size();
			}
			
			commonCourse.setQuestionsuccess(succsee);
			
			commonCourses.add(commonCourse);
		}
		
		// 获取到的数据
		for (CommonCourse commonCourse2 : commonCourses) {
			System.out.println(commonCourse2.toString());
		}
		
		renderJson(commonCourses);
		
	}
	
	public void getTuData(){
		//从session 中获取到登录到的student
		Student student = (Student)getSessionAttr("student");
		
		//获取到班级ID
		Integer clazzId = (Integer) student.getClassid();
		
		System.out.println("班级ID"+ clazzId);
		
		String sql = Db.getSql("student.getCourseByclazzId");
		String getQuestions = Db.getSql("student.getQuestionBycourseId");
		String getSuccess = Db.getSql("student.getQuestionSuccessByStudentIdAndCourseId");
		//获取班级 对应的课程
		List<Course> courses = Course.dao.find(sql,clazzId);
		List<String> courseNames = new ArrayList<String>(); 
		
		List<Integer>  sums = new ArrayList<Integer>(); 
		List<Integer>  success = new ArrayList<Integer>();
		for (Course course : courses) {
			courseNames.add(course.getName());
			//该课程的所有实验
			List<Question> find = Question.dao.find(getQuestions,course.getId());
			sums.add(find.size());
			
			//已经完成的实验
			System.out.println("查找已经完成的实验");
			
			List<Studentexpertiment> find2 = Studentexpertiment.dao.find(getSuccess,student.getId(),course.getId());
			success.add(find2.size());
		}
		
		setAttr("courseNames", courseNames);
		setAttr("sums", sums);
		setAttr("success", success);
		
		renderJson();
		
		
	}
	
	
	
	/**
	 * 获取Tree列表
	 */
	public void tree(){
		
		// 目录以层级不能为空
		long courseid = this.getParaToLong("courseid");
		System.out.println("开始获取树");		
		// 根据课程ID显示Tree结构树
		String sql = Db.getSql("student.findChapterByCourse");
		List<Chapter> chapters = Chapter.dao.find(sql, courseid);
		
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Chapter chapter : chapters) {
			Map<String , Object> map = new HashMap<String, Object>();
			map.put("id", "chapterid"+chapter.getId());      // 设置节点ID
			map.put("text", chapter.getName());  // 设置节点Name
			map.put("level", 1);  // 设置节点level
			map.put("url", "/student/course/experiment?courseid="+courseid+"&chapterid="+chapter.getId()+"&level=1");
			map.put("children", courseService.getChapterChildren(courseid, chapter.getId()));
			list.add(map);
		}
		renderJson(list);
	}
	

	/**
	 * 获取各个章节的实验
	 */
	public void experiment(){
		
		Long studentid = ((Student)getSessionAttr("student")).getId();
		
		//CourseService courseService = new CourseService();
		// 获取目录层次
		Long courseid = this.getParaToLong("courseid"); // 课程ID
		Long chapterid = this.getParaToLong("chapterid");
		Integer level = this.getParaToInt("level");
		String sql = "";
		
		List<Question> questions = new ArrayList<Question>();
		List<CommonQuestion> commonQuestions = new ArrayList<CommonQuestion>();
		if (level == 1) {
			// 获取本章实验
			sql = Db.getSql("student.findExperimentByChapter");
			questions = Question.dao.find(sql, courseid, chapterid);
			commonQuestions =  courseService.showQuestions(studentid.intValue(), questions);
			
			
			
		}else if (level == 2) {
			// 获取本节实验
			Long sectionid = this.getParaToLong("sectionid");
			sql = Db.getSql("student.findExperimentBySectionid");
			questions = Question.dao.find(sql, courseid, chapterid, sectionid);
			commonQuestions =  courseService.showQuestions(studentid.intValue(), questions);
		}else if (level == 3) {
			// 获取本小节实验
			Long sectionid = this.getParaToLong("sectionid");
			Long partid = this.getParaToLong("partid");
			sql = Db.getSql("student.findExperimentBypartid");
			questions =Question.dao.find(sql, courseid, chapterid, sectionid, partid);
			commonQuestions =  courseService.showQuestions(studentid.intValue(), questions);
		}

		for (CommonQuestion commonQuestion : commonQuestions) {
			System.out.println("========"+commonQuestion.toString());
		}
		
		
		renderJson(commonQuestions);
	}
	
	public void doQuestioned(){
		Long studentid = ((Student)getSessionAttr("student")).getId();
		
		String sql = Db.getSql("student.findexperimentBystudentId");
		
		List<Experiment> experiments = Experiment.dao.find(sql,studentid);
		List<Question> questions = new ArrayList<Question>();
		Question question;
		for (Experiment experiment : experiments) {
			question = new Question();
			question = Question.dao.findById(experiment.getQuestionid());
			questions.add(question);	
		}
		List<CommonQuestion> commonQuestions = courseService.showQuestions(studentid, questions);
		
		renderJson(commonQuestions);
	}
	
	
}
