/**
 * 教师操作
 */
#sql("findTeacherByNumber")
  SELECT * FROM teacher where number = ? and flag = 1
#end




/**
 * 学生管理
 */
#sql("findStudentByClazzid")
  SELECT * FROM student where classid = ? and flag = 1
#end



/**
 * 班级管理
 */
#sql("findClazzByTeacher")
  SELECT * FROM teachercourseclazz where teachernumber = ? and flag = 1
#end

#sql("findClazzByNumber")
  SELECT * FROM clazz where number = ? and flag = 1
#end




/**
 * 课程管理
 */
#sql("findCourseByTeacher")
  SELECT * FROM teachercourseclazz where teachernumber = ? and flag = 1
#end

#sql("findCourseByNumber")
  SELECT * FROM course where number = ? and flag = 1
#end



/**
 * 章
 */
#sql("findChapterByCourse")
  SELECT * FROM chapter where courseid = ? and flag = 1 ORDER BY sort ASC
#end

-- 查询课程第一章
#sql("findFirstChapterIdByCourse")
  SELECT * FROM chapter where courseid = ?  AND sort = 1 and flag = 1
#end






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
  SELECT * FROM experiment where courseid = ? AND chapterid = ? and flag = 1
#end

#sql("findExperimentBySectionid")
  SELECT * FROM experiment where courseid = ? AND chapterid = ? and sectionid = ? and flag = 1
#end

#sql("findExperimentBypartid")
  SELECT * FROM experiment where courseid = ? AND chapterid = ? and sectionid = ? and partid = ? and flag = 1
#end

#sql("findExperimentByCourseidAndChapterid")
  SELECT * FROM experiment where courseid = ? AND chapterid = ? and flag = 1
#end