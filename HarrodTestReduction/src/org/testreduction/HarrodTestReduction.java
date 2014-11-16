package org.testreduction;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.testreduction.misc.EssentialTestOperator;
import org.testreduction.misc.MatrixInputParser;
import org.testreduction.models.HarrodStepsOutput;
import org.testreduction.models.HarrodTestsOutput;
import org.testreduction.models.MatrixInput;

public class HarrodTestReduction {
	private MatrixInput input;
	private HarrodTestsOutput testsOutput;
	private HarrodStepsOutput stepsOutput;
	
	public HarrodTestReduction(String filename){
		MatrixInputParser parser = new MatrixInputParser(filename);
		input = parser.parse();

		testsOutput = new HarrodTestsOutput();
		stepsOutput = new HarrodStepsOutput();
	}
	
	public void execute(){
		harrodEssential();
		harrodRest();
		System.out.print(stepsOutput);
		System.out.print(testsOutput);
		writeToFile(stepsOutput, "harrod-steps-output.txt");
		writeToFile(testsOutput, "harrod-tests-output.txt");
	}
	
	private void harrodEssential(){
		Set<String> essentialTests = input.getEssentialTests();
		testsOutput.add(essentialTests);
		stepsOutput.addStep("List of Test(R1): " + setToString(essentialTests));
		Set<String> setR1 = input.getEssentialReqs();
		stepsOutput.addStep("List of R1: " + setToString(setR1));
		stepsOutput.addStep("Selected test case is: " + setToString(essentialTests));
	}
	
	private void harrodRest(){
		List<Integer> indexes = input.getIndexes();
		int pos = 0;
		
		while(pos < indexes.size() && !isAllReqsSatisfied()){
			int index = indexes.get(pos);
			Set<String> unsatisfiedReqs = getUnsatisfiedReqs(index);
			stepsOutput.addStep("List of Test(R" + index + "): " + setToString(input.getTests(index)));
			stepsOutput.addStep("List of unselected Test(R" + index + "): " + setToString(getUnselectedTests(index)));
			stepsOutput.addStep("List of R" + index + ": " + setToString(input.getReqs(index)));
			stepsOutput.addStep("List of unsatisfied reqs in R" + index + ": " + setToString(unsatisfiedReqs));
			while(unsatisfiedReqs.size() > 0){
				String solution = mostSatisfiedTest(pos, unsatisfiedReqs);
				testsOutput.add(solution);
				stepsOutput.addStep("Selected test case is: " + solution);
				
				Set<String> satisfiedReqs = input.getReqs(solution);
				unsatisfiedReqs.removeAll(satisfiedReqs);
				stepsOutput.addStep("Mark reqs: " + setToString(satisfiedReqs));
				stepsOutput.addStep("List of unsatisfied reqs in R" + index + ": " + setToString(unsatisfiedReqs));
			}
			pos++;
		}
	}
	
	private boolean isAllReqsSatisfied(){
		Set<String> selectedTests = testsOutput.getTestsAsSet();
		Set<String> satisfiedReqs = input.getReqs(selectedTests);
		Set<String> allReqs = input.getReqs();
		return allReqs.equals(satisfiedReqs);
	}
	
	private String mostSatisfiedTest(int pos, Set<String> initialReqs){
		String result = "";
		String prevResult = "";
		List<Integer> indexes = input.getIndexes();
		int index = indexes.get(pos);
		Set<String> testCandidates = getUnselectedTests(index);
		EssentialTestOperator operator = new EssentialTestOperator();
		Set<String> currentReqs = initialReqs;
		
		do {
			operator.reset();
			Iterator<String> iterator = testCandidates.iterator();
			stepsOutput.addStep(String.format("Evaluating test candidates: [%s] with unsatisfied reqs in R%d: [%s]", setToString(testCandidates), index, setToString(currentReqs)));
			
			while(iterator.hasNext()){
				String testCandidate = iterator.next();
				Set<String> testReqs = input.getReqs(testCandidate);
				
				testReqs.retainAll(currentReqs);
				int degree = testReqs.size();
				operator.operate(degree, testCandidate);
			}
			
			if (operator.isTie()){
				stepsOutput.addStep(String.format("Tie evaluating R%d with degree: %d and test candidates: [%s]", indexes.get(pos), operator.getDegree(), setToString(operator.getCandidatesAsSet())));
				pos++;
				prevResult = operator.getCandidate();
				
				if  (pos == indexes.size()){
					operator.pickRandom();
					stepsOutput.addStep(String.format("Pick random candidate from %s with degree %d", setToString(operator.getCandidatesAsSet()), operator.getDegree()));
				} else {
					index = indexes.get(pos);
					currentReqs = getUnsatisfiedReqs(index);
					testCandidates = operator.getCandidatesAsSet();
				}
			}
		} while(operator.isTie());
		
		if (operator.isEmpty()){
			result = prevResult;
			stepsOutput.addStep(String.format("Select candidate from previous evalution: %s", result));
		} else {
			result = operator.getCandidate();
			stepsOutput.addStep(String.format("Select candidate from current evalution: %s", result));
		}
		
		return result;
	}
	
	private Set<String> getSatisfiedReqs(){
		Set<String> selectedTests = testsOutput.getTestsAsSet();
		Set<String> satisfiedReqs = input.getReqs(selectedTests);
		return satisfiedReqs;
	}
	
	private Set<String> getUnsatisfiedReqs(int index){
		Set<String> reqCandidates = input.getReqs(index);
		Set<String> satisfiedReqs = getSatisfiedReqs();
		
		reqCandidates.removeAll(satisfiedReqs);
		return reqCandidates;
	}
	
	private Set<String> getUnselectedTests(int index){
		Set<String> testCandidates = input.getTests(index);
		Set<String> selectedTests = testsOutput.getTestsAsSet();
		
		testCandidates.removeAll(selectedTests);
		return testCandidates;
	}
	
	private String setToString(Set<String> coll){
		StringBuffer buf = new StringBuffer();
		Set<String> sortedSet = new TreeSet<String>(coll);
		Iterator<String> iterator = sortedSet.iterator();
		
		while(iterator.hasNext()){
			buf.append(iterator.next()).append(",");
		}
		if (coll.size() > 0){
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}
	
	public static String mapSetToString(Map<String, Set<String>> object){
		StringBuffer buffer = new StringBuffer();
		Set<String> keys = object.keySet();
		Iterator<String> iterator = keys.iterator();
		
		while(iterator.hasNext()){
			String key = iterator.next();
			Set<String> values = object.get(key);
			buffer.append(key).append(" -> ").append("[");
			ArrayList<String> list = new ArrayList<String>(values);
			buffer.append(list.get(0));
			for (int i=1; i<list.size(); i++){
				buffer.append(",").append(list.get(i));
			}
			buffer.append("]\n");
		}
		return buffer.toString();
	}
	

	public static void main(String[] args) {
		String fileName = args[0];
		
		HarrodTestReduction harrod = new HarrodTestReduction(fileName);
		harrod.execute();
	}
	
	public static void writeToFile(Object object, String filename){
		PrintWriter out = null;
		
		try {
			out = new PrintWriter(filename);
			out.write(object.toString());
		} catch (FileNotFoundException fnfe){
			fnfe.printStackTrace();
		} finally {
			if (out != null){
				out.close();
			}
		}
		
	}
	
	public static void sop(Object object){
		System.out.println(object);
	}
}
