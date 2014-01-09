package Core.client.sounds;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Playlist {
	
	public Playlist(File file, String name){
		
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.file = file;
		this.name = name;
		songs = new ArrayList<File>();
		
		RewriteFile();
	}
	
	public File file;
	public ArrayList<File> songs;
	public String name;
	
	
	public void ReloadFile(){
		songs = new ArrayList<File>();
		
		if (file.canRead()){
			Scanner scanner = null;
			try {
				scanner = new Scanner(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			String buffer;
			if (scanner.hasNextLine()){
				scanner.nextLine();
			}
			while (scanner.hasNextLine()){
				buffer = scanner.nextLine();
				songs.add(new File(buffer));
				System.out.println(buffer);
			}
		}
	}
	
	public void RewriteFile(){
		file.delete();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (!songs.isEmpty()){
			try {
		        BufferedWriter out = new BufferedWriter(new FileWriter(file));
		        out.write("Playlist" + "\n");
		            for (File file : songs) {
		                out.write(file.getAbsolutePath() + "\n");
		            }
		            out.close();
		    }catch (IOException e) {
		    	e.printStackTrace();
		    }
		}
	}
	
	public void removeSong(String fullPath){
		ReloadFile();
		
		if (!songs.isEmpty()){
			for (int i = 0; i < songs.size();i++){
				File file = songs.get(i);
				if (file.getAbsolutePath().equals(fullPath)){
					songs.remove(i);
				}
			}
			RewriteFile();
		}
	}
	
	public void removeSong(int index){
		ReloadFile();
		
		if (!songs.isEmpty()){
			songs.remove(index);
			RewriteFile();
		}
	}
	
	public void addSong(File file){
		ReloadFile();
		songs.add(file);
		RewriteFile();
	}
	
	public static boolean isPlaylist(File file){
		if (file.canRead()){
			Scanner scanner = null;
			try {
				scanner = new Scanner(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if (scanner.hasNextLine() && scanner.nextLine().equals("Playlist")){
				return true;
			}
		}
		return false;
	}

}
