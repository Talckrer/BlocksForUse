package Core.handlers;

import java.util.EnumSet;

import Core.client.Interfaces.Info;
import Core.client.Interfaces.guis.GuiMP3Player;
import Core.client.keybinds.GuiMP3PlayerKeyBind;
import Core.client.keybinds.NextKeyBind;
import Core.client.keybinds.PlayKeyBind;
import Core.client.keybinds.PreviousKeyBind;
import Core.client.keybinds.StopKeyBind;
import Core.client.sounds.Sounds;
import Core.modBFU.BlocksForUse;

import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.FMLNetworkHandler;

public class PlayerTickHandler implements ITickHandler {
	
	private final EnumSet<TickType> ticksToGet;
	
	public PlayerTickHandler(EnumSet<TickType> ticksToGet){
	         this.ticksToGet = ticksToGet;
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		playerTick((EntityPlayer)tickData[0]);
	}


	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
	public EnumSet<TickType> ticks() {
		return ticksToGet;
	}

	@Override
	public String getLabel() {
		return "PlayerTickHandler";
	}
	
	private void playerTick(EntityPlayer player) {
		
		if (NextKeyBind.keyPressed && !NextKeyBind.keyPressedBefore){
			if (Info.GuiMp3Player == null){
				player.addChatMessage("Needs item MP3Player to be activated");
			}else{
				boolean continuePlaying;
				if (!Sounds.files.isEmpty()){
					continuePlaying = Sounds.fileRunning;
					Sounds.stopPlaying();
					if (Info.isShuffle){
						Info.MP3PlayerIndexToOpen = Math.round((float)Math.random() * Sounds.getList().size());
					}else{
						if (Info.MP3PlayerIndexToOpen == Sounds.getList().size() - 1){
							Info.MP3PlayerIndexToOpen = 0;
						}else{
							Info.MP3PlayerIndexToOpen += 1;
						}
					}
					Info.GuiMp3Player.isPlayingIndex = Info.MP3PlayerIndexToOpen;
					if (Sounds.canRetrieveInfo(Info.MP3PlayerIndexToOpen)){
						Info.GuiMp3Player.durationInt = Integer.parseInt(Sounds.getInfo(Info.MP3PlayerIndexToOpen, "durationInt"));
					}
					Info.SecondsPlayed = 0;
					Sounds.UpdateFile(Info.MP3PlayerIndexToOpen);
					
					if (continuePlaying){
						player.addChatMessage("Now playing: "+Sounds.files.get(Info.MP3PlayerIndexToOpen).getName());
						Sounds.playRecord(Info.MP3PlayerIndexToOpen);
					}
				}
			}
        }
		NextKeyBind.keyPressedBefore = NextKeyBind.keyPressed;
		if (PreviousKeyBind.keyPressed && !PreviousKeyBind.keyPressedBefore){
			if (Info.GuiMp3Player == null){
				player.addChatMessage("Needs item MP3Player to be activated");
			}else{
				boolean continuePlaying;
				if (!Sounds.files.isEmpty()){
					continuePlaying = Sounds.fileRunning;
					Sounds.stopPlaying();
					if (Info.isShuffle){
						Info.MP3PlayerIndexToOpen = Math.round((float)Math.random() * Sounds.getList().size());
					}else{
						if (Info.MP3PlayerIndexToOpen == 0){
							Info.MP3PlayerIndexToOpen = Sounds.getList().size() - 1;
						}else{
							Info.MP3PlayerIndexToOpen -= 1;
						}
					}
					Info.GuiMp3Player.isPlayingIndex = Info.MP3PlayerIndexToOpen;
					if (Sounds.canRetrieveInfo(Info.MP3PlayerIndexToOpen)){
						Info.GuiMp3Player.durationInt = Integer.parseInt(Sounds.getInfo(Info.MP3PlayerIndexToOpen, "durationInt"));
					}
					Info.SecondsPlayed = 0;
					Sounds.UpdateFile(Info.MP3PlayerIndexToOpen);
					
					if (continuePlaying){
						player.addChatMessage("Now playing: "+Sounds.files.get(Info.MP3PlayerIndexToOpen).getName());
						Sounds.playRecord(Info.MP3PlayerIndexToOpen);
					}
				}
			}
		}
		PreviousKeyBind.keyPressedBefore = PreviousKeyBind.keyPressed;
		if (PlayKeyBind.keyPressed && !PlayKeyBind.keyPressedBefore){
			if (Info.GuiMp3Player == null){
				player.addChatMessage("Needs item MP3Player to be activated");
			}else{
				if (!Sounds.files.isEmpty()){
					if (Sounds.fileRunning){
						player.addChatMessage("Paused");
						Sounds.UpdateFile(Info.MP3PlayerIndexToOpen);
						Sounds.pauseRecord(Info.MP3PlayerIndexToOpen);
						if (Sounds.canRetrieveInfo(Info.MP3PlayerIndexToOpen)){
							Sounds.startFrame = Math.round(((float)Info.SecondsPlayed / Integer.parseInt(Sounds.getInfo(Info.MP3PlayerIndexToOpen, "durationInt")) * Integer.parseInt(Sounds.getInfo(Info.MP3PlayerIndexToOpen, "TotalFrames"))));
						}
					}else{
						if (Sounds.startFrame == 0){
							player.addChatMessage("Now playing: "+Sounds.files.get(Info.MP3PlayerIndexToOpen).getName());
							Sounds.playRecord(Info.MP3PlayerIndexToOpen);
						}else{
							player.addChatMessage("Resuming: "+Sounds.files.get(Info.MP3PlayerIndexToOpen).getName());
							Sounds.resumeRecord();
						}
					}
				}
			}
		}
		PlayKeyBind.keyPressedBefore = PlayKeyBind.keyPressed;
		if (StopKeyBind.keyPressed && !StopKeyBind.keyPressedBefore){
			if (Info.GuiMp3Player == null){
				player.addChatMessage("Needs item MP3Player to be activated");
			}else{
				if (!Sounds.files.isEmpty()){
					Info.SecondsPlayed = 0;
					Sounds.stopPlaying();
				}
			}
		}
		StopKeyBind.keyPressedBefore = StopKeyBind.keyPressed;
		if (GuiMP3PlayerKeyBind.keyPressed && !GuiMP3PlayerKeyBind.keyPressedBefore){
			FMLNetworkHandler.openGui(player, BlocksForUse.instance, 1, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
		}
		GuiMP3PlayerKeyBind.keyPressedBefore = GuiMP3PlayerKeyBind.keyPressed;
	}

}
