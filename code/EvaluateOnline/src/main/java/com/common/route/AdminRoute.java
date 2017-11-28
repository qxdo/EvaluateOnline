package com.common.route;

import com.common.interceptor.AdminInterceptor;
import com.core.system.admin.chapter.controller.ChapterController;
import com.core.system.admin.clazz.controller.ClassController;
import com.core.system.admin.course.controller.CourseController;
import com.core.system.admin.experiment.controller.ExperimentController;
import com.core.system.admin.index.controller.AdminController;
import com.core.system.admin.part.controller.PartController;
import com.core.system.admin.section.controller.SectionController;
import com.core.system.admin.student.controller.StudentController;
import com.core.system.admin.teacher.controller.TeacherController;
import com.jfinal.config.Routes;

/**
 * 管理员端路由
 * @author Hang
 *
 */
public class AdminRoute extends Routes {

	@Override
	public void config() {
		// TODO Auto-generated method stub
		
		// 添加管理员拦截器
		addInterceptor(new AdminInterceptor());
		
		// 设置系统模块映射的根目录
		setBaseViewPath("/views/system");
		
		add("/admin", AdminController.class);  // 业务管理员操作
		/**
		 * 系统管理员（基础信息的录入与操作）及业务管理员功能（业务逻辑操作）
		 */
		// 公共功能映射
		add("/admin/teacher", TeacherController.class, "/admin");  // 教师操作
		add("/admin/student", StudentController.class, "/admin");  // 学生操作
		add("/admin/class", ClassController.class, "/admin");  // 班级操作
		
		// 业务管理员功能映射
		add("/admin/course", CourseController.class, "/admin");  // 课程操作
		add("/admin/chapter", ChapterController.class, "/admin");  // 章操作 
		add("/admin/section", SectionController.class, "/admin");  // 节操作
		add("/admin/part", PartController.class, "/admin");  // 小节操作 
		add("/admin/experiment", ExperimentController.class, "/admin");  // 实验操作
	}

}
