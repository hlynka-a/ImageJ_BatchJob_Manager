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
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ImageJ_Manager {

	public static void main (String[] args) {
		
		String version = "v0.721 | 2021-04-06";
		
		/*UtilClass util = new UtilClass();
		double[] inputAvg = {4.5, 6, 7.1};
		double avgTest = util.returnAverage(inputAvg);
		System.out.println(avgTest);*/
		
		// input parameters the user can define, to be passed to instance of program
		String paramFile = null;	//filepath
		boolean gui = true;
		String functionMode = "1";		// example: 12	=> task 1 and task 2, in this order
		//int maxThreads = 4;
		//int timeout = 60000;
		//int retryFails = 2;
		//String progCommand = "ImageJ-win64.exe";
		//String progDir = "";
		int task01maxThreads = 4;
		int task01timeout = 60000;
		int task01retryFails = 2;
		String task01cmd = "||1||ImageJ-win64.exe --file-name ||2||||3|| -macro ||4||||5||";
		/*String [] task01input01;	// default: program directory
		String [] task01input02;	// default: input script directory
		String [] task01input03;	// default: input script
		String [] task01input04;	// default: input image file directory
		String [] task01input05;	// default: input image file(s)
		String [] task01input06;	
		String [] task01input07;	
		String [] task01input08;	
		String [] task01input09;*/
		String [][] task01input = new String[10][];
		String task01imagesDir = "";
		String task01images = "";
		
		//String inputScript = "Morphometry_no_Export.I.ijm";
		//String inputScriptDir = "";
		//String[] inputData = null;
		//String inputDataDir = "";
		int task02maxThreads = 4;
		int task02timeout = 60000;
		int task02retryFails = 2;
		String task02cmd = "||1||ImageJ-win64.exe --file-name ||2||||3|| -macro ||4||||5||";
		/*String [] task02input01;	// default: program directory
		String [] task02input02;	// default: input script directory
		String [] task02input03;	// default: input script
		String [] task02input04;	// default: input image file directory
		String [] task02input05;	// default: input image file(s)
		String [] task02input06;	
		String [] task02input07;	
		String [] task02input08;	
		String [] task02input09;*/
		String[][] task02input = new String[10][];
		String task02imagesDir = "";
		String task02images = "";
		
		ImageJ_Manager thisManager = new ImageJ_Manager();
		
		if (args != null && args.length != 0) {
			for (int i = 0; i < args.length; i++) {
				if (args[i].toLowerCase().contains("--help") == true) {
					// UPDATE: Generalized into assuming 3 different jobs, with better support for how variable arguements will be handled.
					thisManager.PrintHelpDocs(version);
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
						System.out.println("ERROR: Input parameter '--paramFile' recognized, but issue parsing out value.");
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
			// if inputData == null and inputDataDir != null, read all image files in that immediate directory
			if (task01images.length()>0 && task01imagesDir.length()>0) {
				int task01imagesNum = Integer.parseInt(task01images.replace("|", ""));
				int task01imagesDirNum = Integer.parseInt(task01imagesDir.replace("|", ""));
				if ((task01input[task01imagesNum] == null || task01input[task01imagesNum].length == 0) && (task01input[task01imagesDirNum] != null)) {
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
			if (task02images.length()>0 && task02imagesDir.length()>0) {
				int task02imagesNum = Integer.parseInt(task02images.replace("|", ""));
				int task02imagesDirNum = Integer.parseInt(task02imagesDir.replace("|", ""));
				if ((task02input[task02imagesNum] == null || task02input[task02imagesNum].length == 0) && (task02input[task02imagesDirNum] != null)) {
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

			for (int i = 0; i < args.length; i++) {
				if (args[i].toLowerCase().contains("--printparam") == true && args[i].toLowerCase().contains("--printparamfile") == false) {
					System.out.println("****These are the recognized input parameters (if not specified by the user, these are the defaults).****");
					System.out.println("gui = " + gui);
					System.out.println("functionMode = " + functionMode);
					System.out.println("task01maxThreads = " + task01maxThreads);
					System.out.println("task01timeout = " + task01timeout);
					System.out.println("task01retryFails = " + task01retryFails);
					System.out.println("task01cmd = " + task01cmd);
					System.out.println("task01imagesDir = " + task01imagesDir);
					System.out.println("task01images = " + task01images);
					for (int a = 0; a < task01input.length; a++) {
						if (task01input[a] != null) {
							System.out.print("task01input0" + a + " = ");
							for (int b = 0; b < task01input[a].length; b++) {
								System.out.print(task01input[a][b]);
								if (b < task01input[a].length - 1) {
									System.out.print(", ");
								}
							}
							System.out.print("\n");
						}
					}
					System.out.println("task02maxThreads = " + task02maxThreads);
					System.out.println("task02timeout = " + task02timeout);
					System.out.println("task02retryFails = " + task02retryFails);
					System.out.println("task02cmd = " + task02cmd);
					System.out.println("task02imagesDir = " + task02imagesDir);
					System.out.println("task02images = " + task02images);
					for (int a = 0; a < task02input.length; a++) {
						if (task02input[a] != null) {
							System.out.print("task02input0" + a + " = ");
							for (int b = 0; b < task02input[a].length; b++) {
								System.out.print(task02input[a][b]);
								if (b < task02input[a].length - 1) {
									System.out.print(", ");
								}
							}
							System.out.print("\n");
						}
					}
					System.out.println("****End of input parameters.****\n\n");
				}
			}
		} else {
			System.out.println("No parameters provided. Will run with defaults.");
			System.out.println("(Run again with parameter '--help' to print out parameter options and other information.)");
			System.out.println("(Run again with parameter '--printParam' to print out default or specified parameters for this execution.)");
		}
		
		System.out.println("Starting up ImageJ Batch Job Handler (or whatever software we're launching multiple instances of) in 2 seconds.");
		System.out.println("VERSION: " + version);
		System.out.println("---");
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		thisManager.task01maxThreads = task01maxThreads;
		thisManager.task01timeout = task01timeout;
		thisManager.task01retryFails = task01retryFails;
		thisManager.task01cmd = task01cmd;
		thisManager.task01input = task01input;
		thisManager.task01images = Integer.parseInt(task01images.replace("|", ""));
		
		thisManager.task02maxThreads = task02maxThreads;
		thisManager.task02timeout = task02timeout;
		thisManager.task02retryFails = task02retryFails;
		thisManager.task02cmd = task02cmd;
		thisManager.task02input = task02input;
		thisManager.task02images = Integer.parseInt(task02images.replace("|", ""));
		thisManager.task02imagesDir = Integer.parseInt(task02imagesDir.replace("|", ""));
		
		
		if (functionMode.contains("1") == true) {
			thisManager.Initialize_Start();
		}
		if (functionMode.contains("2") == true) {
			thisManager.ImageJ_StartJobs();
		} 
		if (functionMode.contains("3") == true) {
			thisManager.CombineCSV_Start();
		}
		
		
		System.out.println("---");
		System.out.println("Finished, closed.");
	}
	
	public ImageJ_Manager() {
		
	}
	
	public void PrintHelpDocs(String version) {
		System.out.println("**** HELP DOCS ****");
		System.out.println("This is a program written in Java to batch-run multiple instances of one program");
		System.out.println("  as they would be run in a command-line interface.");
		System.out.println("This was originally written to run an ImageJ script as a batch job on a compute server.");
		System.out.println("Originally written by Andrew Hlynka at the University of Michigan in 2021.");
		System.out.println("VERSION: " + version);
		System.out.println("---");
		System.out.println("Parameters:");
		/*System.out.println("\t--help \t\t\t: List out documentation / parameter options.");
		System.out.println("\t--paramFile \t\t: Use input .txt file (with full file path or local path) as parameter input."); 
		System.out.println("\t\t\t\tThis input is overrided by any parameters defined in command line alongside --file definition.");
		System.out.println("\t--printParamFile\t: Do not run program, but print out example input .txt file in same directory as this .jar for use.");
		System.out.println("\t--printParam\t\t: Print / display parameter values used for this execution.");
		System.out.println("\t--maxThreads \t\t: Number (integer, >= 1) that defines how many parallel threads to run at a time.");
		System.out.println("\t--timeout \t\t: Number (integer, >= 0) in milliseconds (1000 = 1 second) that defines how long to wait before forcing a program thread to close.");
		System.out.println("\t\t\t\tAssume this occurs only due to error in program instance that causes it to pause / wait indefinitely.");
		System.out.println("\t\t\t\tIf timeout occurs, this program will assume user's input program had failed for that instance.");
		System.out.println("\t--retryFails \t\t: Number (integer, >= 0). If program instance from batch jobs fails (based on 'timeout' value), launch again 'n' number of times.");
		System.out.println("\t--gui \t\t\t: Open with built-in Java Swing GUI (NOT IMPLEMENTED YET). True by default.");
		System.out.println("\t--progCommand \t\t: The explicit command to run one instance of the program through the command line.");
		System.out.println("\t--progDir \t\t: The directory of the program (would be used with --progCommand).");
		System.out.println("\t--inputScript \t\t: Input script to open and launch with program.");
		System.out.println("\t--inputScriptDir \t\t: The directory of the input script (would be used with --inputScript).");
		System.out.println("\t--inputData \t\t: Input data / image files. Assumption is that each batch-job instance will work on 1 data file.");
		System.out.println("\t\t\t\tAll input data should be listed as one parameter, separated by ',' with no spaces. ");
		System.out.println("\t--inputDataDir \t\t: The directory of the input data files (if 'inputData' is undefined, assume all files in this directory).");
		System.out.println("\t--initCommand \t\t: System command to launch user's script (example: Python) to initialize data.");
		System.out.println("\t--functionMode \t\t: Defines which function (or combination of functions) to run.");
		System.out.println("\t\t\t\t1 = Run ImageJ job(s) to analyze data (parallel),");
		System.out.println("\t\t\t\t2 = Run initial job(s) to prepare data (not parallel),");
		System.out.println("\t\t\t\t3 = Combine and summarize .csv output files,");
		System.out.println("\t\t\t\t4 = function 2, then 1, 5 = function 1, then 3, 6 = function 2, then 1, then 3.");*/
		System.out.println("\t--help \t\t\t: List out documentation / parameter options.");
		System.out.println("\t--paramFile \t\t: Use input .txt file (with full file path or local path) as parameter input."); 
		System.out.println("\t\t\t\t\tThis input is overrided by any parameters defined in command line alongside --file definition.");
		System.out.println("\t--printParamFile\t: Do not run program, but print out example input .txt file in same directory as this .jar for use.");
		System.out.println("\t--printParam\t\t: Print / display parameter values used for this execution.");
		System.out.println("\t--gui \t\t\t: Open with built-in Java Swing GUI (NOT IMPLEMENTED YET). True by default.");
		System.out.println("\t--functionMode \t\t: Defines which function (or combination of functions) to run.");
		System.out.println("\t\t\t\t\t1 = Run Python job(s) to prepare data (parallel),");
		System.out.println("\t\t\t\t\t2 = Run ImageJ job(s) to analyze data (parallel),");
		System.out.println("\t\t\t\t\t3 = Combine and summarize .csv output files,");
		System.out.println("\t\t\t\t\t123 = Example that runs 1, then 2, then 3.");
		System.out.println("");
		System.out.println("\t--task01cmd\t\t: The explicit command to run one instance of the program through the command line.");
		System.out.println("\t\t\t\t\tIncludes special notation (||1||, ||2||, etc.) to set variables that can be defined.");
		System.out.println("\t--task01timeout\t\t: Number (integer, >= 0) in milliseconds (1000 = 1 second) that defines how long to wait before forcing a program thread to close.");
		System.out.println("\t--task01maxThreads\t: Number (integer, >= 1) that defines how many parallel threads to run at a time.");
		System.out.println("\t--task01retryFails\t: Number (integer, >= 0). If program instance from batch jobs fails (based on 'timeout' value), launch again 'n' number of times.");
		System.out.println("\t--task01input01\t\t: The corresponding value(s) as defined by ||1|| in task01cmd.");
		System.out.println("\t\t\t\t\tRepeats for task01input01, 02, 03, ..., 09");
		System.out.println("\t\t\t\t\tFor explicitly defining multiple images, use ',' to separate within one input. Example: 'image01,image02,image03' ");
		System.out.println("\t--task01images\t\t: Which of task01input0X corresponds to file names of input images? (Example: ||2||)");
		System.out.println("\t\t\t\t\tIf blank, assumes all image files in 'task01imagesDir' are the input.");
		System.out.println("\t--task01imagesDir\t: Which of task01input0X corresponds to file directory of input images? (Example: ||2||)");
		System.out.println("");
		System.out.println("\tAll parameters that start with 'task01' exist for 'task02' as well.");
		
		System.out.println("---");
		System.out.println("Examples:");
		System.out.println("\t(run with default internal parameters)");
		System.out.println("\t(run with just input file)");
		System.out.println("\t(run with defined input parameters, absolute directory)");
		System.out.println("\t(run with some defined parameters, local directory)");
		System.out.println("---");
	}
	
	public void PrintExampleParamFile() {
		System.out.println("Printing example parameter file for this program in same directory (not running program).");
		System.out.println("Use / edit the input parameter file as needed, use with parameter '--paramFile' in command line execution.");
		System.out.println("WARNING: THIS FEATURE ISN'T IMPLEMENTED YET.");
		System.out.println("Finished.");
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
			System.out.println("ERROR: Input parameter " + removeString + " recognized, but issue parsing out value.");
			e.printStackTrace();
		}
		System.out.println(">>>> read parameter -> " + removeString + " = " + returnValue);
		return returnValue;
	}
	
	public String[] ImageJ_ReadParameterArray(String removeString, String inputArg) {
		String [] returnValue = null;
		try {
			inputArg = inputArg.toLowerCase().replace(removeString, "");
			returnValue = inputArg.split(",");
		} catch (Exception e) {
			System.out.println("ERROR: Input parameter ' " + removeString + " ' recognized, but issue parsing out value.");
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
	int task01maxThreads = 1;
	int task01timeout = 60000;
	int task01retryFails = 2;
	String task01cmd = "";
	String[][] task01input;
	int task01images = 1;
	
	int task02maxThreads = 1;
	int task02timeout = 60000;
	int task02retryFails = 2;
	String task02cmd = "";
	String[][] task02input;
	int task02images = 1;
	int task02imagesDir = 1;
	// why not provide an option for the user to specify the entire command to launch ImageJ? Because we want to be able to handle parallel jobs, the user would have to write out every command for every job
	
	long startExecutionTime = 0;
	
	public void ImageJ_StartJobs() {
		
		System.out.println("TASK 2:");
		
		System.out.println("System OS: " + System.getProperty("os.name"));
		System.out.println("Total number of system cores: " + Runtime.getRuntime().availableProcessors());
		System.out.println("Total amount of JVM memory (GB): " + String.format("%.4f",Runtime.getRuntime().totalMemory()*0.001f*0.001f*0.001f));
		
		System.out.println("Set to run this many concurrent threads: " + task02maxThreads);
		System.out.println("Defined timeout time (seconds): " + task02timeout*0.001f);
		long startTime = System.nanoTime();
		startExecutionTime = System.currentTimeMillis();
		
		boolean[] threadSuccess = new boolean[task02input[task02images].length];
		
		ExecutorService execService = Executors.newFixedThreadPool(task02maxThreads);
		ImageJ_Thread [] processThreads = new ImageJ_Thread[task02input[task02images].length];
		
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
		
		for (int i = 0; i < task02input[task02images].length; i++) {
			execService.execute(processThreads[i]);
		}
		
		System.out.println("Waiting for threads to finish...");
		System.out.println("");
		execService.shutdown();
		try {
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
		
		System.out.println("");
		System.out.println("All threads finished.");
		double totalSeconds = (System.nanoTime() - startTime)*0.000000001f;
		System.out.println("Total execution time (seconds): " + String.format("%.0f",totalSeconds) + "   |   (minutes): " + String.format("%.2f",(totalSeconds/60f)));
		System.out.println("Average execution time for successful threads (seconds): " + String.format("%.0f", avgSeconds));
		System.out.println("Max execution time for successful threads (seconds): " + String.format("%.0f", maxSeconds));
		int failedRuns = 0;
		for (int i = 0; i < task02input[task02images].length; i++)
		{
			System.out.print("Thread " + (i+1) + " : ");
			if (threadSuccess[i] == true) {
				System.out.print("SUCCESS");
			} else {
				System.out.print("FAIL");
				failedRuns++;
			}
			System.out.print(", \t");
			if (i % 3 == 2) {
				System.out.print("\n");
			}
		}
		System.out.print("\n");
		
		while (task02retryFails > 0 && failedRuns > 0) {
			System.out.println("Retry allowed for failed threads. Retries left: " + task02retryFails);
			task02retryFails--;
			failedRuns = 0;
			
			execService = Executors.newFixedThreadPool(task02maxThreads);
			
			for (int i = 0; i < task02input[task02images].length; i++) {
				if (processThreads[i].forcedClosed == true) {
					execService.execute(processThreads[i]);
				}
			}
			
			System.out.println("Waiting for threads to finish...");
			System.out.println("");
			execService.shutdown();
			try {
				execService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			execService.shutdownNow();
			
			for (int i = 0; i < task02input[task02images].length; i++) {
				threadSuccess[i] = !processThreads[i].forcedClosed;
			}
			
			System.out.println("");
			System.out.println("All threads finished.");
			totalSeconds = (System.nanoTime() - startTime)*0.000000001f;
			System.out.println("Total execution time (seconds): " + String.format("%.0f",totalSeconds) + "   |   (minutes): " + String.format("%.2f",(totalSeconds/60f)));
			for (int i = 0; i < task02input[task02images].length; i++)
			{
				System.out.print("Thread " + (i+1) + " : ");
				if (threadSuccess[i] == true) {
					System.out.print("SUCCESS");
				} else {
					System.out.print("FAIL");
					failedRuns++;
				}
				System.out.print(", \t");
				if (i % 3 == 2) {
					System.out.print("\n");
				}
			}
			System.out.print("\n");
		}
		System.out.println("---\n\n");
	}
	
	public void CombineCSV_Start() {
		
		System.out.println("TASK 3:");
		
		String currentDateText = "";
		Calendar calendar = Calendar.getInstance();
		calendar.get(Calendar.YEAR);
		currentDateText = "" + calendar.get(Calendar.YEAR) + "" 
				+ String.format("%02d", calendar.get(Calendar.MONTH)+1) + "" 
				+ String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + "_"
				+ String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + "" 
				+ String.format("%02d", calendar.get(Calendar.MINUTE)) + ""
				+ String.format("%02d", calendar.get(Calendar.SECOND)); 
		
		System.out.println("Now combining .csv files into one large file called 'summary_" + currentDateText + ".csv'...");
		
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
		
		System.out.println("This many .csv files found: " + csvFiles.length);
		
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
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		try {
			FileWriter writeCsvFile = new FileWriter(task02input[task02imagesDir][0] + "summary_"+currentDateText+".csv");
			writeCsvFile.write(outputCsv);
			writeCsvFile.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println(".csv file task done.");
		System.out.println("---\n\n");
	}
	
	public void Initialize_Start() {
		System.out.println("TASK 01:");
		System.out.println("Running user's specified script to initialize data before ImageJ part.");
		
		long startTime = System.nanoTime();
		startExecutionTime = System.currentTimeMillis();
		
		boolean[] threadSuccess = new boolean[task01input[task01images].length];
		
		ExecutorService execService = Executors.newFixedThreadPool(task01maxThreads);
		ImageJ_Thread [] processThreads = new ImageJ_Thread[task01input[task01images].length];
		
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
			String imageString = task01input[task01images][i];
			/*name.toLowerCase().endsWith(".jpg")
			|| name.toLowerCase().endsWith(".jpeg")
			|| name.toLowerCase().endsWith(".png")
			|| name.toLowerCase().endsWith(".gif")
			|| name.toLowerCase().endsWith(".tif")
			|| name.toLowerCase().endsWith(".tiff")*/
			imageString = imageString.replace(".jpg", "");
			imageString = imageString.replace(".jpeg", "");
			imageString = imageString.replace(".png", "");
			imageString = imageString.replace(".gif", "");
			imageString = imageString.replace(".tif", "");
			imageString = imageString.replace(".tiff", "");
			sysCommand = sysCommand.replace("||" + task01images + "||", imageString);
			processThreads[i].sysCommand = sysCommand;
			processThreads[i].milisecondsTimeout = task01timeout;
		}
		
		for (int i = 0; i < task01input[task01images].length; i++) {
			execService.execute(processThreads[i]);
		}
		
		System.out.println("Waiting for threads to finish...");
		System.out.println("");
		execService.shutdown();
		try {
			execService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
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
		
		System.out.println("");
		System.out.println("All threads finished.");
		double totalSeconds = (System.nanoTime() - startTime)*0.000000001f;
		System.out.println("Total execution time (seconds): " + String.format("%.0f",totalSeconds) + "   |   (minutes): " + String.format("%.2f",(totalSeconds/60f)));
		System.out.println("Average execution time for successful threads (seconds): " + String.format("%.0f", avgSeconds));
		System.out.println("Max execution time for successful threads (seconds): " + String.format("%.0f", maxSeconds));
		int failedRuns = 0;
		for (int i = 0; i < task01input[task01images].length; i++)
		{
			System.out.print("Thread " + (i+1) + " : ");
			if (threadSuccess[i] == true) {
				System.out.print("SUCCESS");
			} else {
				System.out.print("FAIL");
				failedRuns++;
			}
			System.out.print(", \t");
			if (i % 3 == 2) {
				System.out.print("\n");
			}
		}
		System.out.print("\n");
		
		while (task01retryFails > 0 && failedRuns > 0) {
			System.out.println("Retry allowed for failed threads. Retries left: " + task01retryFails);
			task01retryFails--;
			failedRuns = 0;
			
			execService = Executors.newFixedThreadPool(task01maxThreads);
			
			for (int i = 0; i < task01input[task01images].length; i++) {
				if (processThreads[i].forcedClosed == true) {
					execService.execute(processThreads[i]);
				}
			}
			
			System.out.println("Waiting for threads to finish...");
			System.out.println("");
			execService.shutdown();
			try {
				execService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			execService.shutdownNow();
			
			for (int i = 0; i < task01input[task01images].length; i++) {
				threadSuccess[i] = !processThreads[i].forcedClosed;
			}
			
			System.out.println("");
			System.out.println("All threads finished.");
			totalSeconds = (System.nanoTime() - startTime)*0.000000001f;
			System.out.println("Total execution time (seconds): " + String.format("%.0f",totalSeconds) + "   |   (minutes): " + String.format("%.2f",(totalSeconds/60f)));
			for (int i = 0; i < task01input[task01images].length; i++)
			{
				System.out.print("Thread " + (i+1) + " : ");
				if (threadSuccess[i] == true) {
					System.out.print("SUCCESS");
				} else {
					System.out.print("FAIL");
					failedRuns++;
				}
				System.out.print(", \t");
				if (i % 3 == 2) {
					System.out.print("\n");
				}
			}
			System.out.print("\n");
		}
		
		System.out.println("Finished initializing data.");
		System.out.println("---\n\n");
	}
}
