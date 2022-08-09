package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	
	private ObservableList<Task> taskData = FXCollections.observableArrayList();
	private boolean printParam;
	private boolean gui;
	private String functionMode;
	private List<String> functionModeList = new ArrayList<String>();
	
	public MainApp() {
	}
	
	public ObservableList<Task> getTaskData(){
		return taskData;
	}
	
	public void setPrintParam(boolean p) {
		this.printParam = p;
	}
	
	public boolean getPrintParam() {
		return this.printParam;
	}
	
	public void setGui(boolean g) {
		this.gui = g;
	}
	
	public boolean getGui() {
		return this.gui;
	}
	
	public void setFunctionMode(String functionMode) {
		this.functionMode = functionMode;
	}
	
	public String getFunctionMode() {
		return this.functionMode;
	}
	
	public void setFunctionModeList(List<String> functionModeList) {
		this.functionModeList = functionModeList;
	}

	@Override
	public void start(Stage primaryStage) {
		
		Parameters params = getParameters();
		List<String> args = params.getRaw();
		
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("ImageJ_BatchJobHandler");
		
		initRootLayout();
		
		showTaskOverview(args);
	}
	
	//Initializes root layout
	public void initRootLayout() {
		try {
			//Load root layout from fxml file
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			
			//Show the scene containing the root layout
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);
			primaryStage.show();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showTaskOverview(List<String> arglist) {
		try {
			//Load view
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("TaskOverview.fxml"));
			AnchorPane taskOverview = (AnchorPane) loader.load();
			
			rootLayout.setCenter(taskOverview);
			
			TaskOverviewController controller = loader.getController();
			String[] args = arglist.toArray(new String[0]);
			
			controller.setMainApp(this);
			controller.processArgs(args);
			for(Task t : taskData) {
				System.out.println("Task" + t.getTaskNumber() + " in list");
				String taskinput1 = t.getTaskDataAsString(t.getTaskInput(4));
				System.out.println("taskinput4 is: " + taskinput1);
				
			}
			Task task1 = controller.findTask("01", taskData);
			Task task2 = controller.findTask("02", taskData);
			controller.addTaskData(taskData);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		//set up to run from command line or from the GUI
		boolean guiParameter = true;
		for(int i=0; i < args.length; i++) {
			String currentArg = args[i];
			if(currentArg.toLowerCase().contains("--gui") == true) {
				guiParameter = Boolean.parseBoolean(UtilClass.ImageJ_ReadParameter("--gui=",currentArg,2));
			}
		}
		if(guiParameter == true) {
			launch(args);
		}
		else {
			System.out.println("GUI is false");
			//Put logic here to run directly from the command line
		}
	}
	
	public void loadConfigDataFromFile(File file) {
		
	}
	
	public File getConfigFilePath() {
		return null;
	}
	
	public void saveConfigDataToFile(File file) {
		try {
			Writer wr = new FileWriter(file);
			wr.write("--gui=true" + "\n");
			Task task1 = findTask("01", taskData);
			Task task2 = findTask("02", taskData);
			
			wr.write("--functionMode=" + functionMode + "\n");
			
			if (task1.getTaskCmd() != null && task1.getTaskCmd().length() > 0) {
				wr.write("--task01cmd=" + task1.getTaskCmd() + "\n");
			}
			if (task1.getTaskDataAsString(task1.getTaskInput(0)) != null && task1.getTaskDataAsString(task1.getTaskInput(0)).length() > 0) {
				wr.write("--task01input01=" + task1.getTaskDataAsString(task1.getTaskInput(0)) + "\n");
			}
			if (task1.getTaskDataAsString(task1.getTaskInput(1)) != null && task1.getTaskDataAsString(task1.getTaskInput(1)).length() > 0) {
				wr.write("--task01input02=" + task1.getTaskDataAsString(task1.getTaskInput(1)) + "\n");
			}
			if (task1.getTaskDataAsString(task1.getTaskInput(2)) != null && task1.getTaskDataAsString(task1.getTaskInput(2)).length() > 0) {
				wr.write("--task01input03=" + task1.getTaskDataAsString(task1.getTaskInput(2)) + "\n");
			}
			if (task1.getTaskDataAsString(task1.getTaskInput(3)) != null && task1.getTaskDataAsString(task1.getTaskInput(3)).length() > 0) {
				wr.write("--task01input04=" + task1.getTaskDataAsString(task1.getTaskInput(3)) + "\n");
			}
			if (task1.getTaskDataAsString(task1.getTaskInput(4)) != null && task1.getTaskDataAsString(task1.getTaskInput(4)).length() > 0) {
				wr.write("--task01input05=" + task1.getTaskDataAsString(task1.getTaskInput(4)) + "\n");
			}
			if (task1.getTaskDataAsString(task1.getTaskInput(5)) != null && task1.getTaskDataAsString(task1.getTaskInput(5)).length() > 0) {
				wr.write("--task01input06=" + task1.getTaskDataAsString(task1.getTaskInput(5)) + "\n");
			}
			if (task1.getTaskDataAsString(task1.getTaskInput(6)) != null && task1.getTaskDataAsString(task1.getTaskInput(6)).length() > 0) {
				wr.write("--task01input07=" + task1.getTaskDataAsString(task1.getTaskInput(6)) + "\n");
			}
			if (task1.getTaskDataAsString(task1.getTaskInput(7)) != null && task1.getTaskDataAsString(task1.getTaskInput(7)).length() > 0) {
				wr.write("--task01input08=" + task1.getTaskDataAsString(task1.getTaskInput(7)) + "\n");
			}
			if (task1.getTaskDataAsString(task1.getTaskInput(8)) != null && task1.getTaskDataAsString(task1.getTaskInput(8)).length() > 0) {
				wr.write("--task01input09=" + task1.getTaskDataAsString(task1.getTaskInput(8)) + "\n");
			}
			if (Integer.toString(task1.getMaxThreads()) != null && Integer.toString(task1.getMaxThreads()).length() > 0) {
				wr.write("--task01maxThreads=" + Integer.toString(task1.getMaxThreads()) + "\n");
			}
			if (Integer.toString(task1.getTaskRetryFails()) != null && Integer.toString(task1.getTaskRetryFails()).length() > 0) {
				wr.write("--task01retryFails=" + Integer.toString(task1.getTaskRetryFails()) + "\n");
			}
			if (Integer.toString(task1.getTaskTimeout()) != null && Integer.toString(task1.getTaskTimeout()).length() > 0) {
				wr.write("--task01timeout=" + Integer.toString(task1.getTaskTimeout()) + "\n");
			}
			if (task1.getTaskImages() != null && task1.getTaskImages().length() > 0) {
				wr.write("--task01images=" + task1.getTaskImages() + "\n");
			}
			if (task1.getTaskImagesDir() != null && task1.getTaskImagesDir().length() > 0) {
				wr.write("--task01imagesDir=" + task1.getTaskImagesDir() + "\n");
			}
			

			if (task2.getTaskCmd() != null && task2.getTaskCmd().length() > 0) {
				wr.write("--task02cmd=" + task2.getTaskCmd() + "\n");
			}
			if (task2.getTaskDataAsString(task2.getTaskInput(0)) != null && task2.getTaskDataAsString(task2.getTaskInput(0)).length() > 0) {
				wr.write("--task02input01=" + task2.getTaskDataAsString(task2.getTaskInput(0)) + "\n");
			}
			if (task2.getTaskDataAsString(task2.getTaskInput(1)) != null && task2.getTaskDataAsString(task2.getTaskInput(1)).length() > 0) {
				wr.write("--task02input02=" + task2.getTaskDataAsString(task2.getTaskInput(1)) + "\n");
			}
			if (task2.getTaskDataAsString(task2.getTaskInput(2)) != null && task2.getTaskDataAsString(task2.getTaskInput(2)).length() > 0) {
				wr.write("--task02input03=" + task2.getTaskDataAsString(task2.getTaskInput(2)) + "\n");
			}
			if (task2.getTaskDataAsString(task2.getTaskInput(3)) != null && task2.getTaskDataAsString(task2.getTaskInput(3)).length() > 0) {
				wr.write("--task02input04=" + task2.getTaskDataAsString(task2.getTaskInput(3)) + "\n");
			}
			if (task2.getTaskDataAsString(task2.getTaskInput(4)) != null && task2.getTaskDataAsString(task2.getTaskInput(4)).length() > 0) {
				wr.write("--task02input05=" + task2.getTaskDataAsString(task2.getTaskInput(4)) + "\n");
			}
			if (task2.getTaskDataAsString(task2.getTaskInput(5)) != null && task2.getTaskDataAsString(task2.getTaskInput(5)).length() > 0) {
				wr.write("--task02input06=" + task2.getTaskDataAsString(task2.getTaskInput(5)) + "\n");
			}
			if (task2.getTaskDataAsString(task2.getTaskInput(6)) != null && task2.getTaskDataAsString(task2.getTaskInput(6)).length() > 0) {
				wr.write("--task02input07=" + task2.getTaskDataAsString(task2.getTaskInput(6)) + "\n");
			}
			if (task2.getTaskDataAsString(task2.getTaskInput(7)) != null && task2.getTaskDataAsString(task2.getTaskInput(7)).length() > 0) {
				wr.write("--task02input08=" + task2.getTaskDataAsString(task2.getTaskInput(7)) + "\n");
			}
			if (task2.getTaskDataAsString(task2.getTaskInput(8)) != null && task2.getTaskDataAsString(task2.getTaskInput(8)).length() > 0) {
				wr.write("--task02input09=" + task2.getTaskDataAsString(task2.getTaskInput(8)) + "\n");
			}
			if (Integer.toString(task2.getMaxThreads()) != null && Integer.toString(task2.getMaxThreads()).length() > 0) {
				wr.write("--task02maxThreads=" + Integer.toString(task2.getMaxThreads()) + "\n");
			}
			if (Integer.toString(task2.getTaskRetryFails()) != null && Integer.toString(task2.getTaskRetryFails()).length() > 0) {
				wr.write("--task02retryFails=" + Integer.toString(task2.getTaskRetryFails()) + "\n");
			}
			if (Integer.toString(task2.getTaskTimeout()) != null && Integer.toString(task2.getTaskTimeout()).length() > 0) {
				wr.write("--task02timeout=" + Integer.toString(task2.getTaskTimeout()) + "\n");
			}
			if (task2.getTaskImages() != null && task2.getTaskImages().length() > 0) {
				wr.write("--task02images=" + task2.getTaskImages() + "\n");
			}
			if (task2.getTaskImagesDir() != null && task2.getTaskImagesDir().length() > 0) {
				wr.write("--task02imagesDir=" + task2.getTaskImagesDir() + "\n");
			}
			wr.flush();
			wr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
}
