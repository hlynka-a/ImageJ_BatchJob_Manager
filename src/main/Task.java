package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	
	
	public Task() {
		
	}
	
	public void readTaskArgs(String taskNumber, List<String> taskWords) {
		this.taskNumber = taskNumber;
		for (int i = 0; i < taskWords.size(); i++) {
			String currentWord = taskWords.get(i);
			String removeWord = currentWord.split("=")[0] + "=";
			removeWord = removeWord.toLowerCase();
			if (currentWord.toLowerCase().contains("description") == true) {	
				taskdescription = Utilities.ImageJ_ReadParameter(removeWord,currentWord,0); 
			} else if (currentWord.toLowerCase().contains("maxthreads") == true) {
				maxThreads = Integer.parseInt(Utilities.ImageJ_ReadParameter(removeWord,currentWord,1));
			} else if (currentWord.toLowerCase().contains("timeout") == true) {
				tasktimeout = Integer.parseInt(Utilities.ImageJ_ReadParameter(removeWord,currentWord,1));
			} else if (currentWord.toLowerCase().contains("retryfails") == true) {
				taskretryFails = Integer.parseInt(Utilities.ImageJ_ReadParameter(removeWord,currentWord,1));
			} else if (currentWord.toLowerCase().contains("cmd") == true) {
				taskcmd = Utilities.ImageJ_ReadParameter(removeWord,currentWord,0);
			} else if (currentWord.toLowerCase().contains("input") == true) {
				for (int j=1; j < 10; j++) {
					String numString = "0"+j;
					String currentInputWord = "--task" + taskNumber + "input" + numString;
					if(currentWord.toLowerCase().contains(currentInputWord) == true) {
						taskinput[j] = Utilities.ImageJ_ReadParameterArray(removeWord, currentWord);
					}
				}
			} else if (currentWord.toLowerCase().contains("imagesdir") == true) {
				taskimagesDir = Utilities.ImageJ_ReadParameter(removeWord,currentWord,0);
			} else if (currentWord.toLowerCase().contains("images") == true) {
				UtilClass.DebugOutput("Task images here:" + currentWord);
				taskimages = Utilities.ImageJ_ReadParameter(removeWord,currentWord,0);
			}
		}
	}
	
	public Task(String taskNumber, List<String> taskWords) {
		readTaskArgs(taskNumber, taskWords);
	}
	
	
}

