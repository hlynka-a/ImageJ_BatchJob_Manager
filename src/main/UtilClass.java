package main;

public class UtilClass {

	
	public double returnAverage(double[] num) {
		// Follow "avg" logic as considered standard (refer to Microsoft Excel implementation)
		
		double returnValue = 0;
		
		if (num == null || num.length == 0) {
			
		} else {
			double sum = 0;
			for (int i = 0; i < num.length; i++) {
				sum += num[i];
			}
			returnValue = sum / num.length;
		}
		
		return returnValue;
	}
}
