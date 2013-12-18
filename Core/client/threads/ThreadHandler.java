package Core.client.threads;

public class ThreadHandler {
	
	public static void Init(){
		
		second = new ThreadSecondUpdate();
		second.start();
		
	}
	
	public static ThreadSecondUpdate second;

}
