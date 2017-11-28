package com.common.util;


import java.io.IOException;
import java.net.URI;

import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {

	public static String doRequestWithResult(String url,String method, Map<String, String> param) {

		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			// 创建uri
			URIBuilder builder = new URIBuilder(url);
			if (param != null) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();

			HttpUriRequest request =null;
			// 创建http GET请求
			switch (method) {
			case "POST":
				request = new HttpPost(uri);
				break;
			case "PUT":
				request = new HttpPut(uri);
				break;
			case "DELETE":
				request = new HttpDelete(uri);
				break;
			case "GET":
				request = new HttpGet(uri);
				break;

			default:
				request = new HttpGet(uri);
				break;
			}

			CloseableHttpResponse response = httpclient.execute(request);
			if(response.getStatusLine().getStatusCode() == 200){
				
				return EntityUtils.toString(response.getEntity(), "UTF-8");
			}
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public static Integer doRequestWithStatus(String url,String method, Map<String, String> param) {

		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();
		Integer status = 0;
		CloseableHttpResponse response = null;
		try {
			// 创建uri
			URIBuilder builder = new URIBuilder(url);
			if (param != null) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();

			HttpUriRequest request =null;
			// 创建http GET请求
			switch (method) {
			case "POST":
				request = new HttpPost(uri);
				break;
			case "PUT":
				request = new HttpPut(uri);
				break;
			case "DELETE":
				request = new HttpDelete(uri);
				break;
			case "GET":
				request = new HttpGet(uri);
				break;

			default:
				request = new HttpGet(uri);
				break;
			}

			response = httpclient.execute(request);
			
			
			status  = response.getStatusLine().getStatusCode();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return status;
	}
	
	public static String doJSONWithResult(String url, String json) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容
			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultString;
	}
	public static Integer doJSONWithStatus(String url, String json) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		Integer status = 0;

		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容
			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
			// 执行http请求
			response = httpClient.execute(httpPost);
			status =  response.getStatusLine().getStatusCode();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return status;

	}
}
