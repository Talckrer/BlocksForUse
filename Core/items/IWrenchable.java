/**
 * 
 */
package Core.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * @author Talckrer
 * 
 * @Info Only used by Tile entities
 *
 */
public interface IWrenchable {
	
	public boolean IsWrenchable(World world, EntityPlayer player, int side, float hitX, float hitY, float hitZ);
	
}
