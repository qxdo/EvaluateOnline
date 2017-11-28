package com.core.system.admin.course.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.PrimitiveIterator.OfDouble;

import org.apache.commons.collections4.map.HashedMap;

import com.core.common.model.Admin;
import com.core.common.model.Chapter;
import com.core.common.model.Clazz;
import com.core.common.model.Course;
import com.core.common.model.Part;
import com.core.common.model.Section;
import com.core.common.model.Teacher;
import com.core.common.model.Teachercourseclazz;
import com.core.common.vo.ObjectVo;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

/**
 * 课程管理(业务管理员)
 * @author Hang
 *
 */
public class CourseController extends Controller {
	
	/**
	 * 进入课程管理页面
	 */
	public void index(){
		render("course_list.html");
	}
	
	/**
	 * 业务管理进入添加课程页面
	 */
	public void add(){
		render("course_add.html");
	}
	
	/**
	 * 业务管理员保存课程信息
	 */
	public void save(){
		String number = this.getPara("number");
		String name = this.getPara("name");
		boolean flag = false;
		ObjectVo vo = new ObjectVo();
		if (!number.equals("") && !name.equals("")) {
			// 编号和名字不能为空
			// Course course = new Course();
			String sql = Db.getSql("admin.findCourseByNumber");
			Course course = Course.dao.findFirst(sql, number);
			if (null != course) {
				vo.setCode(2);
				vo.setMsg("课程已存在");
			}else{
				Admin admin = getSessionAttr("admin");
				
				flag = new Course().set("number", number)
							     .set("name", name)
							     .set("adminid", admin.getId())
							     .set("adminnumber", admin.getNumber())
							     .set("createtime", new Date())
							     .set("createby", admin.getNumber())
							     .set("modifytime", new Date())
							     .set("modifyby", admin.getNumber())
							     .save();
			}
		}
		if (flag) {
			// 课程保存成功
			vo.setCode(1);
			vo.setMsg("课程保存成功");
		}else {
			// 课程保存失败
			vo.setCode(0);
			vo.setMsg("课程保存失败");
		}
		renderJson(vo);
	}
	
	/**
	 * 业务管理员操作删除课程
	 */
	public void delete(){
		String number = this.getPara("number");
		boolean flag = false;
		ObjectVo vo = new ObjectVo();
		if (!number.equals("")) {
			// 课程编号不能为空
			String sql = Db.getSql("admin.findCourseByNumber");
			Course course = Course.dao.findFirst(sql, number);
			// 删除课程
			flag = course.set("flag", 0).update();
		}
		if (flag) {
			// 删除成功
			vo.setCode(1);
			vo.setMsg("删除课程成功");
		}else{
			// 删除失败
			vo.setCode(0);
			vo.setMsg("删除课程失败");
		}
		renderJson(vo);
	}
	
	/**
	 * 进入修改课程信息页面
	 */
	public void update(){
		// 获取课程编号 
		String number = this.getPara("number");
		String sql = Db.getSql("admin.findCourseByNumber");
		Course course = Course.dao.findFirst(sql, number);
		setAttr("course", course);
		render("course_add.html");
	}
	
	/**
	 * 列表显示    课程负责人   课程信息
	 */
	public void list(){
		Admin admin = getSessionAttr("admin");
		String sql = Db.getSql("admin.listAdminCourse");
		List<Course> courses = Course.dao.find(sql, admin.getId());
		renderJson(courses);
	}
	
	// --------------------------------------- 业务管理操作
	
	/**
	 * 业务管理员进行课程操作
	 */
	public void yw(){
		setAttr("type", "course");
		render("list.html");
	}
	
	/**
	 * 查询课程信息
	 */
	public void findCourseByNumber(){
		// 获取课程编号
		String number = this.getPara("number");
		String sql = Db.getSql("admin.findCourseByNumber");
		Course course = Course.dao.findFirst(sql, number);
		ObjectVo vo = new ObjectVo();
		if (null != course) {
			// 课程不能为空
			vo.setCode(1);
			vo.setMsg("课程已存在");
			vo.setObject(course);
		}else {
			// 
			vo.setCode(0);
			vo.setMsg("查询课程为空");
			vo.setObject(course);
		}
		renderJson(vo);
	}
	
	/**
	 * 业务管理员对课程进行分配（进入页面）
	 */
	public void distribute(){
		// 获取课程编号
		String number = this.getPara("number");
		String sql = Db.getSql("admin.findCourseByNumber");
		Course course = Course.dao.findFirst(sql, number);
		
		// 查询显示所有的教师
		String teachersql = Db.getSql("admin.findAllTeahcer");
		List<Teacher> teachers = Teacher.dao.find(teachersql);
		
		// 查询班级信息
		String clazzsql = Db.getSql("admin.findAllClass");
		List<Clazz> clazzs = Clazz.dao.find(clazzsql);
		
		setAttr("type", "course");
		setAttr("distribute", course);
		setAttr("teachers", teachers);
		setAttr("clazzs", clazzs);
		render("distribute.html");
	}
	
	/**
	 * 分配信息保存
	 */
	public void distributesave(){
		// 分配信息直接保存到表中
		Long courseid = this.getParaToLong("courseid");
		String clazznumber = this.getPara("clazznumber");
		String teachernumber = this.getPara("teachernumber");
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
		String number= this.getPara("number");
		setAttr("type", "course");
		
		render("distribute_manage.html");
	}
	
	/**
	 * 获取分配列表
	 */
	public void distributelist(){
		String number = this.getPara("number");
		List<Map<String, Object>> list = new ArrayList<>();
		if (null != number && !number.equals("")) {
			String sql = Db.getSql("admin.findDistributeByCourseNumber");
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
	
	
	// ------------------------------------------------------- 课程负责人对树操作
	
	/**
	 * 点击添加Tree(根据当前节点的信息设置新节点信息)
	 */
	public void addTree(){
		Long courseid = this.getParaToLong("courseid");
		Integer level = this.getParaToInt("level");
		ObjectVo vo = new ObjectVo();
		// 课程ID以及目录深度不能为空
		if (null != level && !courseid.equals("")) {
			if(level == 0){
				// 添加章 (通过课程查询有多少章节)
				String sql = Db.getSql("admin.findChapterByCourse");
				List<Chapter> chapters = Chapter.dao.find(sql, courseid);
				vo.setCode(1);
				vo.setMsg("获取信息成功");
				vo.setObject("第  "+(chapters.size()+1)+" 章");
			}else if (level == 1) {
				// 添加节
				Long chapterid = this.getParaToLong("id");  // 章ID\
				String sql = Db.getSql("admin.findSectionByCCID");
				List<Section> sections = Section.dao.find(sql, courseid, chapterid);
				vo.setCode(1);
				vo.setMsg("获取信息成功");
				vo.setObject("第  "+(sections.size()+1)+" 节");
			}else if (level == 2) {
				// 添加小节
				Long chapterid = this.getParaToLong("fid");  // 章ID
				Long sectionid = this.getParaToLong("id");  // 节ID
				String sql = Db.getSql("admin.findPartByCCSID");
				List<Part> parts = Part.dao.find(sql, courseid, chapterid, sectionid);
				vo.setCode(1);
				vo.setMsg("获取信息成功");
				vo.setObject("第  "+(parts.size()+1)+" 小节");
			}
			
		}
		renderJson(vo);
	}
	
	/**
	 * 添加树
	 */
	public void saveTree(){
		// 树的深度(0表示根及课程，1表示章，2表示节，3表示小节)
		Integer level = this.getParaToInt("level");
		// 章节名
		String name = this.getPara("name");
		// 获取课程ID
		Long courseid = this.getParaToLong("courseid");
		boolean flag = false;
		ObjectVo vo = new ObjectVo();
		if (null != level && !name.equals("")) {
			Course course = Course.dao.findById(courseid);
			if (level == 0) {
				// 添加章
				// 查询课程的所有章节
				String sql = Db.getSql("admin.findChapterByCourse");
				List<Chapter> chapters = Chapter.dao.find(sql, courseid);
				Chapter chapter = new Chapter().set("name", name)
							  				   .set("courseid", courseid)
							  				   .set("coursenumber", course.getNumber())
							                   .set("sort", chapters.size()+1)
							                   .set("createtime", new Date())
							                   .set("modifytime", new Date());
				flag = chapter.save();
				if (flag) {
					vo.setCode(1);
					vo.setObject(chapter);
				}else {
					vo.setCode(0);
				}
			}else if (level == 1) {
				// 添加节
				// 查询章节下对应的所有课程
				Long chapterid = this.getParaToLong("id");  // 章ID
				Chapter chapter = Chapter.dao.findById(chapterid);
				String sql = Db.getSql("admin.findSectionByCCID");
				List<Section> sections = Section.dao.find(sql, courseid, chapterid);
				Section section = new Section().set("name", name)
											   .set("courseid", courseid)
											   .set("coursenumber", course.getNumber())
											   .set("coursename", course.getName())
											   .set("chapterid", chapterid)
											   .set("chaptername", chapter.getName())
											   .set("chapternumber", chapter.getNumber())
											   .set("sort", sections.size()+1)
											   .set("createtime", new Date())
							                   .set("modifytime", new Date());
				flag = section.save();
				if (flag) {
					vo.setCode(1);
					vo.setObject(section);
				}else {
					vo.setCode(0);
				}
			}else if (level == 2) {
				// 添加小节
				Long chapterid = this.getParaToLong("fid");  // 章ID
				Long sectionid = this.getParaToLong("id");  // 节ID
				String sql = Db.getSql("admin.findPartByCCSID");
				List<Part> parts = Part.dao.find(sql, courseid, chapterid, sectionid);
				Part part = new Part().set("name", name)
									  .set("courseid", courseid)
									  .set("chapterid", chapterid)
									  .set("sectionid", sectionid)
									  .set("sort", parts.size()+1)
									  .set("createtime", new Date())
					                  .set("modifytime", new Date());
				flag = part.save();
				if (flag) {
					// 添加成功
					vo.setCode(1);
					vo.setObject(part);
				}else {
					vo.setCode(0);
				}
			}
		}
		renderJson(vo);
		
	}
	
	/**
	 * 删除树
	 */
	public void deleteTree(){
		// 树的深度(0表示根及课程，1表示章，2表示节，3表示小节)
		Integer level = this.getParaToInt("level");
		Long courseid = this.getParaToLong("courseid");
		boolean flag = false;
		ObjectVo vo = new ObjectVo();
		if (null != level) {
			if (level == 1) {
				// 删除章及以下全部内容
				//Long courseid = this.getParaToLong("fid");  // 课程ID
				/*Long chapterid = this.getParaToLong("id");  // 章ID
				
				// 删除小节
				String partsql = Db.getSql("admin.deletePartByCC");
				Db.update(partsql, courseid, chapterid);
				// 删除节
				String sectionsql = Db.getSql("admin.deleteSectionByCC");
				Db.update(sectionsql, courseid, chapterid);
				// 删除章
				flag = Chapter.dao.findById(chapterid).set("flag", 0).update();*/
				
				Long chapterid = this.getParaToLong("id");  // 章ID
				flag = Chapter.dao.findById(chapterid).set("flag", 0).update();
				
			}else if (level == 2) {
				// 删除节及以下全部内容
				//Long chapterid = this.getParaToLong("fid");  // 章ID
				Long sectionid = this.getParaToLong("id");  // 节ID
				// 查询节下的所有所有小节
				//String sql = Db.getSql("admin.deletePartByCC");
				//Db.update(sql, courseid, chapterid, sectionid);
				flag = Section.dao.findById(sectionid).set("flag", 0).update();
				
			}else if (level == 3) {
				// 删除小节内容
				// Long chapterid = this.getParaToLong("fid");  // 章ID
				Long partid = this.getParaToLong("id");  // 小节ID
				flag = Part.dao.deleteById(partid);
			}
		}
		if (flag) {
			// 删除成功
			vo.setCode(1);
		}else{
			// 删除失败
			vo.setCode(0);
		}
		renderJson(vo);
		
	}
	
	/**
	 * 修改树
	 */
	public void updatetree(){
		
	}
	
	
}
