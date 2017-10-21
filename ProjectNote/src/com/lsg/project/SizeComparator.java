package com.lsg.project;

import java.util.Comparator;
import java.util.Map;

public class SizeComparator implements Comparator<String>{
	Map<String, Double> map;
	
	public SizeComparator(Map<String,Double> map) {
		this.map=map;
	}
	
	@Override
	public int compare(String o1, String o2) {
		if (map.get(o1)>map.get(o2)) 
		return 1;
		if (map.get(o1)<map.get(o2)) 
			return -1;
		return 0;
	}

}
