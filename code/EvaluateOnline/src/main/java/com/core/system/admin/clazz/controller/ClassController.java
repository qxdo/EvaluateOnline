package com.core.system.admin.clazz.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import com.core.common.model.Admin;
import com.core.common.model.Clazz;
import com.core.common.model.Course;

import com.core.common.model.Teacher;
import com.core.common.model.Teachercourseclazz;
import com.core.common.vo.ObjectVo;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;

public class ClassController extends Controller{
	
	/**
	 * 进入系统管理员班级管理页面
	 */
	public void index(){
		render("class_list.html");
	}
	
	/**
	 * 进入添加班级信息页面
	 */
	public void add(){
		setAttr("type", "add");
		render("class_add.html");
	}
	
	/**
	 * 添加班级信息
	 */
	public void save(){
		Clazz clazz = this.getModel(Clazz.class);
		boolean flag = false;
		ObjectVo vo = new ObjectVo();
		if (null != clazz) {
			// 班级信息不为空
			Admin admin = getSessionAttr("admin");
			clazz.setFlag(1);
			clazz.setCreatetime(new Date());
			clazz.setCreateby(admin.getNumber());
			clazz.setModifytime(new Date());
			clazz.setModifyby(admin.getNumber());
			flag = clazz.save();
		}
		if (flag) {
			// 保存成功
			vo.setCode(1);
			vo.setMsg("保存班级信息成功");
		}else{
			// 保存失败
			vo.setCode(0);
			vo.setMsg("保存班级信息失败");
		}
		renderJson(vo);
	}
	
	/**
	 * 保存修改班级信息
	 */
	public void updatesave(){
		Clazz clazz = this.getModel(Clazz.class);
		boolean flag = false;
		ObjectVo vo = new ObjectVo();
		
		// 获取班级编号查看是否存在
		Clazz clazz2 = Clazz.dao.findFirst("select * from clazz where number = ? and flag = 1", clazz.getNumber());
		if (null != clazz2) {
			// 班级已存在
			vo.setCode(2);
			vo.setMsg("保存班级信息已存在");
		}else if(null != clazz) {
			// 班级信息不为空
			Admin admin = getSessionAttr("admin");
			clazz.setFlag(1);
			clazz.setCreatetime(new Date());
			clazz.setCreateby(admin.getNumber());
			clazz.setModifytime(new Date());
			clazz.setModifyby(admin.getNumber());
			flag = clazz.update();
			if (flag) {
				// 保存成功
				vo.setCode(1);
				vo.setMsg("保存班级信息成功");
			}else{
				// 保存失败
				vo.setCode(0);
				vo.setMsg("保存班级信息失败");
			}
		}
		renderJson(vo);
	}
	
	/**
	 * 批量添加教师
	 */
	public void batchadd(){
		
	}
	
	/**
	 * 删除班级信息
	 */
	public void delete(){
		String number =  this.getPara("number").trim();
		ObjectVo vo = new ObjectVo();
		if (!number.equals("") || null != number) {
			// 学号不能为空
			String sql = Db.getSql("admin.findClassByNumber");
			Clazz clazz = Clazz.dao.findFirst(sql, number);
			if (null != clazz) {
				// 学生已存在
				clazz.setFlag(0);
				boolean flag = clazz.update();
				if (flag) {
					// 删除学生信息成功
					vo.setCode(1);
					vo.setMsg("删除班级信息成功");
				}else{
					// 删除学生信息失败
					vo.setCode(0);
					vo.setMsg("删除班级信息失败");	
				}
			}
		}
		renderJson(vo);
	}
	
	/**
	 * 修改班级信息
	 */
	public void update(){
		// 获取班级编号
		String number = this.getPara("number").trim();
		Clazz clazz = new Clazz();
		if (!number.equals("") && null != number) {
			// 班级编号不能为空
			String sql = Db.getSql("admin.findClassByNumber");
			clazz = Clazz.dao.findFirst(sql, number);
			if (null != clazz) {
				// 修改的班级存在,返回班级信息
				setAttr("clazz", clazz);
			}
		}
		setAttr("type", "update");
		render("class_add.html");
	}
	
	/**
	 * 以JSON形式显示班级列表信息
	 */
	public void list(){
		String sql = Db.getSql("admin.listClass");
		List<Clazz> clazzs = Clazz.dao.find(sql);
		renderJson(clazzs);
	}
	
	/**
	 * 查询所有班级信息
	 */
	public void findAll(){
		ObjectVo vo = new ObjectVo();
		List<Clazz> clazzs = null;
		try {
			String sql = Db.getSql("admin.findAllClass");
			clazzs = Clazz.dao.find(sql);
			vo.setCode(1);
			vo.setMsg("获取全部班级信息成功");
			vo.setObject(clazzs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			vo.setCode(0);
			vo.setMsg("获取全部班级信息失败");
			vo.setObject(clazzs);
		}
		setAttr("clazzs", clazzs);
		renderJson(vo);
	}
	
	/**
	 * 根据班级编号获取班级信息
	 */
	public void findClassByNumber(){
		// 获取班级编号
		String number = this.getPara("number").trim();
		
		if (null != number || !number.equals("")) {
			// 根据编号查询
			String sql = Db.getSql("admin.findClassByNumber");
			Clazz clazz = Clazz.dao.findFirst(sql, number);
			ObjectVo vo = new ObjectVo();
			if (null != clazz) {
				// class存在
				vo.setCode(1);
				vo.setMsg("班级已存在");
				vo.setObject(clazz);
			}else {
				// class不存在
				vo.setCode(0);
				vo.setMsg("班级不存在");
				vo.setObject(null);
			}
			renderJson(vo);
		}
		
	}
	
	// ----------------------------------------------------- 业务管理员操作
	
	/**
	 * 业务管理员进入班级操作
	 */
	public void yw(){
		setAttr("type", "class");
		render("list.html");
	}
	
	/**
	 * 业务管理员对班级乾分配（进入页面）
	 */
	public void distribute(){
		// 获取班级编号
		String number = this.getPara("number").trim();
		String sql = Db.getSql("admin.findClassByNumber");
		Clazz clazz = Clazz.dao.findFirst(sql, number);
		
		// 查询显示所有的教师
		String teachersql = Db.getSql("admin.findAllTeahcer");
		List<Teacher> teachers = Teacher.dao.find(teachersql);
		
		// 查询所有的课程
		String coursesql = Db.getSql("admin.findAllCourse");
		List<Course> courses = Course.dao.find(coursesql);
		
		setAttr("type", "class");
		setAttr("distribute", clazz);
		setAttr("teachers", teachers);
		setAttr("courses", courses);
		render("distribute.html");
	}
	
	
	/**
	 * 分配信息保存
	 */
	public void distributesave(){
		// 分配信息直接保存到表中
		Long courseid = this.getParaToLong("courseid");
		String clazznumber = this.getPara("clazznumber").trim();
		String teachernumber = this.getPara("teachernumber").trim();
		Integer status = this.getParaToInt("status");
		ObjectVo vo = new ObjectVo();
		boolean flag = false;
		if (!clazznumber.equals("") && !teachernumber.equals("") && null != courseid && null !=status) {
			// 满足条件进行操作（查询关系表中是否存在）
			String sql = Db.getSql("admin.findDistributeOfCTC");
			Teachercourseclazz teachercourseclazz = null;
			teachercourseclazz = Teachercourseclazz.dao.findFirst(sql, clazznumber, teachernumber, courseid);
			if (null == teachercourseclazz) {
				// 添加保存
				Teacher teacher = Teacher.dao.findFirst("select * from teacher where number = ? and flag = 1", teachernumber);;
				Clazz clazz = Clazz.dao.findFirst("select * from clazz where number = ? and flag = 1", clazznumber);
				Course course = Course.dao.findFirst("select * from course where id = ? and flag = 1", courseid);
				System.out.println("===="+teacher.getId());
				teachercourseclazz = new Teachercourseclazz();
				teachercourseclazz.setTeacherid(teacher.getId());
				teachercourseclazz.setTeachernumber(teachernumber);
				teachercourseclazz.setClazzid(clazz.getId());
				teachercourseclazz.setClazznumber(clazznumber);
				teachercourseclazz.setCourseid(courseid);
				teachercourseclazz.setCoursenumber(course.getNumber());
				flag = teachercourseclazz.save();
				if (flag) {
					// 操作成功
					vo.setCode(1);
					vo.setMsg("保存成功");
				}else{
					// 操作失败
					vo.setCode(0);
					vo.setMsg("保存失败");
				}
			}else{
				// 进行修改
				vo.setCode(1);
				vo.setMsg("已保存");
			}
			
		}
		renderJson(vo);
		
	}
	
	
	/**
	 * 进入课程分配管理页面
	 */
	public void distributemanage(){
		String number= this.getPara("number").trim();
		Clazz clazz = null;
		if (null != number && !number.equals("")) {
			// 
			String sql = Db.getSql("admin.findClassByNumber");
			clazz = Clazz.dao.findFirst(sql, number);
		}
		setAttr("type", "class");
		setAttr("distribute", clazz);
		render("distribute_manage.html");
	}
	
	/**
	 * 获取分配列表
	 */
	public void distributelist(){
		String number = this.getPara("number").trim();
		List<Map<String, Object>> list = new ArrayList<>();
		if (null != number && !number.equals("")) {
			String sql = Db.getSql("admin.findDistributeByClazzNumber");
			List<Teachercourseclazz> teachercourseclazzs = Teachercourseclazz.dao.find(sql, number);
			for (Teachercourseclazz teachercourseclazz : teachercourseclazzs) {
				Map<String, Object> map = new HashedMap<String, Object>();
				map.put("id", teachercourseclazz.getId());
				map.put("teachernumber", teachercourseclazz.getTeachernumber());
				map.put("teachername", teachercourseclazz.getTeacher().getName());
				map.put("coursenumber", teachercourseclazz.getCoursenumber());
				map.put("coursename", teachercourseclazz.getCourse().getName());
				map.put("clazznumber", teachercourseclazz.getClazznumber());
				map.put("clazzname", teachercourseclazz.getClazz().getName());
				map.put("status", teachercourseclazz.getStatus());
				list.add(map);
			}
		}
		renderJson(list);
	}
	
	/**
	 * 分配删除操作
	 */
	public void distributedelete(){
		Long number= this.getParaToLong("id");
		ObjectVo vo = new ObjectVo();
		boolean flag = false;
		if (null != number) {
			flag = Teachercourseclazz.dao.findById(number).delete();
		}
		if (flag) {
			// 删除成功
			vo.setCode(1);
			vo.setMsg("删除成功");
		}else{
			vo.setCode(0);
			vo.setMsg("删除失败");
		}
		renderJson(vo);
	}
	
	/**
	 * 课程权限操作
	 */
	public void courseauth(){
		Long id = this.getParaToLong("id");
		boolean flag = false;
		ObjectVo vo = new ObjectVo();
		if (null != id) {
			// ID不能为空
			Teachercourseclazz teachercourseclazz = Teachercourseclazz.dao.findById(id);
			if (teachercourseclazz.getStatus() == 1) {
				teachercourseclazz.set("status", 0);
			}else if (teachercourseclazz.getStatus() == 0) {
				teachercourseclazz.set("status", 1);
			}
			flag = teachercourseclazz.update();
		}
		if (flag) {
			// 
			vo.setCode(1);
			vo.setMsg("修改成功");
		}else{
			vo.setCode(1);
			vo.setMsg("修改失败");
		}
		renderJson(vo);
	}
	
}
