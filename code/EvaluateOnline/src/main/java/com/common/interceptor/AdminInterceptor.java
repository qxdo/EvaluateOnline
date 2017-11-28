package com.common.interceptor;

import com.core.common.model.Admin;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.LogKit;

/**
 * 管理员拦截器
 * @author Hang
 *
 */
public class AdminInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		// TODO Auto-generated method stub
		Controller controller = inv.getController();
		Admin admin = controller.getSessionAttr("admin");
		if (null != admin) {
			try {
				LogKit.info("-------Session存在--------");
				inv.invoke();
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				controller.render("/views/500.html");
			}
		} else {
			LogKit.info("-------Session不存在--------");
			controller.redirect("/admin/login");
		}
	}

}
