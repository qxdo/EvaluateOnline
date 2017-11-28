package com.core.common.controller;

import com.jfinal.core.Controller;

public class CaptchaController extends Controller {
	
	/**
	 * 生成默认的验证码
	 */
	public void index(){
		renderCaptcha();
	}
	
}
