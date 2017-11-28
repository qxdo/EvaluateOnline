package com.core.system.admin.index.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.server.handler.IPAccessHandler;

import com.common.util.FileRW;
import com.core.common.model.Admin;
import com.core.common.model.Student;
import com.core.common.model.Teacher;
import com.core.common.vo.ObjectVo;
import com.core.system.admin.clazz.pojo.ClazzPojo;
import com.core.system.admin.index.pojo.AdminPojo;
import com.core.system.admin.student.pojo.StudentPojo;
import com.core.system.admin.teacher.pojo.TeacherPojo;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ImportParams;

public class AdminController extends Controller {
	
	public void maintainStart(){
		String path =  this.getSession().getServletContext().getRealPath("static/modules/prop/status.properties");
		FileRW.writestart(path);
		setAttr("maintain", "start");
		renderJson();
	}
	public void maintainStop(){
		String path =  this.getSession().getServletContext().getRealPath("static/modules/prop/status.properties");
		FileRW.writestop(path);
		setAttr("maintain", "stop");
		renderJson();
	}
	
	/**
	 * 进入首页面
	 */
	public void index(){
		Admin admin = getSessionAttr("admin");
		render("index.html");
	}
	
	/**
	 * 进入欢迎页面
	 */
	public void welcome(){
		render("welcome.html");
	}
	
	/**
	 * 进入管理员及业务管理员登录页面
	 */
	@Clear
	public void login(){
		render("login.html");
	}
	
	/**
	 * 登录处理
	 */
	@Clear
	public void logindeal(){
		ObjectVo vo = new ObjectVo();
		// 判断验证码是否正确
		boolean result = validateCaptcha("captcha");
				
		if (!result) {
			// 验证码错误
			vo.setCode(0);
			vo.setMsg("验证码出错");
			vo.setObject(null);
			renderJson(vo);
			return;
		}else {
			// 验证成功,判断帐号与密码
			String number = getPara("number").trim();  // 获取帐号
			String password = getPara("password").trim();  // 获取密码
			int type = getParaToInt("type");  // 获取管理员类
			
			if (null == number || number.equals("") || null == password || password.equals("")) {
				vo.setCode(2);
				vo.setMsg("信息不能为空");
				vo.setObject(null);
				renderJson(vo);
				return;
			}else {
				// 进行Session判断
				Admin admin = getSessionAttr("admin");
				if (admin != null) {
					// session存在
					if (admin.getNumber().equals(number) && admin.getPassword().equals(password) && admin.getType() == type) {
						// 登录成功
						vo.setCode(1);
						vo.setMsg("登录成功");
						vo.setObject(null);
						renderJson(vo);
						return;
					}
				}
				// 获取SQL语句
				String sql = Db.getSql("admin.findAdminByNumberAndType");
				// 根据帐号以及帐呈类开判断用户是否存在
				admin = Admin.dao.findFirst(sql, number, type);
				if (null == admin) {
					// 用户不存在
					vo.setCode(3);
					vo.setMsg("用户不存在");
					vo.setObject(null);
					renderJson(vo);
				}else {
					// 获取Admin
					if (!admin.getPassword().equals(HashKit.md5(password))) {
						// 密码错误
						vo.setCode(4);
						vo.setMsg("密码错误");
						vo.setObject(null);
						renderJson(vo);
					}else {
						// 保存session
						setSessionAttr("admin", admin);
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
	
	/**
	 * 管理员退出登录
	 */
	public void logout(){
		removeSessionAttr("admin");
		render("login.html");
	}
	
	//------------------------------------------------ 业务管理员乾操作
	
	/**
	 * 进入业务管理员页面
	 */
	public void ywadmin(){
		render("yw_list.html");
	}
	
	/**
	 * 进入业务管理员添加页面
	 */
	public void add(){
		setAttr("type", "add");
		setAttr("usertype", "ywadmin");
		render("user_add.html");
	}
	
	// 判断管理员的密码是否正确
	public void checkPass(){
		//获取旧密码
		String oldPass = HashKit.md5(getPara("oldpsd"));
		
		Admin admin = (Admin) getSessionAttr("admin");
		//获取到数据库中用户信息
		Admin ad = Admin.dao.findById(admin.getId());
		
		ObjectVo vo = new ObjectVo();
		// 密码比对
		if(!oldPass.equals(ad.getPassword())){
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
	
	
	
	//修改管理员密码
	public void updateAdminPass(){
		//获取新的密码
		String newPass = HashKit.md5(getPara("newpsd"));
		
		//获取登录的用户
		Admin admin = (Admin) getSessionAttr("admin");
		String sql1 = Db.getSql("admin.findAdminByNumberForType1");
		String sql2 = Db.getSql("admin.findAdminByNumberForType2");
		ObjectVo vo = new ObjectVo();		
		//判断用户类型，为系统管理员 或者是 课程管理员，对应不同的修改方法
		if(admin.getType() == 1){
			Record tea = Db.findFirst(sql1,admin.getNumber()).set("password", newPass);
			boolean update = Db.update("admin",tea);
			if(update == true){
				vo.setCode(1);
				vo.setMsg("修改成功,请关闭窗口");
				vo.setObject(null);
				
			} else {
				vo.setCode(0);
				vo.setMsg("修改失败,请重新输入");
				vo.setObject(null);
			}
		
		}else{
			Record tea = Db.findFirst(sql2,admin.getNumber()).set("password", newPass);
			boolean update = Db.update("admin",tea);
			if(update == true){
				vo.setCode(1);
				vo.setMsg("修改成功,请关闭窗口");
				vo.setObject(null);
				
			} else {
				vo.setCode(0);
				vo.setMsg("修改失败,请重新输入");
				vo.setObject(null);
			}
		}
		
		renderJson(vo);
		
		
		
		
	}
	
	
	/**
	 * 下载课程模版
	 */
	public void download(){
		String type = this.getPara("type");
		String param = "";
		String newparam = "";
		if (type.equals("student")) {
			param = "student.xlsx";
			newparam = "学生模版";
		}else if (type.equals("teacher")) {
			param = "teacher.xlsx";
			newparam = "教师模版";
		}else if (type.equals("courseadmin")) {
			param = "courseadmin.xlsx";
			newparam = "课程负责人模版";
		}else if(type.equals("class")){
			param = "class.xlsx";
			newparam = "班级信息模板";
		}
		renderFile(param, newparam+".xlsx");
	}
	
	/**
	 * 批量添加
	 */
	public void batchAdd(){
		// getFile
		UploadFile uploadFile = getFile("file");

		String type = this.getPara("type");

		String filepath = uploadFile.getUploadPath();
		String filename = uploadFile.getFileName();
		String path = filepath + "/" + filename;

		File file = new File(path);
		
		if (type.equals("student")) {
			// 批量添加学生
			String newpath = filepath + "/student/" + filename;
			File newfile = new File(newpath);
			

			File uploadFolder = new File(filepath+ "/student/" );
			if(!uploadFolder.exists()){
				uploadFolder.mkdirs();
			}
			
			if (newfile.exists()) {
				newfile.delete();
			}
			
			// 移到到新文件夹
			file.renameTo(newfile);

			ImportParams params = new ImportParams();
			params.setTitleRows(1);
			params.setHeadRows(1);
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(newfile);
				//inputStream = new FileInputStream(filePath);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			List<StudentPojo> lists = null;
			try {
				lists = ExcelImportUtil.importExcel(inputStream, StudentPojo.class, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (StudentPojo list : lists) {
				if (null != list.getNumber().trim() || !list.getNumber().trim().equals("")) {
					Record student = new Record().set("number", list.getNumber())
							 .set("name", list.getName())
						 	 .set("sex", list.getSex())
					 		 .set("email", list.getEmail())
					 		 .set("phone", list.getPhone())
					 	  	 .set("classid", list.getClassid())
							 .set("classnumber", list.getClassnumber());
					Db.save("student", student);
				}
			}
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			redirect("/admin/student");
		} else if (type.equals("teacher")) {
			// 批量添加教师
			/*String newpath = filepath + "/teacher/" + filename;
			File newfile = new File(newpath);
			file.renameTo(newfile);*/
			
			String newpath = filepath + "/teacher/" + filename;
			
			
			
			
			File uploadFolder = new File(filepath+ "/teacher/" );
			if(!uploadFolder.exists()){
				uploadFolder.mkdirs();
			}
			File newfile = new File(newpath);
			if (newfile.exists()) {
				newfile.delete();
			}
			
			// 移到到新文件夹
			file.renameTo(newfile);

			ImportParams params = new ImportParams();
			params.setTitleRows(1);
			params.setHeadRows(1);
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(newfile);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			List<TeacherPojo> lists = null;
			try {
				lists = ExcelImportUtil.importExcel(inputStream, TeacherPojo.class, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (TeacherPojo list : lists) {
				if (null != list.getNumber().trim() || !list.getNumber().trim().equals("")) {
					Record teacher = new Record().set("number", list.getNumber())
							.set("name",list.getName())
							.set("sex", list.getSex())
							.set("email", list.getEmail())
							.set("phone",list.getPhone());
					Db.save("teacher", teacher);
				}
			}
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			redirect("/admin/teacher");
		} else if (type.equals("courseadmin")) {
			// 批量添加课负责人
			String newpath = filepath + "/courseadmin/" + filename;
			File newfile = new File(newpath);
			
			
			File uploadFolder = new File(filepath+ "/courseadmin/" );
			if(!uploadFolder.exists()){
				uploadFolder.mkdirs();
			}
			if (newfile.exists()) {
				newfile.delete();
			}
			// 移到到新文件夹
			file.renameTo(newfile);

			ImportParams params = new ImportParams();
			params.setTitleRows(1);
			params.setHeadRows(1);
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(newfile);
				//inputStream = new FileInputStream(filePath);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			List<AdminPojo> lists = null;
			try {
				lists = ExcelImportUtil.importExcel(inputStream, AdminPojo.class, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (AdminPojo list : lists) {
				if (null != list.getNumber().trim() || !list.getNumber().trim().equals("")) {
					Record admin = new Record().set("number", list.getNumber())
							   .set("name", list.getName())
							   .set("sex", list.getSex())
							   .set("email", list.getEmail())
							   .set("phone", list.getPhone())
							   .set("flag", 1);
					Db.save("admin", admin);
				}
			}
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			redirect("/admin/ywadmin");
		} else if (type.equals("class")) {
			// 批量添加班级信息
			String newpath = filepath + "/class/" + filename;
			File newfile = new File(newpath);
			File uploadFolder = new File(filepath+ "/class/" );
			if(!uploadFolder.exists()){
				uploadFolder.mkdirs();
			}
			if (newfile.exists()) {
				newfile.delete();
			}
			// 移到到新文件夹
			file.renameTo(newfile);

			ImportParams params = new ImportParams();
			params.setTitleRows(1);
			params.setHeadRows(1);
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(newfile);
				//inputStream = new FileInputStream(filePath);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			List<ClazzPojo> lists = null;
			try {
				lists = ExcelImportUtil.importExcel(inputStream, ClazzPojo.class, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (ClazzPojo list : lists) {
				if (null != list.getNumber().trim() || !list.getNumber().trim().equals("")) {
					Record clazz = new Record().set("number", list.getNumber()).set("name", list.getName());
					Db.save("clazz", clazz);
				}
			}
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			redirect("/admin/class");
			
			
			
	
		}
	}
	
	/**
	 * 对业务管理员信息进行保存
	 */
	public void save(){
		String number = this.getPara("number").trim();
		String name = this.getPara("number").trim();
		int sex = this.getParaToInt("sex");
		String email = this.getPara("email").trim();
		String phone = this.getPara("phone").trim();
		boolean flag = false;
		ObjectVo vo = new ObjectVo();
		if (!number.equals("") && !name.equals("")) {
			// 课程管理员的编号及名字不能为空
			Admin admin = new Admin();
			flag = admin.set("number", number)
					    .set("name", name)
					    .set("sex", sex)
					    .set("email", email)
					    .set("phone", phone)
					    .set("type", 2)
					    .set("createtime", new Date())
					    .set("modifytime", new Date())
					    .set("password", HashKit.md5("123456"))
					    .save();
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
	 * 对业务管理员进行删除
	 */
	public void delete(){
		Long id = this.getParaToLong("id");
		ObjectVo vo = new ObjectVo();
		boolean flag = false;
		if (null !=id) {
			flag = Admin.dao.findById(id).set("flag", 0).update();
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
	 * 对业务管理员进行修改
	 */
	public void update(){
		
		String number = this.getPara("number").trim();
		Admin admin = new Admin();
		if (!number.equals("") && null != number) {
			// 班级编号不能为空
			String sql = Db.getSql("admin.findAdminByNumberForType2");
			admin = Admin.dao.findFirst(sql, number);
			if (null != admin) {
				// 修改的班级存在,返回班级信息
				setAttr("user", admin);
			}
		}
		setAttr("type", "update");
		setAttr("usertype", "ywadmin");
		render("user_add.html");
	}
	
	/**
	 * 修改
	 */
	public void updatedeal(){
		Long id = this.getParaToLong("id");
		String number = this.getPara("number").trim();
		String name = this.getPara("name");
		int sex = this.getParaToInt("sex");
		String email = this.getPara("email");
		String phone = this.getPara("phone");
		
		// 
		ObjectVo vo = new ObjectVo();
		boolean flag = false;
		
		String sql = Db.getSql("admin.findAdminByNumberForType2");
		// 查询编号是否使用
		Admin admin = Admin.dao.findFirst(sql, number);
		if (admin == null || admin.getNumber().equals(number)) {
			// 修改
			flag = Admin.dao.findById(id).set("number", number)
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
	 * 返回业务管理员列表
	 */
	public void list(){
		String sql = Db.getSql("admin.listAdmin");
		List<Admin> admins = Admin.dao.find(sql);
		renderJson(admins);
	}
	
	/**
	 * 查询显示所有业务管理员
	 */
	public void findAll(){
		Admin.dao.find("");
	}
	
	/**
	 * 根据管理员工号查询管理员信息
	 */
	public void findByNumber(){
		// 
		String number = this.getPara("number").trim();
		ObjectVo vo = new ObjectVo();
		if (null == number || number.equals("")) {
			// 工号不能为空
			vo.setCode(0);
			vo.setMsg("工号不能为空");
		}else{
			String sql = Db.getSql("admin.findAdminByNumberForType2");
			Admin admin = Admin.dao.findFirst(sql, number);
			if (null != admin) {
				// 用户已经存在
				vo.setCode(1);
				vo.setMsg("用户已存在");
			}else {
				// 用户不存在
				vo.setCode(0);
				vo.setMsg("用户已存在，可以添加");
			}
		}
		renderJson(vo);
	}
	
	/**
	 * 重置业务管理员密码
	 */
	public void reset(){
		String id = this.getPara("id").trim();
		String usertype = this.getPara("usertype").trim();
		boolean flag = false;
		ObjectVo vo = new ObjectVo();
		if (usertype.equals("student")) {
			flag = Student.dao.findById(id).set("password", HashKit.md5("123456")).update();
		}else if(usertype.equals("teacher")){
			flag = Teacher.dao.findById(id).set("password", HashKit.md5("123456")).update();
		}else if(usertype.equals("ywadmin")){
			flag = Admin.dao.findById(id).set("password", HashKit.md5("123456")).update();
		}
		if (flag) {
			// 修改成功
			vo.setCode(1);
			vo.setMsg("密码重置成功");
		}else{
			// 修改失败
			vo.setCode(0);
			vo.setMsg("密码重置失败");
		}
		renderJson(vo);
	}
	
}
