package Core.handlers;

import Core.client.threads.ThreadSecondUpdate;

public class ThreadHandler {
	
	public static void Init(){
		
		second = new ThreadSecondUpdate();
		second.start();
		
	}
	
	public static ThreadSecondUpdate second;

}
