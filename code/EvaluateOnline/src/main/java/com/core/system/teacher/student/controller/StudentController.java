package com.core.system.teacher.student.controller;

import com.core.common.model.Studentexpertiment;
import com.jfinal.core.Controller;

public class StudentController extends Controller {
	public void info(){
		Long id = this.getParaToLong("id");
		Studentexpertiment experiment = Studentexpertiment.dao.findById(id);
		setAttr("experiment", experiment);
		render("stu_question.html");
	}

}
