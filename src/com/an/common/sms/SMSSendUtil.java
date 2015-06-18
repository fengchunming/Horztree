package com.an.common.sms;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class SMSSendUtil {
	private String url = "http://yunpian.com/v1/sms/send.json";
	private String apiKey = "4d32bc45998b2e66aecfa8e5fb880006";//用户唯一标示
	private String mobile = "";//发送的手机号，多个以逗号分隔，最多100个
	private String text = "";//短信内容
	
	
	private String returnMsg = ""; 

	/*
{
	"code":0,
	"msg":"OK",
	"result":{
		"count":1,
		"fee":1,
		"sid":1097
	}
}
	 */
	
	public static boolean sendSms(String text, String mobile) {
		

		
		return true;
	}
	
	public static String readContentFromPost(String url, Map<String, String> params) throws IOException {
		String result = "";
		// Post请求的url，与get不同的是不需要带参数
		URL postUrl = new URL(url);
		// 打开连接
		HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
		// 打开读写属性，默认均为false
		connection.setDoOutput(true);
		connection.setDoInput(true);
		// 设置请求方式，默认为GET
		connection.setRequestMethod("POST");
		// Post 请求不能使用缓存
		connection.setUseCaches(false);
		// URLConnection.setFollowRedirects是static 函数，作用于所有的URLConnection对象。
		// connection.setFollowRedirects(true);
		// URLConnection.setInstanceFollowRedirects 是成员函数，仅作用于当前函数
		connection.setInstanceFollowRedirects(true);
		// 配置连接的Content-type，配置为application/x-
		// www-form-urlencoded的意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode进行编码
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		connection.setRequestProperty("Accept", "application/json;charset=utf-8");

		// 连接，从postUrl.openConnection()至此的配置必须要在 connect之前完成，
		// 要注意的是connection.getOutputStream()会隐含的进行调用 connect()，所以这里可以省略
		// connection.connect();
		DataOutputStream out = new DataOutputStream(connection.getOutputStream());
		// 正文内容其实跟get的URL中'?'后的参数字符串一致
		String content = "firstname=" + URLEncoder.encode("一个大肥人", "utf-8");
		// DataOutputStream.writeBytes将字符串中的16位的 unicode字符以8位的字符形式写道流里面
		out.writeBytes(content);
		out.flush();
		out.close(); // flush and close
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line = "";
		while ((line = reader.readLine()) != null) {
			result += line; 
		}
		reader.close();
		connection.disconnect();
		return result;
	}
}
