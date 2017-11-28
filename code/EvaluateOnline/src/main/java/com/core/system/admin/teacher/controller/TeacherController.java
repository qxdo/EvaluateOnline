package com.core.system.admin.teacher.controller;

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
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.Db;

public class TeacherController extends Controller{
	
	/**
	 * 进入教师管理页面
	 */
	public void index(){
		render("teacher_list.html");
	}
	
	/**
	 * 进入添加教师页面
	 */
	public void add(){
		setAttr("type", "add");
		setAttr("usertype", "teacher");
		render("user_add.html");
	}
	
	/**
	 * 保存老师信息
	 */
	public void save(){
		// 获取教师信息
		String number = this.getPara("number").trim();
		String name = this.getPara("number").trim();
		int sex = this.getParaToInt("sex");
		String email = this.getPara("email").trim();
		String phone = this.getPara("phone").trim();
		boolean flag = false;
		ObjectVo vo = new ObjectVo();
		if (!number.equals("") && !name.equals("")) {
			// 教师信息的编号及名字不能为空
			Teacher teacher = new Teacher();
			flag = teacher.set("number", number)
						  .set("name", name)
						  .set("sex", sex)
						  .set("email", email)
						  .set("phone", phone)
						  .set("createtime", new Date())
						  .set("modifytime", new Date())
						  .set("password", HashKit.md5("123456"))
						  .save();
		}
		if (flag) {
			// 保存教师信息成功
			vo.setCode(1);
			vo.setMsg("保存教师信息成功");
		}else{
			// 保存教师信息失败
			vo.setCode(0);
			vo.setMsg("保存教师信息失败");
		}
		renderJson(vo);
	}
	
	/**
	 * 批量添加教师
	 */
	public void batchadd(){
		
	}
	
	
	/**
	 * 删除教师
	 */
	public void delete(){
		// 获取教师信息
		Long id = this.getParaToLong("id");
		boolean flag = false;
		if (null != id) {
			flag = Teacher.dao.findById(id).set("flag", 0).update();
		}
		ObjectVo vo = new ObjectVo();
		if (flag) {
			// 删除成功
			vo.setCode(1);
			vo.setMsg("删除教师成功");
		}else{
			// 删除失败
			vo.setCode(0);
			vo.setMsg("删除教师失败");
		}
		renderJson(vo);
	}
	
	/**
	 * 修改教师
	 */
	public void update(){
		String number = this.getPara("number").trim();
		Teacher teacher = new Teacher();
		if (!number.equals("") && null != number) {
			// 班级编号不能为空
			String sql = Db.getSql("admin.findTeacherByNumber");
			teacher = Teacher.dao.findFirst(sql, number);
		}
		setAttr("type", "update");
		setAttr("user", teacher);
		setAttr("usertype", "teacher");
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
		
		String sql = Db.getSql("admin.findTeacherByNumber");
		// 查询编号是否使用
		Teacher teacher = Teacher.dao.findFirst(sql, number);
		if (teacher == null || teacher.getNumber().equals(number)) {
			// 修改
			flag = Teacher.dao.findById(id).set("number", number)
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
	 * 显示用户列表，以JSON形式
	 */
	public void list(){
		LogKit.info("========进入教师列表========");
		String sql = Db.getSql("admin.listTeacher");
		List<Teacher> list = Teacher.dao.find(sql);
		renderJson(list);
	}
	
	/**
	 * 查询所有教师信息
	 */
	public void findAll(){
		
	}
	
	/**
	 * 根据教师编号查询教师信息
	 */
	public void findByNumber(){
		// 查询
		String number = this.getPara("number").trim();
		if (null != number || number.equals("")) {
			// 教师编号不能为空
			String sql = Db.getSql("admin.findTeacherByNumber");
			Teacher teacher = Teacher.dao.findFirst(sql, number);
			ObjectVo vo  = new ObjectVo();
			if (null != teacher) {
				// 教师存在
				vo.setCode(1);
				vo.setMsg("教师已存在");
				vo.setObject(teacher);
			}else {
				// 教师不存在
				vo.setCode(0);
				vo.setMsg("教师不存在,可以添加");
				vo.setObject(null);
			}
			renderJson(vo);
		}
	}
	
	// ----------------------------------- 业务管理员操作
	
	/**
	 * 业务管理员进入教师页面
	 */
	public void yw(){
		setAttr("type", "teacher");
		render("list.html");
	}
	
	/**
	 * 业务管理员对教师进行分配（进入页面）
	 */
	public void distribute(){
		// 获取教师工号
		String number = this.getPara("number").trim();
		String sql = Db.getSql("admin.findTeacherByNumber");
		Teacher teacher = Teacher.dao.findFirst(sql, number);
		
		// 查询班级信息
		String classsql = Db.getSql("admin.findAllClass");
		List<Clazz> clazzs = Clazz.dao.find(classsql);
		
		// 查询课程信息
		String coursesql = Db.getSql("admin.findAllCourse");
		List<Course> courses = Course.dao.find(coursesql);
		
		setAttr("type", "teacher");
		setAttr("distribute", teacher);
		setAttr("clazzs", clazzs);
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
		// 获取教师工号
		String number= this.getPara("number").trim();
		Teacher teacher = new Teacher();
		if (null != number && !number.equals("")) {
			String sql = Db.getSql("admin.findTeacherByNumber");
			teacher = Teacher.dao.findFirst(sql, number);
		}
		setAttr("type", "teacher");
		setAttr("distribute", teacher);
		render("distribute_manage.html");
	}
	
	/**
	 * 获取分配列表
	 */
	public void distributelist(){
		// 获取教师工号
		String number = this.getPara("number").trim();
		List<Map<String, Object>> list = new ArrayList<>();
		if (null != number && !number.equals("")) {
			String sql = Db.getSql("admin.findDistributeByTeacherNumber");
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
