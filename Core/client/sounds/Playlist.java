package Core.client.sounds;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Playlist {
	
	public Playlist(File file){
		
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.file = file;
		songs = new ArrayList<File>();
		
	}
	
	public File file;
	public ArrayList<File> songs;
	
	
	public void readPlaylist(){
		songs = new ArrayList<File>();
		
		if (file.canRead()){
			Scanner scanner = null;
			try {
				scanner = new Scanner(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			String buffer;
			while (scanner.hasNextLine()){
				buffer = scanner.nextLine();
				songs.add(new File(buffer));
				System.out.println(buffer);
			}
		}
	}
	
	public void removeSong(String fullPath){
		if (!songs.isEmpty()){
			for (int i = 0; i < songs.size();i++){
				File file = songs.get(i);
				if (file.getAbsolutePath().equals(fullPath)){
					songs.remove(i);
				}
			}
		}
	}

}
