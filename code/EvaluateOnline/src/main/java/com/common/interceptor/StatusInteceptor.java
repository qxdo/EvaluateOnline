package com.common.interceptor;

import com.common.util.FileRW;
import com.core.common.model.Teacher;
import com.core.system.admin.index.controller.AdminController;
import com.core.system.admin.teacher.controller.TeacherController;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class StatusInteceptor implements Interceptor {

	public void intercept(Invocation inv) {
		System.out.println("状态拦截器.......");
		Controller controller = inv.getController();
		System.out.println();
		String path =  controller.getSession().getServletContext().getRealPath("static/modules/prop/status.properties");
		String uri =  controller.getRequest().getRequestURI();
		System.out.println(uri);
		if(uri.contains("teacher") || uri.contains("admin") || uri.contains("captcha")){
			inv.invoke();
		}else{
			String status =  FileRW.readfile(path);
			System.out.println(path);
			System.out.println(status);
			if ( status.equals("0")) {
				// 系统正常
				System.out.println("系统正常。。。。");
				inv.invoke();
			} else {
				System.out.println("系统挂了。。。。");
				System.out.println("系统正在维护期...请耐心等待维护结束后访问....");
				controller.redirect("index.html");
			}
			
		}
	}
}
