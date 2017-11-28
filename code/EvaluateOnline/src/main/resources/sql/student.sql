/**
 * 学生管理
 */




/**
 * 班级管理
 */
/**
 * 通过 学生id 获取学生的做过的题
 */

#sql("findexperimentBystudentId")
SELECT * from experiment WHERE id in (SELECT experimentid from studentexpertiment where studentid = ? and flag = 1)
#end

/**
 * 登录
 */

#sql("findStudentByNumber")
	select * from student where number = ? and flag = 1
#end


/**
 *  修改密码
 */




/**
 * 课程管理
 */
#sql("getCourseByclazzId")
  select * from course where id in(select courseid from teachercourseclazz where clazzid = ? and flag = 1)
#end

/**
 * 通过 课程  获取 老师 
 */
#sql("getTeacherBycourseId")
  select * from teacher where id in(select teacherid from teachercourseclazz where courseid = ? and flag = 1)
#end

/**
 * 通过 课程获取所有的该课程 所有的题
 */

#sql("getQuestionBycourseId")
  select * from experiment where courseid = ? and flag = 1
#end

#sql("findChapterByCourse")
  SELECT * FROM chapter where courseid = ? and flag = 1 ORDER BY sort ASC
#end
/**
 * 通过 课程 和学生 id 获取到该学生做对了多少
 */
#sql("getQuestionSuccessByStudentIdAndCourseId")
  select * from studentexpertiment where studentid = ? and courseid = ? and status = 6 and flag = 1
#end
/**
 * 章
 */

/**
 * 节
 */

#sql("findSectionByCC")
  SELECT * FROM section where courseid = ?  AND chapterid = ? and flag = 1
#end



/**
 * 小节
 */
#sql("findPartByCCS")
  SELECT * FROM part where courseid = ?  AND chapterid = ? and sectionid = ? and flag = 1
#end





/**
 * 实验
 */
#sql("findExperimentByChapter")
   select * from question where id in (SELECT questionid FROM experiment where courseid = ? AND chapterid = ? and flag = 1)
#end

#sql("findExperimentBySectionid")
  select * from question where id in (SELECT questionid FROM experiment where courseid = ? AND chapterid = ? and sectionid = ? and flag = 1)
#end

#sql("findExperimentBypartid")
 select * from question where id in (SELECT questionid FROM experiment where courseid = ? AND chapterid = ? and sectionid = ? and partid = ? and flag = 1)
#end

/**
 * 获取实验状态
 */
#sql("getStatusByStudentIdAndQuestionId")
  SELECT * FROM studentexpertiment WHERE studentid = ? and experimentid = ? and flag = 1
#end





/**
 *  通过 questionID 获取到 该 question中 所属的章节小节信息
*/

#sql("updateStatusByQuestionidAndStudentId")
select * from studentexpertiment where studentid = ? and experimentid = ? and flag = 1
#end


/**
 * 实验记录
 */

#sql("getExperimentInfoByQuestionId")
select * from experiment where questionid = ? and flag = 1 
#end

