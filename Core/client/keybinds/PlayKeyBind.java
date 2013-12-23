package Core.client.keybinds;

import java.util.EnumSet;

import Core.client.Interfaces.Info;
import Core.client.sounds.Sounds;

import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PlayKeyBind extends KeyHandler {
	
	private EnumSet tickTypes = EnumSet.of(TickType.CLIENT);
	public static boolean keyPressed = false;
	public static boolean keyPressedBefore = false;

	public PlayKeyBind(KeyBinding[] keyBindings, boolean[] repeatings) {
		super(keyBindings, repeatings);
	}

	@Override
	public String getLabel() {
		return "Play song(BFU)";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		keyPressed = true;
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
		keyPressed = false;
	}

	@Override
	public EnumSet<TickType> ticks() {
		return tickTypes;
	}

}
