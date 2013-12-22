package Core.client.keybinds;

import java.util.EnumSet;

import Core.client.Interfaces.Info;
import Core.client.sounds.Sounds;

import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class StopKeyBind extends KeyHandler {

	private EnumSet tickTypes = EnumSet.of(TickType.CLIENT);

	public StopKeyBind(KeyBinding[] keyBindings, boolean[] repeatings) {
		super(keyBindings, repeatings);
	}

	@Override
	public String getLabel() {
		return "Stop song(BFU)";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		System.out.println("Pressed stop");
		Info.SecondsPlayed = 0;
		Sounds.stopPlaying();
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
	}

	@Override
	public EnumSet<TickType> ticks() {
		return tickTypes;
	}

}
