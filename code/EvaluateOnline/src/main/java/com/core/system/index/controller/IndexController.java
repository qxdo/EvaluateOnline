package com.core.system.index.controller;

import com.jfinal.core.Controller;

public class IndexController extends Controller {
	
	public void index(){
		redirect("/student");
	}
	
}
