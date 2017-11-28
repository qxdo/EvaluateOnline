package com.common.interceptor;

import com.core.common.model.Student;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.LogKit;

public class StudentInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		// TODO Auto-generated method stub
		Controller controller = inv.getController();
		Student student = controller.getSessionAttr("student");
		if (null != student) {
			try {
				LogKit.info("-------Session存在--------");
				inv.invoke();
			} catch (ClassCastException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				controller.render("/student/login");
			}
		} else {
			LogKit.info("-------Session不存在--------");
			controller.redirect("/student/login");
		}
	}

}
