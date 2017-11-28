package com.common.util;

import java.util.List;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

public class Docker {
	
	public static String host_ip = "";//"192.168.10.8";
	public static String host_port = "";//"4243";
	public static String Image = "";//"oct:18d";
	

	private static String origin =  "";//"http://" + host_ip + ":" + host_port;

	private Docker() {
	}

	private static Docker docker;

	public static Docker Instance() {
		Prop prop =  PropKit.use("docker.properties");
		host_ip   =  prop.get("DOCKER_HOST_IP");
		Image     =  prop.get("DOCKER_IMAGE_TAG");
		host_port =  prop.get("DOCKER_HOST_PORT");
		origin =  "http://" + host_ip + ":" + host_port;
		//System.out.println("HOST_IP:\t"+host_ip+":\t"+Image + "\t:"+host_port);
		
		
		if (docker == null) {
			docker = new Docker();
		}
		return docker;

	}

	/**
	 * 
	 * @Title: create @Description: TODO(创建Docker 并初始化配置 ) @param @param
	 * nm @param @param json @param @return 设定文件 @return Integer 201 Container
	 * created successfully | 400 bad parameter | 404 no such container | 406
	 * impossible to attach | 409 conflict | 500 server error @throws
	 */
	public Integer create(String name, Integer tty_port, Integer socket_port) {
		 //"http://192.168.56.149:4243/containers/create?name=XXX"
		String url = origin + "/containers/create?name=" + name;
		return HttpClientUtil.doJSONWithStatus(url, getJSON(Image, tty_port, socket_port));

	}

	/**
	 * 
	 * @Title: start @Description: TODO() @param @param Docker
	 * Name @param @return @return Integer 204 no error | 304 container already
	 * started | 404 no such container | 500 server error @throws
	 */
	public Integer start(String name) {
		String url = origin + "/containers/" + name + "/start";
		// return HttpClientUtil.doJSONWithResult(url,null);
		return HttpClientUtil.doRequestWithStatus(url, "POST", null);

	}

	/**
	 * 
	 * @Title: stop @Description: TODO(这里用一句话描述这个方法的作用) @param @param
	 *         id @param @return 设定文件 @return Integer 204 no error | 304
	 *         container already stopped | 404 no such container | 500 server
	 *         error @throws
	 */
	public Integer stop(String name) {
		String url = origin + "/containers/" + name + "/stop";
		return HttpClientUtil.doRequestWithStatus(url, "POST", null);

	}

	public String getJSON(String Image, Integer tty_port, Integer socket_port) {
		String json =	"{  \n" +
						"	\"Hostname\": \"\",  \n" +
						"	\"Domainname\": \"\",  \n" +
						"	\"User\": \"\",  \n" +
						"	\"AttachStdin\": true,  \n" +
						"	\"AttachStdout\": true,  \n" +
						"	\"AttachStderr\": true,  \n" +
						"	\"Tty\": true,  \n" +
						"	\"OpenStdin\": true,  \n" +
						"	\"StdinOnce\": false,  \n" +
						"	\"Env\": [    \n" +
						"		\"LANG=zh_CN.UTF-8\",    \n" +
						"		\"BAZ=quux\"  \n" +
						"		],  \n" +
						"	\"Cmd\": [    \n" +
						"		\"/bin/bash\"  \n" +
						"		],  \n" +
						"	\"Entrypoint\": \"\",  \n" +
						"	\"Image\": \"IMAGE_TAG_REPLACE\",  \n" +
						"	\"Labels\": {    \n" +
						"		\"com.example.vendor\": \"Acme\",    \n" +
						"		\"com.example.license\": \"GPL\",    \n" +
						"		\"com.example.version\": \"1.0\"  \n" +
						"		},  \n" +
						"	\"Volumes\": {	\n" +
						"		\"/fire\": { }  \n" +
						"		},  \n" +
						"	\"WorkingDir\": \"\",  \n" +
						"	\"NetworkDisabled\": false,  \n" +
						"	\"ExposedPorts\": {    \n" +
						"		\"8181/tcp\": {},    \n" +
						"		\"8080/tcp\": {}  \n" +
						"		},  \n" +
						"	\"StopSignal\": \"SIGTERM\",  \n" +
						"	\"StopTimeout\": 10,  \n" +
						"	\"HostConfig\": {    \n" +
						"		\"Binds\": [\"/usr/auto:/fire:ro\"],    \n" +
						"		\"PortBindings\": {      \n" +
						"			\"8181/tcp\": [        \n" +
						"				{          \n" +
						"					\"HostPort\": \"SOCKET_PORT_REPLACE\"        \n" +
						"				}      \n" +
						"			],      \n" +
						"			\"8080/tcp\": [        \n" +
						"				{          \n" +
						"					\"HostPort\": \"TTY_PORT_REPLACE\"       \n" +
						"				}      \n" +
						"			]    \n" +
						"		},    \n" +
						"		\"PublishAllPorts\": false,    \n" +
						"		\"Privileged\": false,    \n" +
						"		\"ReadonlyRootfs\": false,    \n" +
						"		\"CapAdd\": [      \n" +
						"			\"NET_ADMIN\"    \n" +
						"			],    \n" +
						"		\"CapDrop\": [      \n" +
						"			\"MKNOD\"  \n" +
						"		],    \n" +
						"		\"NetworkMode\": \"bridge\",  \n" +
						"		\"Devices\": [],    \n" +
						"		\"LogConfig\": {      \n" +
						"		\"Type\": \n" +
						"		\"json-file\",      \n" +
						"		\"Config\": {}    \n" +
						"		}  \n" +
						"	}\n" +
						"}";
		json = json.replace("IMAGE_TAG_REPLACE", Image).replace("TTY_PORT_REPLACE", tty_port.toString())
				.replace("SOCKET_PORT_REPLACE", socket_port.toString());
		return json;
	}
	public Integer remove(String name) {
		String url = origin + "/containers/" + name + "?force=true";
		Integer status = HttpClientUtil.doRequestWithStatus(url, "DELETE", null);
		return status;
	}

	/**
	 * 
	 * @param @param name
	 * @param @param cmd
	 * @param @return
	 * @param @throws Exception 
	 * @return String
	 */
	public String createExec(String name, String cmd) throws Exception {
		String id = null;
		String url = origin + "/containers/" + name + "/exec";

		String result = HttpClientUtil.doJSONWithResult(url, cmd);

		if (JSONUtil.toMap(result).get("Id") != null) {
			id = (String) JSONUtil.toMap(result).get("Id");
		}
		return id;
	}

	/**
	 * 对Docker创建执行命令的Cmd进行封装,从List拼成一个Cmd并返回
	 * 
	 * @param cmd
	 *            要执行命令的List
	 * @return  可以直接向Docker发送的封装过的字符串 
	 * {"Cmd":"/bin/bash"}
	 */
	public static String createExecStr(List<String> cmd) {
		String start = "{  \"Cmd\": [";
		String end = "]}";
		String spr = "\"";
		StringBuffer sb = new StringBuffer(start);

		for (int i = 0; i < cmd.size(); i++) {

			sb.append(spr + cmd.get(i) + spr + ",");

		}
		String temp = sb.toString();
		temp = temp.substring(0, temp.length() - 1);
		return temp + end;

	}

	/** 
	 * @return int 200 No error | 404 No such exec instance | 409 Container is stopped or paused 
	 * @throws
	 */
	public int startExec(String ExecId) {
		String url = origin + "/exec/" + ExecId + "/start";
		String exec = "{  \"Detach\": false,  \"Tty\": false}";
		return HttpClientUtil.doJSONWithStatus(url, exec);
	}

	

}