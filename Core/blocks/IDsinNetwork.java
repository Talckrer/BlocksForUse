package Core.blocks;

import java.util.ArrayList;

public class IDsinNetwork {
	
	public IDsinNetwork(){
		
	}
	
	private ArrayList<Integer> IDs = new ArrayList<Integer>();
	
	public void addID(int id){
		IDs.add(id);
	}
	
	public ArrayList<Integer> getList(){
		return IDs;
	}
}
