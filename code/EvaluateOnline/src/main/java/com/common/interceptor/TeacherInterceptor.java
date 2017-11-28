package com.common.interceptor;

import com.core.common.model.Teacher;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.LogKit;

public class TeacherInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		// TODO Auto-generated method stub
		Controller controller = inv.getController();
		Teacher teacher = controller.getSessionAttr("teacher");
		if (null != teacher) {
			try {
				LogKit.info("-------Session存在--------");
				inv.invoke();
			} catch (ClassCastException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				controller.render("/teacher/login");
			}
		} else {
			LogKit.info("-------Session不存在--------");
			controller.redirect("/teacher/login");
		}
	}

}
