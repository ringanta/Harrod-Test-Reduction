package org.testreduction.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MatrixInput {
	private Map<String, Set<String>> requirements;
	private Map<String, Set<String>> tests;
	private BigR bigR;
	
	public MatrixInput(Map<String, List<String>> reqs, Map<String, List<String>> tests){
		this.requirements = listToSet(reqs);
		this.tests = listToSet(tests);
		bigR = new BigR(requirements);
	}
	
	public Map<String, Set<String>> getRequirements(){
		return requirements;
	}
	
	public Map<String, Set<String>> getTests(){
		return tests;
	}
	
	public BigR getBigR(){
		return bigR;
	}
	
	public List<Integer> getIndexes(){
		return bigR.getIndexes();
	}
	
	public Set<String> getEssentialTests(){
		Set<String> result = new HashSet<String>();
		Set<String> reqs = bigR.getEssentialReqs();
		
		if (reqs != null){
			result = getTests(reqs);
		}
		return result;
	}
	
	public Set<String> getTests(int index){
		Set<String> result = new HashSet<String>();
		Set<String> reqs = getReqs(index);
		
		if (reqs != null){
			result = getTests(reqs);
		}
		return result;
	}
	
	public Set<String> getTests(Set<String> reqs){
		Set<String> result = new HashSet<String>();
		
		Iterator<String> iterator = reqs.iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			Set<String> tests = requirements.get(key);
			result.addAll(tests);
		}
		return result;
	}
	
	public Set<String> getReqs(){
		Set<String> result = new HashSet<String>(requirements.keySet());
		return result;
	}
	
	public Set<String> getReqs(int index){
		Set<String> result = new HashSet<String>();
		Set<String> reqs = bigR.get(index);
		if (reqs != null){
			result.addAll(reqs);
		}
		return result;
	}
	
	public Set<String> getReqs(String test){
		Set<String> result = new HashSet<String>(tests.get(test));
		return result;
	}
	
	public Set<String> getReqs(Set<String> tests){
		Set<String> result = new HashSet<String>();
		
		Iterator<String> iterator = tests.iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			Set<String> reqs = this.tests.get(key);
			if (reqs != null){
				result.addAll(reqs);
			}
		}
		return result;
	}
	
	private static Map<String, Set<String>> listToSet(Map<String, List<String>> in){
		Map<String, Set<String>> result = new HashMap<String, Set<String>>();
		Set<String> keys = in.keySet();
		Iterator<String> iterator = keys.iterator();
		
		while(iterator.hasNext()){
			String key = iterator.next();
			List<String> temp = in.get(key);
			result.put(key, new HashSet<String>(temp));
		}
		return result;
	}
}
