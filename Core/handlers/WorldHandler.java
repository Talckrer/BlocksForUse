package Core.handlers;

import net.minecraft.world.World;

public class WorldHandler {
	
	
	public static void toggleRain(World world, boolean isRaining){
		if (!world.isRemote){
			if (!isRaining){
				world.getWorldInfo().setRaining(true);
			}else{
				world.getWorldInfo().setRaining(false);
			}
		}
	}
}
