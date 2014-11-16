package org.testreduction.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HarrodStepsOutput {
	private List<String> lines;
	private int number;
	
	public HarrodStepsOutput(){
		lines = new ArrayList<String>();
		number = 0;
	}
	
	public void addStep(String desc){
		StringBuffer buf = new StringBuffer();
		
		number++;
		buf.append("Step-").append(number).append(": ");
		buf.append(desc);
	}
	
	public String toString(){
		StringBuffer buf = new StringBuffer();
		Iterator<String> iterator = lines.iterator();
		
		while(iterator.hasNext()){
			buf.append(iterator.next()).append("\n");
		}
		return buf.toString();
	}
}
