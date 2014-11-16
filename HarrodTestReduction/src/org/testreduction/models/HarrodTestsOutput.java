package org.testreduction.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class HarrodTestsOutput {
	private List<String> tests;
	
	public HarrodTestsOutput(){
		tests = new ArrayList<String>();
	}
	
	public void add(String test){
		tests.add(test);
	}
	
	public void add(Set<String> tests){
		this.tests.addAll(tests);
	}
	
	public Set<String> getTestsAsSet(){
		return new HashSet<String>(tests);
	}
	
	public String toString(){
		StringBuffer buf = new StringBuffer();
		Iterator<String> iterator = tests.iterator();
		
		while(iterator.hasNext()){
			buf.append(iterator.next()).append("\n");
		}
		return buf.toString();
	}
}
