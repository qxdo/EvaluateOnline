package com.common.route;

import com.core.common.controller.DockerController;
import com.jfinal.config.Routes;


public class DockerRoute extends Routes{

	@Override
	public void config() {
		// 设置系统模块映射的根目录
		setBaseViewPath("/views/system");
		
		add("/docker", DockerController.class);  // 业务管理员操作
		
	}

}
