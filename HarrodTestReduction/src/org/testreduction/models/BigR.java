package org.testreduction.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class BigR {
	private Map<Integer, Set<String>> bigR;
	private Set<String> essential;
	
	public BigR(Map<String, Set<String>> reqs){
		bigR = new TreeMap<Integer, Set<String>>();
		essential = new HashSet<String>();
		
		Set<String> keys = reqs.keySet();
		Iterator<String> iterator = keys.iterator();
		
		while(iterator.hasNext()){
			String key = iterator.next();
			
			int size = reqs.get(key).size();
			if (size == 1){
				essential.add(key);
			} else {
				Set<String> values = bigR.get(size);
				if (values == null){
					HashSet<String> temp = new HashSet<String>();
					temp.add(key);
					bigR.put(size, temp);
				} else {
					values.add(key);
				}
			}
		}
	}
	
	public Set<String> getEssentialReqs(){
		return essential;
	}
	
	public Set<String> get(int index){
		return bigR.get(index);
	}
	
	public List<Integer> getIndexes(){
		return new ArrayList<Integer>(bigR.keySet());
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		Set<Integer> keys = bigR.keySet();
		Iterator<Integer> iterator = keys.iterator();
		
		while(iterator.hasNext()){
			Integer key = iterator.next();
			Set<String> value = bigR.get(key);
			buffer.append(key).append(" -> ").append("[");
			ArrayList<String> list = new ArrayList<String>(value);
			buffer.append(list.get(0));
			for (int i=1; i<list.size(); i++){
				buffer.append(",").append(list.get(i));
			}
			buffer.append("]\n");
		}
		return buffer.toString();
	}
	
}
