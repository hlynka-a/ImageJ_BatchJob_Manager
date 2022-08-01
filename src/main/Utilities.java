package main;

public final class Utilities {
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
