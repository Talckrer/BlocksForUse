package Core.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import Core.handlers.WorldHandler;
import Core.modBFU.BlocksForUse;
import Core.tileEntities.TileEntityWeather;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class BlockWeather extends BlockContainer {

    public BlockWeather(int id) {
        super(id, Material.iron);
        setCreativeTab(BlocksForUse.tabBFUThings);
        setBlockUnbreakable();
        setStepSound(Block.soundMetalFootstep);
        setUnlocalizedName(BlockInfo.WEATHER_BLOCK_UNLOCALIZED_NAME);
    }
    
    protected boolean enabled = true;
    
    public static boolean isRaining = true;
    		
    		
    public boolean isRaining(World world){
    	isRaining = world.isRaining();
    	return isRaining;
    	
    }
    
    
    
    public boolean enabled(){
        return enabled;
    }
    

    
    @SideOnly(Side.CLIENT)
    private Icon topIcon;
    @SideOnly(Side.CLIENT)
    private Icon otherIcon;
    
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister register){
        topIcon = register.registerIcon(BlockInfo.TEXTURE_LOCATION + ":" + BlockInfo.WEATHER_BLOCK_TEXTURE_TOP);
        otherIcon = register.registerIcon(BlockInfo.TEXTURE_LOCATION + ":" + BlockInfo.WEATHER_BLOCK_TEXTURE_OTHER);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon(int side, int meta){
        if (side == 1){
            return topIcon;
        }else{
            return otherIcon;
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEntityWeather();
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
        onActivation(world);
    	return false;
    }

	public void onActivation(World world) {
		if (enabled){
			WorldHandler.toggleRain(world, isRaining(world));
		}
		
	}
}