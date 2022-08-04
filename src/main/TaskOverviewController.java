package main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
	
	private MainApp mainApp;
	
	public TaskOverviewController() {
		
	}
	
	@FXML
	private void initialize() {
		//Listeners for text fields
		task1timeout.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("1", mainApp.getTaskData());
			task1.setTasktimeout(Integer.valueOf(newValue));
		    System.out.println("task1timeout changed from " + oldValue + " to " + task1.getTaskTimeout());
		});
		task1retrylimit.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("1", mainApp.getTaskData());
			task1.setTaskretryFails(Integer.valueOf(newValue));
		    System.out.println("task1retry changed from " + oldValue + " to " + task1.getTaskRetryFails());
		});
		task1maxthreads.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("1", mainApp.getTaskData());
			task1.setMaxThreads(Integer.valueOf(newValue));
		});
		task1input1.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("1", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task1.taskinput[1] = propertyArray;
		});
		task1input2.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("1", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task1.taskinput[2] = propertyArray;
		});
		task1input3.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("1", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task1.taskinput[3] = propertyArray;
		});
		task1input4.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("1", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task1.taskinput[4] = propertyArray;
		});
		task1input5.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("1", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task1.taskinput[5] = propertyArray;
		});
		task1input6.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("1", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task1.taskinput[6] = propertyArray;
		});
		task1input7.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("1", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task1.taskinput[7] = propertyArray;
		});
		task1input8.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("1", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task1.taskinput[8] = propertyArray;
		});
		task1input9.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("1", mainApp.getTaskData());
			String[] newValueArray = newValue.split(",");
			StringProperty[] propertyArray = new StringProperty[newValueArray.length];
			for(int k=0; k < newValueArray.length; k++) {
				propertyArray[k] = new SimpleStringProperty(newValueArray[k]);
			}
			task1.taskinput[9] = propertyArray;
		});
		task1imageinput.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("1", mainApp.getTaskData());
			task1.setTaskimages(newValue);
		});
		task1imagedir.textProperty().addListener((observable, oldValue, newValue) -> {
			Task task1 = findTask("1", mainApp.getTaskData());
			task1.setTaskimagesDir(newValue);
		});
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		
		addTaskData(mainApp.getTaskData());
	}
	
	public void addTaskData(ObservableList<Task> tasks) {
		Task task1 = findTask("1", tasks);
		Task task2 = findTask("2", tasks);
		
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
		task2imageinput.setText(task1.getTaskImages());
		task2imagedir.setText(task1.getTaskImagesDir());
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
}
