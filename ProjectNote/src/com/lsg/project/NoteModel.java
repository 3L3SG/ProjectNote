package com.lsg.project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class NoteModel {
	//NoteBean note;
	SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
//----------------------------------------------

	public String addNote(NoteBean note,String catName) {
		BufferedWriter bw=null;
		try {
			bw=new BufferedWriter(new FileWriter(catName+".notes",true));
			String creationDate=sdf.format(note.getCreationDate());
			if (note.getType().equals(NoteType.TASK)) {
				String planningDate=sdf.format(note.getPlannedDate());
				bw.write(note.getTitle()+":"+note.getDescription()+":"+note.getTags()+":"+creationDate+":"+note.getType()+":"+planningDate+":"+note.getStatus()+":"+note.getAttachment());
			}
			else {
				bw.write(note.getTitle()+":"+note.getDescription()+":"+note.getTags()+":"+creationDate+":"+note.getType()+":"+note.getAttachment());
			}
			bw.newLine();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
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
		return "SUCCESS";
	}
//----------------------------------------------

	public List<NoteBean> listNotes(String catName){
		List <NoteBean> al=new ArrayList<NoteBean>();
		NoteBean bean=null;
		BufferedReader br=null;
		String line;
		try {
			br=new BufferedReader(new FileReader(catName+".notes"));
			while ((line=br.readLine())!=null) {
				String[] string=line.split(":");
				if (string.length==8) {
					bean=new NoteBean();
					bean.setTitle(string[0]);
					bean.setDescription(string[1]);
					bean.setTags(string[2]);
					Date cdate=sdf.parse(string[3]);
					bean.setCreationDate(cdate);
					bean.setType(NoteType.TASK);//string[4];
					Date pdate=sdf.parse(string[5]);
					bean.setPlannedDate(pdate);
					if (string[6].equals("NEW")) {
						bean.setStatus(NoteStatus.NEW);
					}
					if (string[6].equals("PENDING")) {
						bean.setStatus(NoteStatus.PENDING);
					}
					if (string[6].equals("COMPLETED")) {
						bean.setStatus(NoteStatus.COMPLETED);
					}
					bean.setAttachment(string[7]);
					al.add(bean);
				}
				if (string.length==6) {
					bean=new NoteBean();
					bean.setTitle(string[0]);
					bean.setDescription(string[1]);
					bean.setTags(string[2]);
					Date cdate=sdf.parse(string[3]);
					bean.setCreationDate(cdate);
					bean.setType(NoteType.SELF);//string[4];
					bean.setAttachment(string[5]);
					al.add(bean);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return al;
	}

//----------------------------------------------

	public boolean isCatExist(String catName) {
		return new File(catName+".notes").exists();
	}

//----------------------------------------------

	public boolean isTitleExist(String title, String catName) {
		BufferedReader br=null;
		String line;
		try {
			if (isCatExist(catName)) {
				br=new BufferedReader(new FileReader(catName+".notes"));
				while ((line=br.readLine())!=null) {
					String[] str=line.split(":");
					if (str[0].equals(title)) {
						return true;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
//----------------------------------------------

	public List<NoteBean> searchNote(String title, String catName) {
		NoteBean bean;
		List<NoteBean> beanList=new ArrayList<NoteBean>();
		BufferedReader br=null;
		String line="";
		try {
			br=new BufferedReader(new FileReader(catName+".notes"));
			//Date cdate,pdate;
			while ((line=br.readLine())!=null) {
				String[] string=line.split(":");
				if (string.length==8) {
					if (string[0].contains(title) || string[1].contains(title) || string[2].contains(title) 
							|| string[4].contains(title) || string[6].contains(title) || string[7].contains(title) ) {
						bean=new NoteBean();
						bean.setTitle(string[0]);
						bean.setDescription(string[1]);
						bean.setTags(string[2]);
						Date cdate=sdf.parse(string[3]);
						bean.setCreationDate(cdate);
						bean.setType(NoteType.TASK);//string[4];
						Date pdate=sdf.parse(string[5]);
						bean.setPlannedDate(pdate);
						if (string[6].equals("NEW")) {
							bean.setStatus(NoteStatus.NEW);
						}
						if (string[6].equals("PENDING")) {
							bean.setStatus(NoteStatus.PENDING);
						}
						if (string[6].equals("COMPLETED")) {
							bean.setStatus(NoteStatus.COMPLETED);
						}
						bean.setAttachment(string[7]);
						beanList.add(bean);
					}
				}
				if (string.length==6) {
					if (string[0].contains(title) || string[1].contains(title) || string[2].contains(title) ||string[4].contains(title)||string[5].contains(title)){
						bean=new NoteBean();
						bean.setTitle(string[0]);
						bean.setDescription(string[1]);
						bean.setTags(string[2]);
						Date cdate=sdf.parse(string[3]);
						bean.setCreationDate(cdate);
						bean.setType(NoteType.SELF);//string[4];
						bean.setAttachment(string[5]);
						beanList.add(bean);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Logger.getInstance().log(e.getMessage());
		}
		finally {
			if (br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return beanList;
	}
//----------------------------------------------

	public List<NoteBean> readCat(String catName){
		List<NoteBean> beanList=new ArrayList<NoteBean>();
		NoteBean bean;
		BufferedReader br=null;
		String line;
		try {
			br=new BufferedReader(new FileReader(catName+".notes"));
			while ((line=br.readLine())!=null) {
				String[] string=line.split(":");
				bean=new NoteBean();
				if (string.length==8) {
					bean=new NoteBean();
					bean.setTitle(string[0]);
					bean.setDescription(string[1]);
					bean.setTags(string[2]);
					Date cdate=sdf.parse(string[3]);
					bean.setCreationDate(cdate);
					bean.setType(NoteType.TASK);//string[4];
					Date pdate=sdf.parse(string[5]);
					bean.setPlannedDate(pdate);
					if (string[6].equals("NEW")) {
						bean.setStatus(NoteStatus.NEW);
					}
					if (string[6].equals("PENDING")) {
						bean.setStatus(NoteStatus.PENDING);
					}
					if (string[6].equals("COMPLETED")) {
						bean.setStatus(NoteStatus.COMPLETED);
					}
					bean.setAttachment(string[7]);
					beanList.add(bean);
				}
				if (string.length==6) {
					bean=new NoteBean();
					bean.setTitle(string[0]);
					bean.setDescription(string[1]);
					bean.setTags(string[2]);
					Date cdate=sdf.parse(string[3]);
					bean.setCreationDate(cdate);
					bean.setType(NoteType.SELF);//string[4];
					bean.setAttachment(string[5]);
					beanList.add(bean);
				}

			}
		} catch (Exception e) {
			Logger.getInstance().log(e.getMessage());
		}
		finally {
			if (br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					Logger.getInstance().log(e.getMessage());
				}
			}
		}
		return beanList;
	}
//-----------------------------------------------
	public boolean deleteCat(String catName){
		File file=new File(catName+".notes");
		if (file.exists()) {
			file.delete();
			return true;
		}
		return false;
	}
//----------------------------------------------
	public List<NoteBean> searchGen(String title) {
		NoteBean bean;
		List<NoteBean> beanList=new ArrayList<NoteBean>();
		BufferedReader br=null;
		String line="";
		File file=null;
		try {
			file=new File(".");
			String[] str=file.list();
			for (String stringFile : str) {
				if(stringFile.endsWith(".notes")){
					br=new BufferedReader(new FileReader(stringFile));
					while ((line=br.readLine())!=null) {
						String[] string=line.split(":");
						if (string.length==8) {
							if (string[0].contains(title) || string[1].contains(title) || string[2].contains(title) 
									|| string[4].contains(title) || string[6].contains(title) || string[7].contains(title) ) {
								bean=new NoteBean();
								bean.setTitle(string[0]);
								bean.setDescription(string[1]);
								bean.setTags(string[2]);
								Date cdate=sdf.parse(string[3]);
								bean.setCreationDate(cdate);
								bean.setType(NoteType.TASK);//string[4];
								Date pdate=sdf.parse(string[5]);
								bean.setPlannedDate(pdate);
								if (string[6].equals("NEW")) {
									bean.setStatus(NoteStatus.NEW);
								}
								if (string[6].equals("PENDING")) {
									bean.setStatus(NoteStatus.PENDING);
								}
								if (string[6].equals("COMPLETED")) {
									bean.setStatus(NoteStatus.COMPLETED);
								}
								bean.setAttachment(string[7]);
								beanList.add(bean);
							}
						}
						if (string.length==6) {
							if (string[0].contains(title) || string[1].contains(title) || string[2].contains(title) ||string[4].contains(title)||string[5].contains(title)){
								bean=new NoteBean();
								bean.setTitle(string[0]);
								bean.setDescription(string[1]);
								bean.setTags(string[2]);
								Date cdate=sdf.parse(string[3]);
								bean.setCreationDate(cdate);
								bean.setType(NoteType.SELF);//string[4];
								bean.setAttachment(string[5]);
								beanList.add(bean);
							}
						}
					}
					
				}
			}
			
		}catch (Exception e) {
			Logger.getInstance().log(e.getMessage());
			e.printStackTrace();
		}
		finally {
			if (br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					Logger.getInstance().log(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return beanList;

	}

//------------------------------------------------------------
	public boolean SendMail(String fromEmailId, String password, String toEmailId, 
							String subject, String content) {
		
		String  d_uname = "laxmikant2123",
				d_host = "smtp.gmail.com",
				d_port  = "465";
		Properties props = new Properties();
		props.put("mail.smtp.user", fromEmailId);
		props.put("mail.smtp.host", d_host);
		props.put("mail.smtp.port", d_port);
		props.put("mail.smtp.starttls.enable","true");
		props.put("mail.smtp.debug", "true");
		props.put("mail.smtp.auth", "true");

		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmailId, password);
			}
		};	 

		Session session = Session.getInstance(props, auth);
		//session.setDebug(true);

		MimeMessage msg = new MimeMessage(session);
		try {
			msg.setSubject(subject);
			msg.setFrom(new InternetAddress(fromEmailId));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmailId));
			msg.setContent(content, "text/plain");
			Transport transport = session.getTransport("smtps");
			transport.connect(d_host, Integer.valueOf(d_port), d_uname, password);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			return true;
		} catch (AddressException e) {
			e.printStackTrace();
			return false;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
	
//--------------------------------------------------------------
	
	public List<NoteBean> listGen() {
		NoteBean bean;
		List<NoteBean> beanList=new ArrayList<NoteBean>();
		BufferedReader br=null;
		String line="";
		File file=null;
		try {
			file=new File(".");
			String[] str=file.list();
			for (String stringFile : str) {
				if(stringFile.endsWith(".notes")){
					br=new BufferedReader(new FileReader(stringFile));
					while ((line=br.readLine())!=null) {
						String[] string=line.split(":");
						if (string.length==8) {
								bean=new NoteBean();
								bean.setTitle(string[0]);
								bean.setDescription(string[1]);
								bean.setTags(string[2]);
								Date cdate=sdf.parse(string[3]);
								bean.setCreationDate(cdate);
								bean.setType(NoteType.TASK);//string[4];
								Date pdate=sdf.parse(string[5]);
								bean.setPlannedDate(pdate);
								if (string[6].equals("NEW")) {
									bean.setStatus(NoteStatus.NEW);
								}
								if (string[6].equals("PENDING")) {
									bean.setStatus(NoteStatus.PENDING);
								}
								if (string[6].equals("COMPLETED")) {
									bean.setStatus(NoteStatus.COMPLETED);
								}
								bean.setAttachment(string[7]);
								beanList.add(bean);
						}
						if (string.length==6) {
								bean=new NoteBean();
								bean.setTitle(string[0]);
								bean.setDescription(string[1]);
								bean.setTags(string[2]);
								Date cdate=sdf.parse(string[3]);
								bean.setCreationDate(cdate);
								bean.setType(NoteType.SELF);//string[4];
								bean.setAttachment(string[5]);
								beanList.add(bean);
						}
					}
					
				}
			}
			
		}catch (Exception e) {
			Logger.getInstance().log(e.getMessage());
			e.printStackTrace();
		}
		finally {
			if (br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					Logger.getInstance().log(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return beanList;

	}
	
//-------------------------------------------------------------------------
	public List<String> listCats() {
		File file=null;
		List<String> catList =new ArrayList<String>();
		try {
			file=new File(".");
			String[] list=file.list();
			for (String string : list) {
				if (string.endsWith(".notes")) {
					String[] str=string.split("\\.");
					catList.add(str[0]);
				}
			}
		} catch (Exception e) {
			Logger.getInstance().log(e.getMessage());
			e.printStackTrace();
		}
		return catList;		
	}
	
	
//-------------------------------------------------------------------------
	
	public List<NoteBean> getAllNotes(String catName){
		List<NoteBean> noteBean=new ArrayList<NoteBean>();
		NoteBean bean=null;
		BufferedReader br=null;
		try {
			String line=null;
			br=new BufferedReader(new FileReader(catName+".notes"));
			while ((line=br.readLine())!=null) {
				String[] string=line.split(":");
				if (string.length==8) {
						bean=new NoteBean();
						bean.setTitle(string[0]);
						bean.setDescription(string[1]);
						bean.setTags(string[2]);
						Date cdate=sdf.parse(string[3]);
						bean.setCreationDate(cdate);
						bean.setType(NoteType.TASK);//string[4];
						Date pdate=sdf.parse(string[5]);
						bean.setPlannedDate(pdate);
						if (string[6].equals("NEW")) {
							bean.setStatus(NoteStatus.NEW);
						}
						if (string[6].equals("PENDING")) {
							bean.setStatus(NoteStatus.PENDING);
						}
						if (string[6].equals("COMPLETED")) {
							bean.setStatus(NoteStatus.COMPLETED);
						}
						bean.setAttachment(string[7]);
						noteBean.add(bean);
				}
				if (string.length==6) {
						bean=new NoteBean();
						bean.setTitle(string[0]);
						bean.setDescription(string[1]);
						bean.setTags(string[2]);
						Date cdate=sdf.parse(string[3]);
						bean.setCreationDate(cdate);
						bean.setType(NoteType.SELF);//string[4];
						bean.setAttachment(string[5]);
						noteBean.add(bean);
				}
			}
		} catch (Exception e) {
			Logger.getInstance().log(e.getMessage());
			e.printStackTrace();
		}finally {
			if (br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					Logger.getInstance().log(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		
		return noteBean;
	}
	
//------------------------to create xls file--------------------------------------
	
	public boolean covertXls(List<NoteBean> noteBean,String fileName) {
		
		try {

			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("Excel Sheet");
			HSSFRow rowhead = sheet.createRow((short)0);

			rowhead.createCell(0).setCellValue("Task Name");
			rowhead.createCell(1).setCellValue("Description");
			rowhead.createCell(2).setCellValue("Tags");
			rowhead.createCell(3).setCellValue("Created Date");
			rowhead.createCell(4).setCellValue("Note Type");
			rowhead.createCell(5).setCellValue("Planned Date");
			rowhead.createCell(6).setCellValue("Task Status");
			rowhead.createCell(7).setCellValue("Attachments");


			int index=1;
			Iterator<NoteBean> itr=null;
			for (itr=noteBean.iterator(); itr.hasNext(); )
			{
				NoteBean note=itr.next();
				if (note.getType().equals(NoteType.TASK)) {
					
				HSSFRow row = sheet.createRow((short)index);

				row.createCell(0).setCellValue(note.getTitle());
				row.createCell(1).setCellValue(note.getDescription());
				row.createCell(2).setCellValue(note.getTags());
				row.createCell(3).setCellValue(sdf.format(note.getCreationDate()));
				row.createCell(4).setCellValue(note.getType().toString());
				row.createCell(5).setCellValue(sdf.format(note.getPlannedDate()));
				row.createCell(6).setCellValue(note.getStatus().toString());
				row.createCell(7).setCellValue(note.getAttachment());

				index++;
			
				}
				else {
					HSSFRow row = sheet.createRow((short)index);

					row.createCell(0).setCellValue(note.getTitle());
					row.createCell(1).setCellValue(note.getDescription());
					row.createCell(2).setCellValue(note.getTags());
					row.createCell(3).setCellValue(sdf.format(note.getCreationDate()));
					row.createCell(4).setCellValue(note.getType().toString());
					row.createCell(7).setCellValue(note.getAttachment());


					index++;
				}
				}


			FileOutputStream fileOut = new FileOutputStream(fileName+".xls");
			wb.write(fileOut);
			fileOut.close();

		} catch (Exception e) {
			Logger.getInstance().log(e.getMessage());
			e.printStackTrace();
		}

		return true;
	}
//-----------------------------list by size-----------------------
	public Map<String,Double> getFileSize() {
		Map<String,Double> map=new LinkedHashMap<String,Double>();
		File file=null;
		try {
			file=new File(".");
			File[] f=file.listFiles();
			for (File file2 : f) {
				String fname=file2.getName();
				if (fname.endsWith(".notes")) {
					double size=file2.length();
					map.put(fname, size);
				}
				
			}
		} catch (Exception e) {
			Logger.getInstance().log(e.getMessage());
			e.printStackTrace();
		}
		return map;
		
	}

	//-------------------content generator to send mail----------
	
	public String genContent(NoteBean bean) {
		String contentMail="";
		if (bean.getType().equals(NoteType.TASK)) {
			contentMail="Title : "+bean.getTitle()+" , "+
					" Description :"+bean.getDescription()+" , "+
					" Tags : "+bean.getTags()+" , "+
					" Created date : "+sdf.format(bean.getCreationDate())+" , "+
					" Note type : "+bean.getType()+" , "+
					" Planned date : "+sdf.format(bean.getPlannedDate())+" , "+
					" Task Status : "+bean.getStatus()+" , "+
					" Attachments : "+bean.getAttachment();
		}else
		{
			contentMail="Title : "+bean.getTitle()+" , "+
					" Description :"+bean.getDescription()+" , "+
					" Tags : "+bean.getTags()+" , "+
					" Created date : "+sdf.format(bean.getCreationDate())+" , "+
					" Note type : "+bean.getType()+" , "+
					" Attachments : "+bean.getAttachment();
		}
		return contentMail;
		
	}

	
	public List<NoteBean> todaysRemainders(Date date){
		List<NoteBean> beanList=new ArrayList<NoteBean>();
		NoteBean bean=null;
		String dt=sdf.format(date);
		BufferedReader br=null;
		String line="";
		File file=null;
		try {
			file=new File(".");
			String[] str=file.list();
			for (String stringFile : str) {
				if(stringFile.endsWith(".notes")){
					br=new BufferedReader(new FileReader(stringFile));
					while ((line=br.readLine())!=null) {
						String[] string=line.split(":");
						if (string.length==8) {
							if (string[5].equals(dt)) {
								bean=new NoteBean();
								bean.setTitle(string[0]);
								bean.setDescription(string[1]);
								bean.setTags(string[2]);
								Date cdate=sdf.parse(string[3]);
								bean.setCreationDate(cdate);
								bean.setType(NoteType.TASK);//string[4];
								Date pdate=sdf.parse(string[5]);
								bean.setPlannedDate(pdate);
								if (string[6].equals("NEW")) {
									bean.setStatus(NoteStatus.NEW);
								}
								if (string[6].equals("PENDING")) {
									bean.setStatus(NoteStatus.PENDING);
								}
								if (string[6].equals("COMPLETED")) {
									bean.setStatus(NoteStatus.COMPLETED);
								}
								bean.setAttachment(string[7]);
								beanList.add(bean);
							}
						}
					}
					
				}
			}
			
		}catch (Exception e) {
			Logger.getInstance().log(e.getMessage());
			e.printStackTrace();
		}
		finally {
			if (br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					Logger.getInstance().log(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return beanList;
	}
	
	
	//----------------------------------------------------------------------
	
	public void mailer() {
		String toEmailId = "gslaxmikant@gmail.com";       // it should be gmailId (to whom you want to send)
		String fromEmailId=null;
		String password=null;
		String subject="** important information **";

		String content="Hello, This is LSG....";

		Calendar today = Calendar.getInstance();
		Timer timer = new Timer();
		
		timer.schedule(new TimerTask() {
			public void run() {
				long min=LocalDateTime.now().getMinute();
				long hour=LocalDateTime.now().getHour();
				if(hour==00 && (min==13)){                //6:43PM        //Time at which you want to send the mail //for PM add +12 to your time
					System.out.println("Sending mail now");	
					SendMail(fromEmailId, password, toEmailId,subject,content);
					System.exit(1);
					
				}else{
					System.out.println("Checking for the time");
				}
			}
		}, today.getTime(), TimeUnit.SECONDS.toMillis(5)); //TimeUnit.SECONDS.toMillis(5) -->> interval


	}
	
	
	//----------------------------------------------
	public Config confReader() {
		Config conf=null;
		BufferedReader br=null;
		try {
			String line="";
			br= new BufferedReader(new FileReader("config.conf"));
			line=br.readLine();
				String[] resConf=line.split(":");
				conf=new Config();
				conf.setEmail(resConf[0]);
				conf.setPassword(resConf[1]);
				conf.setHrs(resConf[2]);
				conf.setMins(resConf[3]);
			
		} catch (Exception e) {
			Logger.getInstance().log(e.getMessage());
			e.printStackTrace();
		}
		finally {
			if (br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					Logger.getInstance().log(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return conf;
		
		
	}
	
	
	//-----------------------------------------------
	public boolean confUpdater(Config conf) {
		BufferedWriter bw=null;
		try {
			bw=new BufferedWriter(new FileWriter("config.conf") );
			bw.write(conf.getEmail()+":"+conf.getPassword()+":"+conf.getHrs()+":"+conf.getMins());

		} catch (Exception e) {
			Logger.getInstance().log(e.getMessage());
			e.printStackTrace();
			return false;
		}
		finally {
			if (bw!=null) {
				try {
					bw.close();
				} catch (IOException e) {
					Logger.getInstance().log(e.getMessage());
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}
	
	//-----------------------------------------------
		public boolean deleteConf(){
			File file=new File("config.conf");
			if (file.exists()) {
				file.delete();
				return true;
			}
			return false;
		}
	
	
	
	
	
	
	
	
	
}
