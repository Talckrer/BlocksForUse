package Core.client.Interfaces;

import java.io.File;

public class MovableSelectionMP3Gui {
	
	/**
	 * Selection in the MP3Gui as folder or music file.
	 * 
	 * @param isFolder, name, index
	 */
	public MovableSelectionMP3Gui(Folder folder){
		Name = folder.Name;
		isFolder = true;
		this.folder = folder;
	}
	public MovableSelectionMP3Gui(File file){
		Name = file.getName();
		isFolder = false;
		this.file = file;
		
	}
	
	public String Name;
	public boolean isFolder;
	public int guiY = 0;
	public boolean doDraw = false;
	public int ID = 0;
	
	public File file = null;
	public Folder folder = null;
	
}
