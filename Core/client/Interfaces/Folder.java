package Core.client.Interfaces;

import java.io.File;
import java.util.ArrayList;

import Core.client.sounds.SoundLoader;

public class Folder {
	
	public Folder(String fullPath, String name){
		this.fullPath = fullPath;
		Name = name;
		
		rootFolderFullPath = SoundLoader.removeLastThing(fullPath);
	}
	
	public ArrayList<File> AllMusicFiles = new ArrayList<File>();
	
	public String fullPath;
	public String Name;
	public String rootFolderFullPath;
}
