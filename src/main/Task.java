package main;

import java.util.List;

public class Task{
	String taskNumber = "-1";
	int maxThreads = 4;
	int tasktimeout = 60000;
	int taskretryFails = 2;
	String taskcmd = "";
	String [][] taskinput = new String[10][];
	String taskimagesDir = "-1";
	String taskimages = "-1";
	String taskdescription = "(Task" + taskNumber + ", written to do stuff.)";
	
	
	public void readTaskArgs(String taskNumber, List<String> taskWords) {
		this.taskNumber = taskNumber;
		for (int i = 0; i < taskWords.size(); i++) {
			String currentWord = taskWords.get(i);
			String removeWord = currentWord.split("=")[0] + "=";
			removeWord = removeWord.toLowerCase();
			if (currentWord.toLowerCase().contains("description") == true) {	
				taskdescription = this.ImageJ_ReadParameter(removeWord,currentWord,0); 
			} else if (currentWord.toLowerCase().contains("maxthreads") == true) {
				maxThreads = Integer.parseInt(this.ImageJ_ReadParameter(removeWord,currentWord,1));
			} else if (currentWord.toLowerCase().contains("timeout") == true) {
				tasktimeout = Integer.parseInt(this.ImageJ_ReadParameter(removeWord,currentWord,1));
			} else if (currentWord.toLowerCase().contains("retryfails") == true) {
				taskretryFails = Integer.parseInt(this.ImageJ_ReadParameter(removeWord,currentWord,1));
			} else if (currentWord.toLowerCase().contains("cmd") == true) {
				taskcmd = this.ImageJ_ReadParameter(removeWord,currentWord,0);
			} else if (currentWord.toLowerCase().contains("input") == true) {
				for (int j=1; j < 10; j++) {
					String numString = "0"+j;
					String currentInputWord = "--task" + taskNumber + "input" + numString;
					if(currentWord.toLowerCase().contains(currentInputWord) == true) {
						taskinput[j] = this.ImageJ_ReadParameterArray(removeWord, currentWord);
					}
				}
			} else if (currentWord.toLowerCase().contains("--taskimagesDir") == true) {
				taskimagesDir = this.ImageJ_ReadParameter(removeWord,currentWord,0);
			} else if (currentWord.toLowerCase().contains("--taskimages") == true) {
				UtilClass.DebugOutput("Task images here:" + currentWord);
				taskimages = this.ImageJ_ReadParameter(removeWord,currentWord,0);
			}
		}
	}
	public Task() {
		
	}
	
	public Task(String taskNumber, List<String> taskWords) {
		readTaskArgs(taskNumber, taskWords);
	}
	
	public String ImageJ_ReadParameter(String removeString, String inputArg, int type) {
		// type [0 = string, 1 = int, 2 = boolean]
		String returnValue = "";
		//System.out.println(removeString);
		try {
			returnValue = inputArg.toLowerCase().replace(removeString, "");
			if (type == 0) {
				
			} else if (type == 1) {
				//System.out.println(removeString + " ___ " + inputArg);
				//System.out.println(">>>>" + returnValue);
				int testInt = Integer.parseInt(returnValue);
			} else if (type == 2) {
				boolean testBool = Boolean.parseBoolean(returnValue);
			}
		} catch (Exception e) {
			UtilClass.DebugOutput("ERROR: Input parameter " + removeString + " recognized, but issue parsing out value.");
			e.printStackTrace();
		}
		UtilClass.DebugOutput(">>>> read parameter -> " + removeString + " = " + returnValue);
		return returnValue;
	}
	
	public String[] ImageJ_ReadParameterArray(String removeString, String inputArg) {
		String [] returnValue = null;
		try {
			inputArg = inputArg.toLowerCase().replace(removeString, "");
			returnValue = inputArg.split(",");
		} catch (Exception e) {
			UtilClass.DebugOutput("ERROR: Input parameter ' " + removeString + " ' recognized, but issue parsing out value.");
			e.printStackTrace();
		}
		UtilClass.DebugOutput(">>>> read parameter -> " + removeString + " = ");
		for(int i=0; i < returnValue.length; i++) {
			UtilClass.DebugOutput("\t" + returnValue[i]);
		}
		return returnValue;
	}
}

