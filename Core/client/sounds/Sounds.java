package Core.client.sounds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import net.minecraft.client.Minecraft;

import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;

import Core.client.Interfaces.Info;
import Core.modBFU.BlocksForUse;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class Sounds implements Runnable{
	
	public Sounds(){
		
	}
	
	public static ArrayList<File> files = new ArrayList<File>();
	
	public static ArrayList getList(){
		return files;
	}
	
	public static File getFile(int index){
		return files.get(index);
	}
	
	public static void addFile(File file){
		files.add(file);
	}
	
	public static String getInfo(int index, String type){
		
		File file = files.get(index);
		MP3AudioHeader Mp3audioHeader = null;
		try {
			Mp3audioHeader = new MP3AudioHeader(file);
		} catch (IOException e) {
			if (Info.isShuffle){
				Info.MP3PlayerIndexToOpen = Math.round((float)Math.random() * getList().size()-1);
				
				System.out.println("Skipping unplayable song");
				Info.GuiMp3Player.isPlayingIndex = Info.MP3PlayerIndexToOpen;
				if (canRetrieveInfo(Info.GuiMp3Player.isPlayingIndex)){
					Info.GuiMp3Player.durationInt = Integer.parseInt(getInfo(Info.GuiMp3Player.isPlayingIndex, "durationInt"));
				}
				playRecord(Info.MP3PlayerIndexToOpen);
			}else{
				Info.player.addChatMessage("Can't play, because not mp3");
			}
		} catch (InvalidAudioFrameException e){
			if (Info.isShuffle){
				Info.MP3PlayerIndexToOpen = Math.round((float)Math.random() * getList().size()-1);
				
				System.out.println("Skipping unplayable song");
				Info.GuiMp3Player.isPlayingIndex = Info.MP3PlayerIndexToOpen;
				if (canRetrieveInfo(Info.GuiMp3Player.isPlayingIndex)){
					Info.GuiMp3Player.durationInt = Integer.parseInt(getInfo(Info.GuiMp3Player.isPlayingIndex, "durationInt"));
				}
				playRecord(Info.MP3PlayerIndexToOpen);
			}else{
				Info.player.addChatMessage("Can't play, because not mp3");
			}
		}
		if (file.getName().indexOf(".mp3") == -1){
			return null;
		}
		
		if (type.equals("duration")){
			String duration = Mp3audioHeader.getTrackLengthAsString();
			return duration == null ? "" : duration;
		}
		if (type.equals("durationInt")){
			return Integer.toString(Mp3audioHeader.getTrackLength());
		}
		if (type.equals("version")){
			return Mp3audioHeader.getMpegVersion();
		}
		if (type.equals("layer")){
			return Mp3audioHeader.getMpegLayer();
		}
		if (type.equals("TotalFrames")){
			return Long.toString(Mp3audioHeader.getNumberOfFrames());
		}
		if (type.equals("BitRate")){
			return Mp3audioHeader.getBitRate();
		}
		if (type.equals("sampleRate")){
			return Integer.toString(Mp3audioHeader.getSampleRateAsNumber());
		}
		if (type.equals("All")){
			return Mp3audioHeader.toString();
		}
		return "";

	}
	
	public static boolean canRetrieveInfo(int index){
		if (!SoundLoader.musicFiles.isEmpty()){
			return files.get(index).getName().indexOf(".mp3") != -1 ? true : false;
		}else{
			return false;
		}
		
	}
	
	public static void playRecord(int index){
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT){
			try{
				File file = new File(getFile(index).getAbsolutePath());
				if (file.exists()){
					try{
						stopPlaying();
						
						Sounds.file = file;
						FileInputStream stream = new FileInputStream(file);
						startFrame = Math.round((float)Info.SecondsPlayed / Integer.parseInt(getInfo(index, "durationInt")) * Integer.parseInt(getInfo(index, "TotalFrames")));
						playMp3(stream, startFrame);
							
					} catch (JavaLayerException e) {
						e.printStackTrace();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}catch (NumberFormatException e){
					}finally{
							
					}
				}
			}finally{
				
			}
			
		}
	}
	
	public static void stopPlaying(){
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && Minecraft.getMinecraft().gameSettings.soundVolume != 0.0F){
			try{
				if (mp3Player != null){
					mp3Player.close();
				}
				fileRunning = false;
				startFrame = 0;
				
			}finally{
			
			}
		}
		
	}
	
	public static File file;
	public static int startFrame = 0;
	
	
	public static void pauseRecord(int index){
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && Minecraft.getMinecraft().gameSettings.soundVolume != 0.0F){
			if (!SoundLoader.musicFiles.isEmpty()){
				try{
					File file = new File(getFile(index).getAbsolutePath());
					if (file.exists()){
						Sounds.file = file;
						mp3Player.stop();
						fileRunning = false;
					}
				}finally{
					
				}
			}
			
		}
	}
	
	
	
	public static void resumeRecord(){
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && Minecraft.getMinecraft().gameSettings.soundVolume != 0.0F){
			try{
				if (file != null && file.exists()){
					FileInputStream stream = new FileInputStream(file);
					fileRunning = false;
					playMp3(stream, (int) Math.round(startFrame));
				}
			} catch (JavaLayerException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}finally{
				
			}
		}
	}
	
	
	public static AdvancedPlayer mp3Player;
	private static int Volume = 100;
    private static Thread playerThread;

    public static boolean fileRunning = false;

    public static void playMp3(FileInputStream file, int start) throws JavaLayerException{

        if (fileRunning == false){
        	
        	startFrame = start;
            mp3Player = new AdvancedPlayer(file);
            
            mp3Player.setPlayBackListener(new PlaybackListener() {
            		@Override
            		public void playbackFinished(PlaybackEvent event){
            			if (event.getFrame() == 0){
            				System.out.println("Playing next song");
            				Info.SecondsPlayed = 0;
            				stopPlaying();
            				if (!Info.isRepeat){
            					if (Info.isShuffle){
                					Info.MP3PlayerIndexToOpen = Math.round((float)Math.random() * getList().size()-1);
                				}else{
                					int index = Info.MP3PlayerIndexToOpen;
                    				if (index == files.size() - 1){
                    					Info.MP3PlayerIndexToOpen = 0;
                    				}else{
                    					Info.MP3PlayerIndexToOpen = index + 1;
                    				}
                				}
            				}
            				if (Info.GuiMp3Player != null){
            					Info.GuiMp3Player.isPlayingIndex = Info.MP3PlayerIndexToOpen;
                				if (canRetrieveInfo(Info.GuiMp3Player.isPlayingIndex)){
                					try{
                						Info.GuiMp3Player.durationInt = Integer.parseInt(getInfo(Info.GuiMp3Player.isPlayingIndex, "durationInt"));
                					}catch (NumberFormatException e){
                					}
                				}
            				}
            				
            				playRecord(Info.MP3PlayerIndexToOpen);
            				
            			}
            			startFrame += event.getFrame();
            		}
			});

            playerThread = new Thread(BlocksForUse.sounds);
            playerThread.start();

            fileRunning = true;

        }
    }
    
    

    @Override
    public void run(){
    	
    	try {
    		mp3Player.play(Math.min(Integer.parseInt(getInfo(Info.MP3PlayerIndexToOpen, "TotalFrames")) - 10, startFrame), Integer.MAX_VALUE);
    	}catch (JavaLayerException ex) {
    		if (Info.isShuffle){
				Info.MP3PlayerIndexToOpen = Math.round((float)Math.random() * getList().size()-1);
				
				System.out.println("Skipping unplayable song");
				Info.GuiMp3Player.isPlayingIndex = Info.MP3PlayerIndexToOpen;
				if (canRetrieveInfo(Info.GuiMp3Player.isPlayingIndex)){
					Info.GuiMp3Player.durationInt = Integer.parseInt(getInfo(Info.GuiMp3Player.isPlayingIndex, "durationInt"));
				}
				playRecord(Info.MP3PlayerIndexToOpen);
			}else{
				Info.player.addChatMessage("Can't play, because not mp3");
			}
    	}
    }

	public static void UpdateFile(int index) {
		if (!SoundLoader.musicFiles.isEmpty()){
			file = new File(getFile(index).getAbsolutePath());
		}
	}
	
	public static void setVolume(int volume){
		Sounds.Volume = volume;
	}
	
	


	
}
