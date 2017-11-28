package com.common.util;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileRW {
	public static String readfile(String filepath){
    	String chstr="";
        try{
            FileReader fr = new FileReader(filepath);
            int ch = 0;	            
            while((ch=fr.read())!=-1){//fr.read()读取一个字节，判断此字节的值为-1表示读到文件末尾了。
                chstr = String.valueOf((char)ch);	            
            }
            System.out.println("status.properties文件:"+chstr);
        }catch(IOException e){
            e.printStackTrace();
        }
		return chstr;
    }
	
	public static void writestart(String path){        
        try {
            FileWriter fw = new FileWriter(path);
            fw.write("1");            
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("写入status.properties中1");
    }
    
    public static void writestop(String path){        
        try {
            FileWriter fw = new FileWriter(path);
            fw.write("0");            
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("写入status.properties中0");
    }
}
