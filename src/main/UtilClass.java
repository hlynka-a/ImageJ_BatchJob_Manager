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
	
}
