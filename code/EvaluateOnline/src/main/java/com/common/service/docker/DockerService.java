package com.common.service.docker;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.common.util.Docker;
import com.common.util.SocketUtil;
import com.common.util.StringUtil;
public class DockerService {
	/**
	 *  编译（功能）
	 * @param srcName   源文件名称
	 * @param code    源文件代码
	 * @param keywords   源代码的关键字
	 * @param split    	 源代码关键字的分隔符
	 * @param studentId   学生ID
	 * @param port      学生的port 属性
	 * @param ip        docker IP
	 * @param sendMessage    socket 发送的信息   
	 * @return
	 * @throws Exception
	 */
	public String compile(String srcName,String code,String keywords,String split,
		String studentId,Integer port,String ip,Integer times,String sendMessage) 
	throws Exception{
		//编译错误信息采集
		System.out.println(ip);
		
		sendMessage = StringUtil.removeAnnotation(sendMessage);
		code = StringUtil.removeAnnotation(code);
			
		StringBuffer errors = new StringBuffer();
		if ( !specification(srcName, code, keywords, split).trim().equals("")){
			String specificationErrors =  specification(srcName, code, keywords, split);
			errors.append(specificationErrors);
		}else {
			int port1 = port + 20000, port2 = port + 30000;
			Docker docker = Docker.Instance();
			Integer start_status =  docker.start(studentId);
			if(start_status == 404){
				docker.create(studentId, port1,port2);
				start_status = docker.start(studentId);
			}
			if(start_status  == 204){
				List<String> run = new ArrayList<>();
				run.add("/fire/run.sh");
				
				String runCmd = Docker.createExecStr(run);
				

				String runId = docker.createExec(studentId, runCmd);

				Integer run_status =  docker.startExec(runId);
				System.out.println("run_staus:"+run_status);
				
				Thread.sleep(200);
				//开始发送Socket消息
				boolean compiled = false;
				//int times=3;
				for(int i=0;i<times;i++){
					if(!compiled){
						System.out.println("服务器配置:"+ip+":"+port2);
						//System.out.println("发送的消息:"+sendMessage);
						
						
						String result =  SocketUtil.createSocketConnection(ip, port2,sendMessage);
						
						System.out.println("Socket返回的结果:"+result);
						if(result.startsWith("fail")){
							String[] items = result.split("\n");
							for (int n = 1; n < items.length; n++) {
								
								String s = items[n];
								if (s.contains("^")) {
								} else {
									errors.append( s );
								}
							}
							compiled = true;
						}else if(result.startsWith("ok")){
							compiled = true;
							errors.append("ok");
						}
						docker.stop(studentId);
						
						
						
					}
					
				}
				if (!compiled) {
					errors.append("服务器 连接失败");
					docker.stop(studentId);
				}
			}else if (start_status == 304) {
				docker.stop(studentId);
				errors.append("关闭此对话框,再试一次");
			} else {
				System.out.println("status："+start_status);
				errors.append("Docker容器 " + studentId + " 出现故障");
			}
		}
		System.out.println("Service返回的消息:"+errors.toString());
		return errors.toString();
		
	}

	
	
	
	
	
	
	/**
	 * 运行Docker容器
	* @Title: run  
	* @Description: TODO(这里用一句话描述这个方法的作用)  
	* @param @param studentId
	* @param @param dir    题号 的目录 
	* @param @return    设定文件  
	* @return String    返回类型  
	* @throws
	 */
	public String run(String studentId,String dir) {
		StringBuffer errors = new StringBuffer();
		try {
			Docker docker = Docker.Instance();
			int status = docker.start(studentId);
			if (status == 204) {
				List<String> run2 = new ArrayList<String>();
				run2.add("/fire/run2.sh");
				run2.add(dir);
				
				String run2Cmd =  Docker.createExecStr(run2);
				String run2Id = docker.createExec(studentId, run2Cmd);
				
				int statusCode = docker.startExec(run2Id);
				
				errors.append(statusCode == 200 ? "ok" : "fail");
				
			} else if (status == 304) {
				docker.stop(studentId);
				errors.append("关闭此对话框，再试一次。");
			} else {
				errors.append("Docker容器 " + studentId + " 出现故障");
			}
		} catch (Exception e) {
			System.err.print(e);
			errors.append(e);
		}
		return errors.toString();
	}
	
	/**
	 * 停止（功能）
	 * @param studentId
	 * @return
	 */
	public Integer stop(String studentId) {
		Integer stop_status =  Docker.Instance().stop(studentId);
		System.out.println("停止状态:"+stop_status);
		return stop_status;	
	}
	
	/**
	 * 评测代码  （功能）
	 * @param studentId
	 * @param ip
	 * @param port
	 * @param times
	 * @param sendMessage
	 * @return
	 */
	public String judge(String studentId,String ip,Integer port,Integer times,String sendMessage) {
		StringBuffer errors = new StringBuffer();
		try {
			
			
			int port1 = port + 30000; // 映射8181_Tester服务
			Docker docker = Docker.Instance();
			int status = docker.start(studentId);
			System.out.println("第一次启动状态码:"+status);
			if (status == 404) {
				errors.append("故障，请先编译。");
			} else if (status == 204) {
				
				List<String> run = new ArrayList<String>();
				run.add("/fire/run.sh");
				String runCmd = Docker.createExecStr(run);
				System.out.println("发送Cmd："+runCmd);
				String execId = docker.createExec(studentId,runCmd);
				Integer stau =  docker.startExec(execId);
				System.out.println("Cmd Status:\t"+stau);
				boolean connected = false;
				
				for (int i = 0; i < times;i++) {
					String line = SocketUtil.createSocketConnection(ip, port1, sendMessage);
					System.out.println("Socket返回的消息:"+line);										
					if(null != line) {
						if (line.startsWith("fail")) {// compile fail
							errors.append("测评失败！");
							errors.append("请确认你的代码已通过编译");
							errors.append("检查你的输出格式是否符合任务描述");
						} else {
							errors.append("ok");
						}
						connected = true;
						break;
					}				
				}
				docker.stop(studentId);
				if (!connected) {
					errors.append("Tester服务器 故障");
				}
			} else if (status == 304) {
				docker.stop(studentId);
				errors.append("关闭此对话框，再试一次.");
			} else {
				errors.append("Docker容器 " + studentId + " 出现故障");
			}
		} catch (Exception e) {
			System.err.print(e);
		}
		return errors.toString();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 对代码进行拼写检查
	* @Title: specification  
	* @Description: TODO(这里用一句话描述这个方法的作用)  
	* @param @param srcName
	* @param @param code
	* @param @param keywords
	* @param @param split
	* @param @return    设定文件  
	* @return String    返回类型  
	* @throws
	 */
	//	split ="\\s|,"
	
	public String specification(String srcName,String code,String keywords,String split){
		StringBuffer errors = new StringBuffer();
		if(isNull(srcName) || !extension(srcName, "cpp")){			
			errors.append("源文件扩展名必须是 .cpp");
			
		}
		if(isNull(code)){			
			errors.append("源文件不能为空");
			
		}else if(isNotNull(keywords)){
			String[] words = keywords.split(split);
			for (String w : words) {
				    //检测要匹配的单词，前后需要加空格或符号，若为另起一行则不需要.
				    Pattern pattern = Pattern.compile("[^\\w]"+makeQueryStringAllRegExp(w.trim())+"[^\\w]");
				    Matcher matcher = pattern.matcher(code);
				if (!matcher.find()) {
					errors.append("源代码必须包含:" + w);
					
				}
			}
		}
		return errors.toString();
	}
	
	 public static String makeQueryStringAllRegExp(String str) {
	        if(str == null || str.trim().equals("")){
	            return str;
	        }

	        return str.replace("\\", "\\\\").replace("*", "\\*")
	                .replace("+", "\\+").replace("|", "\\|")
	                .replace("{", "\\{").replace("}", "\\}")
	                .replace("(", "\\(").replace(")", "\\)")
	                .replace("^", "\\^").replace("$", "\\$")
	                .replace("[", "\\[").replace("]", "\\]")
	                .replace("?", "\\?").replace(",", "\\,")
	                .replace(".", "\\.").replace("&", "\\&");
	    }
	
	public static boolean  isNotNull(String str){
		return  str!= null && !str.trim().equals("");
	}
	public static boolean isNull(String str){
		return str == null || str.trim().equals("");
	}
	public static boolean extension(String FileName,String end){
		if(FileName.endsWith("."+end)){
			String name =   FileName.substring(0, FileName.indexOf("."+end));
			
			if(isNotNull(name)){
				return true;
			}		
		}
		return false;
	} 

	

}
