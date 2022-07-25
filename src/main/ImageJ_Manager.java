package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ImageJ_Manager {
	
	// input parameters the user can define, to be passed to instance of program
	String paramFile = null;	//filepath
	boolean gui = true;
	boolean printParam = false;
	String functionMode = "1";		// example: 12	=> task 1 and task 2, in this order
	List<String> functionModeList = new ArrayList<String>();
	List<Task> tasks = new ArrayList<Task>();
	
	static ImageJ_Manager thisManager = new ImageJ_Manager();
	

	public static void main (String[] args) {
		
		
		
		if (args != null && args.length != 0) {
			for (int i = 0; i < args.length; i++) {
				if (args[i].toLowerCase().contains("--help") == true) {
					// UPDATE: Generalized into assuming 3 different jobs, with better support for how variable arguements will be handled.
					thisManager.PrintHelpDocs(UtilClass.version);
					return;
				}
			}
			for (int i = 0; i < args.length; i++) {
				if (args[i].toLowerCase().contains("--printparamfile") == true) {
					thisManager.PrintExampleParamFile();
					return;
				}
			}
			for (int i = 0; i < args.length; i++) {
				if (args[i].toLowerCase().contains("--paramfile") == true) {
					try {
						String inputArg = args[i];
						inputArg = inputArg.toLowerCase().replace("--paramfile=", "");
						File inputFile = new File(inputArg);
						Scanner readFile = new Scanner(inputFile);
						List<String> allArgs = new ArrayList<String>();
						while (readFile.hasNextLine()) {
							String argsLine = readFile.nextLine();
							allArgs.add(argsLine);
						}
						thisManager.processArgs(allArgs);
						readFile.close();
					} catch (Exception e) {
						UtilClass.DebugOutput("ERROR: Input parameter '--paramFile' recognized, but issue parsing out value.");
						e.printStackTrace();
						return;
					}
					
				}
			}
			
			//change array of args to list of args
			List<String> allArgs = Arrays.asList(args);
			thisManager.processArgs(allArgs);

			for (int i = 0; i < args.length; i++) {
				if (args[i].toLowerCase().contains("--printparam") == true && args[i].toLowerCase().contains("--printparamfile") == false) {
					thisManager.printParam = true;
				}
			}
		} else {
			UtilClass.DebugOutput("No parameters provided. Will run with defaults.");
			UtilClass.DebugOutput("(Run again with parameter '--help' to print out parameter options and other information.)");
			UtilClass.DebugOutput("(Run again with parameter '--printParam' to print out default or specified parameters for this execution.)");
		}
		
		
		if (thisManager.gui == true) {
			ImageJ_Jobs_GUI guiManager = new ImageJ_Jobs_GUI();
			guiManager.StartGUI();
			return;
		}
		
		UtilClass.DebugOutput("Starting up ImageJ Batch Job Handler (or whatever software we're launching multiple instances of) in 2 seconds.");
		UtilClass.DebugOutput("VERSION: " + UtilClass.version);
		UtilClass.DebugOutput("---");
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		

		

		
		//If this is true, we should have a Task that is task1
		if (thisManager.functionMode.contains("1") == true) {
			Task task01 = thisManager.findTask("01");
			thisManager.executeTask(task01);
		}
			
		if (thisManager.functionMode.contains("2") == true) {
			Task task02 = thisManager.findTask("02");
			thisManager.executeTask(task02);
		}
			
		if (thisManager.functionMode.contains("3") == true) {
			thisManager.CombineCSV_Start();
		}
		
		
		UtilClass.DebugOutput("---");
		UtilClass.DebugOutput("Finished, closed.");
	}
	
	
	public ImageJ_Manager() {
		
	}
	
	public void executeTask(Task task) {
		if (task.taskimages.length()>0 && task.taskimagesDir.length()>0) {
			int taskimagesNum = Integer.parseInt(task.taskimages.replace("|", ""));
			int taskimagesDirNum = Integer.parseInt(task.taskimagesDir.replace("|", ""));
			if (taskimagesNum == -1 && taskimagesDirNum == -1) {
				// assume no image input, just run as 1 job with literal command.
			}
			else if ((task.taskinput[taskimagesNum] == null || task.taskinput[taskimagesNum].length == 0) && (task.taskinput[taskimagesDirNum] != null)) {
				// get input images list, used to determine how many jobs need to be done (assume 1 image per job) 
				File fileDirectory = new File (task.taskinput[taskimagesDirNum][0]);
				if (fileDirectory.isDirectory() == true) {
					File[] f = fileDirectory.listFiles(new FilenameFilter() {
						@Override
						public boolean accept(File dir, String name) {
							boolean returnValue = false;
							if (name.toLowerCase().endsWith(".jpg")
									|| name.toLowerCase().endsWith(".jpeg")
									|| name.toLowerCase().endsWith(".png")
									|| name.toLowerCase().endsWith(".gif")
									|| name.toLowerCase().endsWith(".tif")
									|| name.toLowerCase().endsWith(".tiff")) {
								returnValue = true;
							}
							return returnValue;
						}
					});
					task.taskinput[taskimagesNum] = new String[f.length];
					for (int i = 0; i < f.length; i++) {
						task.taskinput[taskimagesNum][i] = f[i].getName();
					}
				}	
			}
		}
		if (thisManager.printParam == true) {
			String taskString = "task" + task.taskNumber;
			UtilClass.DebugOutput("****These are the recognized input parameters (if not specified by the user, these are the defaults).****");
			UtilClass.DebugOutput("gui = " + thisManager.gui);
			UtilClass.DebugOutput("functionMode = " + thisManager.functionMode);
			UtilClass.DebugOutput(taskString + "maxThreads = " + task.maxThreads);
			UtilClass.DebugOutput(taskString + "timeout = " + task.tasktimeout);
			UtilClass.DebugOutput(taskString + "retryFails = " + task.taskretryFails);
			UtilClass.DebugOutput(taskString + "cmd = " + task.taskcmd);
			UtilClass.DebugOutput(taskString + "imagesDir = " + task.taskimagesDir);
			UtilClass.DebugOutput(taskString + "images = " + task.taskimages);
			for (int a = 0; a < task.taskinput.length; a++) {
				if (task.taskinput[a] != null) {
					UtilClass.DebugOutput(taskString + "input0" + a + " = ");
					for (int b = 0; b < task.taskinput[a].length; b++) {
						UtilClass.DebugOutput(task.taskinput[a][b]);
						if (b < task.taskinput[a].length - 1) {
							UtilClass.DebugOutput(", ");
						}
					}
					UtilClass.DebugOutput("\n");
				}
			}
			UtilClass.DebugOutput("****End of input parameters.****\n\n");
		}
		int taskImages = Integer.parseInt(task.taskimages.replace("|", ""));			//indicating variable 1 - 9, should be a number
		int imagesDir = Integer.parseInt(task.taskimagesDir.replace("|", ""));
		//thisManager.ImageJ_StartJobs();
		if (taskImages == -1 && imagesDir == -1) {
			thisManager.RunGenericTask(task.taskNumber, true);
		} else {
			thisManager.RunGenericTask(task.taskNumber, false);
		}
	}
	
	public Task findTask(String number) {
		for(int i=0; i < tasks.size(); i++) {
			if(tasks.get(i).taskNumber == number) {
				return tasks.get(i);
			}
		}
		UtilClass.DebugOutput("Could not find task with number " + number);
		return new Task();
	}
	
	
	public void processArgs(List<String> args) {
		//First, deal with the variables that don't have to do with tasks
		for (int i=0; i < args.size(); i++) {
			String currentArg = args.get(i);
			if (currentArg.toLowerCase().contains("--gui") == true) {
				gui = Boolean.parseBoolean(thisManager.ImageJ_ReadParameter("--gui=",currentArg,2));
			} else if (currentArg.toLowerCase().contains("--functionmode") == true) {
				functionMode = thisManager.ImageJ_ReadParameter("--functionmode=",currentArg,0);
				functionModeList = Arrays.asList(functionMode.split(""));
			}
		}
		//Next, deal with the tasks- make new tasks for each one in the map, and add it to the list of overall tasks
		Map<String, List<String>> taskMap = thisManager.processTaskParams(args);
		for (String task : taskMap.keySet()) {
			List<String> taskWords = taskMap.get(task);
			Task t = new Task(task, taskWords);
			tasks.add(t);
		}
		
	}
	
	public Map<String, List<String>> processTaskParams(List<String> args) {
		List<String> taskWords = new ArrayList<String>();
		for (int i=0; i < args.size(); i++) {
			if(args.get(i).toLowerCase().contains("--task") == true) {
				taskWords.add(args.get(i));
			}
		}
		Map<String, List<String>> taskMap = new HashMap<String, List<String>>();
		for (int i=0; i < taskWords.size(); i++) {
			String taskWord = taskWords.get(i);
			//this is not a great way to code this, but the task number is index 4-5 of the word
			String taskNumber = taskWord.substring(4,6);
			if(taskMap.containsKey(taskNumber)==true) {
				taskMap.get(taskNumber).add(taskWord);
			}else {
				List<String> words = new ArrayList<String>();
				words.add(taskWord);
				taskMap.put(taskNumber, words);
			}
		}
		return taskMap;
	}
	
	public void PrintHelpDocs(String version) {
		String totalString = "";
		totalString += "**** HELP DOCS ****" + "\n";
		totalString += "This is a program written in Java to batch-run multiple instances of one program" + "\n";
		totalString += "  as they would be run in a command-line interface." + "\n";
		totalString += "This was originally written to run an ImageJ script as a batch job on a compute server." + "\n";
		totalString += "Originally written by Andrew Hlynka at the University of Michigan in 2021." + "\n";
		totalString += "VERSION: " + version + "\n";
		totalString += "---" + "\n";
		totalString += "Parameters:" + "\n";
		totalString += "\t--help \t\t\t: List out documentation / parameter options." + "\n";
		totalString += "\t--paramFile \t\t: Use input .txt file (with full file path or local path) as parameter input." + "\n"; 
		totalString += "\t\t\t\t\tThis input is overrided by any parameters defined in command line alongside --file definition." + "\n";
		totalString += "\t--printParamFile\t: Do not run program, but print out example input .txt file in same directory as this .jar for use." + "\n";
		totalString += "\t--printParam\t\t: Print / display parameter values used for this execution." + "\n";
		totalString += "\t--gui \t\t\t: Open with built-in Java Swing GUI (NOT IMPLEMENTED YET). True by default." + "\n";
		totalString += "\t--functionMode \t\t: Defines which function (or combination of functions) to run." + "\n";
		totalString += "\t\t\t\t\t1 = Run Python job(s) to prepare data (parallel)," + "\n";
		totalString += "\t\t\t\t\t2 = Run ImageJ job(s) to analyze data (parallel)," + "\n";
		totalString += "\t\t\t\t\t3 = Combine and summarize .csv output files," + "\n";
		totalString += "\t\t\t\t\t123 = Example that runs 1, then 2, then 3." + "\n";
		totalString += "" + "\n";
		totalString += "\t--task01description\t: (Optional) Provide user a text dsecription for this task." + "\n";
		totalString += "\t--task01cmd\t\t: The explicit command to run one instance of the program through the command line."+"\n";
		totalString += "\t\t\t\t\tIncludes special notation (||1||, ||2||, etc.) to set variables that can be defined."+"\n";
		totalString += "\t--task01timeout\t\t: Number (integer, >= 0) in milliseconds (1000 = 1 second) that defines how long to wait before forcing a program thread to close."+"\n";
		totalString += "\t--task01maxThreads\t: Number (integer, >= 1) that defines how many parallel threads to run at a time."+"\n";
		totalString += "\t--task01retryFails\t: Number (integer, >= 0). If program instance from batch jobs fails (based on 'timeout' value), launch again 'n' number of times."+"\n";
		totalString += "\t--task01input01\t\t: The corresponding value(s) as defined by ||1|| in task01cmd."+"\n";
		totalString += "\t\t\t\t\tRepeats for task01input01, 02, 03, ..., 09"+"\n";
		totalString += "\t\t\t\t\tFor explicitly defining multiple images, use ',' to separate within one input. Example: 'image01,image02,image03' "+"\n";
		totalString += "\t--task01images\t\t: Which of task01input0X corresponds to file names of input images? (Example: ||2||)"+"\n";
		totalString += "\t\t\t\t\tIf blank, assumes all image files in 'task01imagesDir' are the input."+"\n";
		totalString += "\t\t\t\t\tIf both 'task01images' and 'task01imagesDir' are blank or missing, assume there is no user input for this task that allows for multiple-thread management."+"\n";
		totalString += "\t--task01imagesDir\t: Which of task01input0X corresponds to file directory of input images? (Example: ||2||)"+"\n";
		totalString += ""+"\n";
		totalString += "\tAll parameters that start with 'task01' exist for 'task02' as well."+"\n";
		
		totalString += "---"+"\n";
		totalString += "Examples:"+"\n";
		totalString += "\t(simple example, run just 1 task, no gui)"+"\n";
		totalString += "... --gui=false --functionMode=1 --task01cmd=\"cmd /c echo 'Hello!'\" --task01maxThreads=1 --task01retryFails=1 --task01timeout=10000";
		totalString += "\t(run with just input file, no gui)"+"\n";
		totalString += "... --gui=false --paramfile='./inputParamFileExample.txt'" + "\n";
		totalString += "---"+"\n";
		
		UtilClass.DebugOutput(totalString);
	}
	
	public void PrintExampleParamFile() {
		UtilClass.DebugOutput("Printing example parameter file for this program in same directory (not running program).");
		UtilClass.DebugOutput("Use / edit the input parameter file as needed, use with parameter '--paramFile' in command line execution.");
		UtilClass.DebugOutput("WARNING: THIS FEATURE ISN'T IMPLEMENTED YET.");
		UtilClass.DebugOutput("Finished.");
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
	
	/*String progCommand = "";
	String progDirectory = "";
	int maxConcurrentThreads = 4;
	int timeoutTime = 60000;
	int retryFails = 2;
	String scriptLocation = "";
	String scriptFilename = "";
	String imageLocations = "";
	String[] imageFilenames;*/
	String task01description = "";
	int task01maxThreads = 1;
	int task01timeout = 60000;
	int task01retryFails = 2;
	String task01cmd = "";
	String[][] task01input;
	int task01images = -1;
	int task01imagesDir = -1;
	ImageJ_Thread [] processThreads;
	ExecutorService execService;
	
	String task02description = "";
	int task02maxThreads = 1;
	int task02timeout = 60000;
	int task02retryFails = 2;
	String task02cmd = "";
	String[][] task02input;
	int task02images = -1;
	int task02imagesDir = -1;
	// why not provide an option for the user to specify the entire command to launch ImageJ? Because we want to be able to handle parallel jobs, the user would have to write out every command for every job
	
	//create lists but don't populate yet
	List<String> taskDescriptions = new ArrayList<String>();
	List<Integer> taskMaxThreads = new ArrayList<Integer>();
	List<Integer> taskTimeouts = new ArrayList<Integer>();
	List<Integer> taskRetryFails = new ArrayList<Integer>();
	List<String> taskCommands = new ArrayList<String>();
	List<String[][]> taskInputs = new ArrayList<String[][]>();
	List<Integer> taskImages = new ArrayList<Integer>();
	List<Integer> taskImagesDirs = new ArrayList<Integer>();
	
	public void populateListsFromGUI() {
		//Start work for generalizing tasks
		taskDescriptions = new ArrayList<String>(Arrays.asList(task01description, task02description));
		taskMaxThreads = new ArrayList<Integer>(Arrays.asList(task01maxThreads, task02maxThreads));
		taskTimeouts = new ArrayList<Integer>(Arrays.asList(task01timeout, task02timeout));
		taskRetryFails = new ArrayList<Integer>(Arrays.asList(task01retryFails, task02retryFails));
		taskCommands = new ArrayList<String>(Arrays.asList(task01cmd, task02cmd));
		taskInputs = new ArrayList<String[][]>(Arrays.asList(task01input, task02input));
		taskImages = new ArrayList<Integer>(Arrays.asList(task01images, task02images));
		taskImagesDirs = new ArrayList<Integer>(Arrays.asList(task01imagesDir, task02imagesDir));
	}
	
	long startExecutionTime = 0;
	
	public void modifyCancelRequest(String taskNumber) {
		UtilClass.DebugOutput("CANCEL requested during Task " + taskNumber + ", canceling further executions.");
		cancelRequest = 0;
	}
	
	public void cancelProcessThreads(int taskInputImageLength) {
		for (int i = 0; i < taskInputImageLength; i++) {
			processThreads[i].Cancel();
		}
	}
	
	public int debugSuccessFailOutput(int failedRuns, int taskInputImageLength, boolean[] threadSuccess) {
		for (int i = 0; i < taskInputImageLength; i++)
		{
			UtilClass.DebugOutputNoLine("Thread " + (i+1) + " : ");
			if (threadSuccess[i] == true) {
				UtilClass.DebugOutputNoLine("SUCCESS");
			} else {
				UtilClass.DebugOutputNoLine("FAIL");
				failedRuns++;
			}
			UtilClass.DebugOutputNoLine(", \t");
			if (i % 3 == 2) {
				UtilClass.DebugOutputNoLine("\n");
			}
		}
		return failedRuns;
	}
	
	public int RunGenericTask(String taskNumber, boolean singleThread) {
		
		Task task = thisManager.findTask(taskNumber);
		//get info from GUI
		//populateListsFromGUI();
		
		//Get all the variables relevant to this task
		String taskDescription = task.taskdescription;
		int taskMaxThreadCount = task.maxThreads;
		int taskTimeout = task.tasktimeout;
		int taskRetryFail = task.taskretryFails;
		String taskCmd = task.taskcmd;
		String[][]taskInput = task.taskinput;
		
		int taskImage = Integer.parseInt(task.taskimages.replace("|", ""));			//indicating variable 1 - 9, should be a number
		int taskimagesDir = Integer.parseInt(task.taskimagesDir.replace("|", ""));
		
		int taskInputImageLength = 1;
		if(singleThread == false) {
			taskInputImageLength = taskInput[taskImage].length;
		}
		
		UtilClass.DebugOutput("TASK " + taskNumber + ":");
		
		UtilClass.DebugOutput(taskDescription);
		
		UtilClass.DebugOutput("System OS: " + System.getProperty("os.name"));
		UtilClass.DebugOutput("Total number of system cores: " + Runtime.getRuntime().availableProcessors());
		UtilClass.DebugOutput("Total amount of JVM memory (GB): " + String.format("%.4f",Runtime.getRuntime().totalMemory()*0.001f*0.001f*0.001f));
		
		UtilClass.DebugOutput("Set to run this many concurrent threads: " + taskMaxThreadCount);
		UtilClass.DebugOutput("Defined timeout time (seconds): " + taskTimeout*0.001f);
		
		long startTime = System.nanoTime();
		startExecutionTime = System.currentTimeMillis();
		
		
		
		
		
		boolean[] threadSuccess = new boolean[taskInputImageLength];
		
		execService = Executors.newFixedThreadPool(taskMaxThreadCount);
		processThreads = new ImageJ_Thread[taskInputImageLength];
		
		for (int i = 0; i < taskInputImageLength; i++) {
			processThreads[i] = new ImageJ_Thread();
			processThreads[i].threadIndex = i + 1;
			processThreads[i].threadTotal = taskInputImageLength;
			String sysCommand = taskCmd;
			for (int j = 0; j < 10; j++) {
				if (j != taskImage && taskInput[j] != null) {
					sysCommand = sysCommand.replace("||" + j + "||", taskInput[j][0]);
				}
			}
			if(singleThread == false) {
				sysCommand = sysCommand.replace("||" + taskImage + "||", taskInput[taskImage][i]);
			}
			processThreads[i].sysCommand = sysCommand;
			processThreads[i].milisecondsTimeout = taskTimeout;
		}
		
		if (cancelRequest == 1) {
			modifyCancelRequest(taskNumber);
			return -2;
		}
		
		for (int i = 0; i < taskInputImageLength; i++) {
			execService.execute(processThreads[i]);
		}
		
		UtilClass.DebugOutput("Waiting for threads to finish...");
		UtilClass.DebugOutput("");
		execService.shutdown();
		try {
			if(singleThread) {
				execService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
			}
			if (cancelRequest == 1) {
				modifyCancelRequest(taskNumber);
				cancelProcessThreads(taskInputImageLength);
				return -2;
			}
			execService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		execService.shutdownNow();
		double avgSeconds = 0;
		int avgSuccessThreads = 0;
		double maxSeconds = 0;
		for (int i = 0; i < taskInputImageLength; i++) {
			threadSuccess[i] = !processThreads[i].forcedClosed;
			// (compare time in seconds)
			if (processThreads[i].timeFinished < (taskTimeout*0.001f) - 1) {
				avgSeconds += processThreads[i].timeFinished;
				avgSuccessThreads++;
				
				if (processThreads[i].timeFinished > maxSeconds) {
					maxSeconds = processThreads[i].timeFinished;
				}
			}
		}
		if (avgSuccessThreads > 0) {
			avgSeconds = avgSeconds / avgSuccessThreads;
		} else {
			avgSeconds = -1;
		}
		
		UtilClass.DebugOutput("");
		UtilClass.DebugOutput("All threads finished.");
		double totalSeconds = (System.nanoTime() - startTime)*0.000000001f;
		UtilClass.DebugOutput("Total execution time (seconds): " + String.format("%.0f",totalSeconds) + "   |   (minutes): " + String.format("%.2f",(totalSeconds/60f)));
		UtilClass.DebugOutput("Average execution time for successful threads (seconds): " + String.format("%.0f", avgSeconds));
		UtilClass.DebugOutput("Max execution time for successful threads (seconds): " + String.format("%.0f", maxSeconds));
		UtilClass.DebugOutputNoLine("\n");
		int failedRuns = 0;
		
		failedRuns = debugSuccessFailOutput(failedRuns, taskInputImageLength, threadSuccess);
				
		UtilClass.DebugOutput("\n");
		
		while (taskRetryFail > 0 && failedRuns > 0) {
			UtilClass.DebugOutput("Retry allowed for failed threads. Retries left: " + taskRetryFail);
			taskRetryFail--;
			failedRuns = 0;
			
			if (cancelRequest == 1) {
				modifyCancelRequest(taskNumber);
				return -2;
			}
			
			execService = Executors.newFixedThreadPool(taskMaxThreadCount);
			
			for (int i = 0; i < taskInputImageLength; i++) {
				if (processThreads[i].forcedClosed == true) {
					execService.execute(processThreads[i]);
				}
			}
			
			UtilClass.DebugOutput("Waiting for threads to finish...");
			UtilClass.DebugOutput("");
			execService.shutdown();
			try {
				execService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
				if (cancelRequest == 1) {
					modifyCancelRequest(taskNumber);
					cancelProcessThreads(taskInputImageLength);
					return -2;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			execService.shutdownNow();
			
			for (int i = 0; i < taskInputImageLength; i++) {
				threadSuccess[i] = !processThreads[i].forcedClosed;
			}
			
			UtilClass.DebugOutput("");
			UtilClass.DebugOutput("All threads finished.");
			totalSeconds = (System.nanoTime() - startTime)*0.000000001f;
			UtilClass.DebugOutput("Total execution time (seconds): " + String.format("%.0f",totalSeconds) + "   |   (minutes): " + String.format("%.2f",(totalSeconds/60f)));
			UtilClass.DebugOutputNoLine("\n");
			
			failedRuns = debugSuccessFailOutput(failedRuns, taskInputImageLength, threadSuccess);
			
			UtilClass.DebugOutput("\n");
		}
		
		UtilClass.DebugOutput("Finished Task " + taskNumber + ".");
		UtilClass.DebugOutput("---\n\n");
		
		if (failedRuns > 0)
			return -1;
		else
			return 1;

	}
	
	//public int ImageJ_StartJobs() {
	public int RunTask02() {
		
		UtilClass.DebugOutput("TASK 2:");
		
		UtilClass.DebugOutput(task02description);
		
		UtilClass.DebugOutput("System OS: " + System.getProperty("os.name"));
		UtilClass.DebugOutput("Total number of system cores: " + Runtime.getRuntime().availableProcessors());
		UtilClass.DebugOutput("Total amount of JVM memory (GB): " + String.format("%.4f",Runtime.getRuntime().totalMemory()*0.001f*0.001f*0.001f));
		
		UtilClass.DebugOutput("Set to run this many concurrent threads: " + task02maxThreads);
		UtilClass.DebugOutput("Defined timeout time (seconds): " + task02timeout*0.001f);
		
		long startTime = System.nanoTime();
		startExecutionTime = System.currentTimeMillis();
		
		boolean[] threadSuccess = new boolean[task02input[task02images].length];
		
		execService = Executors.newFixedThreadPool(task02maxThreads);
		processThreads = new ImageJ_Thread[task02input[task02images].length];
		
		for (int i = 0; i < task02input[task02images].length; i++) {
			processThreads[i] = new ImageJ_Thread();
			processThreads[i].threadIndex = i + 1;
			processThreads[i].threadTotal = task02input[task02images].length;
			String sysCommand = task02cmd;
			for (int j = 0; j < 10; j++) {
				if (j != task02images && task02input[j] != null) {
					sysCommand = sysCommand.replace("||" + j + "||", task02input[j][0]);
				}
			}
			sysCommand = sysCommand.replace("||" + task02images + "||", task02input[task02images][i]);
			processThreads[i].sysCommand = sysCommand;
			processThreads[i].milisecondsTimeout = task02timeout;
		}
		
		if (cancelRequest == 1) {
			UtilClass.DebugOutput("CANCEL requested during Task 02, canceling further executions.");
			cancelRequest = 0;
			return -2;
		}
		
		for (int i = 0; i < task02input[task02images].length; i++) {
			execService.execute(processThreads[i]);
		}
		
		UtilClass.DebugOutput("Waiting for threads to finish...");
		UtilClass.DebugOutput("");
		execService.shutdown();
		try {
			if (cancelRequest == 1) {
				UtilClass.DebugOutput("CANCEL requested during Task 02, canceling further executions.");
				cancelRequest = 0;
				for (int i = 0; i < task02input[task02images].length; i++) {
					processThreads[i].Cancel();
				}
				return -2;
			}
			execService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		execService.shutdownNow();
		double avgSeconds = 0;
		int avgSuccessThreads = 0;
		double maxSeconds = 0;
		for (int i = 0; i < task02input[task02images].length; i++) {
			threadSuccess[i] = !processThreads[i].forcedClosed;
			// (compare time in seconds)
			if (processThreads[i].timeFinished < (task02timeout*0.001f) - 1) {
				avgSeconds += processThreads[i].timeFinished;
				avgSuccessThreads++;
				
				if (processThreads[i].timeFinished > maxSeconds) {
					maxSeconds = processThreads[i].timeFinished;
				}
			}
		}
		if (avgSuccessThreads > 0) {
			avgSeconds = avgSeconds / avgSuccessThreads;
		} else {
			avgSeconds = -1;
		}
		
		UtilClass.DebugOutput("");
		UtilClass.DebugOutput("All threads finished.");
		double totalSeconds = (System.nanoTime() - startTime)*0.000000001f;
		UtilClass.DebugOutput("Total execution time (seconds): " + String.format("%.0f",totalSeconds) + "   |   (minutes): " + String.format("%.2f",(totalSeconds/60f)));
		UtilClass.DebugOutput("Average execution time for successful threads (seconds): " + String.format("%.0f", avgSeconds));
		UtilClass.DebugOutput("Max execution time for successful threads (seconds): " + String.format("%.0f", maxSeconds));
		UtilClass.DebugOutputNoLine("\n");
		int failedRuns = 0;
		for (int i = 0; i < task02input[task02images].length; i++)
		{
			UtilClass.DebugOutputNoLine("Thread " + (i+1) + " : ");
			if (threadSuccess[i] == true) {
				UtilClass.DebugOutputNoLine("SUCCESS");
			} else {
				UtilClass.DebugOutputNoLine("FAIL");
				failedRuns++;
			}
			UtilClass.DebugOutputNoLine(", \t");
			if (i % 3 == 2) {
				UtilClass.DebugOutputNoLine("\n");
			}
		}
		UtilClass.DebugOutput("\n");
		
		while (task02retryFails > 0 && failedRuns > 0) {
			UtilClass.DebugOutput("Retry allowed for failed threads. Retries left: " + task02retryFails);
			task02retryFails--;
			failedRuns = 0;
			
			if (cancelRequest == 1) {
				UtilClass.DebugOutput("CANCEL requested during Task 02, canceling further executions.");
				cancelRequest = 0;
				return -2;
			}
			
			execService = Executors.newFixedThreadPool(task02maxThreads);
			
			for (int i = 0; i < task02input[task02images].length; i++) {
				if (processThreads[i].forcedClosed == true) {
					execService.execute(processThreads[i]);
				}
			}
			
			UtilClass.DebugOutput("Waiting for threads to finish...");
			UtilClass.DebugOutput("");
			execService.shutdown();
			try {
				execService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
				if (cancelRequest == 1) {
					UtilClass.DebugOutput("CANCEL requested during Task 02, canceling further executions.");
					cancelRequest = 0;
					for (int i = 0; i < task02input[task02images].length; i++) {
						processThreads[i].Cancel();
					}
					return -2;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			execService.shutdownNow();
			
			for (int i = 0; i < task02input[task02images].length; i++) {
				threadSuccess[i] = !processThreads[i].forcedClosed;
			}
			
			UtilClass.DebugOutput("");
			UtilClass.DebugOutput("All threads finished.");
			totalSeconds = (System.nanoTime() - startTime)*0.000000001f;
			UtilClass.DebugOutput("Total execution time (seconds): " + String.format("%.0f",totalSeconds) + "   |   (minutes): " + String.format("%.2f",(totalSeconds/60f)));
			UtilClass.DebugOutputNoLine("\n");
			for (int i = 0; i < task02input[task02images].length; i++)
			{
				UtilClass.DebugOutputNoLine("Thread " + (i+1) + " : ");
				if (threadSuccess[i] == true) {
					UtilClass.DebugOutputNoLine("SUCCESS");
				} else {
					UtilClass.DebugOutputNoLine("FAIL");
					failedRuns++;
				}
				UtilClass.DebugOutputNoLine(", \t");
				if (i % 3 == 2) {
					UtilClass.DebugOutputNoLine("\n");
				}
			}
			UtilClass.DebugOutput("\n");
		}
		
		UtilClass.DebugOutput("Finished Task 02.");
		UtilClass.DebugOutput("---\n\n");
		
		if (failedRuns > 0)
			return -1;
		else
			return 1;
	}

	//public int ImageJ_StartJobs1Thread() {
	public int RunTask02SingleThread() {
		
		// re-written version of "RunTask02()" to ignore attempt to create multiple threads for multiple image inputs (assuming there is no image input at all)
		
		UtilClass.DebugOutput("TASK 2 (single task, no multiple input sources specified):");
		
		UtilClass.DebugOutput(task02description);
		
		UtilClass.DebugOutput("System OS: " + System.getProperty("os.name"));
		UtilClass.DebugOutput("Total number of system cores: " + Runtime.getRuntime().availableProcessors());
		UtilClass.DebugOutput("Total amount of JVM memory (GB): " + String.format("%.4f",Runtime.getRuntime().totalMemory()*0.001f*0.001f*0.001f));
		
		UtilClass.DebugOutput("Set to run this many concurrent threads: " + task02maxThreads);
		UtilClass.DebugOutput("Defined timeout time (seconds): " + task02timeout*0.001f);
		long startTime = System.nanoTime();
		startExecutionTime = System.currentTimeMillis();
		
		boolean[] threadSuccess = new boolean[1];
		
		execService = Executors.newFixedThreadPool(task02maxThreads);
		processThreads = new ImageJ_Thread[1];
		
		for (int i = 0; i < 1; i++) {
			processThreads[i] = new ImageJ_Thread();
			processThreads[i].threadIndex = i + 1;
			processThreads[i].threadTotal = 1;
			String sysCommand = task02cmd;
			for (int j = 0; j < 10; j++) {
				if (j != task02images && task02input[j] != null) {
					sysCommand = sysCommand.replace("||" + j + "||", task02input[j][0]);
				}
			}
			processThreads[i].sysCommand = sysCommand;
			processThreads[i].milisecondsTimeout = task02timeout;
		}
		
		if (cancelRequest == 1) {
			UtilClass.DebugOutput("CANCEL requested during Task 02, canceling further executions.");
			cancelRequest = 0;
			return -2;
		}
		
		for (int i = 0; i < 1; i++) {
			execService.execute(processThreads[i]);
		}
		
		UtilClass.DebugOutput("Waiting for threads to finish...");
		UtilClass.DebugOutput("");
		execService.shutdown();
		try {
			execService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
			if (cancelRequest == 1) {
				UtilClass.DebugOutput("CANCEL requested during Task 02, canceling further executions.");
				cancelRequest = 0;
				for (int i = 0; i < task02input[task02images].length; i++) {
					processThreads[i].Cancel();
				}
				return -2;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		execService.shutdownNow();
		double avgSeconds = 0;
		int avgSuccessThreads = 0;
		double maxSeconds = 0;
		for (int i = 0; i < 1; i++) {
			threadSuccess[i] = !processThreads[i].forcedClosed;
			// (compare time in seconds)
			if (processThreads[i].timeFinished < (task02timeout*0.001f) - 1) {
				avgSeconds += processThreads[i].timeFinished;
				avgSuccessThreads++;
				
				if (processThreads[i].timeFinished > maxSeconds) {
					maxSeconds = processThreads[i].timeFinished;
				}
			}
		}
		if (avgSuccessThreads > 0) {
			avgSeconds = avgSeconds / avgSuccessThreads;
		} else {
			avgSeconds = -1;
		}
		
		UtilClass.DebugOutput("");
		UtilClass.DebugOutput("All threads finished.");
		double totalSeconds = (System.nanoTime() - startTime)*0.000000001f;
		UtilClass.DebugOutput("Total execution time (seconds): " + String.format("%.0f",totalSeconds) + "   |   (minutes): " + String.format("%.2f",(totalSeconds/60f)));
		UtilClass.DebugOutput("Average execution time for successful threads (seconds): " + String.format("%.0f", avgSeconds));
		UtilClass.DebugOutput("Max execution time for successful threads (seconds): " + String.format("%.0f", maxSeconds));
		UtilClass.DebugOutputNoLine("\n");
		int failedRuns = 0;
		for (int i = 0; i < 1; i++)
		{
			UtilClass.DebugOutputNoLine("Thread " + (i+1) + " : ");
			if (threadSuccess[i] == true) {
				UtilClass.DebugOutputNoLine("SUCCESS");
			} else {
				UtilClass.DebugOutputNoLine("FAIL");
				failedRuns++;
			}
			UtilClass.DebugOutputNoLine(", \t");
			if (i % 3 == 2) {
				UtilClass.DebugOutputNoLine("\n");
			}
		}
		UtilClass.DebugOutput("\n");
		
		while (task02retryFails > 0 && failedRuns > 0) {
			UtilClass.DebugOutput("Retry allowed for failed threads. Retries left: " + task02retryFails);
			task02retryFails--;
			failedRuns = 0;
			
			if (cancelRequest == 1) {
				UtilClass.DebugOutput("CANCEL requested during Task 02, canceling further executions.");
				cancelRequest = 0;
				return -2;
			}
			
			execService = Executors.newFixedThreadPool(task02maxThreads);
			
			for (int i = 0; i < 1; i++) {
				if (processThreads[i].forcedClosed == true) {
					execService.execute(processThreads[i]);
				}
			}
			
			UtilClass.DebugOutput("Waiting for threads to finish...");
			UtilClass.DebugOutput("");
			execService.shutdown();
			try {
				execService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
				if (cancelRequest == 1) {
					UtilClass.DebugOutput("CANCEL requested during Task 02, canceling further executions.");
					cancelRequest = 0;
					for (int i = 0; i < task02input[task02images].length; i++) {
						processThreads[i].Cancel();
					}
					return -2;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			execService.shutdownNow();
			
			for (int i = 0; i < 1; i++) {
				threadSuccess[i] = !processThreads[i].forcedClosed;
			}
			
			UtilClass.DebugOutput("");
			UtilClass.DebugOutput("All threads finished.");
			totalSeconds = (System.nanoTime() - startTime)*0.000000001f;
			UtilClass.DebugOutput("Total execution time (seconds): " + String.format("%.0f",totalSeconds) + "   |   (minutes): " + String.format("%.2f",(totalSeconds/60f)));
			UtilClass.DebugOutputNoLine("\n");
			for (int i = 0; i < 1; i++)
			{
				UtilClass.DebugOutputNoLine("Thread " + (i+1) + " : ");
				if (threadSuccess[i] == true) {
					UtilClass.DebugOutputNoLine("SUCCESS");
				} else {
					UtilClass.DebugOutputNoLine("FAIL");
					failedRuns++;
				}
				UtilClass.DebugOutputNoLine(", \t");
				if (i % 3 == 2) {
					UtilClass.DebugOutputNoLine("\n");
				}
			}
			UtilClass.DebugOutput("\n");
		}
		UtilClass.DebugOutput("---\n\n");
		
		if (failedRuns > 0)
			return -1;
		else
			return 1;
	}
	
	public int CombineCSV_Start() {
		
		UtilClass.DebugOutput("TASK 3:");
		
		String currentDateText = "";
		Calendar calendar = Calendar.getInstance();
		calendar.get(Calendar.YEAR);
		currentDateText = "" + calendar.get(Calendar.YEAR) + "" 
				+ String.format("%02d", calendar.get(Calendar.MONTH)+1) + "" 
				+ String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + "_"
				+ String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + "" 
				+ String.format("%02d", calendar.get(Calendar.MINUTE)) + ""
				+ String.format("%02d", calendar.get(Calendar.SECOND)); 
		
		UtilClass.DebugOutput("Now combining .csv files into one large file called 'summary_" + currentDateText + ".csv'...");
		
		String[] csvFiles = null;
		if (task02input[task02imagesDir][0] != null) {
			File fileDirectory = new File (task02input[task02imagesDir][0]);
			if (fileDirectory.isDirectory() == true) {
				File[] f = fileDirectory.listFiles(new FileFilter() {
					@Override
					public boolean accept(File f) {
						boolean returnValue = false;
						if (f.getName().toLowerCase().endsWith(".csv") && f.lastModified() > startExecutionTime) {
							returnValue = true;
						}
						return returnValue;
					}
				});
				csvFiles = new String[f.length];
				for (int i = 0; i < f.length; i++) {
					//f[i].lastModified();
					csvFiles[i] = f[i].getName();
				}
			}
		}
		
		UtilClass.DebugOutput("This many .csv files found: " + csvFiles.length);
		
		String outputCsv = "";
		for (int i = 0; i < csvFiles.length; i++) {
			File inputFile = new File(task02input[task02imagesDir][0] + csvFiles[i]);
			Scanner readFile;
			try {
				readFile = new Scanner(inputFile);
				String fileLine = readFile.nextLine();
				if (outputCsv.length() <= 1) {
					outputCsv += fileLine + ",," + fileLine + ",," + fileLine + "\n";
				}
				while (readFile.hasNextLine()) {
					fileLine = readFile.nextLine();
					outputCsv += fileLine;
					if (readFile.hasNextLine()) {
						outputCsv += ",,";
					}
					//System.out.println(argsLine);					
				}
				outputCsv += "\n";
				readFile.close();
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
			
		}
		try {
			FileWriter writeCsvFile = new FileWriter(task02input[task02imagesDir][0] + "summary_"+currentDateText+".csv");
			writeCsvFile.write(outputCsv);
			writeCsvFile.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
		
		UtilClass.DebugOutput(".csv file task done.");
		UtilClass.DebugOutput("---\n\n");
		
		return 1;
	}
	
	//public int Initialize_Start() {
	public int RunTask01() {
		
		UtilClass.DebugOutput("TASK 01:");
		
		UtilClass.DebugOutput(task01description);
		
		UtilClass.DebugOutput("System OS: " + System.getProperty("os.name"));
		UtilClass.DebugOutput("Total number of system cores: " + Runtime.getRuntime().availableProcessors());
		UtilClass.DebugOutput("Total amount of JVM memory (GB): " + String.format("%.4f",Runtime.getRuntime().totalMemory()*0.001f*0.001f*0.001f));
		
		UtilClass.DebugOutput("Set to run this many concurrent threads: " + task01maxThreads);
		UtilClass.DebugOutput("Defined timeout time (seconds): " + task01timeout*0.001f);
		
		long startTime = System.nanoTime();
		startExecutionTime = System.currentTimeMillis();
		
		boolean[] threadSuccess = new boolean[task01input[task01images].length];
		
		execService = Executors.newFixedThreadPool(task01maxThreads);
		processThreads = new ImageJ_Thread[task01input[task01images].length];
		
		for (int i = 0; i < task01input[task01images].length; i++) {
			processThreads[i] = new ImageJ_Thread();
			processThreads[i].threadIndex = i + 1;
			processThreads[i].threadTotal = task01input[task01images].length;
			String sysCommand = task01cmd;
			for (int j = 0; j < 10; j++) {
				if (j != task01images && task01input[j] != null) {
					sysCommand = sysCommand.replace("||" + j + "||", task01input[j][0]);
				}
			}
			// python script does not want extension of image (.tif, .png, etc)
			// originally, forcibly removed extension for task 01. Instead, we'll expect user to specify image without extension.
			sysCommand = sysCommand.replace("||" + task01images + "||", task01input[task01images][i]);
			processThreads[i].sysCommand = sysCommand;
			processThreads[i].milisecondsTimeout = task01timeout;
		}
		
		if (cancelRequest == 1) {
			UtilClass.DebugOutput("CANCEL requested during Task 01, canceling further executions.");
			cancelRequest = 0;
			return -2;
		}
		
		for (int i = 0; i < task01input[task01images].length; i++) {
			execService.execute(processThreads[i]);
		}
		
		UtilClass.DebugOutput("Waiting for threads to finish...");
		UtilClass.DebugOutput("");
		execService.shutdown();
		try {
			execService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
			if (cancelRequest == 1) {
				UtilClass.DebugOutput("CANCEL requested during Task 01, canceling further executions.");
				cancelRequest = 0;
				for (int i = 0; i < processThreads.length; i++) {
					processThreads[i].Cancel();
				}
				return -2;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		execService.shutdownNow();
		double avgSeconds = 0;
		int avgSuccessThreads = 0;
		double maxSeconds = 0;
		for (int i = 0; i < task01input[task01images].length; i++) {
			threadSuccess[i] = !processThreads[i].forcedClosed;
			// (compare time in seconds)
			if (processThreads[i].timeFinished < (task01timeout*0.001f) - 1) {
				avgSeconds += processThreads[i].timeFinished;
				avgSuccessThreads++;
				
				if (processThreads[i].timeFinished > maxSeconds) {
					maxSeconds = processThreads[i].timeFinished;
				}
			}
		}
		if (avgSuccessThreads > 0) {
			avgSeconds = avgSeconds / avgSuccessThreads;
		} else {
			avgSeconds = -1;
		}
		
		UtilClass.DebugOutput("");
		UtilClass.DebugOutput("All threads finished.");
		double totalSeconds = (System.nanoTime() - startTime)*0.000000001f;
		UtilClass.DebugOutput("Total execution time (seconds): " + String.format("%.0f",totalSeconds) + "   |   (minutes): " + String.format("%.2f",(totalSeconds/60f)));
		UtilClass.DebugOutput("Average execution time for successful threads (seconds): " + String.format("%.0f", avgSeconds));
		UtilClass.DebugOutput("Max execution time for successful threads (seconds): " + String.format("%.0f", maxSeconds));
		UtilClass.DebugOutputNoLine("\n");
		int failedRuns = 0;
		for (int i = 0; i < task01input[task01images].length; i++)
		{
			UtilClass.DebugOutputNoLine("Thread " + (i+1) + " : ");
			if (threadSuccess[i] == true) {
				UtilClass.DebugOutputNoLine("SUCCESS");
			} else {
				UtilClass.DebugOutputNoLine("FAIL");
				failedRuns++;
			}
			UtilClass.DebugOutputNoLine(", \t");
			if (i % 3 == 2) {
				UtilClass.DebugOutputNoLine("\n");
			}
		}
		UtilClass.DebugOutput("\n");
		
		while (task01retryFails > 0 && failedRuns > 0) {
			UtilClass.DebugOutput("Retry allowed for failed threads. Retries left: " + task01retryFails);
			task01retryFails--;
			failedRuns = 0;
			
			if (cancelRequest == 1) {
				UtilClass.DebugOutput("CANCEL requested during Task 01, canceling further executions.");
				cancelRequest = 0;
				return -2;
			}
			
			execService = Executors.newFixedThreadPool(task01maxThreads);
			
			for (int i = 0; i < task01input[task01images].length; i++) {
				if (processThreads[i].forcedClosed == true) {
					execService.execute(processThreads[i]);
				}
			}
			
			UtilClass.DebugOutput("Waiting for threads to finish...");
			UtilClass.DebugOutput("");
			execService.shutdown();
			try {
				execService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
				if (cancelRequest == 1) {
					UtilClass.DebugOutput("CANCEL requested during Task 01, canceling further executions.");
					cancelRequest = 0;
					for (int i = 0; i < processThreads.length; i++) {
						processThreads[i].Cancel();
					}
					return -2;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			execService.shutdownNow();
			
			for (int i = 0; i < task01input[task01images].length; i++) {
				threadSuccess[i] = !processThreads[i].forcedClosed;
			}
			
			UtilClass.DebugOutput("");
			UtilClass.DebugOutput("All threads finished.");
			totalSeconds = (System.nanoTime() - startTime)*0.000000001f;
			UtilClass.DebugOutput("Total execution time (seconds): " + String.format("%.0f",totalSeconds) + "   |   (minutes): " + String.format("%.2f",(totalSeconds/60f)));
			UtilClass.DebugOutputNoLine("\n");
			for (int i = 0; i < task01input[task01images].length; i++)
			{
				UtilClass.DebugOutputNoLine("Thread " + (i+1) + " : ");
				if (threadSuccess[i] == true) {
					UtilClass.DebugOutputNoLine("SUCCESS");
				} else {
					UtilClass.DebugOutputNoLine("FAIL");
					failedRuns++;
				}
				UtilClass.DebugOutputNoLine(", \t");
				if (i % 3 == 2) {
					UtilClass.DebugOutputNoLine("\n");
				}
			}
			UtilClass.DebugOutput("\n");
		}
		
		UtilClass.DebugOutput("Finished Task 01.");
		UtilClass.DebugOutput("---\n\n");
		
		if (failedRuns > 0)
			return -1;
		else
			return 1;
	}

	public int RunTask01SingleThread() {
		// re-written version of "RunTask01()" to ignore attempt to create multiple threads for multiple image inputs (assuming there is no image input at all)
		
		UtilClass.DebugOutput("TASK 1 (single task, no multiple input sources specified):");
				
		UtilClass.DebugOutput(task01description);
				
		UtilClass.DebugOutput("System OS: " + System.getProperty("os.name"));
		UtilClass.DebugOutput("Total number of system cores: " + Runtime.getRuntime().availableProcessors());
		UtilClass.DebugOutput("Total amount of JVM memory (GB): " + String.format("%.4f",Runtime.getRuntime().totalMemory()*0.001f*0.001f*0.001f));
				
		UtilClass.DebugOutput("Set to run this many concurrent threads: " + task01maxThreads);
		UtilClass.DebugOutput("Defined timeout time (seconds): " + task01timeout*0.001f);
		long startTime = System.nanoTime();
		startExecutionTime = System.currentTimeMillis();
				
		boolean[] threadSuccess = new boolean[1];
				
		execService = Executors.newFixedThreadPool(task01maxThreads);
		processThreads = new ImageJ_Thread[1];
				
		for (int i = 0; i < 1; i++) {	//set taskInputImageLength to 1 and use same variable
			processThreads[i] = new ImageJ_Thread();
			processThreads[i].threadIndex = i + 1;
			processThreads[i].threadTotal = 1;	//taskInputImageLength
			String sysCommand = task01cmd;
			for (int j = 0; j < 10; j++) {
				if (j != task01images && task01input[j] != null) {
					sysCommand = sysCommand.replace("||" + j + "||", task01input[j][0]);
				}
			}
			processThreads[i].sysCommand = sysCommand;
			processThreads[i].milisecondsTimeout = task01timeout;
		}
				
		if (cancelRequest == 1) {
			UtilClass.DebugOutput("CANCEL requested during Task 01, canceling further executions.");
			cancelRequest = 0;
			return -2;
		}
		
		for (int i = 0; i < 1; i++) {
			execService.execute(processThreads[i]);
		}
				
		UtilClass.DebugOutput("Waiting for threads to finish...");
		UtilClass.DebugOutput("");
		execService.shutdown();
		try {
			execService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
			if (cancelRequest == 1) {
				UtilClass.DebugOutput("CANCEL requested during Task 01, canceling further executions.");
				cancelRequest = 0;
				for (int i = 0; i < processThreads.length; i++) {
					processThreads[i].Cancel();
				}
				return -2;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		execService.shutdownNow();
		double avgSeconds = 0;
		int avgSuccessThreads = 0;
		double maxSeconds = 0;
		for (int i = 0; i < 1; i++) {
			threadSuccess[i] = !processThreads[i].forcedClosed;
			// (compare time in seconds)
			if (processThreads[i].timeFinished < (task01timeout*0.001f) - 1) {
				avgSeconds += processThreads[i].timeFinished;
				avgSuccessThreads++;
						
				if (processThreads[i].timeFinished > maxSeconds) {
					maxSeconds = processThreads[i].timeFinished;
				}
			}
		}
		if (avgSuccessThreads > 0) {
			avgSeconds = avgSeconds / avgSuccessThreads;
		} else {
			avgSeconds = -1;
		}
				
		UtilClass.DebugOutput("");
		UtilClass.DebugOutput("All threads finished.");
		double totalSeconds = (System.nanoTime() - startTime)*0.000000001f;
		UtilClass.DebugOutput("Total execution time (seconds): " + String.format("%.0f",totalSeconds) + "   |   (minutes): " + String.format("%.2f",(totalSeconds/60f)));
		UtilClass.DebugOutput("Average execution time for successful threads (seconds): " + String.format("%.0f", avgSeconds));
		UtilClass.DebugOutput("Max execution time for successful threads (seconds): " + String.format("%.0f", maxSeconds));
		UtilClass.DebugOutputNoLine("\n");
		int failedRuns = 0;
		for (int i = 0; i < 1; i++)
		{
			UtilClass.DebugOutputNoLine("Thread " + (i+1) + " : ");
			if (threadSuccess[i] == true) {
				UtilClass.DebugOutputNoLine("SUCCESS");
			} else {
				UtilClass.DebugOutputNoLine("FAIL");
				failedRuns++;
			}
			UtilClass.DebugOutputNoLine(", \t");
			if (i % 3 == 2) {
				UtilClass.DebugOutputNoLine("\n");
			}
		}
		UtilClass.DebugOutput("\n");
				
		while (task01retryFails > 0 && failedRuns > 0) {
			UtilClass.DebugOutput("Retry allowed for failed threads. Retries left: " + task01retryFails);
			task01retryFails--;
			failedRuns = 0;
			
			if (cancelRequest == 1) {
				UtilClass.DebugOutput("CANCEL requested during Task 01, canceling further executions.");
				cancelRequest = 0;
				return -2;
			}
				
			execService = Executors.newFixedThreadPool(task01maxThreads);
					
			for (int i = 0; i < 1; i++) {
				if (processThreads[i].forcedClosed == true) {
					execService.execute(processThreads[i]);
				}
			}
					
			UtilClass.DebugOutput("Waiting for threads to finish...");
			UtilClass.DebugOutput("");
			execService.shutdown();
			try {
				execService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
				if (cancelRequest == 1) {
					UtilClass.DebugOutput("CANCEL requested during Task 01, canceling further executions.");
					cancelRequest = 0;
					for (int i = 0; i < processThreads.length; i++) {
						processThreads[i].Cancel();
					}
					return -2;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			execService.shutdownNow();
					
			for (int i = 0; i < 1; i++) {
				threadSuccess[i] = !processThreads[i].forcedClosed;
			}
					
			UtilClass.DebugOutput("");
			UtilClass.DebugOutput("All threads finished.");
			totalSeconds = (System.nanoTime() - startTime)*0.000000001f;
			UtilClass.DebugOutput("Total execution time (seconds): " + String.format("%.0f",totalSeconds) + "   |   (minutes): " + String.format("%.2f",(totalSeconds/60f)));
			UtilClass.DebugOutputNoLine("\n");
			for (int i = 0; i < 1; i++)
			{
				UtilClass.DebugOutputNoLine("Thread " + (i+1) + " : ");
				if (threadSuccess[i] == true) {
					UtilClass.DebugOutputNoLine("SUCCESS");
				} else {
					UtilClass.DebugOutputNoLine("FAIL");
					failedRuns++;
				}
				UtilClass.DebugOutputNoLine(", \t");
				if (i % 3 == 2) {
					UtilClass.DebugOutputNoLine("\n");
				}
			}
			UtilClass.DebugOutput("\n");
		}
		UtilClass.DebugOutput("---\n\n");
				
		if (failedRuns > 0)
			return -1;
		else
			return 1;		
	}

	int cancelRequest = 0;
	
	public int CancelTasks() {
		cancelRequest = 1;
		
		if (processThreads != null) {
			UtilClass.DebugOutput("Requested to cancel tasks, canceling this many scheduled tasks: " + processThreads.length);
		
			for (int i = 0; i < processThreads.length; i++) {
				processThreads[i].Cancel();
			}
		}
		if (execService != null) {
			execService.shutdownNow();
		}
		
		return 0;
	}
}
