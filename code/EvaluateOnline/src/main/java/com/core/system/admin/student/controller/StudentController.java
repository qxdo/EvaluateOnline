package com.core.system.admin.student.controller;

import java.util.Date;
import java.util.List;

import com.core.common.model.Admin;
import com.core.common.model.Clazz;
import com.core.common.model.Student;
import com.core.common.model.Teacher;
import com.core.common.vo.ObjectVo;
import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.Db;

public class StudentController extends Controller {
	
	/**
	 * 管理员进入到学生管理页
	 */
	public void index(){
		render("student_list.html");
	}
	
	/**
	 * 进入到添加学生信息页面
	 */
	public void add(){
		// 添加学生时在页面显示有效的班级
		String sql = Db.getSql("admin.findAllClass");
		List<Clazz> clazzs = Clazz.dao.find(sql);
		setAttr("type", "add");
		setAttr("usertype", "student");
		setAttr("clazzs", clazzs);
		render("user_add.html");
	}
	
	/**
	 * 保存学生信息
	 */
	public void save(){
		// 获取学生信息
		Long classid = this.getParaToLong("classid");
		String number = this.getPara("number").trim();
		String name = this.getPara("number").trim();
		int sex = this.getParaToInt("sex");
		String email = this.getPara("email").trim();
		String phone = this.getPara("phone").trim();
		boolean flag = false;
		ObjectVo vo = new ObjectVo();
		if (!number.equals("") && !name.equals("")) {
			// 学生信息的编号及名字不能为空
			Student student = new Student();
			flag = student.set("number", number)
						  .set("name", name)
						  .set("sex", sex)
					 	  .set("email", email)
					   	  .set("phone", phone)
					   	  .set("classid", classid)
					   	  .set("flag", 1)
					  	  .set("createtime", new Date())
						  .set("modifytime", new Date())
						  .set("password", HashKit.md5("123456"))
						  .save();
		}
		if (flag) {
			// 保存学生信息成功
			vo.setCode(1);
			vo.setMsg("添加学生信息成功");
		}else{
			// 保存学生信息失败
			vo.setCode(0);
			vo.setMsg("添加学生信息失败");
		}
		renderJson(vo);
	}
	
	/**
	 * 批量添加教师
	 */
	public void batchadd(){
		this.getFile();
	}
	
	/**
	 * 删除学生信息
	 */
	public void delete(){
		Long id = this.getParaToLong("id");
		ObjectVo vo = new ObjectVo();
		boolean flag = false;
		if (null !=id) {
			flag = Student.dao.findById(id).set("flag", 0).update();
		}
		if (flag) {
			// 删除学生信息成功
			vo.setCode(1);
			vo.setMsg("删除学生信息成功");
		}else{
			// 删除学生信息失败
			vo.setCode(0);
			vo.setMsg("删除学生信息失败");	
		}
		renderJson(vo);
	}
	
	/**
	 * 进入到修改学生信息页面
	 */
	public void update(){
		String number = this.getPara("number");
		Student student = new Student();
		if (!number.equals("") && null != number) {
			// 班级编号不能为空
			String sql = Db.getSql("admin.findStudentByNumber");
			student = Student.dao.findFirst(sql, number);
			if (null != student) {
				// 修改的班级存在,返回班级信息
				setAttr("user", student);
			}
		}
		String sql = Db.getSql("admin.findAllClass");
		List<Clazz> clazzs = Clazz.dao.find(sql);
		setAttr("type", "update");
		setAttr("usertype", "student");
		setAttr("clazzs", clazzs);
		render("user_add.html");
	}
	
	/**
	 * 修改
	 */
	public void updatedeal(){
		
		Long id = this.getParaToLong("id");
		String number = this.getPara("number").trim();
		String name = this.getPara("name").trim();
		int sex = this.getParaToInt("sex");
		String email = this.getPara("email").trim();
		String phone = this.getPara("phone").trim();
		
		// 
		ObjectVo vo = new ObjectVo();
		boolean flag = false;
		
		String sql = Db.getSql("admin.findStudentByNumber");
		// 查询编号是否使用
		Student student = Student.dao.findFirst(sql, number);
		if (student == null || student.getNumber().equals(number)) {
			// 修改
			flag = Student.dao.findById(id).set("number", number)
								    .set("name", name)
								    .set("sex", sex)
								    .set("email", email)
								    .set("phone", phone)
								    .update();
		}else{
			vo.setCode(0);
			vo.setMsg("保存信息失败");
		}
		
		if (flag) {
			// 保存业务管理员信息成功
			vo.setCode(1);
			vo.setMsg("保存信息成功");
		}else{
			// 保存业务管理员信息失败
			vo.setCode(0);
			vo.setMsg("保存信息失败");
		}
		renderJson(vo);
	}
	
	/**
	 * 进入到学生列表页面
	 */
	public void list(){
		LogKit.info("========进入学生列表========");
		String sql = Db.getSql("admin.listStudent");
		List<Student> list = Student.dao.find(sql);
		LogKit.info("========"+list);
		renderJson(list);
	}
	
	/**
	 * 根据学生的学号查询学生信息
	 */
	public void findByNumber(){
		String number = this.getPara("number").trim();
		ObjectVo vo = new ObjectVo();
		if (!number.equals("") || null != number) {
			// 学生学号不能为空
			String sql = Db.getSql("admin.findStudentByNumber");
			Student student = Student.dao.findFirst(sql, number);
			if (null != student) {
				// 学生以存在
				vo.setCode(1);
				vo.setMsg("获取学生信息成功");
				vo.setObject(student);
			}else {
				vo.setCode(0);
				vo.setMsg("获取学生信息失败");
				vo.setObject(null);
			}
		}
		renderJson(vo);
	}
	
	/**
	 * 查询所有的学生信息
	 */
	public void findAll(){
		String sql = Db.getSql("admin.findStudentByNumber");
	}
}
