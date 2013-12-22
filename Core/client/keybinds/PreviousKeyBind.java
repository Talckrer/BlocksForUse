package Core.client.keybinds;

import java.util.EnumSet;

import Core.client.Interfaces.Info;
import Core.client.sounds.Sounds;

import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class PreviousKeyBind extends KeyHandler {

	private EnumSet tickTypes = EnumSet.of(TickType.CLIENT);
	
	private boolean continuePlaying = false;

	public PreviousKeyBind(KeyBinding[] keyBindings, boolean[] repeatings) {
		super(keyBindings, repeatings);
	}

	@Override
	public String getLabel() {
		return "Previous song(BFU)";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		System.out.println("Pressed previous");
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
		Info.SecondsPlayed = 0;
		Sounds.UpdateFile(Info.MP3PlayerIndexToOpen);
		
		if (continuePlaying){
			Sounds.playRecord(Info.MP3PlayerIndexToOpen);
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
	}

	@Override
	public EnumSet<TickType> ticks() {
		return tickTypes;
	}

}
