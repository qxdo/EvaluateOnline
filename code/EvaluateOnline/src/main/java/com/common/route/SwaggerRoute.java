package com.common.route;

import com.core.moudles.swagger.controller.SwaggerController;
import com.jfinal.config.Routes;

public class SwaggerRoute extends Routes {

	@Override
	public void config() {
		// TODO Auto-generated method stub
		// 
		setBaseViewPath("/views/modules");
		add("/swagger", SwaggerController.class);
	}

}
