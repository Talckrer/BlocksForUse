package Core.client.keybinds;

import java.util.EnumSet;

import Core.client.Interfaces.Info;
import Core.client.sounds.Sounds;

import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class PlayKeyBind extends KeyHandler {
	
	private EnumSet tickTypes = EnumSet.of(TickType.CLIENT);

	public PlayKeyBind(KeyBinding[] keyBindings, boolean[] repeatings) {
		super(keyBindings, repeatings);
	}

	@Override
	public String getLabel() {
		return "Play song(BFU)";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		System.out.println("Pressed play");
		if (Sounds.fileRunning){
			if (Sounds.fileRunning){
				Sounds.UpdateFile(Info.MP3PlayerIndexToOpen);
				Sounds.pauseRecord(Info.MP3PlayerIndexToOpen);
				if (Sounds.canRetrieveInfo(Info.MP3PlayerIndexToOpen)){
					Sounds.startFrame = Math.round(((float)Info.SecondsPlayed / Integer.parseInt(Sounds.getInfo(Info.MP3PlayerIndexToOpen, "durationInt")) * Integer.parseInt(Sounds.getInfo(Info.MP3PlayerIndexToOpen, "TotalFrames"))));
				}
			}
		}else{
			if (Sounds.startFrame == 0){
				Sounds.playRecord(Info.MP3PlayerIndexToOpen);
			}else{
				Sounds.resumeRecord();
			}
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
