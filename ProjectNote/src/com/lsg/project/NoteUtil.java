package com.lsg.project;

public class NoteUtil {

	public static boolean validateID(String name) {
		if (name==null) 
			return false;
		if (name.equals("")) 
			return false;
		if (name.equals(" ")) 
			return false;
		if (!Character.isLetter(name.charAt(0))) 
			return false;
		return true;
	}
	
	
}
