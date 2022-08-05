package main;

import java.io.IOException;
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
	
	public void setGui(boolean g) {
		this.gui = g;
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
		launch(args);
	}
}
