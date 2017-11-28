package com.common.route;

import com.core.system.teacher.chapter.controller.ChapterController;
import com.core.system.teacher.clazz.controller.ClazzController;
import com.core.system.teacher.course.controller.CourseController;
import com.core.system.teacher.experiment.controller.ExperimentController;
import com.core.system.teacher.part.controller.PartController;
import com.common.interceptor.TeacherInterceptor;
import com.core.system.admin.section.controller.SectionController;
import com.core.system.teacher.index.controller.TeacherController;
import com.core.system.teacher.student.controller.StudentController;
import com.jfinal.config.Routes;

/**
 * 教师端路由
 * @author Hang
 *
 */
public class TeacherRoute extends Routes {

	@Override
	public void config() {
		// TODO Auto-generated method stub
		
		// 设置教师拦截器
		addInterceptor(new TeacherInterceptor());
		
		// 设置教师模块映射的根目录
		setBaseViewPath("/views/system");
		
		add("/teacher", TeacherController.class);  // 教师端操作
		
		add("/teacher/student", StudentController.class ,"/teacher");  // 教师对学生操作
		add("/teacher/class", ClazzController.class ,"/teacher");  // 教师对班级进行操作
		add("/teacher/course", CourseController.class ,"/teacher");  // 课程操作
		add("/teacher/chapter", ChapterController.class ,"/teacher");  // 章操作 
		add("/teacher/section", SectionController.class ,"/teacher");  // 节操作
		add("/teacher/part", PartController.class ,"/teacher");  // 小节操作 
		add("/teacher/experiment", ExperimentController.class ,"/teacher");  // 实验操作
		
		
	}

}
