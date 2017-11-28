package com.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Tester {
	
	public static void main(String[] args) throws FileNotFoundException {
		ServerSocket serverSocket = null;

		try {
			System.err.println("开始创建Socket服务器.......");

			serverSocket = new ServerSocket(8181);
			Socket socket = serverSocket.accept();
			
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

			String msg = dis.readUTF();

			String[] fields = msg.split("&_&_201512_#_#");
			String type = fields[0];
			StringBuffer result = new StringBuffer();
			String path = "/usr/src/" + fields[1] + "/";

			if ("compile".equals(type)) {

				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
					System.err.println("服务器不存在此文件夹 。。。。。创建。。。。。");

				}

				if (fields[2] != null && !fields[2].trim().equals("")) {
					FileWriter headerWriter = new FileWriter(path + fields[2]);
					headerWriter.write(fields[3]);
					headerWriter.close();
				}
				String srcName = fields[4];

				FileWriter srcWriter = new FileWriter(path + srcName);
				srcWriter.write(fields[5]);
				srcWriter.close();

				ProcessBuilder pb = new ProcessBuilder("g++", "-w", "-c", srcName);
				// -w 关闭所有警告
				// -c 只激活预处理,编译,和汇编,也就是他只把程序做成obj文件
				pb.directory(dir);
				pb.redirectErrorStream(true);
				Process compile = pb.start();
				BufferedReader br = new BufferedReader(new InputStreamReader(compile.getInputStream()));
				String tip = br.readLine();
				if (tip == null) {
					System.err.println("好了......");
					result.append("ok\r\n");
				} else {
					result.append("fail\r\n");
					System.err.println("失败.......");

					result.append(tip);
					while ((tip = br.readLine()) != null) {
						result.append(tip + "\r\n");
					}
				}
				br.close();
			} else if ("judge".equals(type)) {
				// judge + dir + judgeType + 
				System.err.println("评测功能........");

				File dir = new File(path);
				List<String> arguments = new ArrayList<String>();
				String[] names = dir.list();
				boolean hasCpp = false;
				if (names != null) {
					arguments.add("g++");
					arguments.add("-w");
					for (String nm : names) {
						if (nm.endsWith(".cpp")) {
							arguments.add(nm);
							hasCpp = true;
						}
					}
				}

				if (hasCpp) {
					ProcessBuilder pb = new ProcessBuilder(arguments);
					pb.directory(dir);
					pb.redirectErrorStream(true);
					Process judge = pb.start();
					BufferedReader br = new BufferedReader(new InputStreamReader(judge.getInputStream()));
					String tip = br.readLine();
					if (tip == null) {
						//String judgeType = fields[4];
						String judgeType = null;
						if( fields[3] == null || fields[3].trim().equals("") ){
							
							
							judgeType = "0";
							
						}else{
							judgeType = "1";
						}
						/*               0                           1                                    2     
						 * String x = "judge" + delimiter + submitBean.getDir() + delimiter + problem.getInput() + "\n"
								+ delimiter + 3 problem.getOutput() + delimiter + 4 problem.getType()
						 * */
						
						//judge &_&_201512_#_# 151 &_&_201512_#_# &_&_201512_#_# This is a c program. &_&_201512_#_# 
						// System.err.println(judgeType);
						
						
						if ("0".equals(judgeType)) {// 仅做符号和语法检查
							System.err.println("仅做符号和语法检查.....");
							result.append("ok\n");
						}
						
						
						else if ("1".equals(judgeType)) {// 根据屏幕输出测评
							System.err.println("根据屏幕输出测评。。。。。");

							// 此处可以传递一个List<String>将所有要加的参数都分隔开添加进入
							ProcessBuilder pb2 = new ProcessBuilder("./a.out");
							pb2.directory(dir);
							pb2.redirectErrorStream(true);
							Process run = pb2.start();
							if (fields[2] != null && !fields[2].trim().equals("")) {
								BufferedWriter input = new BufferedWriter(
										new OutputStreamWriter(run.getOutputStream()));
								input.write(fields[2]);
								input.flush();
							}
							//输入
							BufferedReader output = new BufferedReader(new InputStreamReader(run.getInputStream()));
							StringBuffer answer = new StringBuffer();


						
							
							String line = null;
						
							
							while ((line = output.readLine()) != null) {
								answer.append(line.replace("\n", "")+ "\n");
							}
							
							if (fields[3].trim().equals(answer.toString().trim())) {
								result.append("ok\n");
							} else {
								result.append("fail\n");
								result.append("未实现题目要求的功能!\n");
								// result.append(answer.toString());
							}
							
							
							
							
							
							
						} /*else if ("2".equals(judgeType)) {// 根据测试文件及程序对学生代码做模块测试
							result.append("fail\n");
							result.append("模块测试\n");
						}*/ else {
							result.append("不支持此评测类型：" + judgeType + "\n");
						}
					} else {
						result.append("fail\n");
						result.append(tip);
						while ((tip = br.readLine()) != null) {
							result.append(tip + "\n");
						}
						br.close();
					}
				} else {
					result.append("fail\n");
					result.append("请先点击 '编译' 按钮！\n");
				}
			} else {
				result.append("fail\n");
				result.append("不支持此类型请求：" + type + "\n");
			}
			dos.writeUTF(result.toString());
			dos.flush();
			serverSocket.close();
		} catch (Exception e) {
			System.err.print(e);
		}

	}

}
