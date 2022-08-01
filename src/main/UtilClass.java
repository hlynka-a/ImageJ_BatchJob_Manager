package main;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class UtilClass {

	
	public static JTextArea jtextarea_debug;
	public static JLabel jlabel_status;
	public static JPanel jpanel_status;
	public static boolean gui = false;
	public static String version = "v0.96 | 2021-06-29";
	
	public static void DebugOutput(String message) {
		System.out.println(message);
    	
		if (gui == true && jtextarea_debug != null) {
			jtextarea_debug.setText(jtextarea_debug.getText() + "\n" + message);
		}
	}
	
	public static void DebugOutputNoLine(String message) {
		System.out.print(message);
    	
		if (gui == true && jtextarea_debug != null) {
			jtextarea_debug.setText(jtextarea_debug.getText() + message);
		}
	}
	
	public static void StatusOutput(String message, Color newC) {
		if (gui == true && jlabel_status != null) {
			jlabel_status.setText(message);
			jpanel_status.setBackground(newC);
		}
	}
	
	public static String ImageJ_ReadParameter(String removeString, String inputArg, int type) {
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
	
	public static String[] ImageJ_ReadParameterArray(String removeString, String inputArg) {
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
}
