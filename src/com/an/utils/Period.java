package com.an.utils;

import java.sql.Timestamp;

public class Period {
	public static String[] getsplitPeriod(String reportPeriod,int frequent){
		String[] period=new String[3];
		
		if(frequent==1){
			period[0]=reportPeriod.split("年")[0];
			period[1]="";
			period[2]="";
		}else if(frequent==2){
			period[0]=reportPeriod.split("年")[0];
			period[1]=reportPeriod.split("年")[1].split("季度")[0];
			period[2]="";
		}else{
			period[0]=reportPeriod.split("年")[0];
			period[2]=reportPeriod.split("年")[1].split("月")[0];
			if(Integer.parseInt(period[2])%3==0){
				period[1]=String.valueOf(Integer.parseInt(period[2])/3);
			}else{
				period[1]=String.valueOf(Integer.parseInt(period[2])/3+1);
			}
			
		}
		return period;
	}

	/**
	 * 获取系统时间 取年 月 日 时 分 秒 eg：20150411163502
	 */
	public static String getSystemTime() {
		
		String str = new Timestamp(System.currentTimeMillis()).toString()
				.substring(0, 19);
		str = str.substring(0, 4) + str.substring(5, 7) + str.substring(8, 10)
				+ str.substring(11, 13) + str.substring(14, 16)
				+ str.substring(17, 19);
		return str;
	}
}
