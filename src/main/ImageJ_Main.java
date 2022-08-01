package main;

import java.util.concurrent.TimeUnit;

public class ImageJ_Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ImageJ_Controller controller = new ImageJ_Controller();
		
		controller.processArgs(args);
		
		if (controller.getGui() == true) {
			ImageJ_Jobs_GUI view = new ImageJ_Jobs_GUI();
			view.StartGUI();
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
		if (controller.getFunctionMode().contains("1") == true) {
			Task task01 = controller.findTask("01");
			controller.executeTask(task01);
		}
			
		if (controller.getFunctionMode().contains("2") == true) {
			Task task02 = controller.findTask("02");
			controller.executeTask(task02);
		}
			
		if (controller.getFunctionMode().contains("3") == true) {
			controller.CombineCSV_Start();
		}
		
		UtilClass.DebugOutput("---");
		UtilClass.DebugOutput("Finished, closed.");
	}

}
