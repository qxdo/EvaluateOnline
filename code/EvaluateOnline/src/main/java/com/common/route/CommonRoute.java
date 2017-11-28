package com.common.route;

import com.core.common.controller.CaptchaController;
import com.jfinal.config.Routes;

/**
 * 公共路由
 * @author Hang
 *
 */
public class CommonRoute extends Routes {

	@Override
	public void config() {
		// TODO Auto-generated method stub
		// 设置验证码生成方法
		add("/captcha", CaptchaController.class);
		
	}

}
