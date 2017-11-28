package com.common.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class StringUtil {
	public static String  removeAnnotation(String target){
		String result = target.replaceAll("\\/\\/[^\\n]*|\\/\\*([^\\*^\\/]*|[\\*^\\/*]*|[^\\**\\/]*)*\\*+\\/", "");
		return result;
	}
	public static String ReadFileToString(String FilePath) {
		FileInputStream fis = null;
		BufferedReader br = null;
		try {
			fis = new FileInputStream(FilePath);
			br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 构建成String
		StringBuffer sb = new StringBuffer();
		String temp = null;
		try {
			while ((temp = br.readLine()) != null) {
				sb.append(temp + '\n');
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}
