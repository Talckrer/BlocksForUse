package Core.client.Interfaces;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;

public class Info {
	
	public Info(){
		
	}
	
	//music
	public static int MP3PlayerIndexToOpen = 0;
	public static GuiMP3Player GuiMp3Player = null;
	public static int SecondsPlayed = 0;
	public static EntityPlayer player = null;
	public static String MusicPath = "";
	public static boolean isShuffle = false;
	public static boolean isRepeat = false;
	
	public static String TempDiscWriterString = "";

	public static ArrayList<MovableSelectionMP3Gui> musicTabs;
	public static ArrayList<MovableSelectionMP3Gui> folderTabs;
	
	//Gui
	public static boolean isSecond = true;
	
}
