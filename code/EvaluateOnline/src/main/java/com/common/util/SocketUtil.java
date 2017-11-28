package com.common.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class SocketUtil {

	public static String createSocketConnection(String ip, Integer port, String sendMessage) throws InterruptedException {
		String line = new String();
		
		//StringBuffer result = new StringBuffer();
		try {
			
			Socket socket = new Socket(ip, port);
			socket.setSoTimeout(10000);
			
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			//ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
			//outputStream.writeObject(sendMessage);
			//outputStream.flush();
			dos.writeUTF(sendMessage);
			//ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			//Object object =  inputStream.readObject();
			//inputStream.close();
			
			DataInputStream dis = new DataInputStream(socket.getInputStream());

			line = dis.readUTF();
			
			/*if(line.startsWith("fail")){
				String[] items = line.split("\n");
				for (int n = 1; n < items.length; n++) {
					String s = items[n];
					if (s.contains("^")) {
					} else {
						result.append("<li>" + s + "</li>");
					}
				}
			}else if(line.startsWith("ok")){
				//compiled = true;
				result.append("ok");
			}*/
			dos.close();
			dis.close();
			socket.close();
			return line;
			//System.out.println(result.toString());
			
			//return result.toString();
		} catch (Exception e) {
			Thread.sleep(200);
			e.printStackTrace();
		}
		return null; //result.toString();
	}

}
