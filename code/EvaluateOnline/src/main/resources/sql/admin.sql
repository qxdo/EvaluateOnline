/**
 * 管理员操作
 */

#sql("login")
  SELECT * FROM admin where number = ? and password = ? and type = ?
#end






/**
 * 业务管理员操作
 */
-- 查询有效的业务管理员
#sql("listAdmin")
  SELECT id,number,name,sex,email,phone,createtime,createby,modifytime,modifyby FROM admin where type = 2 and flag = 1 
#end

#sql("findAdminByNumberForType2")
  SELECT * FROM admin where number = ? and type = 2 and flag = 1
#end

#sql("findAdminByNumberForType1")
  SELECT * FROM admin where number = ? and type = 1 and flag = 1
#end
#sql("findAdminByNumberAndType")
  SELECT * FROM admin where number = ? and type = ?
#end






/**
 * 老师管理
 */
#sql("findAllTeahcer")
  SELECT id,number,name,sex,email,phone,createtime,createby,modifytime,modifyby FROM teacher where flag = 1
#end

#sql("listTeacher")
  SELECT id,number,name,sex,email,phone,createtime,createby,modifytime,modifyby FROM teacher where flag = 1
#end

#sql("findTeacherByNumber")
  SELECT id,number,name,sex,email,phone,createtime,createby,modifytime,modifyby FROM teacher where number = ? and flag = 1
#end






/**
 * 学生管理
 */
-- 查询有效的学生
#sql("listStudent")
  SELECT id,number,name,sex,email,phone,createtime,createby,modifytime,modifyby FROM student where flag = 1
#end

#sql("findStudentByNumber")
  SELECT id,number,name,sex,email,phone,createtime,createby,modifytime,modifyby FROM student where number = ? and flag = 1
#end






/**
 * 班级管理
 */

-- 查询有效的班级
#sql("listClass")
  SELECT id,number,name,createtime,createby,modifyby,modifytime FROM clazz where flag = 1
#end

-- 查询所有班级
#sql("findAllClass")
  SELECT * FROM clazz where flag = 1
#end

#sql("findClassByNumber")
  SELECT * FROM clazz where number = ? and flag = 1
#end






/**
 * 课程管理
 */
#sql("listAdminCourse")
  SELECT * FROM course where adminid = ? and flag = 1
#end

#sql("findAllCourse")
  SELECT * FROM course where flag = 1
#end

#sql("findCourseByNumber")
  SELECT * FROM course where number = ? and flag = 1
#end


#sql("findDistributeByCourseNumber")
  SELECT * FROM teachercourseclazz where coursenumber = ? and flag = 1
#end
#sql("findDistributeByClazzNumber")
  SELECT * FROM teachercourseclazz where clazznumber = ? and flag = 1
#end
#sql("findDistributeByTeacherNumber")
  SELECT * FROM teachercourseclazz where teachernumber = ? and flag = 1
#end

#sql("findDistributeOfCTC")
  SELECT * FROM teachercourseclazz where clazznumber = ? and teachernumber = ? and courseid = ? and flag = 1
#end


/**
 * 章
 */
#sql("findChapterByCourse")
  SELECT * FROM chapter where courseid = ? and flag = 1
#end


/**
 * 节
 */
-- 通过章ID查询所有有效的节
#sql("findSectionByChapter")
  SELECT * FROM section where chapterid = ? and flag = 1
#end

#sql("findSectionByCCID")
  SELECT * FROM section where courseid = ? and chapterid = ? and flag = 1
#end

-- 删除章下边的所有节
#sql("deleteSectionByCC")
  UPDATE section SET flag = 0 WHERE courseid = ? AND chapterid = ?
#end

/**
 * 小节
 */
-- 通过节ID查询所有有效的小节
#sql("findPartBySection")
  SELECT * FROM part where sectionid = ? and flag = 1
#end

#sql("findPartByCCSID")
  SELECT * FROM part where courseid = ? and chapterid = ? and sectionid = ? and flag = 1
#end

-- ORDER BY chapterid,sectionid,sort ASC
#sql("findPartByCourse")
  SELECT * FROM part where courseid = ? and flag = 1 
#end

-- 删除节下的所有小节
#sql("deletePartByCC")
  UPDATE part SET flag = 0 WHERE courseid = ? AND chapterid = ?
#end





/**
 * 实验
 */


