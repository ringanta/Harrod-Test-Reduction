package org.testreduction.models;

import java.util.ArrayList;
import java.util.List;

public class EssentialTestOperator {
	private int degree;
	private List<String> candidates;

	public EssentialTestOperator(){
		reset();
	}
	
	public void operate(int degree, String candidate){
		if (degree == this.degree){
			candidates.add(candidate);
		} else if (degree > this.degree){
			reset();
			this.degree = degree;
			candidates.add(candidate);
		}
	}
	
	void reset(){
		degree = -1;
		candidates = new ArrayList<String>();
	}
}
