package com.lsg.project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
			//System.out.println("Categories r listed below :");
			for (String stringFile : str) {
				if(stringFile.endsWith(".notes")){
					br=new BufferedReader(new FileReader(stringFile));
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
	public boolean SendMail(String fromEmailId, String password, String toEmailId, String subject, String content) {
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
		    session.setDebug(true);

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
	
	
	
	
	
	
	
}
