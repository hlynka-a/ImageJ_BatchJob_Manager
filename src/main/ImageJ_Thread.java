package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ImageJ_Thread implements Runnable{

	public String sysCommand = "";
	public int threadIndex = 0;
	public int threadTotal = 0;
	public int milisecondsTimeout = 60000;
	
	public boolean threadFinished = false;
	boolean forcedClosed = false;
	public double timeFinished = 0;
	
	public void run() {
		forcedClosed = false;
		threadFinished = false;
		Timer t = new Timer();
		long startTime = System.nanoTime();
		try {
			System.out.println(">> Running thread " + threadIndex + " of " + threadTotal + " ... " + sysCommand);
			
			Process process = Runtime.getRuntime().exec(sysCommand);
			
			
			t.schedule(new TimerTask() {
				@Override
				public void run() {
					System.out.println(">>>> ERROR (thread " + threadIndex + ") has not automatically closed after " + milisecondsTimeout * 0.001f +" seconds. Force it to close and move on.");
					forcedClosed = true;
					process.destroyForcibly();
					System.out.println(">> Thread " + threadIndex + " FAILED and was forced closed (due to timeout).");
					threadFinished = true;
				}
			}, milisecondsTimeout);	//1000 = 1 second
			
			// ImageJ does not seem to print anything out to command line, so this doesn't have any effect...
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(">>" + line);
			}
			reader.close();
			
			process.waitFor();
			
			if (forcedClosed == false) {
				System.out.println(">> Thread " + threadIndex + " has finished SUCCESSFULLY on its own. Finished thread.");
			}
			threadFinished = true;
		} catch(IOException e) {
			System.out.println(">>>> ERROR (thread " + threadIndex + " ) >> " + e.getStackTrace());
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println(">>>> ERROR (thead " + threadIndex + " ) >> " + e.getStackTrace());
			e.printStackTrace();
		}
		t.cancel();
		timeFinished = (System.nanoTime() - startTime)*0.000000001f;
		return;
	}
}
