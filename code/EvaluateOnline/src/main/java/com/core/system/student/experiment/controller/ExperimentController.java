package com.core.system.student.experiment.controller;


import java.util.List;

import com.core.common.model.Experiment;
import com.core.common.model.Question;
import com.core.common.model.Student;
import com.core.common.model.Studentexpertiment;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
public class ExperimentController extends Controller {
	// 通过实验id 获取实验数据，如果实验已经有实验记录那么 头文件名和 源文件名不能修改
	
	public void getQuestionByQuestionId(){
		
		long studentid =((Student)getSessionAttr("student")).getId();
		long id = getParaToLong("questionId");
		// 获取 Experiment ID 通过 questionid
		String sql = Db.getSql("student.getExperimentInfoByQuestionId");
		List<Experiment> experiments = Experiment.dao.find(sql,id);
		long experimentid = experiments.get(0).getId();
		//通过experimentid 和 studentid 获取这个题是否有记录，如果有记录那么文件名不可写
		String sql1 = Db.getSql("student.updateStatusByQuestionidAndStudentId");
		List<Studentexpertiment> studentexpertiments = Studentexpertiment.dao.find(sql1,studentid,experimentid);;
		if(studentexpertiments.isEmpty()){
			setAttr("isWrite", "no");
		}else{
			//获取头文件名和源文件名
			String hfname = studentexpertiments.get(0).getHfname();
			String sfname = studentexpertiments.get(0).getSfname();
			String hfcontent = studentexpertiments.get(0).getHfcontent();
			String sfcontent = studentexpertiments.get(0).getSfcontent();
			if(hfcontent != null&&hfcontent != ""){
				setAttr("hfcontent",hfcontent);
			}
			if(sfcontent != null&&sfcontent != ""){
				setAttr("sfcontent",sfcontent);
			}
			
			if(hfname != null&&hfname != ""){
				
				setAttr("hfname",hfname.split("\\.")[0]);
			}
			if(sfname != null&&sfname != ""){
				
				setAttr("sfname", sfname.split("\\.")[0]);
			}
			setAttr("isWrite","yes");
		}
		
		Question question = Question.dao.findById(id);
		setAttr("title", question.getTitle());
		
		//setAttr("content", question.getContent());
		String content = question.getContent();
		
		
		setAttr("content", content);
		setAttr("template", question.getTemplate());
		setAttr("questionid", id);
		setAttr("studentid", ((Student)getSessionAttr("student")).getId());
		setSessionAttr("keywords", question.getKeywords());
		
		
		renderJson();
		
	}
	
}
