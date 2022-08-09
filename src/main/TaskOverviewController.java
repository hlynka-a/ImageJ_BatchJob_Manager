package main;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TaskOverviewController {

	@FXML
	private TextArea task1syscmd;
	@FXML
	private TextField task1timeout;
	@FXML
	private TextField task1retrylimit;
	@FXML
	private TextField task1maxthreads;
	@FXML
	private TextField task1input1;
	@FXML
	private TextField task1input2;
	@FXML
	private TextField task1input3;
	@FXML
	private TextField task1input4;
	@FXML
	private TextField task1input5;
	@FXML
	private TextField task1input6;
	@FXML
	private TextField task1input7;
	@FXML
	private TextField task1input8;
	@FXML
	private TextField task1input9;
	@FXML
	private TextField task1imageinput;
	@FXML
	private TextField task1imagedir;
	@FXML
	private TextArea task2syscmd;
	@FXML
	private TextField task2timeout;
	@FXML
	private TextField task2retrylimit;
	@FXML
	private TextField task2maxthreads;
	@FXML
	private TextField task2input1;
	@FXML
	private TextField task2input2;
	@FXML
	private TextField task2input3;
	@FXML
	private TextField task2input4;
	@FXML
	private TextField task2input5;
	@FXML
	private TextField task2input6;
	@FXML
	private TextField task2input7;
	@FXML
	private TextField task2input8;
	@FXML
	private TextField task2input9;
	@FXML
	private TextField task2imageinput;
	@FXML
	private TextField task2imagedir;
	
	@FXML
	private Button executeButton;
	@FXML
	private Button cancelButton;
	
	@FXML
	private CheckBox task1checkbox;
	@FXML
	private CheckBox task2checkbox;
	@FXML
	private CheckBox task3checkbox;
	
	@FXML
	private Label statusLabel;
	
	@FXML
	private TextArea debugWindow;
	
	
	private MainApp mainApp;
	
	long startExecutionTime = 0;
	ImageJ_Thread [] processThreads;
	ExecutorService execService;
	int cancelRequest = 0;
	
	public TaskOverviewController() {
		
	}
	
	@FXML
	private void initialize() {
		//Listeners for text fields
		task1timeout.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("01", mainApp.getTaskData());
			task1.setTasktimeout(Integer.valueOf(newValue));
		    System.out.println("task1timeout changed from " + oldValue + " to " + task1.getTaskTimeout());
		});
		task1retrylimit.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("01", mainApp.getTaskData());
			task1.setTaskretryFails(Integer.valueOf(newValue));
		    System.out.println("task1retry changed from " + oldValue + " to " + task1.getTaskRetryFails());
		});
		task1maxthreads.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("01", mainApp.getTaskData());
			task1.setMaxThreads(Integer.valueOf(newValue));
		});
		task1input1.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("01", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task1.taskinput[0] = propertyArray;
		});
		task1input2.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("01", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task1.taskinput[1] = propertyArray;
		});
		task1input3.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("01", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task1.taskinput[2] = propertyArray;
		});
		task1input4.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("01", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task1.taskinput[3] = propertyArray;
		});
		task1input5.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("01", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task1.taskinput[4] = propertyArray;
		});
		task1input6.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("01", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task1.taskinput[5] = propertyArray;
		});
		task1input7.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("01", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task1.taskinput[6] = propertyArray;
		});
		task1input8.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("01", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task1.taskinput[7] = propertyArray;
		});
		task1input9.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("01", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task1.taskinput[8] = propertyArray;
		});
		task1imageinput.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("01", mainApp.getTaskData());
			task1.setTaskimages(newValue);
		});
		task1imagedir.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("01", mainApp.getTaskData());
			task1.setTaskimagesDir(newValue);
		});
		
		//Listeners for text fields Task 2
		task2timeout.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task2 = findTask("02", mainApp.getTaskData());
			task2.setTasktimeout(Integer.valueOf(newValue));
		});
		task2retrylimit.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task2 = findTask("02", mainApp.getTaskData());
			task2.setTaskretryFails(Integer.valueOf(newValue));
		});
		task2maxthreads.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task2 = findTask("02", mainApp.getTaskData());
			task2.setMaxThreads(Integer.valueOf(newValue));
		});
		task2input1.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task2 = findTask("02", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task2.taskinput[0] = propertyArray;
		});
		task2input2.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task2 = findTask("02", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task2.taskinput[1] = propertyArray;
		});
		task2input3.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task2 = findTask("02", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task2.taskinput[2] = propertyArray;
		});
		task2input4.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task2 = findTask("02", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task2.taskinput[3] = propertyArray;
		});
		task2input5.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task2 = findTask("02", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task2.taskinput[4] = propertyArray;
		});
		task2input6.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task2 = findTask("02", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task2.taskinput[5] = propertyArray;
		});
		task2input7.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task2 = findTask("02", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task2.taskinput[6] = propertyArray;
		});
		task2input8.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task2 = findTask("02", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task2.taskinput[7] = propertyArray;
		});
		task2input9.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task2 = findTask("02", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task2.taskinput[8] = propertyArray;
		});
		task2imageinput.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task2 = findTask("02", mainApp.getTaskData());
			task2.setTaskimages(newValue);
		});
		task2imagedir.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task2 = findTask("02", mainApp.getTaskData());
			task2.setTaskimagesDir(newValue);
		});
		
		task1checkbox.setSelected(false);
		task2checkbox.setSelected(true);
		task3checkbox.setSelected(true);
		
		task1checkbox.setOnAction(value -> {
			String currentFunctionMode = mainApp.getFunctionMode();
			if(task1checkbox.isSelected()) {
				if(!currentFunctionMode.contains("1")) {
					mainApp.setFunctionMode(currentFunctionMode + "1");
				}
			}
			else {
				if(currentFunctionMode.contains("1")) {
					mainApp.setFunctionMode(currentFunctionMode.replace("1", ""));
				}
			}
			System.out.println("function mode is now: " + mainApp.getFunctionMode());
		});
		
		task2checkbox.setOnAction(value -> {
			String currentFunctionMode = mainApp.getFunctionMode();
			if(task2checkbox.isSelected()) {
				if(!currentFunctionMode.contains("2")) {
					mainApp.setFunctionMode(currentFunctionMode + "2");
				}
			}
			else {
				if(currentFunctionMode.contains("2")) {
					mainApp.setFunctionMode(currentFunctionMode.replace("2", ""));
				}
			}
			System.out.println("function mode is now: " + mainApp.getFunctionMode());
		});
		
		task3checkbox.setOnAction(value -> {
			String currentFunctionMode = mainApp.getFunctionMode();
			if(task3checkbox.isSelected()) {
				if(!currentFunctionMode.contains("3")) {
					mainApp.setFunctionMode(currentFunctionMode + "3");
				}
			}
			else {
				if(currentFunctionMode.contains("3")) {
					mainApp.setFunctionMode(currentFunctionMode.replace("3", ""));
				}
			}
			System.out.println("function mode is now: " + mainApp.getFunctionMode());
		});
		
		executeButton.setOnAction(value ->  {
	           statusLabel.setText("RUNNING...");
	           handleExecute();
	        });
		
		cancelButton.setOnAction(value ->  {
	           statusLabel.setText("CANCELING...");
	           handleCancel();
	        });
		
		debugWindow.textProperty().addListener(new ChangeListener<Object>() {
		    @Override
		    public void changed(ObservableValue<?> observable, Object oldValue,
		            Object newValue) {
		        debugWindow.setScrollTop(Double.MAX_VALUE); //this will scroll to the bottom
		        //use Double.MIN_VALUE to scroll to the top
		    }
		});
	}
	
	@FXML
	private void handleExecute() {
		
		if (mainApp.getFunctionMode().contains("1") == true) {
			Task task01 = this.findTask("01", mainApp.getTaskData());
			executeTask(task01);
		}
			
		if (mainApp.getFunctionMode().contains("2") == true) {
			Task task02 = this.findTask("02", mainApp.getTaskData());
			executeTask(task02);
		}
			
		if (mainApp.getFunctionMode().contains("3") == true) {
			CombineCSV_Start();
		}
	}
	
	@FXML
	private void handleCancel() {
		
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		
		//addTaskData(mainApp.getTaskData());
	}
	
	public void addTaskData(ObservableList<Task> tasks) {
		Task task1 = findTask("01", tasks);
		Task task2 = findTask("02", tasks);
		
		task1syscmd.setText(task1.getTaskCmd());
		task1timeout.setText(Integer.toString(task1.getTaskTimeout()));
		task1retrylimit.setText(Integer.toString(task1.getTaskRetryFails()));
		task1maxthreads.setText(Integer.toString(task1.getMaxThreads()));
		task1input1.setText(task1.getTaskDataAsString(task1.getTaskInput(0)));
		task1input2.setText(task1.getTaskDataAsString(task1.getTaskInput(1)));
		task1input3.setText(task1.getTaskDataAsString(task1.getTaskInput(2)));
		task1input4.setText(task1.getTaskDataAsString(task1.getTaskInput(3)));
		task1input5.setText(task1.getTaskDataAsString(task1.getTaskInput(4)));
		task1input6.setText(task1.getTaskDataAsString(task1.getTaskInput(5)));
		task1input7.setText(task1.getTaskDataAsString(task1.getTaskInput(6)));
		task1input8.setText(task1.getTaskDataAsString(task1.getTaskInput(7)));
		task1input9.setText(task1.getTaskDataAsString(task1.getTaskInput(8)));
		task1imageinput.setText(task1.getTaskImages());
		task1imagedir.setText(task1.getTaskImagesDir());
		
		task2syscmd.setText(task2.getTaskCmd());
		task2timeout.setText(Integer.toString(task2.getTaskTimeout()));
		task2retrylimit.setText(Integer.toString(task2.getTaskRetryFails()));
		task2maxthreads.setText(Integer.toString(task2.getMaxThreads()));
		task2input1.setText(task2.getTaskDataAsString(task2.getTaskInput(0)));
		task2input2.setText(task2.getTaskDataAsString(task2.getTaskInput(1)));
		task2input3.setText(task2.getTaskDataAsString(task2.getTaskInput(2)));
		task2input4.setText(task2.getTaskDataAsString(task2.getTaskInput(3)));
		task2input5.setText(task2.getTaskDataAsString(task2.getTaskInput(4)));
		task2input6.setText(task2.getTaskDataAsString(task2.getTaskInput(5)));
		task2input7.setText(task2.getTaskDataAsString(task2.getTaskInput(6)));
		task2input8.setText(task2.getTaskDataAsString(task2.getTaskInput(7)));
		task2input9.setText(task2.getTaskDataAsString(task2.getTaskInput(8)));
		task2imageinput.setText(task2.getTaskImages());
		task2imagedir.setText(task2.getTaskImagesDir());
	}
	
	public Task findTask(String number, ObservableList<Task> tasks) {
		for(int i=0; i < tasks.size(); i++) {
			if(tasks.get(i).taskNumber.equals(number)) {
				return tasks.get(i);
			}
		}
		UtilClass.DebugOutput("Could not find task with number " + number);
		return new Task();
	}
	
	private void fillTaskDetails(Task task) {
		if(task.getTaskNumber() == "1") {
			task1syscmd.setText(task.getTaskCmd());
			task1timeout.setText(Integer.toString(task.getTaskTimeout()));
			task1retrylimit.setText(Integer.toString(task.getTaskRetryFails()));
			task1maxthreads.setText(Integer.toString(task.getMaxThreads()));
			task1input1.setText(task.getTaskDataAsString(task.getTaskInput(0)));
			task1input2.setText(task.getTaskDataAsString(task.getTaskInput(1)));
			task1input3.setText(task.getTaskDataAsString(task.getTaskInput(2)));
			task1input4.setText(task.getTaskDataAsString(task.getTaskInput(3)));
			task1input5.setText(task.getTaskDataAsString(task.getTaskInput(4)));
			task1input6.setText(task.getTaskDataAsString(task.getTaskInput(5)));
			task1input7.setText(task.getTaskDataAsString(task.getTaskInput(6)));
			task1input8.setText(task.getTaskDataAsString(task.getTaskInput(7)));
			task1input9.setText(task.getTaskDataAsString(task.getTaskInput(8)));
			task1imageinput.setText(task.getTaskImages());
			task1imagedir.setText(task.getTaskImagesDir());
		}
		else if(task.getTaskNumber() == "2") {
			task2syscmd.setText(task.getTaskCmd());
			task2timeout.setText(Integer.toString(task.getTaskTimeout()));
			task2retrylimit.setText(Integer.toString(task.getTaskRetryFails()));
			task2maxthreads.setText(Integer.toString(task.getMaxThreads()));
			task2input1.setText(task.getTaskDataAsString(task.getTaskInput(0)));
			task2input2.setText(task.getTaskDataAsString(task.getTaskInput(1)));
			task2input3.setText(task.getTaskDataAsString(task.getTaskInput(2)));
			task2input4.setText(task.getTaskDataAsString(task.getTaskInput(3)));
			task2input5.setText(task.getTaskDataAsString(task.getTaskInput(4)));
			task2input6.setText(task.getTaskDataAsString(task.getTaskInput(5)));
			task2input7.setText(task.getTaskDataAsString(task.getTaskInput(6)));
			task2input8.setText(task.getTaskDataAsString(task.getTaskInput(7)));
			task2input9.setText(task.getTaskDataAsString(task.getTaskInput(8)));
			task2imageinput.setText(task.getTaskImages());
			task2imagedir.setText(task.getTaskImagesDir());
		}
	}
	
	public void processArgs(String[] args) {
		if (args != null && args.length != 0) {
			for (int i = 0; i < args.length; i++) {
				if (args[i].toLowerCase().contains("--help") == true) {
					// UPDATE: Generalized into assuming 3 different jobs, with better support for how variable arguements will be handled.
					PrintHelpDocs(UtilClass.version);
					return;
				}
			}
			for (int i = 0; i < args.length; i++) {
				if (args[i].toLowerCase().contains("--printparamfile") == true) {
					PrintExampleParamFile();
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
						processInputParameters(allArgs);
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
			processInputParameters(allArgs);

			for (int i = 0; i < args.length; i++) {
				if (args[i].toLowerCase().contains("--printparam") == true && args[i].toLowerCase().contains("--printparamfile") == false) {
					mainApp.setPrintParam(true);
				}
			}
		} else {
			UtilClass.DebugOutput("No parameters provided. Will run with defaults.");
			UtilClass.DebugOutput("(Run again with parameter '--help' to print out parameter options and other information.)");
			UtilClass.DebugOutput("(Run again with parameter '--printParam' to print out default or specified parameters for this execution.)");
		}
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
	
	public void processInputParameters(List<String> args) {
		//First, deal with the variables that don't have to do with tasks
		for (int i=0; i < args.size(); i++) {
			String currentArg = args.get(i);
			if (currentArg.toLowerCase().contains("--gui") == true) {
				mainApp.setGui(Boolean.parseBoolean(UtilClass.ImageJ_ReadParameter("--gui=",currentArg,2)));
			} else if (currentArg.toLowerCase().contains("--functionmode") == true) {
				mainApp.setFunctionMode(UtilClass.ImageJ_ReadParameter("--functionmode=",currentArg,0));
				mainApp.setFunctionModeList(Arrays.asList(mainApp.getFunctionMode().split("")));
			}
		}
		//Next, deal with the tasks- make new tasks for each one in the map, and add it to the list of overall tasks
		Map<String, List<String>> taskMap = processTaskParams(args);
		for(String task: taskMap.keySet()) {
			UtilClass.DebugOutput("Task: " + task);
		}
		for (String task : taskMap.keySet()) {
			List<String> taskWords = taskMap.get(task);
			Task t = new Task(task, taskWords);
			mainApp.getTaskData().add(t);
			UtilClass.DebugOutput("Task added: " + task);
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
			//this is not a great way to code this, but the task number is index 6-8 of the word
			String taskNumber = taskWord.substring(6,8);
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
	
	public int RunGenericTask(String taskNumber, boolean singleThread) {
		
		
		Task task = findTask(taskNumber, mainApp.getTaskData());
		//get info from GUI
		//populateListsFromGUI();
		
		//Get all the variables relevant to this task
		String taskDescription = task.getTaskDescription();
		int taskMaxThreadCount = task.getMaxThreads();
		int taskTimeout = task.getTaskTimeout();
		int taskRetryFail = task.getTaskRetryFails();
		String taskCmd = task.getTaskCmd();
		StringProperty[][]taskInput = task.getTaskinput();
		
		//We index these differently, so subtract 1 from the indexes
		int taskImage = Integer.parseInt(task.getTaskImages().replace("|", ""))-1;			//indicating variable 1 - 9, should be a number
		int taskimagesDir = Integer.parseInt(task.getTaskImagesDir().replace("|", ""))-1;
		
		int taskInputImageLength = 1;
		if(singleThread == false) {
			taskInputImageLength = taskInput[taskImage].length;
		}
		
		UtilClass.DebugOutput("System OS: " + System.getProperty("os.name"));
		UtilClass.DebugOutput("Total number of system cores: " + Runtime.getRuntime().availableProcessors());
		UtilClass.DebugOutput("Total amount of JVM memory (GB): " + String.format("%.4f",Runtime.getRuntime().totalMemory()*0.001f*0.001f*0.001f));
		
		UtilClass.DebugOutput("Set to run this many concurrent threads: " + taskMaxThreadCount);
		UtilClass.DebugOutput("Defined timeout time (seconds): " + taskTimeout*0.001f);
		
		if(mainApp.getGui() == true) {
			debugWindow.setText(debugWindow.getText() + "System OS: " + System.getProperty("os.name") + "\n");
			debugWindow.setText(debugWindow.getText() + "Total number of system cores: " + Runtime.getRuntime().availableProcessors() + "\n");
			debugWindow.setText(debugWindow.getText() + "Total amount of JVM memory (GB): " + String.format("%.4f",Runtime.getRuntime().totalMemory()*0.001f*0.001f*0.001f) + "\n");
			
			debugWindow.setText(debugWindow.getText() + "Set to run this many concurrent threads: " + taskMaxThreadCount + "\n");
			debugWindow.setText(debugWindow.getText() + "Defined timeout time (seconds): " + taskTimeout*0.001f + "\n");
			debugWindow.appendText("");
		}
		
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
					String[] taskInputString = task.getTaskInput(j);
					sysCommand = sysCommand.replace("||" + j + "||", taskInputString[0]);
				}
			}
			if(singleThread == false) {
				String[] taskInputString = task.getTaskInput(taskImage);
				sysCommand = sysCommand.replace("||" + taskImage + "||", taskInputString[i]);
			}
			processThreads[i].sysCommand = sysCommand;
			//processThreads[i].sysCommand = "C:\\Users\\richeym\\ImageJ_BatchJob\\JavaBatchManager_v1-05_complete\\JavaBatchManager_v1-05_complete\\install_imagej\\fiji-win64\\fiji.app\\imagej-win64.exe";
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
	
	public void executeTask(Task task) {
		UtilClass.DebugOutput("Inside execute task");
		if (task.getTaskImages().length()>0 && task.getTaskImagesDir().length()>0) {
			int taskimagesNum = Integer.parseInt(task.getTaskImages().replace("|", ""));
			int taskimagesDirNum = Integer.parseInt(task.getTaskImagesDir().replace("|", ""));
			UtilClass.DebugOutput("taskimagesNum:" + taskimagesNum);
			if (taskimagesNum == -1 && taskimagesDirNum == -1) {
				UtilClass.DebugOutput("no image input");
				// assume no image input, just run as 1 job with literal command.
			}
			else if ((task.taskinput[taskimagesNum] == null || task.taskinput[taskimagesNum].length == 0) && (task.taskinput[taskimagesDirNum] != null)) {
				// get input images list, used to determine how many jobs need to be done (assume 1 image per job) 
				String[] taskInputArray = task.getTaskInput(taskimagesDirNum);
				File fileDirectory = new File (taskInputArray[0]);
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
					String[] newTaskStringArray = new String[f.length];
					task.setTaskinput(taskimagesNum, newTaskStringArray);
					for (int i = 0; i < f.length; i++) {
						task.setTaskinput(taskimagesNum, i, f[i].getName());
					}
				}	
			}
		}
		if (mainApp.getPrintParam() == true) {
			String taskString = "task" + task.taskNumber;
			UtilClass.DebugOutput("****These are the recognized input parameters (if not specified by the user, these are the defaults).****");
			UtilClass.DebugOutput("gui = " + mainApp.getGui());
			UtilClass.DebugOutput("functionMode = " + mainApp.getFunctionMode());
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
		int taskImages = Integer.parseInt(task.getTaskImages().replace("|", ""));			//indicating variable 1 - 9, should be a number
		int imagesDir = Integer.parseInt(task.getTaskImagesDir().replace("|", ""));
		//thisManager.ImageJ_StartJobs();
		if (taskImages == -1 && imagesDir == -1) {
			UtilClass.DebugOutput("Running generic task with true");
			RunGenericTask(task.getTaskNumber(), true);
		} else {
			UtilClass.DebugOutput("Running generic task with false");
			RunGenericTask(task.getTaskNumber(), false);
		}
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
		Task task2 = findTask("02", mainApp.getTaskData());
		int imagesDir = Integer.parseInt(task2.getTaskImagesDir().replace("|", ""));
		String[] taskInputArray = task2.getTaskInput(imagesDir);
		if (taskInputArray[0] != null) {
			File fileDirectory = new File (taskInputArray[0]);
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
			File inputFile = new File(taskInputArray[0] + csvFiles[i]);
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
			FileWriter writeCsvFile = new FileWriter(taskInputArray[0] + "summary_"+currentDateText+".csv");
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
}
