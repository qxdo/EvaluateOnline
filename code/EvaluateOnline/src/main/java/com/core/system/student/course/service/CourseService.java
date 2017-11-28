package com.core.system.student.course.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.core.common.model.Part;
import com.core.common.model.Question;
import com.core.common.model.Section;
import com.core.common.model.Studentexpertiment;
import com.core.common.vo.CommonQuestion;
import com.core.system.student.course.service.CourseService;
import com.jfinal.plugin.activerecord.Db;

public class CourseService {
	// private CourseService courseService = new CourseService();
	/**
	 * 获取章子节点
	 * @param courseid
	 * @param chapterid
	 */
	public List<Map<String, Object>> getChapterChildren(Long courseid, Long chapterid){
		String sql = Db.getSql("student.findSectionByCC");
		List<Section> sections = Section.dao.find(sql, courseid, chapterid);
		List<Map<String, Object>> list = new ArrayList<>();
		for (Section section : sections) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", "sectionid"+section.getId());      // 设置节点ID
			map.put("text", section.getName());  // 设置节点Name
			map.put("level", 2);  // 设置节点level
			map.put("url", "/student/course/experiment?courseid="+courseid+"&chapterid="+chapterid+"&sectionid="+section.getId()+"&level=2");
			map.put("children", this.getSectionChildren(courseid, chapterid, section.getId()));
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 获取节节点
	 * @param courseid
	 * @param chapterid
	 */
	public List<Map<String, Object>> getSectionChildren(Long courseid, Long chapterid, Long sectionid){
		String sql = Db.getSql("student.findPartByCCS");
		List<Part> parts = Part.dao.find(sql, courseid, chapterid, sectionid);
		List<Map<String, Object>> list = new ArrayList<>();
		for (Part part : parts) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", "partid"+part.getId());      // 设置节点ID
			map.put("text", part.getName());  // 设置节点Name
			map.put("level", 3);  // 设置节点level
			map.put("url", "/student/course/experiment?courseid="+courseid+"&chapterid="+chapterid+"&sectionid="+sectionid+"&partid="+part.getId()+"&level=3");
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 通过传递过来的Quesiton 获取Question 的状态
	 * @param questions
	 * @return
	 */
	public List<CommonQuestion> showQuestions(long studentid,List<Question> questions){
		List<CommonQuestion> commonQuestions = new ArrayList<CommonQuestion>();
		
		CommonQuestion commonQuestion ;
		String getstatus = Db.getSql("student.getStatusByStudentIdAndQuestionId");
		for (Question question : questions) {
			
			commonQuestion = new CommonQuestion();
			commonQuestion.setQid(question.getId());
			commonQuestion.setQname(question.getTitle());
			List<Studentexpertiment> find = Studentexpertiment.dao.find(getstatus,studentid,question.getId());
			
			if(find.isEmpty()){
				commonQuestion.setQstatue(1);
			}else {
				commonQuestion.setQstatue(find.get(0).getStatus());
			}
			
			commonQuestions.add(commonQuestion);
			
		}
		return commonQuestions;
	}
	
	
}
