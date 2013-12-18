package Core.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import Core.ModBFU.BlocksForUse;
import Core.blocks.BlockInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemWrench extends Item {

    public ItemWrench(int id) {
        super(id);
        setCreativeTab(BlocksForUse.tabBFUThings);

    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register){
        itemIcon = register.registerIcon(BlockInfo.TEXTURE_LOCATION + ":" + ItemInfo.WRENCH_TEXTURE);

    }
    
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player,World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
    	if (!world.isRemote){
    		if (player.isSneaking()){
    			if (world.getBlockTileEntity(x, y, z) instanceof IWrenchable){
    				if (((IWrenchable) world.getBlockTileEntity(x, y, z)).IsWrenchable(world, player, side, hitZ, hitZ, hitZ)){
    					world.destroyBlock(x, y, z, true);
    				}
    				 
    			}
    		}
    	}
    	
    	return false;
    }
}
