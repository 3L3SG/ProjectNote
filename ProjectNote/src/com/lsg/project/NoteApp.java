package com.lsg.project;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class NoteApp {

	public static void main(String[] args) {
		Scanner scan=new Scanner(System.in);
		Scanner scanLine=new Scanner(System.in);
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");

		try {
			int choice=0;
			int ch2=0;
			String catName,attachment;
			String title=null;
			String description,tags,planningDate;
			NoteType type;
			NoteStatus status;
			Date date,pdate;
			String fromEmailId = "laxmikant2123@gmail.com";
			String password = "laxmikant21";
			NoteModel model=new NoteModel();
		outer:	while (choice!=8) {
				System.out.println("");
				System.out.println("choose an option");
				System.out.println("1. to create a category");
				System.out.println("2. to load catagory");
				System.out.println("3. to search");
				System.out.println("4. to list");
				System.out.println("5. to export");
				System.out.println("6. to to remainder");
				System.out.println("7. to send note");
				System.out.println("8. to exit");
				System.out.println("");
				while (!scan.hasNextInt()) {
					System.out.println("enter the value in range of 0-->8");
					scan.next();
				}
				choice=scan.nextInt();
				switch (choice) {
				//-----------------------create category------------
				case 1 :
					System.out.println("");
					System.out.println("Enter category name");
					System.out.println("");
					while (!NoteUtil.validateID(catName=scanLine.nextLine())) {
						System.out.println("");
						System.out.println("enter a valid catagory name");
						System.out.println("");
						continue;
					}
					System.out.println("");
					if (model.isCatExist(catName)) {
						System.out.println("");
						System.out.println("\""+catName+"\" category name is already exist and selected to create notes");
						System.out.println("");
					}else {
						System.out.println("");
						System.out.println("creating a new category : \""+catName+"\"");
						System.out.println("");
					}
					while (ch2!=6) {
						System.out.println("");
						System.out.println("choose an option");
						System.out.println("1. to create note");
						System.out.println("2. to edit note");
						System.out.println("3. to remove note");
						System.out.println("4. to search");
						System.out.println("5. to list notes");
						System.out.println("6. to go back");
						System.out.println("");
						while (!scan.hasNextInt()) {
							System.out.println("");
							System.out.println("enter choice in range 0-6");
							scan.next();
						}
						ch2=scan.nextInt();
						//----------------------switch to add note-------------------------
						switch (ch2) {
						case 1: 
							System.out.println("");
							System.out.println("Enter title of note");
							while (!NoteUtil.validateID(title=scanLine.nextLine())) {
								System.out.println("");
								System.out.println("enter a valid title for note");
								System.out.println("");
								continue;
							}if (model.isTitleExist(title, catName)) {
								System.out.println("");
								System.out.println("\""+title+"\" title already exist");
								System.out.println("");
								break;
							} else {
								System.out.println("enter note description");
								description=scanLine.nextLine();
								System.out.println("enter tags for note(seperating by semicolon)");
								tags=scan.next();
								System.out.println("do you want to set this note as remainder..? enter y/n or yes/no to proceed further..??");
								String tp=scan.next();
								tp=tp.toLowerCase();
								if (tp.equals("y") || tp.equals("yes")) {
									type=NoteType.TASK;
									status=NoteStatus.NEW;
									System.out.println("enter planning date in format dd/MM/yyyy");
									planningDate=scan.next();
									pdate=sdf.parse(planningDate);
									date=new Date();
									System.out.println("any attachment..?? ex: url , address..any");
									attachment=scanLine.nextLine();
									NoteBean note=new NoteBean(title,description,tags,date,type,pdate,status,attachment);
									String res=model.addNote(note, catName);
									if (res.equals("SUCCESS")) {
										System.out.println("");
										System.out.println("\""+title+"\" is sucessfully added to \""+catName+"\"");
										System.out.println("");
									}
									else {
										System.out.println("");
										System.out.println("there is problem in adding \""+title+"\" to \""+catName+"\" problem is :"+res);
										System.out.println("");
									}
								} 
								else {
									type=NoteType.SELF;
									date=new Date();
									System.out.println("any attachment..?? ex: url, address, email..or any");
									attachment=scanLine.nextLine();
									NoteBean note=new NoteBean(title,description,tags,date,type,attachment);
									String res=model.addNote(note, catName);
									if (res.equals("SUCCESS")) {
										System.out.println("");
										System.out.println("\""+title+"\" is sucessfully added to \""+catName+"\"");
										System.out.println("");
									}
									else {
										System.out.println("");
										System.out.println("there is problem in adding \""+title+"\" to \""+catName+"\" problem is :"+res);
										System.out.println("");
									}
								}
							}
							break;
						//----------------------------list all notes------------------------------
						case 5: 
							if (model.isCatExist(catName)) {
								System.out.println("");
								System.out.println("Listing all notes in catagory :\""+catName+"\"");
								System.out.println("");
								List<NoteBean> al=model.listNotes(catName);
								Iterator<NoteBean> itl=al.iterator();
								while (itl.hasNext()) {
									NoteBean bean=itl.next();
									if (bean.getType().equals(NoteType.TASK)) {
										System.out.println("Title : "+bean.getTitle());
										System.out.println("Description : "+bean.getDescription());
										System.out.println("Tags : "+bean.getTags());
										System.out.println("Creadted date : "+sdf.format(bean.getCreationDate()));
										System.out.println("Note type : "+bean.getType());
										System.out.println("Planned date : "+sdf.format(bean.getPlannedDate()));
										System.out.println("Task Status : "+bean.getStatus());
										System.out.println("Attachments : "+bean.getAttachment());
										System.out.println("");
									}else
									{
										System.out.println("Title : "+bean.getTitle());
										System.out.println("Description : "+bean.getDescription());
										System.out.println("Tags : "+bean.getTags());
										System.out.println("Creadted date : "+sdf.format(bean.getCreationDate()));
										System.out.println("Note type : "+bean.getType());
										System.out.println("Attachments : "+bean.getAttachment());
										System.out.println("");
									}
								}
							}else {
								System.out.println("");
								System.out.println("\""+catName+"\" catagory is empty...");
								System.out.println("");
								}
							break;
							//-------------------edit note----------------------------- 
						case 2:
							if (model.isCatExist(catName)) {
								System.out.println("");
								System.out.println("Listing all notes in catagory :\""+catName+"\"");
								System.out.println("");
								boolean editFlag=false;
								int counte=1;
								List<NoteBean> al1=model.listNotes(catName);
								Iterator<NoteBean> it2=al1.iterator();
								while (it2.hasNext()) {
									NoteBean bean=it2.next();
									System.out.println(counte+" : "+bean.getTitle());
									counte++;
								}
								System.out.println("");
								System.out.println("enter any above given note title to edit");
								System.out.println("");
								String editNote=scanLine.nextLine();
								List<NoteBean> editBean=model.readCat(catName);
								int cedit=0;
								Iterator<NoteBean> editIt=editBean.iterator();
								while (editIt.hasNext()) {
									NoteBean noteBean =editIt.next();
									if (noteBean.getTitle().equals(editNote)) {
										editFlag=true;
										if (noteBean.getType().equals(NoteType.TASK)) {
											while (cedit!=7) {
												System.out.println();
												System.out.println("choose an option edit note");
												System.out.println("1. to edit title");
												System.out.println("2. to edit description");
												System.out.println("3. to edit tags");
												System.out.println("4. to edit planning date");
												System.out.println("5. to edit status");
												System.out.println("6. to edit attachment");
												System.out.println("7. to save and exit");
												System.out.println("");
												cedit=scan.nextInt();
												switch (cedit) {
												//------------------------------------------------
												case 1: 
													System.out.println("");
													System.out.println("enter a new title name");
													System.out.println("");
													title=scan.next();
													noteBean.setTitle(title);
													System.out.println("");
													System.out.println("title raplced with \""+title+"\" successfully");
													System.out.println("");
													break;
													//-------------------------------------------------
												case 2: 
													System.out.println("");
													System.out.println("enter anew description");
													System.out.println("");
													description=scanLine.nextLine();
													noteBean.setDescription(description);
													System.out.println("Description edited successfully");
													break;
													//---------------------------------------------------
												case 3: 
													System.out.println("");
													System.out.println("enter new tags separting by semicolon");
													System.out.println("");
													tags=scan.next();
													noteBean.setTags(tags);
													System.out.println("");
													System.out.println("tags edited successfully");
													System.out.println("");
													break;
													//---------------------------------------------------
												case 4: 
													System.out.println("");
													System.out.println("enter new date in foramt dd/mm/yyyy");
													System.out.println("");
													planningDate=scan.next();
													noteBean.setPlannedDate(sdf.parse(planningDate));
													System.out.println("");
													System.out.println("tags edited successfully");
													System.out.println("");
													break;
													//---------------------------------------------------
												case 5: 
													System.out.println("");
													System.out.println("change the current status to \"NEW\" or \"COMPLETED\" or to \"PENDING\"");
													System.out.println("");
													String stat=scan.next();
													if (stat.equals("NEW")) {
														noteBean.setStatus(NoteStatus.NEW);
													}
													if (stat.equals("PENDING")) {
														noteBean.setStatus(NoteStatus.PENDING);
													}
													if (stat.equals("COMPLETED")) {
														noteBean.setStatus(NoteStatus.COMPLETED);
													}
													System.out.println("");
													System.out.println("status edited successfully");
													System.out.println("");
													break;
													//---------------------------------------------------
												case 6: 
													System.out.println("");
													System.out.println("enter new attachment");
													System.out.println("");
													String att=scanLine.nextLine();
													noteBean.setAttachment(att);
													System.out.println("");
													System.out.println("attachment updated successfully");
													System.out.println("");
													break;
													//---------------------------------------------------
												case 7: 
													System.out.println("");
													System.out.println("note saved and exiting from menu");
													System.out.println("");
													break;
													//---------------------------------------------------
												default: 
													System.out.println("");
													System.out.println("ooops...option not supported");
													System.out.println("");
													break;
												}
											}
										}
										else {
											while (cedit!=5) {
												System.out.println("");
												System.out.println("choose an option edit note");
												System.out.println("1. to edit title");
												System.out.println("2. to edit description");
												System.out.println("3. to edit tags");
												System.out.println("4. to edit attachment");
												System.out.println("5. to save and exit");
												System.out.println("");
												cedit=scan.nextInt();
												switch (cedit) {
												//------------------------------------------------
												case 1: 
													System.out.println("");
													System.out.println("enter a new title name");
													System.out.println("");
													title=scan.next();
													noteBean.setTitle(title);
													System.out.println("");
													System.out.println("title raplced with \""+title+"\" successfully");
													System.out.println("");
													break;
													//-------------------------------------------------
												case 2: 
													System.out.println("");
													System.out.println("enter anew description");
													System.out.println("");
													description=scanLine.nextLine();
													noteBean.setDescription(description);
													System.out.println("");
													System.out.println("Description edited successfully");
													System.out.println("");
													break;
													//---------------------------------------------------
												case 3: 
													System.out.println("");
													System.out.println("enter new tags separting by semicolon");
													System.out.println("");
													tags=scan.next();
													noteBean.setTags(tags);
													System.out.println("");
													System.out.println("tags edited successfully");
													System.out.println("");
													break;
													//---------------------------------------------------
												case 4: 
													System.out.println("");
													System.out.println("enter new attachment");
													System.out.println("");
													String att=scanLine.nextLine();
													noteBean.setAttachment(att);
													System.out.println("");
													System.out.println("attachment updated successfully");
													System.out.println("");
													break;
													//---------------------------------------------------
												case 5: 
													System.out.println("");
													System.out.println("note saved and exiting from menu");
													System.out.println("");
													break;
													//---------------------------------------------------
												default: 
													System.out.println("");
													System.out.println("ooops...option not supported");
													System.out.println("");
													break;
												}
											}
										}
									}

								}
								if (editFlag==false) {
									System.out.println();
									System.out.println("note not found try again...");
									System.out.println();
								}
								if (model.deleteCat(catName)) {
									for (NoteBean noteBean : editBean) {
										model.addNote(noteBean, catName);
									}
								}
							}else {
								System.out.println("");
								System.out.println("\""+catName+"\" catagory is empty...");
								System.out.println();
							}
							break;
							//---------------------remove note----------------------------
						case 3:
							if (model.isCatExist(catName)) {
								List<NoteBean> al2=model.listNotes(catName);
								Iterator<NoteBean> it3=al2.iterator();
								System.out.println("");
								System.out.println("Listing all notes in catagory :\""+catName+"\"");
								System.out.println("");
								int counter=1;
								while (it3.hasNext()) {
									NoteBean bean=it3.next();
									System.out.println(counter+" : "+bean.getTitle());
									counter++;
								}
								System.out.println("");
								System.out.println("Enter note name to remove");
								System.out.println("");
								String removeTitle=scanLine.nextLine();
								List<NoteBean> listBean=model.readCat(catName);
								Iterator<NoteBean> itBean=listBean.iterator();
								while (itBean.hasNext()) {
									NoteBean noteBean =itBean.next();
									if (noteBean.getTitle().equals(removeTitle)) {
										System.out.println("");
										System.out.println("note \""+noteBean.getTitle()+"\" is succesfully removed");
										System.out.println("");
										itBean.remove();
									}
								}
								if (model.deleteCat(catName)) {
									for (NoteBean noteBean : listBean) {
										model.addNote(noteBean, catName);
									}
								}
							}else {
								System.out.println("");
								System.out.println("\""+catName+"\" category is empty...");
								System.out.println("");	
							}
							break;
							//----------------------search note----------------------------
						case 4: 
							if (model.isCatExist(catName)) { 
								System.out.println("");
								System.out.println("enter title/keyword to search");
								System.out.println("");
								String searchString=scan.next();
								List<NoteBean> beanList=model.searchNote(searchString, catName);
								Iterator<NoteBean> it=beanList.iterator();
								System.out.println("");
								System.out.println("search result for keyword/title : \""+searchString+"\"");
								System.out.println("");
								while (it.hasNext()) {
									NoteBean bean=it.next();
									if (bean.getType().equals(NoteType.TASK)) {
										System.out.println("Title : "+bean.getTitle());
										System.out.println("Description : "+bean.getDescription());
										System.out.println("Tags : "+bean.getTags());
										System.out.println("Creadted date : "+sdf.format(bean.getCreationDate()));
										System.out.println("Note type : "+bean.getType());
										System.out.println("Planned date : "+sdf.format(bean.getPlannedDate()));
										System.out.println("Task Status : "+bean.getStatus());
										System.out.println("Attachments : "+bean.getAttachment());
										System.out.println("");
									}else
									{
										System.out.println("Title : "+bean.getTitle());
										System.out.println("Description : "+bean.getDescription());
										System.out.println("Tags : "+bean.getTags());
										System.out.println("Creadted date : "+sdf.format(bean.getCreationDate()));
										System.out.println("Note type : "+bean.getType());
										System.out.println("Attachments : "+bean.getAttachment());
										System.out.println("");
									}
								}
							}else {
								System.out.println("");
								System.out.println("\""+catName+"\" catagory is empty...");
								System.out.println();
							}
							break;
							//--------------------------------------------------
						default:
							break;
						}

					}
					break;
					//**********************************************************
					//**********************************************************
				case 2 : System.out.println("load category...");
						System.out.println("enter category name to load");
						int ch3=0;
						while (!(model.isCatExist(catName=scan.next()))) {
							if (catName.equals("*")) {
								continue outer;
							}
							System.out.println("category doesnot exists.. try again [enter \"*\" to go back] ");
							continue;
						}
							System.out.println("category loaded successfully..");
							while (ch3!=6) {
								System.out.println("");
								System.out.println("choose an option");
								System.out.println("1. to create note");
								System.out.println("2. to edit note");
								System.out.println("3. to remove note");
								System.out.println("4. to search");
								System.out.println("5. to list notes");
								System.out.println("6. to go back");
								System.out.println("");
								while (!scan.hasNextInt()) {
									System.out.println("");
									System.out.println("enter choice in range 0-6");
									scan.next();
								}
								ch3=scan.nextInt();
								//----------------------switch to add note-------------------------
								switch (ch3) {
								case 1: 
									System.out.println("");
									System.out.println("Enter title of note");
									while (!NoteUtil.validateID(title=scanLine.nextLine())) {
										System.out.println("");
										System.out.println("enter a valid title for note");
										System.out.println("");
										continue;
									}if (model.isTitleExist(title, catName)) {
										System.out.println("");
										System.out.println("\""+title+"\" title already exist");
										System.out.println("");
										break;
									} else {
										System.out.println("enter note description");
										description=scanLine.nextLine();
										System.out.println("enter tags for note(seperating by semicolon)");
										tags=scan.next();
										System.out.println("do you want to set this note as remainder..? enter y/n or yes/no to proceed further..??");
										String tp=scan.next();
										tp=tp.toLowerCase();
										if (tp.equals("y") || tp.equals("yes")) {
											type=NoteType.TASK;
											status=NoteStatus.NEW;
											System.out.println("enter planning date in format dd/MM/yyyy");
											planningDate=scan.next();
											pdate=sdf.parse(planningDate);
											date=new Date();
											System.out.println("any attachment..?? ex: url , address..any");
											attachment=scanLine.nextLine();
											NoteBean note=new NoteBean(title,description,tags,date,type,pdate,status,attachment);
											String res=model.addNote(note, catName);
											if (res.equals("SUCCESS")) {
												System.out.println("");
												System.out.println("\""+title+"\" is sucessfully added to \""+catName+"\"");
												System.out.println("");
											}
											else {
												System.out.println("");
												System.out.println("there is problem in adding \""+title+"\" to \""+catName+"\" problem is :"+res);
												System.out.println("");
											}
										} 
										else {
											type=NoteType.SELF;
											date=new Date();
											System.out.println("any attachment..?? ex: url, address, email..or any");
											attachment=scanLine.nextLine();
											NoteBean note=new NoteBean(title,description,tags,date,type,attachment);
											String res=model.addNote(note, catName);
											if (res.equals("SUCCESS")) {
												System.out.println("");
												System.out.println("\""+title+"\" is sucessfully added to \""+catName+"\"");
												System.out.println("");
											}
											else {
												System.out.println("");
												System.out.println("there is problem in adding \""+title+"\" to \""+catName+"\" problem is :"+res);
												System.out.println("");
											}
										}
									}
									break;
								//----------------------------list all notes------------------------------
								case 5: 
									if (model.isCatExist(catName)) {
										System.out.println("");
										System.out.println("Listing all notes in catagory :\""+catName+"\"");
										System.out.println("");
										List<NoteBean> al=model.listNotes(catName);
										Iterator<NoteBean> itl=al.iterator();
										while (itl.hasNext()) {
											NoteBean bean=itl.next();
											if (bean.getType().equals(NoteType.TASK)) {
												System.out.println("Title : "+bean.getTitle());
												System.out.println("Description : "+bean.getDescription());
												System.out.println("Tags : "+bean.getTags());
												System.out.println("Creadted date : "+sdf.format(bean.getCreationDate()));
												System.out.println("Note type : "+bean.getType());
												System.out.println("Planned date : "+sdf.format(bean.getPlannedDate()));
												System.out.println("Task Status : "+bean.getStatus());
												System.out.println("Attachments : "+bean.getAttachment());
												System.out.println("");
											}else
											{
												System.out.println("Title : "+bean.getTitle());
												System.out.println("Description : "+bean.getDescription());
												System.out.println("Tags : "+bean.getTags());
												System.out.println("Creadted date : "+sdf.format(bean.getCreationDate()));
												System.out.println("Note type : "+bean.getType());
												System.out.println("Attachments : "+bean.getAttachment());
												System.out.println("");
											}
										}
									}else {
										System.out.println("");
										System.out.println("\""+catName+"\" catagory is empty...");
										System.out.println("");
										}
									break;
									//-------------------edit note----------------------------- 
								case 2:
									if (model.isCatExist(catName)) {
										System.out.println("");
										System.out.println("Listing all notes in catagory :\""+catName+"\"");
										System.out.println("");
										boolean editFlag=false;
										int counte=1;
										List<NoteBean> al1=model.listNotes(catName);
										Iterator<NoteBean> it2=al1.iterator();
										while (it2.hasNext()) {
											NoteBean bean=it2.next();
											System.out.println(counte+" : "+bean.getTitle());
											counte++;
										}
										System.out.println("");
										System.out.println("enter any above given note title to edit");
										System.out.println("");
										String editNote=scanLine.nextLine();
										List<NoteBean> editBean=model.readCat(catName);
										int cedit=0;
										Iterator<NoteBean> editIt=editBean.iterator();
										while (editIt.hasNext()) {
											NoteBean noteBean =editIt.next();
											if (noteBean.getTitle().equals(editNote)) {
												editFlag=true;
												if (noteBean.getType().equals(NoteType.TASK)) {
													while (cedit!=7) {
														System.out.println();
														System.out.println("choose an option edit note");
														System.out.println("1. to edit title");
														System.out.println("2. to edit description");
														System.out.println("3. to edit tags");
														System.out.println("4. to edit planning date");
														System.out.println("5. to edit status");
														System.out.println("6. to edit attachment");
														System.out.println("7. to save and exit");
														System.out.println("");
														cedit=scan.nextInt();
														switch (cedit) {
														//------------------------------------------------
														case 1: 
															System.out.println("");
															System.out.println("enter a new title name");
															System.out.println("");
															title=scanLine.nextLine();
															noteBean.setTitle(title);
															System.out.println("");
															System.out.println("title raplced with \""+title+"\" successfully");
															System.out.println("");
															break;
															//-------------------------------------------------
														case 2: 
															System.out.println("");
															System.out.println("enter anew description");
															System.out.println("");
															description=scanLine.nextLine();
															noteBean.setDescription(description);
															System.out.println("Description edited successfully");
															break;
															//---------------------------------------------------
														case 3: 
															System.out.println("");
															System.out.println("enter new tags separting by semicolon");
															System.out.println("");
															tags=scan.next();
															noteBean.setTags(tags);
															System.out.println("");
															System.out.println("tags edited successfully");
															System.out.println("");
															break;
															//---------------------------------------------------
														case 4: 
															System.out.println("");
															System.out.println("enter new date in foramt dd/mm/yyyy");
															System.out.println("");
															planningDate=scan.next();
															noteBean.setPlannedDate(sdf.parse(planningDate));
															System.out.println("");
															System.out.println("tags edited successfully");
															System.out.println("");
															break;
															//---------------------------------------------------
														case 5: 
															System.out.println("");
															System.out.println("change the current status to \"NEW\" or \"COMPLETED\" or to \"PENDING\"");
															System.out.println("");
															String stat=scan.next();
															if (stat.equals("NEW")) {
																noteBean.setStatus(NoteStatus.NEW);
															}
															if (stat.equals("PENDING")) {
																noteBean.setStatus(NoteStatus.PENDING);
															}
															if (stat.equals("COMPLETED")) {
																noteBean.setStatus(NoteStatus.COMPLETED);
															}
															System.out.println("");
															System.out.println("status edited successfully");
															System.out.println("");
															break;
															//---------------------------------------------------
														case 6: 
															System.out.println("");
															System.out.println("enter new attachment");
															System.out.println("");
															String att=scanLine.nextLine();
															noteBean.setAttachment(att);
															System.out.println("");
															System.out.println("attachment updated successfully");
															System.out.println("");
															break;
															//---------------------------------------------------
														case 7: 
															System.out.println("");
															System.out.println("note saved and exiting from menu");
															System.out.println("");
															break;
															//---------------------------------------------------
														default: 
															System.out.println("");
															System.out.println("ooops...option not supported");
															System.out.println("");
															break;
														}
													}
												}
												else {
													while (cedit!=5) {
														System.out.println("");
														System.out.println("choose an option edit note");
														System.out.println("1. to edit title");
														System.out.println("2. to edit description");
														System.out.println("3. to edit tags");
														System.out.println("4. to edit attachment");
														System.out.println("5. to save and exit");
														System.out.println("");
														cedit=scan.nextInt();
														switch (cedit) {
														//------------------------------------------------
														case 1: 
															System.out.println("");
															System.out.println("enter a new title name");
															System.out.println("");
															title=scanLine.nextLine();
															noteBean.setTitle(title);
															System.out.println("");
															System.out.println("title raplced with \""+title+"\" successfully");
															System.out.println("");
															break;
															//-------------------------------------------------
														case 2: 
															System.out.println("");
															System.out.println("enter anew description");
															System.out.println("");
															description=scanLine.nextLine();
															noteBean.setDescription(description);
															System.out.println("");
															System.out.println("Description edited successfully");
															System.out.println("");
															break;
															//---------------------------------------------------
														case 3: 
															System.out.println("");
															System.out.println("enter new tags separting by semicolon");
															System.out.println("");
															tags=scan.next();
															noteBean.setTags(tags);
															System.out.println("");
															System.out.println("tags edited successfully");
															System.out.println("");
															break;
															//---------------------------------------------------
														case 4: 
															System.out.println("");
															System.out.println("enter new attachment");
															System.out.println("");
															String att=scanLine.nextLine();
															noteBean.setAttachment(att);
															System.out.println("");
															System.out.println("attachment updated successfully");
															System.out.println("");
															break;
															//---------------------------------------------------
														case 5: 
															System.out.println("");
															System.out.println("note saved and exiting from menu");
															System.out.println("");
															break;
															//---------------------------------------------------
														default: 
															System.out.println("");
															System.out.println("ooops...option not supported");
															System.out.println("");
															break;
														}
													}
												}
											}

										}
										if (editFlag==false) {
											System.out.println();
											System.out.println("note not found try again...");
											System.out.println();
										}
										if (model.deleteCat(catName)) {
											for (NoteBean noteBean : editBean) {
												model.addNote(noteBean, catName);
											}
										}
									}else {
										System.out.println("");
										System.out.println("\""+catName+"\" catagory is empty...");
										System.out.println();
									}
									break;
									//---------------------remove note----------------------------
								case 3:
									if (model.isCatExist(catName)) {
										List<NoteBean> al2=model.listNotes(catName);
										Iterator<NoteBean> it3=al2.iterator();
										System.out.println("");
										System.out.println("Listing all notes in catagory :\""+catName+"\"");
										System.out.println("");
										int counter=1;
										while (it3.hasNext()) {
											NoteBean bean=it3.next();
											System.out.println(counter+" : "+bean.getTitle());
											counter++;
										}
										System.out.println("");
										System.out.println("Enter note name to remove");
										System.out.println("");
										String removeTitle=scanLine.nextLine();
										List<NoteBean> listBean=model.readCat(catName);
										Iterator<NoteBean> itBean=listBean.iterator();
										while (itBean.hasNext()) {
											NoteBean noteBean =itBean.next();
											if (noteBean.getTitle().equals(removeTitle)) {
												System.out.println("");
												System.out.println("note \""+noteBean.getTitle()+"\" is succesfully removed");
												System.out.println("");
												itBean.remove();
											}
										}
										if (model.deleteCat(catName)) {
											for (NoteBean noteBean : listBean) {
												model.addNote(noteBean, catName);
											}
										}
									}else {
										System.out.println("");
										System.out.println("\""+catName+"\" category is empty...");
										System.out.println("");	
									}
									break;
									//----------------------search note----------------------------
								case 4: 
									if (model.isCatExist(catName)) { 
										System.out.println("");
										System.out.println("enter title/keyword to search");
										System.out.println("");
										String searchString=scan.next();
										List<NoteBean> beanList=model.searchNote(searchString, catName);
										Iterator<NoteBean> it=beanList.iterator();
										System.out.println("");
										System.out.println("search result for keyword/title : \""+searchString+"\"");
										System.out.println("");
										while (it.hasNext()) {
											NoteBean bean=it.next();
											if (bean.getType().equals(NoteType.TASK)) {
												System.out.println("Title : "+bean.getTitle());
												System.out.println("Description : "+bean.getDescription());
												System.out.println("Tags : "+bean.getTags());
												System.out.println("Creadted date : "+sdf.format(bean.getCreationDate()));
												System.out.println("Note type : "+bean.getType());
												System.out.println("Planned date : "+sdf.format(bean.getPlannedDate()));
												System.out.println("Task Status : "+bean.getStatus());
												System.out.println("Attachments : "+bean.getAttachment());
												System.out.println("");
											}else
											{
												System.out.println("Title : "+bean.getTitle());
												System.out.println("Description : "+bean.getDescription());
												System.out.println("Tags : "+bean.getTags());
												System.out.println("Creadted date : "+sdf.format(bean.getCreationDate()));
												System.out.println("Note type : "+bean.getType());
												System.out.println("Attachments : "+bean.getAttachment());
												System.out.println("");
											}
										}
									}else {
										System.out.println("");
										System.out.println("\""+catName+"\" catagory is empty...");
										System.out.println();
									}
									break;
									//--------------------------------------------------
								default:
									break;
								}

							}
				break;
				//--------------------------------------------------------

				case 3 : System.out.println("search...");
				System.out.println("");
				System.out.println("enter title/keyword to search");
				System.out.println("");
				String searchString=scan.next();
				List<NoteBean> beanList=model.searchGen(searchString);
				Iterator<NoteBean> it=beanList.iterator();
				System.out.println("");
				System.out.println("search result for keyword/title : \""+searchString+"\"");
				System.out.println("");
				while (it.hasNext()) {
					NoteBean bean=it.next();
					if (bean.getType().equals(NoteType.TASK)) {
						System.out.println("Title : "+bean.getTitle());
						System.out.println("Description : "+bean.getDescription());
						System.out.println("Tags : "+bean.getTags());
						System.out.println("Creadted date : "+sdf.format(bean.getCreationDate()));
						System.out.println("Note type : "+bean.getType());
						System.out.println("Planned date : "+sdf.format(bean.getPlannedDate()));
						System.out.println("Task Status : "+bean.getStatus());
						System.out.println("Attachments : "+bean.getAttachment());
						System.out.println("");
					}else
					{
						System.out.println("Title : "+bean.getTitle());
						System.out.println("Description : "+bean.getDescription());
						System.out.println("Tags : "+bean.getTags());
						System.out.println("Creadted date : "+sdf.format(bean.getCreationDate()));
						System.out.println("Note type : "+bean.getType());
						System.out.println("Attachments : "+bean.getAttachment());
						System.out.println("");
					}
				}

				break;
				//--------------------------------------------------------
				case 4 : System.out.println("list...");
				int choiceSort=0;
				while (choiceSort!=5) {
						System.out.println("choose type listing");
						System.out.println("1. list by \"name\" ");
						System.out.println("2. sort by \"name\" ");
						System.out.println("3. list all \"task\" notes");
						System.out.println("4. list all \"self\" notes");
					}

				break;
				//--------------------------------------------------------
				
				case 5 : System.out.println("export...");
				break;
				//--------------------------------------------------------
				
				case 6 : System.out.println("remainder...");
				String toEmailId = "gslaxmikant@gmail.com";       // it should be gmailId (to whom you want to send)

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
							model.SendMail(fromEmailId, password, toEmailId,subject,content);
							System.exit(1);
							
						}else{
							System.out.println("Checking for the time");
						}
					}
				}, today.getTime(), TimeUnit.SECONDS.toMillis(5)); //TimeUnit.SECONDS.toMillis(5) -->> interval


				break;
				//---------------------------------------------------------
				
				case 7 : System.out.println("send note email...");
						int genCount=1;
						List<NoteBean> searchGen=model.listGen();
						Iterator<NoteBean> itGen=searchGen.iterator();
						System.out.println("enter title of note to send..");
						while (itGen.hasNext()) {
							NoteBean bean=itGen.next();
							System.out.println(genCount+". "+bean.getTitle());
							genCount++;
						}
						System.out.println();
						String titleString=scan.next();
						System.out.println();
						Iterator<NoteBean> contentGen=searchGen.iterator();
						String contentMail=null;
						while (contentGen.hasNext()) {
							NoteBean bean=contentGen.next();
							if (bean.getTitle().equals(titleString)) {
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

							}
						}
						System.out.println("Enter Email to send note");
						String emailId=scan.next();
						System.out.println("");
						System.out.println("Enter Email subject");
						String emailSubject=scanLine.nextLine();
						System.out.println("");
						model.SendMail(fromEmailId, password, emailId, emailSubject, contentMail);
						System.out.println();
				break;
				//--------------------------------------------------
				case 8 : System.out.println("Exiting from menu....");

				break;

				default: System.out.println("option not supported...");
				break;
				}
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}
		finally {
			scan.close();
			scanLine.close();
		}
	}

}
