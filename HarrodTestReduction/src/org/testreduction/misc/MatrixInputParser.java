package org.testreduction.misc;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testreduction.models.MatrixInput;

public class MatrixInputParser {
	private String fileName;
	
	public MatrixInputParser(String fileName) {
		this.fileName = fileName;
	}
	
	public MatrixInput parse(){
		Map<String, List<String>> tests = parseTestsToReqs();
		Map<String, List<String>> reqs = parseReqsToTest();
		MatrixInput result = new MatrixInput(reqs, tests);
		return result;
	}
	
	public Map<String, List<String>> parseTestsToReqs(){
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		BufferedReader in;
		String line;
		String key;
		ArrayList<String> values;
		
		try {
			in = new BufferedReader(new FileReader(fileName));
			
			while((line = in.readLine()) != null){
				if (!line.trim().equals("")){
					key = parseTestCase(line);
					values = parseReqs(line);
					result.put(key, values);
				}
			}
			
			in.close();
		} catch (FileNotFoundException e){
			
		} catch (IOException ioErr){ }
		
		return result;
	}
	
	public Map<String, List<String>> parseReqsToTest(){
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		BufferedReader in = null;
		String line;
		String testCase;
		ArrayList<String> reqs;
		
		try {
			in = new BufferedReader(new FileReader(fileName));
			
			while((line = in.readLine()) != null){
				if (!line.trim().equals("")){
					testCase = parseTestCase(line);
					reqs = parseReqs(line);
					Iterator<String> iterator = reqs.iterator();
					while(iterator.hasNext()){
						addValue(result, iterator.next(), testCase);
					}
				}
			}
			
			in.close();
		} catch (FileNotFoundException e){
			
		} catch (IOException ioErr){
			
		} finally {
			try {
				if (in != null){
					in.close();
				}
			} catch (IOException e) { }
		}
		
		return result;
	}
	
	private static String parseTestCase(String line){
		String testCase = line.split(":")[0];
		return testCase.trim();
	}
	
	private static ArrayList<String> parseReqs(String line){
		String reqsPart = line.split(":")[1];
		String[] reqs = reqsPart.split(",");
		
		ArrayList<String> result = new ArrayList<String>();
		for (int i=0; i<reqs.length; i++){
			result.add(reqs[i].trim());
		}
		return result;
	}
	
	private static void addValue(Map<String, List<String>> map, String key, String value){
		List<String> values;
		
		if(map.containsKey(key)){
			values = map.get(key);
			values.add(value);
		} else {
			values = new ArrayList<String>();
			values.add(value);
			map.put(key, values);
		}
	}

}
