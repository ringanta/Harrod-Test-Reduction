package org.testreduction.misc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class EssentialTestOperator {
	private int degree;
	private List<String> candidates;

	public EssentialTestOperator(){
		reset();
	}
	
	public void operate(int degree, String candidate){
		if (degree > 0){
			if (degree == this.degree){
				candidates.add(candidate);
			} else if (degree > this.degree){
				reset();
				this.degree = degree;
				candidates.add(candidate);
			}
		}
	}
	
	public boolean isTie(){
		return candidates.size() > 1;
	}
	
	public boolean isEmpty(){
		return degree == 0;
	}
	
	public Set<String> getCandidatesAsSet(){
		return new HashSet<String>(candidates);
	}
	
	public String getCandidate(){
		Random random = new Random();
		int limit = candidates.size();
		int pos = random.nextInt(limit);
		return candidates.get(pos);
	}
	
	public int getDegree(){
		return degree;
	}
	
	public void pickRandom(){
		String candidate = candidates.get(0);
		candidates = new ArrayList<String>();
		candidates.add(candidate);
	}
	
	public void reset(){
		degree = 0;
		candidates = new ArrayList<String>();
	}
	
	public String toString(){
		StringBuffer buf = new StringBuffer();
		buf.append("degree: ").append(degree);
		
		Iterator<String> iterator = candidates.iterator();
		buf.append("; candidates: ");
		while(iterator.hasNext()){
			String candidate = iterator.next();
			buf.append(candidate).append(",");
		}
		return buf.toString();
	}
}
