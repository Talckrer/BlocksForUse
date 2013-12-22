package Core.tileEntities;

import java.util.TreeSet;

public class ControllerIDs {
	
	public ControllerIDs(){
		lastID = 0;
		FreeIDs = new TreeSet<Integer>();
	}
	
	
	private static int lastID;
	private static TreeSet<Integer> FreeIDs;
	
	public static int getNewID(){
		if (FreeIDs.isEmpty()){
			lastID += 1;
			return lastID;
		}else{
			int id;
			FreeIDs.first();
			id = FreeIDs.first();
			FreeIDs.remove(FreeIDs.first());
			return id;
		}
	}
	
	public static void onBreakBlock(int id){
		FreeIDs.add(id);
	}
	
	public static void onBlockLoad(int id){
		if (id > lastID){
			for (int i = lastID + 1; i < id ;i++){
				FreeIDs.add(i);
			}
			lastID = id;
		}else{
			FreeIDs.remove(id);
		}
	}
}
