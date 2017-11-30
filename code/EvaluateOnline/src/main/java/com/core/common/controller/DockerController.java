package com.core.common.controller;



import com.common.bean.StudentInputToDocker;
import com.common.service.docker.DockerService;
import com.common.service.docker.InputDataTo;
import com.common.util.Docker;
import com.core.common.model.Question;
import com.core.common.model.Student;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;

public class DockerController extends Controller{
	
	private final String delimiter = "&_&_201512_#_#";
	private final String sp ="\\$";
	private InputDataTo inputDataTo = new InputDataTo();
	
	public void index(){
		render("stu-write.html");
	}
	/**
	 * 编译 （docker 编译请求）
	 * @param dir    题号
	 * @param headername   头文件名称
	 * @param header    头文件代码
	 * @param srcname    源文件名称
	 * @param src        源文件代码
	 * @param words     关键字
	 * @param sid       学生ID
	 * @param host_ip   docker IP
	 * @return
	 */
	
	private String host_ip =PropKit.use("docker.properties").get("DOCKER_HOST_IP");
	
	public void compileToDocker(){
		
		StudentInputToDocker sInputToDocker = getBean(StudentInputToDocker.class,"");
		String words = getSessionAttr("keywords");
		DockerService service = new DockerService();
		String sid = ((Student)getSessionAttr("student")).getNumber();
		String headerName = sInputToDocker.getHeadername();
		Long port = ((Student)getSessionAttr("student")).getId();
		
		
		System.out.println(words+"======="+sid);
		
		
		if( headerName!=null && !headerName.trim().equals("")){
			sInputToDocker.setHeadername(sInputToDocker.getHeadername()+ ".h");
		}else{
			sInputToDocker.setHeadername(" ");
		}
		
		String srcName = sInputToDocker.getSrcname();
		if( srcName!=null && !srcName.trim().equals("")){
			sInputToDocker.setSrcname(sInputToDocker.getSrcname()+ ".cpp");
		}else{
			sInputToDocker.setSrcname(" ");
		}
		
		
		String x ="compile" + delimiter + sInputToDocker.getDir() + delimiter
		+ sInputToDocker.getHeadername() + delimiter + sInputToDocker.getHeader() + delimiter
			+ sInputToDocker.getSrcname() + delimiter + sInputToDocker.getSrc(); 
		
		System.out.println("发送的消息:"+x);
		
		String errors = null;
		try {
			errors =  service.compile(sInputToDocker.getSrcname(), sInputToDocker.getSrc(), words, sp, sid, port.intValue(),host_ip,3, x);
		} catch (Exception e) {
			
			e.printStackTrace();
			new RuntimeException("获取信息出错了");
		}
		
		System.out.println("获取到日志信息："+ errors);
		if(errors.startsWith("ok")){
			if(inputDataTo.isHasStudentexpertiment(port, Long.parseLong(sInputToDocker.getDir()))==1){
				inputDataTo.setDataToStudentexpertiment(port, Long.parseLong(sInputToDocker.getDir()) , sInputToDocker.getHeadername(), sInputToDocker.getHeader(), sInputToDocker.getSrcname(), sInputToDocker.getSrc(), 2);
			}else{
				inputDataTo.updateToStudentexpertiment(port, Long.parseLong(sInputToDocker.getDir()), 2);
			}
			setAttr("result", "编译成功");
			renderJson();
		}else{
			/*System.out.println(port);
			System.out.println(Long.parseLong(sInputToDocker.getDir()));
			System.out.println(sInputToDocker.getHeader());
			System.out.println(sInputToDocker.getHeadername());
			System.out.println(sInputToDocker.getSrcname());
			System.out.println(sInputToDocker.getSrc());
			*/
			if(inputDataTo.isHasStudentexpertiment(port, Long.parseLong(sInputToDocker.getDir()))==1){
				inputDataTo.setDataToStudentexpertiment(port, Long.parseLong(sInputToDocker.getDir()) , sInputToDocker.getHeadername(), sInputToDocker.getHeader(), sInputToDocker.getSrcname(), sInputToDocker.getSrc(), 1);
			}else{
				inputDataTo.updateToStudentexpertiment(port, Long.parseLong(sInputToDocker.getDir()), 1);
			}
			setAttr("result", errors);
			renderJson();
		}
	}

	
	/**
	 * 运行 docker 控制
	 * @param sid  学生ID
	 * @param dir  题ID
	 */
	
	public void runToDocker(){
		//getPara("sid");
		String dir = getPara("dir");
		
		System.out.println("Dir:"+dir);
		String sid = ((Student)getSessionAttr("student")).getNumber();
		long studentid =  ((Student)getSessionAttr("student")).getId();
		DockerService service = new DockerService();
		String error =  service.run(sid, dir);
		System.out.println("Service 返回的消息:"+error);
		
		if(error.startsWith("ok")){
			setAttr("result", "success");
			inputDataTo.updateToStudentexpertiment(studentid, Long.parseLong(dir), 4);
			renderJson();
		}else{
			setAttr("result", error);
			inputDataTo.updateToStudentexpertiment(studentid, Long.parseLong(dir), 3);
			service.stop(sid);
			renderJson();
		}
	}
	
	/**
	 * 关闭docker 请求
	 * @param sid  学生ID
	 */
	//@ActionKey("/stopToDocker")
	public void stopToDocker(){
		String sid = ((Student)getSessionAttr("student")).getNumber();
		System.out.println("关闭DOcker...."+sid);
		//getPara("sid");
		DockerService service = new DockerService();
		
		Integer stop =service.stop(sid);
		if(stop == 204){
			setAttr("result","停止成功");
			renderJson();
		}else{
			setAttr("result","fail_Stop");
			renderJson();
		}
		
	}
	/**
	 * 评测（docker 评测请求）
	 * @param dir    题号
	 * @param headername   头文件名称
	 * @param header    头文件代码
	 * @param srcname    源文件名称
	 * @param src        源文件代码
	 * @param words     关键字
	 * @param sid       学生ID
	 * @param host_ip   docker IP
	 * @return
	 */
	
	//@ActionKey("/judgeToDocker")
	public void judgeToDocker(){
		String sid = ((Student)getSessionAttr("student")).getNumber();
		long studentid =  ((Student)getSessionAttr("student")).getId();
		// 获取参数
		String dir = getPara("dir");
		
		
		DockerService service = new DockerService();
		
		Question question = Question.dao.findById(Integer.parseInt(dir));
		//System.out.println("======"+question.getInput()+question.getOutput());
		
		String x = "judge" + delimiter +dir+ delimiter;
		
		if( null !=question.getInput() && !question.getInput().trim().equals("")){
			x+=question.getInput();
		}else{
			x+=" ";
		}
		x += "\n"+ delimiter;
		if( null !=question.getOutput() && !question.getOutput().trim().equals("")){
			x+=question.getOutput();
		}else{
			x+=" ";
		}
		
		
		Long port = ((Student)getSessionAttr("student")).getId();
		String error =  service.judge(sid, host_ip, port.intValue(), 3, x);
		System.out.println("评测"+error);
		
		if(error.startsWith("ok")){
			setAttr("result", "评测成功");
			inputDataTo.updateToStudentexpertiment(studentid, Long.parseLong(dir), 6);
			
			service.stop(sid);
			renderJson();
		}else{
			setAttr("result", error);
			inputDataTo.updateToStudentexpertiment(studentid, Long.parseLong(dir), 5);
			service.stop(sid);
			renderJson();
		}
	}
}

