package com.common.service.docker;

import java.util.List;

import com.core.common.model.Experiment;
import com.core.common.model.Studentexpertiment;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class InputDataTo {
	public void setDataToStudentexpertiment(long studentid,long questionid,String hfname,String hfcontent,String sfname,String sfcontent,int status){
		
		String sql = Db.getSql("student.getExperimentInfoByQuestionId");
		List<Experiment> experiments = Experiment.dao.find(sql,questionid);
		Experiment experiment = experiments.get(0);
		
		Record record = new Record().set("studentid", studentid).set("courseid", experiment.getCourseid()).set("chapterid", experiment.getChapterid()).set("sectionid", experiment.getSectionid()).set("partid", experiment.getPartid()).set("experimentid", experiment.getId()).set("hfname", hfname).set("hfcontent", hfcontent).set("sfname", sfname).set("sfcontent", sfcontent).set("status", status).set("flag", 1);
		Db.save("studentexpertiment", record);
		
	}
	
	public int isHasStudentexpertiment(long studentid,long qusetionid){
		System.out.println("学生id"+studentid+"题的id"+qusetionid);
		
		String getexperiment = Db.getSql("student.getExperimentInfoByQuestionId");
		
		Experiment experiment =( Experiment.dao.find(getexperiment,qusetionid)).get(0);
		System.out.println("experiment-----" + experiment.getId());
		String sql = Db.getSql("student.updateStatusByQuestionidAndStudentId");
		List<Studentexpertiment> studentexpertiments = Studentexpertiment.dao.find(sql,studentid,experiment.getId());
		if(studentexpertiments.isEmpty()){
			return 1;
		}else{
			return 2;
		}
	}
	
	
	public void updateToStudentexpertiment(long studentid,long qusetionid,int status){
		String getexperiment = Db.getSql("student.getExperimentInfoByQuestionId");
		Experiment experiment =(Experiment.dao.find(getexperiment,qusetionid)).get(0);
		String sql = Db.getSql("student.updateStatusByQuestionidAndStudentId");
		Record studentexpertiment = Db.findFirst(sql,studentid,experiment.getId()).set("status", status);
		Db.update("studentexpertiment",studentexpertiment);
	}
	
}
