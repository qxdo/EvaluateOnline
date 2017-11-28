package com.core.system.student.index.controller;

import java.util.List;

import com.core.common.model.Course;
import com.core.common.model.Student;
import com.core.common.vo.ObjectVo;
import com.core.system.student.course.service.CourseService;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class StudentController extends Controller {

	CourseService courseService = new CourseService();
	
	@Clear
	public void login() {
		render("login.html");
	}

	@Clear
	public void logindeal() {
		ObjectVo vo = new ObjectVo();
		// 判断验证码是否正确
		boolean result = validateCaptcha("captcha");
		
		if (result) {
			// 验证码错误
			vo.setCode(0);
			vo.setMsg("验证码出错");
			vo.setObject(null);
			System.out.println("验证码出错");
			renderJson(vo);
			return;
		}else {
			// 验证成功,判断帐号与密码
			String number = getPara("username");  // 获取帐号
			String password = getPara("password");  // 获取密码
			System.out.println(number + password);
			if (null == number || number.equals("") || null == password || password.equals("")) {
				vo.setCode(2);
				vo.setMsg("信息不能为空");
				vo.setObject(null);
				renderJson(vo);
				return;
			}else {
				// 进行Session判断
				Student student = getSessionAttr("student");
				if (student != null) {
					// session存在
					if (student.getNumber().equals(number) && student.getPassword().equals(password)) {
						// 登录成功
						vo.setCode(1);
						vo.setMsg("登录成功");
						vo.setObject(null);
						renderJson(vo);
						System.out.println("登录成功");
						
						return;
					}
				}
				System.out.println("获取学生");
				// 获取SQL语句
				String sql = Db.getSql("student.findStudentByNumber");
				// 根据帐号以及帐呈类开判断用户是否存在
				List<Student> students = Student.dao.find(sql, number);
				if (students.isEmpty()) {
					// 用户不存在
					vo.setCode(3);
					vo.setMsg("学生不存在");
					vo.setObject(null);
					renderJson(vo);
				}else {
					// 获取Student
					student = students.get(0);
					
					
					if (!student.getPassword().equals(HashKit.md5(password))) {
						// 密码错误
						
						System.out.println("密码错误");
						vo.setCode(4);
						vo.setMsg("密码错误");
						vo.setObject(null);
						renderJson(vo);
					}else {
						// 保存session
						setSessionAttr("student", student);
						// 登录成功
						vo.setCode(1);
						vo.setMsg("登录成功");
						vo.setObject(null);
						renderJson(vo);
					}
				}
			}
			
		}
	}
	
	//检查密码是否正确
	public void checkPass(){
		String oldPass = HashKit.md5(getPara("oldpsd"));
		
		Student student = (Student) getSessionAttr("student");
		
		Student stu = Student.dao.findById(student.getId());
		
		ObjectVo vo = new ObjectVo();
		if(!oldPass.equals(stu.getPassword())){
			vo.setCode(0);
			vo.setMsg("密码错误");
			vo.setObject(null);
		}else{
			vo.setCode(1);
			vo.setMsg("密码正确");
			vo.setObject(null);
		}
		renderJson(vo);
	}
	
	
	public void updatePassword(){
		String newPass = HashKit.md5(getPara("newpsd"));
		
		Student student = (Student) getSessionAttr("student");
		String sql = Db.getSql("student.findStudentByNumber");
		
		Record stu = Db.findFirst(sql,student.getNumber()).set("password", newPass);
		boolean update = Db.update("student",stu);
		ObjectVo vo = new ObjectVo();
		if(update == true){
			
			vo.setCode(1);
			vo.setMsg("修改成功,请关闭窗口");
			vo.setObject(null);
			
		} else {
			vo.setCode(0);
			vo.setMsg("修改失败,请重新输入");
			vo.setObject(null);
			
		}
		
		renderJson(vo);
	}
	/**
	 * 进入主页面
	 */
	public void courseManage(){
		render("stu_course.html");
	}
	
	public void index() {
		render("stu_index.html");
	}
	public void CourseWithQuestion(){
		render("stu_course_chapter.html");
	}
	public void logout(){
		removeSessionAttr("student");
		render("login.html");
	}
		
	public void ChapterFromCourse(){
		Long courseid = this.getParaToLong("id");
		Course course = Course.dao.findById(courseid);		// 目录以层级不能为空	
		setAttr("courseid", courseid);
		setAttr("course", course);
		render("course.html");
	}
	
	
}
