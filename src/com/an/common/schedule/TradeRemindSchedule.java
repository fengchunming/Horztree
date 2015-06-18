package com.an.common.schedule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TradeRemindSchedule {
	
	

	public static void main(String[] args) {
		Runnable runnable = new Runnable(){
			@Override
			public void run() {
				System.out.println(System.currentTimeMillis()/1000);
			}
		};
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		//service.scheduleAtFixedRate(runnable, 10, 1, TimeUnit.SECONDS);
		service.scheduleAtFixedRate(runnable, 5, 10, TimeUnit.MINUTES);
	}

}
