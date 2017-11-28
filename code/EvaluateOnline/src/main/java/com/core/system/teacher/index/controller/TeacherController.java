package com.core.system.teacher.index.controller;

import java.util.List;

import com.core.common.model.Student;
import com.core.common.model.Teacher;
import com.core.common.vo.ObjectVo;
import com.jfinal.aop.Clear;
import com.jfinal.captcha.CaptchaRender;
import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class TeacherController extends Controller {
	
	/**
	 * 进入首页面
	 */
	public void index(){
		render("index.html");
	}
	
	/**
	 * 进入欢迎页面
	 */
	public void welcome(){
		render("welcome.html");
	}
	
	/**
	 * 进入教师登录页面
	 */
	@Clear
	public void login(){
		render("login.html");
	}
	
	/**
	 * 对老师登录进行处理
	 */
	@Clear
	public void logindeal(){
		ObjectVo vo = new ObjectVo();
		String captcha = this.getPara("captcha").trim();
		// 判断验证码是否正确
		boolean result = CaptchaRender.validate(this, captcha);
		if (!result) {
			// 验证码错误
			vo.setCode(0);
			vo.setMsg("验证码出错");
			vo.setObject(null);
			renderJson(vo);
			return;
		}else {
			// 验证成功,判断帐号与密码
			String number = getPara("number");  // 获取帐号
			String password = getPara("password");  // 获取密码
			
			if (null == number || number.equals("") || null == password || password.equals("")) {
				vo.setCode(2);
				vo.setMsg("信息不能为空");
				vo.setObject(null);
				renderJson(vo);
				return;
			}else {
				// 进行Session判断
				Teacher teacher = getSessionAttr("teacher");
				if (teacher != null) {
					// session存在
					if (teacher.getNumber().equals(number) && teacher.getPassword().equals(HashKit.md5(password))) {
						// 登录成功
						vo.setCode(1);
						vo.setMsg("登录成功");
						vo.setObject(null);
						renderJson(vo);
						return;
					}
				}
				// 获取SQL语句
				String sql = Db.getSql("teacher.findTeacherByNumber");
				// 根据帐号以及帐呈类开判断用户是否存在
				teacher = Teacher.dao.findFirst(sql, number);
				if (null != teacher) {
					// 教师以存在
					if (!teacher.getPassword().equals(HashKit.md5(password))) {
						// 密码错误
						vo.setCode(4);
						vo.setMsg("密码错误");
						vo.setObject(null);
						renderJson(vo);
					}else {
						// 保存session
						setSessionAttr("teacher", teacher);
						// 登录成功
						vo.setCode(1);
						vo.setMsg("登录成功");
						vo.setObject(null);
						renderJson(vo);
					}
				}else{
					// 用户不存在
					vo.setCode(3);
					vo.setMsg("教师不存在");
					vo.setObject(null);
					renderJson(vo);
				}
				
			}
			
		}
	}
	
	/**
	 * 教师退出登录
	 */
	public void logout(){
		removeSessionAttr("teacher");
		render("login.html");
	}
	
	//检查密码是否正确
	public void checkPass(){
		String oldPass = HashKit.md5(getPara("oldpsd"));
		
		Teacher teacher = (Teacher) getSessionAttr("teacher");
		
		Teacher tea = Teacher.dao.findById(teacher.getId());
		
		ObjectVo vo = new ObjectVo();
		if(!oldPass.equals(tea.getPassword())){
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
	
	
	//修改教师密码
	public void updateTeacherPass(){
		String newPass = HashKit.md5(getPara("newpsd"));
		
		Teacher teacher = (Teacher) getSessionAttr("teacher");
		String sql = Db.getSql("teacher.findTeacherByNumber");
		
		Record tea = Db.findFirst(sql,teacher.getNumber()).set("password", newPass);
		boolean update = Db.update("teacher",tea);
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
	
	
	
}
