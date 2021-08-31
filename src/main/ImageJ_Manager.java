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
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ImageJ_Manager {

	public static void main (String[] args) {
		
		// input parameters the user can define, to be passed to instance of program
		String paramFile = null;	//filepath
		boolean gui = true;
		boolean printParam = false;
		String functionMode = "1";		// example: 12	=> task 1 and task 2, in this order

		int task01maxThreads = 4;
		int task01timeout = 60000;
		int task01retryFails = 2;
		String task01cmd = "";
		String [][] task01input = new String[10][];
		String task01imagesDir = "-1";
		String task01images = "-1";
		String task01description = "(Task 01, written to do stuff.)";
		
		int task02maxThreads = 4;
		int task02timeout = 60000;
		int task02retryFails = 2;
		String task02cmd = "";
		String[][] task02input = new String[10][];
		String task02imagesDir = "-1";
		String task02images = "-1";
		String task02description = "(Task 02, written to do stuff.)";
		
		ImageJ_Manager thisManager = new ImageJ_Manager();
		
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
						while (readFile.hasNextLine()) {
							String argsLine = readFile.nextLine();
							if (argsLine.toLowerCase().contains("--gui") == true) {
								gui = Boolean.parseBoolean(thisManager.ImageJ_ReadParameter("--gui=",argsLine,2));
							} else if (argsLine.toLowerCase().contains("--functionmode") == true) {
								functionMode = thisManager.ImageJ_ReadParameter("--functionmode=",argsLine,0);
							} else if (argsLine.toLowerCase().contains("--task01description") == true) {
								task01description = thisManager.ImageJ_ReadParameter("--task01description=",argsLine,0);
							} else if (argsLine.toLowerCase().contains("--task01maxthreads") == true) {
								task01maxThreads = Integer.parseInt(thisManager.ImageJ_ReadParameter("--task01maxthreads=",argsLine,1));
							} else if (argsLine.toLowerCase().contains("--task01timeout") == true) {
								task01timeout = Integer.parseInt(thisManager.ImageJ_ReadParameter("--task01timeout=",argsLine,1));
							} else if (argsLine.toLowerCase().contains("--task01retryfails") == true) {
								task01retryFails = Integer.parseInt(thisManager.ImageJ_ReadParameter("--task01retryfails=",argsLine,1));
							} else if (argsLine.toLowerCase().contains("--task01cmd") == true) {
								task01cmd = thisManager.ImageJ_ReadParameter("--task01cmd=",argsLine,0);
							} else if (argsLine.toLowerCase().contains("--task01input01") == true) {
								task01input[1] = thisManager.ImageJ_ReadParameterArray("--task01input01=", argsLine);
							} else if (argsLine.toLowerCase().contains("--task01input02") == true) {
								task01input[2] = thisManager.ImageJ_ReadParameterArray("--task01input02=", argsLine);
							} else if (argsLine.toLowerCase().contains("--task01input03") == true) {
								task01input[3] = thisManager.ImageJ_ReadParameterArray("--task01input03=", argsLine);
							} else if (argsLine.toLowerCase().contains("--task01input04") == true) {
								task01input[4] = thisManager.ImageJ_ReadParameterArray("--task01input04=", argsLine);
							} else if (argsLine.toLowerCase().contains("--task01input05") == true) {
								task01input[5] = thisManager.ImageJ_ReadParameterArray("--task01input05=", argsLine);
							} else if (argsLine.toLowerCase().contains("--task01input06") == true) {
								task01input[6] = thisManager.ImageJ_ReadParameterArray("--task01input06=", argsLine);
							} else if (argsLine.toLowerCase().contains("--task01input07") == true) {
								task01input[7] = thisManager.ImageJ_ReadParameterArray("--task01input07=", argsLine);
							} else if (argsLine.toLowerCase().contains("--task01input08") == true) {
								task01input[8] = thisManager.ImageJ_ReadParameterArray("--task01input08=", argsLine);
							} else if (argsLine.toLowerCase().contains("--task01input09") == true) {
								task01input[9] = thisManager.ImageJ_ReadParameterArray("--task01input09=", argsLine);
							} else if (argsLine.toLowerCase().contains("--task01imagesdir") == true) {
								task01imagesDir = thisManager.ImageJ_ReadParameter("--task01imagesdir=",argsLine,0);
							} else if (argsLine.toLowerCase().contains("--task01images") == true) {
								task01images = thisManager.ImageJ_ReadParameter("--task01images=",argsLine,0);
							} else if (argsLine.toLowerCase().contains("--task02description") == true) {
								task02description = thisManager.ImageJ_ReadParameter("--task02description=",argsLine,0); 
							} else if (argsLine.toLowerCase().contains("--task02maxthreads") == true) {
								task02maxThreads = Integer.parseInt(thisManager.ImageJ_ReadParameter("--task02maxthreads=",argsLine,1));
							} else if (argsLine.toLowerCase().contains("--task02timeout") == true) {
								task02timeout = Integer.parseInt(thisManager.ImageJ_ReadParameter("--task02timeout=",argsLine,1));
							} else if (argsLine.toLowerCase().contains("--task02retryfails") == true) {
								task02retryFails = Integer.parseInt(thisManager.ImageJ_ReadParameter("--task02retryfails=",argsLine,1));
							} else if (argsLine.toLowerCase().contains("--task02cmd") == true) {
								task02cmd = thisManager.ImageJ_ReadParameter("--task02cmd=",argsLine,0);
							} else if (argsLine.toLowerCase().contains("--task02input01") == true) {
								task02input[1] = thisManager.ImageJ_ReadParameterArray("--task02input01=", argsLine);
							} else if (argsLine.toLowerCase().contains("--task02input02") == true) {
								task02input[2] = thisManager.ImageJ_ReadParameterArray("--task02input02=", argsLine);
							} else if (argsLine.toLowerCase().contains("--task02input03") == true) {
								task02input[3] = thisManager.ImageJ_ReadParameterArray("--task02input03=", argsLine);
							} else if (argsLine.toLowerCase().contains("--task02input04") == true) {
								task02input[4] = thisManager.ImageJ_ReadParameterArray("--task02input04=", argsLine);
							} else if (argsLine.toLowerCase().contains("--task02input05") == true) {
								task02input[5] = thisManager.ImageJ_ReadParameterArray("--task02input05=", argsLine);
							} else if (argsLine.toLowerCase().contains("--task02input06") == true) {
								task02input[6] = thisManager.ImageJ_ReadParameterArray("--task02input06=", argsLine);
							} else if (argsLine.toLowerCase().contains("--task02input07") == true) {
								task02input[7] = thisManager.ImageJ_ReadParameterArray("--task02input07=", argsLine);
							} else if (argsLine.toLowerCase().contains("--task02input08") == true) {
								task02input[8] = thisManager.ImageJ_ReadParameterArray("--task02input08=", argsLine);
							} else if (argsLine.toLowerCase().contains("--task02input09") == true) {
								task02input[9] = thisManager.ImageJ_ReadParameterArray("--task02input09=", argsLine);
							} else if (argsLine.toLowerCase().contains("--task02imagesdir") == true) {
								task02imagesDir = thisManager.ImageJ_ReadParameter("--task02imagesdir=",argsLine,0);
							} else if (argsLine.toLowerCase().contains("--task02images") == true) {
								task02images = thisManager.ImageJ_ReadParameter("--task02images=",argsLine,0);
							} 
							
						}
						readFile.close();
					} catch (Exception e) {
						UtilClass.DebugOutput("ERROR: Input parameter '--paramFile' recognized, but issue parsing out value.");
						e.printStackTrace();
						return;
					}
					
				}
			}
			for (int i = 0; i < args.length; i++) {
				// override any parameters defined explicitly, even if in --paramFile
				if (args[i].toLowerCase().contains("--gui") == true) {
					gui = Boolean.parseBoolean(thisManager.ImageJ_ReadParameter("--gui=",args[i],2));
				} else if (args[i].toLowerCase().contains("--functionmode") == true) {
					functionMode = thisManager.ImageJ_ReadParameter("--functionmode=",args[i],0);
				} else if (args[i].toLowerCase().contains("--task01description") == true) {
					task01description = thisManager.ImageJ_ReadParameter("--task01description=",args[i],0); 
				} else if (args[i].toLowerCase().contains("--task01maxthreads") == true) {
					task01maxThreads = Integer.parseInt(thisManager.ImageJ_ReadParameter("--task01maxthreads=",args[i],1));
				} else if (args[i].toLowerCase().contains("--task01timeout") == true) {
					task01timeout = Integer.parseInt(thisManager.ImageJ_ReadParameter("--task01timeout=",args[i],1));
				} else if (args[i].toLowerCase().contains("--task01retryfails") == true) {
					task01retryFails = Integer.parseInt(thisManager.ImageJ_ReadParameter("--task01retryfails=",args[i],1));
				} else if (args[i].toLowerCase().contains("--task01cmd") == true) {
					task01cmd = thisManager.ImageJ_ReadParameter("--task01cmd=",args[i],0);
				} else if (args[i].toLowerCase().contains("--task01input01") == true) {
					task01input[1] = thisManager.ImageJ_ReadParameterArray("--task01input01=", args[i]);
				} else if (args[i].toLowerCase().contains("--task01input02") == true) {
					task01input[2] = thisManager.ImageJ_ReadParameterArray("--task01input02=", args[i]);
				} else if (args[i].toLowerCase().contains("--task01input03") == true) {
					task01input[3] = thisManager.ImageJ_ReadParameterArray("--task01input03=", args[i]);
				} else if (args[i].toLowerCase().contains("--task01input04") == true) {
					task01input[4] = thisManager.ImageJ_ReadParameterArray("--task01input04=", args[i]);
				} else if (args[i].toLowerCase().contains("--task01input05") == true) {
					task01input[5] = thisManager.ImageJ_ReadParameterArray("--task01input05=", args[i]);
				} else if (args[i].toLowerCase().contains("--task01input06") == true) {
					task01input[6] = thisManager.ImageJ_ReadParameterArray("--task01input06=", args[i]);
				} else if (args[i].toLowerCase().contains("--task01input07") == true) {
					task01input[7] = thisManager.ImageJ_ReadParameterArray("--task01input07=", args[i]);
				} else if (args[i].toLowerCase().contains("--task01input08") == true) {
					task01input[8] = thisManager.ImageJ_ReadParameterArray("--task01input08=", args[i]);
				} else if (args[i].toLowerCase().contains("--task01input09") == true) {
					task01input[9] = thisManager.ImageJ_ReadParameterArray("--task01input09=", args[i]);
				} else if (args[i].toLowerCase().contains("--task01imagesdir") == true) {
					task01imagesDir = thisManager.ImageJ_ReadParameter("--task01imagesdir=",args[i],0);
				} else if (args[i].toLowerCase().contains("--task01images") == true) {
					task01images = thisManager.ImageJ_ReadParameter("--task01images=",args[i],0);
				} else if (args[i].toLowerCase().contains("--task02description") == true) {
					task02description = thisManager.ImageJ_ReadParameter("--task02description=",args[i],0); 	
				} else if (args[i].toLowerCase().contains("--task02maxthreads") == true) {
					task02maxThreads = Integer.parseInt(thisManager.ImageJ_ReadParameter("--task02maxthreads=",args[i],1));
				} else if (args[i].toLowerCase().contains("--task02timeout") == true) {
					task02timeout = Integer.parseInt(thisManager.ImageJ_ReadParameter("--task02timeout=",args[i],1));
				} else if (args[i].toLowerCase().contains("--task02retryfails") == true) {
					task02retryFails = Integer.parseInt(thisManager.ImageJ_ReadParameter("--task02retryfails=",args[i],1));
				} else if (args[i].toLowerCase().contains("--task02cmd") == true) {
					task02cmd = thisManager.ImageJ_ReadParameter("--task02cmd=",args[i],0);
				} else if (args[i].toLowerCase().contains("--task02input01") == true) {
					task02input[1] = thisManager.ImageJ_ReadParameterArray("--task02input01=",args[i]);
				} else if (args[i].toLowerCase().contains("--task02input02") == true) {
					task02input[2] = thisManager.ImageJ_ReadParameterArray("--task02input02=", args[i]);
				} else if (args[i].toLowerCase().contains("--task02input03") == true) {
					task02input[3] = thisManager.ImageJ_ReadParameterArray("--task02input03=", args[i]);
				} else if (args[i].toLowerCase().contains("--task02input04") == true) {
					task02input[4] = thisManager.ImageJ_ReadParameterArray("--task02input04=",args[i]);
				} else if (args[i].toLowerCase().contains("--task02input05") == true) {
					task02input[5] = thisManager.ImageJ_ReadParameterArray("--task02input05=", args[i]);
				} else if (args[i].toLowerCase().contains("--task02input06") == true) {
					task02input[6] = thisManager.ImageJ_ReadParameterArray("--task02input06=", args[i]);
				} else if (args[i].toLowerCase().contains("--task02input07") == true) {
					task02input[7] = thisManager.ImageJ_ReadParameterArray("--task02input07=", args[i]);
				} else if (args[i].toLowerCase().contains("--task02input08") == true) {
					task02input[8] = thisManager.ImageJ_ReadParameterArray("--task02input08=", args[i]);
				} else if (args[i].toLowerCase().contains("--task02input09") == true) {
					task02input[9] = thisManager.ImageJ_ReadParameterArray("--task02input09=", args[i]);
				} else if (args[i].toLowerCase().contains("--task02imagesdir") == true) {
					task02imagesDir = thisManager.ImageJ_ReadParameter("--task02imagesdir=",args[i],0);
				} else if (args[i].toLowerCase().contains("--task02images") == true) {
					task02images = thisManager.ImageJ_ReadParameter("--task02images=",args[i],0);
				} 
			}

			for (int i = 0; i < args.length; i++) {
				if (args[i].toLowerCase().contains("--printparam") == true && args[i].toLowerCase().contains("--printparamfile") == false) {
					printParam = true;
				}
			}
		} else {
			UtilClass.DebugOutput("No parameters provided. Will run with defaults.");
			UtilClass.DebugOutput("(Run again with parameter '--help' to print out parameter options and other information.)");
			UtilClass.DebugOutput("(Run again with parameter '--printParam' to print out default or specified parameters for this execution.)");
		}
		
		
		if (gui == true) {
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
		

		

		
		
		if (functionMode.contains("1") == true) {
			// if inputData == null and inputDataDir != null, read all image files in that immediate directory
			if (task01images.length()>0 && task01imagesDir.length()>0) {
				int task01imagesNum = Integer.parseInt(task01images.replace("|", ""));
				int task01imagesDirNum = Integer.parseInt(task01imagesDir.replace("|", ""));
				if (task01imagesNum == -1 && task01imagesDirNum == -1) {
					// assume no image input, just run as 1 job with literal command.
				}
				else if ((task01input[task01imagesNum] == null || task01input[task01imagesNum].length == 0) && (task01input[task01imagesDirNum] != null)) {
					// get input images list, used to determine how many jobs need to be done (assume 1 image per job) 
					File fileDirectory = new File (task01input[task01imagesDirNum][0]);
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
						task01input[task01imagesNum] = new String[f.length];
						for (int i = 0; i < f.length; i++) {
							task01input[task01imagesNum][i] = f[i].getName();
						}
					}	
				}
			}
			if (printParam == true) {
				UtilClass.DebugOutput("****These are the recognized input parameters (if not specified by the user, these are the defaults).****");
				UtilClass.DebugOutput("gui = " + gui);
				UtilClass.DebugOutput("functionMode = " + functionMode);
				UtilClass.DebugOutput("task01maxThreads = " + task01maxThreads);
				UtilClass.DebugOutput("task01timeout = " + task01timeout);
				UtilClass.DebugOutput("task01retryFails = " + task01retryFails);
				UtilClass.DebugOutput("task01cmd = " + task01cmd);
				UtilClass.DebugOutput("task01imagesDir = " + task01imagesDir);
				UtilClass.DebugOutput("task01images = " + task01images);
				for (int a = 0; a < task01input.length; a++) {
					if (task01input[a] != null) {
						UtilClass.DebugOutput("task01input0" + a + " = ");
						for (int b = 0; b < task01input[a].length; b++) {
							UtilClass.DebugOutput(task01input[a][b]);
							if (b < task01input[a].length - 1) {
								UtilClass.DebugOutput(", ");
							}
						}
						UtilClass.DebugOutput("\n");
					}
				}
				UtilClass.DebugOutput("****End of input parameters.****\n\n");
			}
			thisManager.task01description = task01description;
			thisManager.task01maxThreads = task01maxThreads;
			thisManager.task01timeout = task01timeout;
			thisManager.task01retryFails = task01retryFails;
			thisManager.task01cmd = task01cmd;
			thisManager.task01input = task01input;
			thisManager.task01images = Integer.parseInt(task01images.replace("|", ""));		//indicating variable 1 - 9, should be a number
			thisManager.task01imagesDir = Integer.parseInt(task01imagesDir.replace("|", ""));
			//thisManager.Initialize_Start();
			if (thisManager.task01images == -1 && thisManager.task01imagesDir == -1) {
				thisManager.RunTask01SingleThread();
			} else {
				thisManager.RunTask01();
			}
		}
		if (functionMode.contains("2") == true) {
			if (task02images.length()>0 && task02imagesDir.length()>0) {
				int task02imagesNum = Integer.parseInt(task02images.replace("|", ""));
				int task02imagesDirNum = Integer.parseInt(task02imagesDir.replace("|", ""));
				if (task02imagesNum == -1 && task02imagesDirNum == -1) {
					// assume no image input, just run as 1 job with literal command.
				}
				else if ((task02input[task02imagesNum] == null || task02input[task02imagesNum].length == 0) && (task02input[task02imagesDirNum] != null)) {
					// get input images list, used to determine how many jobs need to be done (assume 1 image per job) 
					File fileDirectory = new File (task02input[task02imagesDirNum][0]);
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
						task02input[task02imagesNum] = new String[f.length];
						for (int i = 0; i < f.length; i++) {
							task02input[task02imagesNum][i] = f[i].getName();
						}
					}	
				}
			}
			if (printParam == true) {
				UtilClass.DebugOutput("****These are the recognized input parameters (if not specified by the user, these are the defaults).****");
				UtilClass.DebugOutput("task02maxThreads = " + task02maxThreads);
				UtilClass.DebugOutput("task02timeout = " + task02timeout);
				UtilClass.DebugOutput("task02retryFails = " + task02retryFails);
				UtilClass.DebugOutput("task02cmd = " + task02cmd);
				UtilClass.DebugOutput("task02imagesDir = " + task02imagesDir);
				UtilClass.DebugOutput("task02images = " + task02images);
				for (int a = 0; a < task02input.length; a++) {
					if (task02input[a] != null) {
						UtilClass.DebugOutput("task02input0" + a + " = ");
						for (int b = 0; b < task02input[a].length; b++) {
							UtilClass.DebugOutput(task02input[a][b]);
							if (b < task02input[a].length - 1) {
								UtilClass.DebugOutput(", ");
							}
						}
						UtilClass.DebugOutput("\n");
					}
				}
				UtilClass.DebugOutput("****End of input parameters.****\n\n");
			}
			thisManager.task02description = task02description;
			thisManager.task02maxThreads = task02maxThreads;
			thisManager.task02timeout = task02timeout;
			thisManager.task02retryFails = task02retryFails;
			thisManager.task02cmd = task02cmd;
			thisManager.task02input = task02input;
			thisManager.task02images = Integer.parseInt(task02images.replace("|", ""));			//indicating variable 1 - 9, should be a number
			thisManager.task02imagesDir = Integer.parseInt(task02imagesDir.replace("|", ""));
			//thisManager.ImageJ_StartJobs();
			if (thisManager.task02images == -1 && thisManager.task02imagesDir == -1) {
				thisManager.RunTask02SingleThread();
			} else {
				thisManager.RunTask02();
			}
		} 
		if (functionMode.contains("3") == true) {
			thisManager.CombineCSV_Start();
		}
		
		
		UtilClass.DebugOutput("---");
		UtilClass.DebugOutput("Finished, closed.");
	}
	
	public ImageJ_Manager() {
		
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
	
	//Start work for generalizing tasks
	List<String> taskDescriptions = new ArrayList<String>(Arrays.asList(task01description, task02description));
	List<Integer> taskMaxThreads = new ArrayList<Integer>(Arrays.asList(task01maxThreads, task02maxThreads));
	List<Integer> taskTimeouts = new ArrayList<Integer>(Arrays.asList(task01timeout, task02timeout));
	List<Integer> taskRetryFails = new ArrayList<Integer>(Arrays.asList(task01retryFails, task02retryFails));
	List<String> taskCommands = new ArrayList<String>(Arrays.asList(task01cmd, task02cmd));
	List<String[][]> taskInputs = new ArrayList<String[][]>(Arrays.asList(task01input, task02input));
	List<Integer> taskImages = new ArrayList<Integer>(Arrays.asList(task01images, task02images));
	List<Integer> taskImagesDirs = new ArrayList<Integer>(Arrays.asList(task01imagesDir, task02imagesDir));
	
	long startExecutionTime = 0;
	
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
				
		for (int i = 0; i < 1; i++) {
			processThreads[i] = new ImageJ_Thread();
			processThreads[i].threadIndex = i + 1;
			processThreads[i].threadTotal = 1;
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
