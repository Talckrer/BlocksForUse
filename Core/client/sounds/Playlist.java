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
		
		if (isPlaylist(file)){
			ReloadFile();
		}
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
		
		
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write("Playlist");
			out.newLine();
			       
			if (!songs.isEmpty()){
				for (File file : songs) {
					out.write(file.getAbsolutePath());
						out.newLine();
				}
			}
			out.close();
			
		 }catch (IOException e) {
		    e.printStackTrace();
		 }
	}
	
	public void removeSong(File file){
		ReloadFile();
		
		if (!songs.isEmpty()){
			for (int i = 0; i < songs.size();i++){
				File file1 = songs.get(i);
				if (file1.getAbsolutePath().equals(file.getAbsolutePath())){
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
