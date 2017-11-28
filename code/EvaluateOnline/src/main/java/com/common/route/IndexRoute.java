package com.common.route;

import com.core.system.index.controller.IndexController;
import com.jfinal.config.Routes;

public class IndexRoute extends Routes {

	@Override
	public void config() {
		// TODO Auto-generated method stub
		add("/", IndexController.class);
	}

}
