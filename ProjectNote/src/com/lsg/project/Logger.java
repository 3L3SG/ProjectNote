package com.lsg.project;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Logger {
	
	private static Logger log=null;
	public static final boolean LoggerStatus=true;
	
	private Logger() {
		
	}
	
	public static Logger getInstance() {
		if (log==null)
			return log=new Logger();
		return log;
	} 
	
	public void log(String msg) {
		if (LoggerStatus) {
			System.out.println(msg);
		}
		new Thread() {
			public void run() {
		BufferedWriter bw=null;
		try {
			bw=new BufferedWriter(new FileWriter("log.txt"));
			Date date=new Date();
			bw.write(Thread.currentThread().getName()+" : "+date.getTime()+" : "+msg);
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (bw!=null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
			
			}
	}.start();
 }
}
