#sql("select")
  SELECT * FROM
#end

-- 管理员数据库管理
#namespace("admin")
  #include("sql/admin.sql")
#end

-- 教师数据库管理
#namespace("teacher")
  #include("sql/teacher.sql")
#end

-- 学生数据库管理
#namespace("student")
  #include("sql/student.sql")
#end