package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ImageJ_Manager {

	public static void main (String[] args) {
		
		String version = "v0.501 | 2021-03-16";
		
		// input parameters the user can define, to be passed to instance of program
		String paramFile = null;	//filepath
		int maxThreads = 4;
		int timeout = 60000;
		int retryFails = 2;
		boolean gui = true;
		String progCommand = "ImageJ-win64.exe";
		String progDir = "";
		String inputScript = "Morphometry_no_Export.I.ijm";
		String inputScriptDir = "";
		String[] inputData = null;
		String inputDataDir = "";
		
		ImageJ_Manager thisManager = new ImageJ_Manager();
		
		if (args != null && args.length != 0) {
			for (int i = 0; i < args.length; i++) {
				if (args[i].toLowerCase().contains("--help") == true) {
					System.out.println("**** HELP DOCS ****");
					System.out.println("This is a program written in Java to batch-run multiple instances of one program");
					System.out.println("  as they would be run in a command-line interface.");
					System.out.println("This was originally written to run an ImageJ script as a batch job on a compute server.");
					System.out.println("Originally written by Andrew Hlynka at the University of Michigan in March 2021.");
					System.out.println("VERSION: " + version);
					System.out.println("---");
					System.out.println("Parameters:");
					System.out.println("\t--help \t\t\t: List out documentation / parameter options.");
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
					System.out.println("---");
					System.out.println("Examples:");
					System.out.println("\t(run with default internal parameters)");
					System.out.println("\t(run with just input file)");
					System.out.println("\t(run with defined input parameters, absolute directory)");
					System.out.println("\t(run with some defined parameters, local directory)");
					System.out.println("---");
					return;
				}
			}
			for (int i = 0; i < args.length; i++) {
				if (args[i].toLowerCase().contains("--printparamfile") == true) {
					System.out.println("Printing example parameter file for this program in same directory (not running program).");
					System.out.println("Use / edit the input parameter file as needed, use with parameter '--paramFile' in command line execution.");
					System.out.println("WARNING: THIS FEATURE ISN'T IMPLEMENTED YET.");
					System.out.println("Finished.");
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
							if (argsLine.toLowerCase().contains("--maxthreads") == true) {
								maxThreads = Integer.parseInt(thisManager.ImageJ_ReadParameter("--maxthreads=",argsLine,1));
							} else if (argsLine.toLowerCase().contains("--timeout") == true) {
								timeout = Integer.parseInt(thisManager.ImageJ_ReadParameter("--timeout=",argsLine,1));
							} else if (argsLine.toLowerCase().contains("--retryfails") == true) {
								retryFails = Integer.parseInt(thisManager.ImageJ_ReadParameter("--retryfailes=",argsLine,1));
							} else if (argsLine.toLowerCase().contains("--gui") == true) {
								gui = Boolean.parseBoolean(thisManager.ImageJ_ReadParameter("--gui=",argsLine,2));
							} else if (argsLine.toLowerCase().contains("--progcommand") == true) {
								progCommand = thisManager.ImageJ_ReadParameter("--progcommand=",argsLine,0);
							} else if (argsLine.toLowerCase().contains("--progdir") == true) {
								progDir = thisManager.ImageJ_ReadParameter("--progdir=",argsLine,0);
							} else if (argsLine.toLowerCase().contains("--inputscriptdir") == true) {
								inputScriptDir = thisManager.ImageJ_ReadParameter("--inputscriptdir=",argsLine,0);
							} else if (argsLine.toLowerCase().contains("--inputscript") == true) {
								inputScript = thisManager.ImageJ_ReadParameter("--inputscript=",argsLine,0);
							} else if (argsLine.toLowerCase().contains("--inputdatadir") == true) {
								inputDataDir = thisManager.ImageJ_ReadParameter("--inputdatadir=",argsLine,0);
							} else if (argsLine.toLowerCase().contains("--inputdata") == true) {
								try {
									argsLine = argsLine.toLowerCase().replace("--inputdata=", "");
									inputData = argsLine.split(",");
								} catch (Exception e) {
									System.out.println("ERROR: Input parameter '--inputData' recognized, but issue parsing out value.");
									e.printStackTrace();
								}
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
				if (args[i].toLowerCase().contains("--maxthreads") == true) {
					maxThreads = Integer.parseInt(thisManager.ImageJ_ReadParameter("--maxthreads=",args[i],1));
				} else if (args[i].toLowerCase().contains("--timeout") == true) {
					timeout = Integer.parseInt(thisManager.ImageJ_ReadParameter("--timeout=",args[i],1));
				} else if (args[i].toLowerCase().contains("--retryfails") == true) {
					retryFails = Integer.parseInt(thisManager.ImageJ_ReadParameter("--retryfailes=",args[i],1));
				} else if (args[i].toLowerCase().contains("--gui") == true) {
					gui = Boolean.parseBoolean(thisManager.ImageJ_ReadParameter("--gui=",args[i],2));
				} else if (args[i].toLowerCase().contains("--progcommand") == true) {
					progCommand = thisManager.ImageJ_ReadParameter("--progcommand=",args[i],0);
				} else if (args[i].toLowerCase().contains("--progdir") == true) {
					progDir = thisManager.ImageJ_ReadParameter("--progdir=",args[i],0);
				} else if (args[i].toLowerCase().contains("--inputscriptdir") == true) {
					inputScriptDir = thisManager.ImageJ_ReadParameter("--inputscriptdir=",args[i],0);
				} else if (args[i].toLowerCase().contains("--inputscript") == true) {
					inputScript = thisManager.ImageJ_ReadParameter("--inputscript=",args[i],0);
				} else if (args[i].toLowerCase().contains("--inputdatadir") == true) {
					inputDataDir = thisManager.ImageJ_ReadParameter("--inputdatadir=",args[i],0);
				} else if (args[i].toLowerCase().contains("--inputdata") == true) {
					try {
						String inputArg = args[i];
						inputArg = inputArg.toLowerCase().replace("--inputdata=", "");
						inputData = inputArg.split(",");
					} catch (Exception e) {
						System.out.println("ERROR: Input parameter '--inputData' recognized, but issue parsing out value.");
						e.printStackTrace();
					}
				} 
			}
			// if inputData == null and inputDataDir != null, read all image files in that immediate directory
			if ((inputData == null || inputData.length == 0) && (inputDataDir != null)) {
				File fileDirectory = new File (inputDataDir);
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
					inputData = new String[f.length];
					for (int i = 0; i < f.length; i++) {
						inputData[i] = f[i].getName();
					}
				}
			}
			for (int i = 0; i < args.length; i++) {
				if (args[i].toLowerCase().contains("--printparam") == true && args[i].toLowerCase().contains("--printparamfile") == false) {
					System.out.println("****These are the recognized input parameters (if not specified by the user, these are the defaults).****");
					System.out.println("maxThreads = " + maxThreads);
					System.out.println("timeout = " + timeout);
					System.out.println("retryFails = " + retryFails);
					System.out.println("gui = " + gui);
					System.out.println("progCommand = " + progCommand);
					System.out.println("progDir = " + progDir);
					System.out.println("inputScript = " + inputScript);
					System.out.println("inputScriptDir = " + inputScriptDir);
					System.out.println("inputData = ");
					if (inputData != null) {
						for (int j = 0; j < inputData.length; j++) {
							System.out.println("\t" + inputData[j]);
						}
					} else {
						System.out.println("\tnull");
					}
					System.out.println("inputDataDir = " + inputDataDir);
					System.out.println("****End of input parameters.****");
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
		
		
		thisManager.maxConcurrentThreads = maxThreads;
		thisManager.timeoutTime = timeout;
		thisManager.retryFails = retryFails;
		thisManager.progCommand = progCommand;
		thisManager.progDirectory = progDir;
		thisManager.scriptLocation = inputScriptDir;
		thisManager.scriptFilename = inputScript;
		thisManager.imageLocations = inputDataDir;
		thisManager.imageFilenames = inputData;
		thisManager.ImageJ_StartJobs();
		
		System.out.println("---");
		System.out.println("Finished, closed.");
	}
	
	public ImageJ_Manager() {
		
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
		return returnValue;
	}
	
	String progCommand = "";
	String progDirectory = "";
	int maxConcurrentThreads = 4;
	int timeoutTime = 60000;
	int retryFails = 2;
	String scriptLocation = "";
	String scriptFilename = "";
	String imageLocations = "";
	String[] imageFilenames;
	
	public void ImageJ_StartJobs() {
		
		System.out.println("System OS: " + System.getProperty("os.name"));
		System.out.println("Total number of system cores: " + Runtime.getRuntime().availableProcessors());
		System.out.println("Total amount of JVM memory (GB): " + String.format("%.4f",Runtime.getRuntime().totalMemory()*0.001f*0.001f*0.001f));
		
		System.out.println("Set to run this many concurrent threads: " + maxConcurrentThreads);
		System.out.println("Defined timeout time (seconds): " + timeoutTime*0.001f);
		long startTime = System.nanoTime();
		
		/*String[] filenames = new String[9];
		filenames[0] = "01_146632_25734_93_glomOnly.tif";
		filenames[1] = "02_146632_25860_322_glomOnly.tif";
		filenames[2] = "03_146632_25877_116_glomOnly.tif";
		filenames[3] = "04_146632_26088_31_glomOnly.tif";
		filenames[4] = "05_146632_26131_139_glomOnly.tif";
		filenames[5] = "06_146632_26491_197_glomOnly.tif";
		filenames[6] = "07_146632_26494_95_glomOnly.tif";
		filenames[7] = "08_146632_26514_141_glomOnly.tif";
		filenames[8] = "09_146632_26545_9_glomOnly.tif";*/
		boolean[] threadSuccess = new boolean[imageFilenames.length];
		
		ExecutorService execService = Executors.newFixedThreadPool(maxConcurrentThreads);
		ImageJ_Thread [] processThreads = new ImageJ_Thread[imageFilenames.length];
		
		for (int i = 0; i < imageFilenames.length; i++) {
			processThreads[i] = new ImageJ_Thread();
			processThreads[i].threadIndex = i + 1;
			processThreads[i].threadTotal = imageFilenames.length;
			processThreads[i].sysCommand = progDirectory + progCommand + " --file-name " + imageLocations + imageFilenames[i] + " -macro " + scriptLocation + scriptFilename;
			processThreads[i].milisecondsTimeout = timeoutTime;
		}
		
		for (int i = 0; i < imageFilenames.length; i++) {
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
		for (int i = 0; i < imageFilenames.length; i++) {
			threadSuccess[i] = !processThreads[i].forcedClosed;
			// (compare time in seconds)
			if (processThreads[i].timeFinished < (timeoutTime*0.001f) - 1) {
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
		for (int i = 0; i < imageFilenames.length; i++)
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
		
		while (retryFails > 0 && failedRuns > 0) {
			System.out.println("Retry allowed for failed threads. Retries left: " + retryFails);
			retryFails--;
			failedRuns = 0;
			
			execService = Executors.newFixedThreadPool(maxConcurrentThreads);
			
			for (int i = 0; i < imageFilenames.length; i++) {
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
			
			for (int i = 0; i < imageFilenames.length; i++) {
				threadSuccess[i] = !processThreads[i].forcedClosed;
			}
			
			System.out.println("");
			System.out.println("All threads finished.");
			totalSeconds = (System.nanoTime() - startTime)*0.000000001f;
			System.out.println("Total execution time (seconds): " + String.format("%.0f",totalSeconds) + "   |   (minutes): " + String.format("%.2f",(totalSeconds/60f)));
			for (int i = 0; i < imageFilenames.length; i++)
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
	}
	
}
