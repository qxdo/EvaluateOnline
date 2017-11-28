package com.common.route;

import com.common.interceptor.StudentInterceptor;
import com.core.system.admin.section.controller.SectionController;
import com.core.system.student.index.controller.StudentController;
import com.core.system.student.chapter.controller.ChapterController;
import com.core.system.student.course.controller.CourseController;
import com.core.system.student.experiment.controller.ExperimentController;
import com.core.system.student.part.controller.PartController;
import com.jfinal.config.Routes;

/**
 * 学生路由
 * @author Hang
 *
 */
public class StudentRoute extends Routes {

	@Override
	public void config() {
		// TODO Auto-generated method stub
		
		// 添加学生拦截器
		addInterceptor(new StudentInterceptor());
		
		// 设置学生端模块映射的根目录
		setBaseViewPath("/views/system");
		
		add("/student", StudentController.class);
		
		add("/student/course", CourseController.class,"/student");  // 课程操作
		add("/student/chapter", ChapterController.class,"/student");  // 章操作 
		add("/student/section", SectionController.class,"/student");  // 节操作
		add("/student/part", PartController.class,"/student");  // 小节操作 
		add("/student/experiment", ExperimentController.class,"/student");  // 实验操作
		
	}

}
