package Core.client.sounds;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.filechooser.FileSystemView;

import Core.client.Interfaces.Folder;
import Core.client.Interfaces.GuiColor;
import Core.client.Interfaces.Info;
import Core.client.Interfaces.MovableSelectionMP3Gui;
import Core.client.Interfaces.guis.GuiMP3Player;
import Core.modBFU.BlocksForUse;


public class SoundLoader {
	
	public static ArrayList<File> musicFiles = new ArrayList<File>();
	public static ArrayList<Folder> folders = new ArrayList<Folder>(); 
	
	public static Playlist SelectedPlaylist = null;
	public static boolean PlaylistLoaded = false;
	public static String folderLoaded = "";
	public static ArrayList<File> drives = new ArrayList<File>();
	public static ArrayList<String> driveNames = new ArrayList<String>();
	public static ArrayList<MovableSelectionMP3Gui> ListForMusicTabs = new ArrayList<MovableSelectionMP3Gui>();
	public static ArrayList<MovableSelectionMP3Gui> ListForFolderTabs = new ArrayList<MovableSelectionMP3Gui>();
	
	/**
	 * @param overwrite(true when used with discs to overwrite music)
	 * To load folders and music using folderLoaded
	 */
	public static void loadListForGui(boolean overwrite){
		ListForMusicTabs = new ArrayList<MovableSelectionMP3Gui>();
		ListForFolderTabs = new ArrayList<MovableSelectionMP3Gui>();
		
		if (!PlaylistLoaded){
			if (folderLoaded.equals("")){
				drives = new ArrayList<File>();
				for (File file : File.listRoots()){
					drives.add(file);
				}
				FileSystemView viewer = new FileSystemView() {
					@Override
					public File createNewFolder(File containingDir) throws IOException {
						return null;
					}
				};
				
				folders = new ArrayList<Folder>();
				driveNames = new ArrayList<String>();
				for (File file : drives){
					Folder folder = new Folder(file.getAbsolutePath(), file.getAbsolutePath());
					folders.add(folder);
					driveNames.add(viewer.getSystemDisplayName(file));
				}
				for (int i = 0; i < drives.size(); i++){
					if (!driveNames.get(i).equals("")){
						folders.get(i).Name = driveNames.get(i);
					}
				}
				
			}else{
				if (overwrite){
					readFiles(true, folderLoaded);
				}else{
					readFiles(false, folderLoaded);
				}
			}
		}else{//loading playlist
			musicFiles = new ArrayList<File>();
			folders = new ArrayList<Folder>();
			
			if (SelectedPlaylist != null && !SelectedPlaylist.songs.isEmpty()){
				for (File file : SelectedPlaylist.songs){
					musicFiles.add(file);
				}
			}
			
		}
		
		
		
		if (!folders.isEmpty()){
			for (Folder folder1 : folders){
				ListForFolderTabs.add(new MovableSelectionMP3Gui(folder1));
			}
		}
		if (!musicFiles.isEmpty()){
			for (File file : musicFiles){
				ListForMusicTabs.add(new MovableSelectionMP3Gui(file));
			}
			
//			if (!overwrite && Info.GuiMp3Player != null && Info.MP3PlayerIndexToOpen < Sounds.files.size() && Sounds.canRetrieveInfo(Info.MP3PlayerIndexToOpen)){
//				Info.GuiMp3Player.durationInt = Integer.parseInt(Sounds.getInfo(Info.GuiMp3Player.isPlayingIndex, "durationInt"));
//			}
		}
			
		Info.musicTabs = ListForMusicTabs;
		Info.folderTabs = ListForFolderTabs;
		if (Info.GuiMp3Player != null){
			Info.GuiMp3Player.UpdateLists();
		}
	}
	
	/**
	 * 
	 * @param isFolder
	 * @param isPlaylist
	 * @param index
	 */
	public static void ButtonClicked(boolean isFolder, boolean isPlaylist, int index){
		if (isFolder){
			Info.GuiMp3Player.MeasureVertical = 0;
		}
	
		if (folderLoaded.equals("")){//if you press any drive
			if (index != -1){
				folderLoaded = drives.get(index).getAbsolutePath();
				loadListForGui(false);
			}
			
		}else{
			if (index == -1){//If you press back button
				if (PlaylistLoaded){
					PlaylistLoaded = false;
					loadListForGui(false);
					Info.GuiMp3Player.canCancel = false;
					Info.GuiMp3Player.canChange = true;
					Info.GuiMp3Player.canActivate = false;
					
					if (Info.GuiMp3Player.CurrentAction == 5 || Info.GuiMp3Player.CurrentAction == 6){//Activatable actions
						Info.GuiMp3Player.canActivate = true;
					}
				}else{
					if (folderLoaded.length() == 3){
						folderLoaded = "";
						File[] roots = File.listRoots();
						drives = new ArrayList<File>(1);
				    	for(File file : roots){
				    	    SoundLoader.drives.add(file);
				    	}
					}else{
						folderLoaded = removeLastThing(folderLoaded);
					}
					musicFiles = new ArrayList<File>();
					loadListForGui(false);
				}
				
			}else{
				if (isFolder){
					folderLoaded = folders.get(index).fullPath;
					loadListForGui(false);
				}else{
					Sounds.tempFiles = musicFiles;
					if (GuiMP3Player.getExtension(Sounds.tempFiles.get(index)).equals("mp3")){
						Sounds.stopPlaying();
						Sounds.files = Sounds.tempFiles;
						Info.MP3PlayerIndexToOpen = index;
						Info.SecondsPlayed = 0;
						Info.GuiMp3Player.isPlayingIndex = index;
						Info.GuiMp3Player.continuePlaying = true;
						Info.GuiMp3Player.isPlaying = true;
						
						Info.GuiMp3Player.durationInt = Integer.parseInt(Sounds.getInfo(Info.GuiMp3Player.isPlayingIndex, "durationInt"));
						Sounds.playRecord(Info.MP3PlayerIndexToOpen);
					}else{
						if (Desktop.isDesktopSupported()){
							try{
								Desktop.getDesktop().open(Sounds.tempFiles.get(index));
							}catch (IOException e){
								e.printStackTrace();
								Info.GuiMp3Player.player.addChatMessage(GuiColor.RED + "ERROR: Unable to find default program to open this file");
							}
						}
					}
				}
			}
		}
		
	}
	
	/**
	 * 
	 * @param loadMusic
	 * @param fullPath
	 */
	public static void readFiles(boolean loadMusic, String path){
		
		File[] files = new File(path).listFiles();
		if (files != null){
			
			
			for (File file : files){
				if (file.isDirectory()){
					Folder folder = new Folder(file.getAbsolutePath(), file.getName());
					folders.add(folder);
				}else{
					musicFiles.add(file);
				}
			}
			
			if (loadMusic){
				Sounds.files = musicFiles;
			}
		}
		
		
		
	}
	
	public static String getLastThing(String path){
		return path.substring(path.lastIndexOf(path.charAt(2)) + 1, path.length());
	}
	
	public static String removeLastThing(String path){
		String newString = path.substring(path.lastIndexOf(path.charAt(2)), path.length());
		newString = path.substring(0,path.length() - newString.length());
		if (newString.length() == 2){
			newString += BlocksForUse.BackSlash;
		}
		return newString;
	}
	
	
	
}