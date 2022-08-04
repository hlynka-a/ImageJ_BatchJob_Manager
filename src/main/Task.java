package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Task{
	String taskNumber = "-1";
	IntegerProperty maxThreads;
	IntegerProperty tasktimeout;
	IntegerProperty taskretryFails;
	StringProperty taskcmd;
	StringProperty[][] taskinput = new StringProperty[10][]; //StringProperty[what task input it is][arguments for input]
	StringProperty taskimagesDir;
	StringProperty taskimages;
	StringProperty taskdescription;
	
	public Task() {
		
	}
	
	public Task(String taskNumber) {
		this.taskNumber = taskNumber;
		this.maxThreads = new SimpleIntegerProperty(1);
		this.tasktimeout = new SimpleIntegerProperty(60000);
		this.taskretryFails = new SimpleIntegerProperty(4);
		this.taskcmd = new SimpleStringProperty("some cmd");
		this.taskimagesDir = new SimpleStringProperty("-1");
		this.taskimages = new SimpleStringProperty("-1");
		this.taskdescription = new SimpleStringProperty("task created to do something");
	}
	

	public String getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(String taskNumber) {
		this.taskNumber = taskNumber;
	}

	public IntegerProperty maxThreadsProperty() {
		return maxThreads;
	}
	
	public int getMaxThreads() {
		return maxThreads.get();
	}

	public void setMaxThreads(int maxThreads) {
		this.maxThreads.set(maxThreads);
	}

	public IntegerProperty taskTimeoutProperty() {
		return tasktimeout;
	}

	public int getTaskTimeout() {
		return tasktimeout.get();
	}


	public void setTasktimeout(int tasktimeout) {
		this.tasktimeout.set(tasktimeout);
	}

	public IntegerProperty taskRetryFailsProperty() {
		return taskretryFails;
	}

	public int getTaskRetryFails() {
		return taskretryFails.get();
	}

	public void setTaskretryFails(int taskretryFails) {
		this.taskretryFails.set(taskretryFails);
	}

	public StringProperty taskCmdProperty() {
		return taskcmd;
	}

	public String getTaskCmd() {
		return taskcmd.get();
	}

	public void setTaskcmd(String taskcmd) {
		this.taskcmd.set(taskcmd);
	}

	public StringProperty[][] getTaskinput() {
		return taskinput;
	}

	public void setTaskinput(StringProperty[][] taskinput) {
		this.taskinput = taskinput;
	}

	public void setTaskinputProperty(StringProperty[][] taskinput) {
		this.taskinput = taskinput;
	}

	public StringProperty taskImagesDirProperty() {
		return taskimagesDir;
	}

	public String getTaskImagesDir() {
		return taskimagesDir.get();
	}

	public void setTaskimagesDir(String taskimagesDir) {
		this.taskimagesDir.set(taskimagesDir);
	}

	public StringProperty taskImagesProperty() {
		return taskimages;
	}

	public String getTaskImages() {
		return taskimages.get();
	}

	public void setTaskimages(String taskimages) {
		this.taskimages.set(taskimages);
	}

	public StringProperty taskDescriptionProperty() {
		return taskdescription;
	}

	public String getTaskDescription() {
		return taskdescription.get();
	}

	public void setTaskdescription(String taskdescription) {
		this.taskdescription.set(taskdescription);
	}
	
	public String[] getTaskInput(int index) {
		StringProperty[] test = this.taskinput[index];
		if(test == null) {
			String[] newArray = new String[1];
			return newArray;
		}
		int newLength = test.length;
		String[] newArray = new String[newLength];
		for(int i=0; i < newLength; i++) {
			StringProperty t = test[i];
			newArray[i] = t.get();
		}
		return newArray;
	}




	public void readTaskArgs(String taskNumber, List<String> taskWords) {
		this.taskNumber = taskNumber;
		for (int i = 0; i < taskWords.size(); i++) {
			String currentWord = taskWords.get(i);
			String removeWord = currentWord.split("=")[0] + "=";
			removeWord = removeWord.toLowerCase();
			if (currentWord.toLowerCase().contains("description") == true) {	
				this.taskdescription.set(UtilClass.ImageJ_ReadParameter(removeWord, removeWord, 0));
			} else if (currentWord.toLowerCase().contains("maxthreads") == true) {
				this.maxThreads.set(Integer.parseInt(UtilClass.ImageJ_ReadParameter(removeWord, removeWord, 1)));
			} else if (currentWord.toLowerCase().contains("timeout") == true) {
				this.tasktimeout.set(Integer.parseInt(UtilClass.ImageJ_ReadParameter(removeWord, removeWord, 1)));
			} else if (currentWord.toLowerCase().contains("retryfails") == true) {
				this.taskretryFails.set(Integer.parseInt(UtilClass.ImageJ_ReadParameter(removeWord, removeWord, 1)));
			} else if (currentWord.toLowerCase().contains("cmd") == true) {
				this.taskcmd.set(UtilClass.ImageJ_ReadParameter(removeWord, removeWord, 0));
			} else if (currentWord.toLowerCase().contains("input") == true) {
				for (int j=1; j < 10; j++) {
					String numString = "0"+j;
					String currentInputWord = "--task" + taskNumber + "input" + numString;
					if(currentWord.toLowerCase().contains(currentInputWord) == true) {
						String[] newArray = UtilClass.ImageJ_ReadParameterArray(removeWord, currentWord);
						StringProperty[] propertyArray = new StringProperty[newArray.length];
						for(int k=0; k < newArray.length; k++) {
							propertyArray[k].set(newArray[k]);
						}
						this.taskinput[j] = propertyArray;
					}
				}
			} else if (currentWord.toLowerCase().contains("imagesdir") == true) {
				this.taskimagesDir.set(UtilClass.ImageJ_ReadParameter(removeWord,currentWord,0));
			} else if (currentWord.toLowerCase().contains("images") == true) {
				UtilClass.DebugOutput("Task images here:" + currentWord);
				this.taskimages.set(UtilClass.ImageJ_ReadParameter(removeWord,currentWord,0));
			}
		}
	}
	
	public Task(String taskNumber, List<String> taskWords) {
		readTaskArgs(taskNumber, taskWords);
	}
	
	public String getTaskDataAsString(String[] taskArray) {
		String totalString = "";
		for (int i = 1; i < taskArray.length; i++) {
			totalString +=taskArray[i];
	    	if (i < taskArray.length - 1) {
	    		totalString += ",";
	    	}
		}
		return totalString;
	}
	
	
}

