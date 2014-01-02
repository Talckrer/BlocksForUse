package Core.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import Core.modBFU.BlocksForUse;
import Core.tileEntities.TileEntityDiscWriter;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDiscWriter extends BlockContainer {

	public BlockDiscWriter(int id) {
		super(id, Material.iron);
		setUnlocalizedName(BlockInfo.DISC_WRITER_BLOCK_UNLOCALIZED_NAME);
		setCreativeTab(BlocksForUse.tabDisks);
	}
	
	private int x = 0;
	private int y = 0;
	private int z = 0;
	
	@SideOnly(Side.CLIENT)
    private Icon defaultIcon;
    @SideOnly(Side.CLIENT)
    private Icon holeIcon;
    
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister register){
    	defaultIcon = register.registerIcon(BlockInfo.TEXTURE_LOCATION + ":" + BlockInfo.DISC_WRITER_BLOCK_TEXTURE_DEFAULT);
    	holeIcon = register.registerIcon(BlockInfo.TEXTURE_LOCATION + ":" + BlockInfo.DISC_WRITER_BLOCK_TEXTURE_HOLE);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon(int side, int meta){
        if (side == 2){
            return holeIcon;
        }else{
            return defaultIcon;
        }
    }
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		if (x != 0 && y != 0 && z != 0){
			return new TileEntityDiscWriter(world, x, y, z);
		}else{
			return new TileEntityDiscWriter();
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y,int z, EntityPlayer player, int par6, float par7,float par8, float par9) {
		if (!world.isRemote){
			FMLNetworkHandler.openGui(player, BlocksForUse.instance, 2, world, x, y, z);
		}
		return true;
	}
	
	@Override
	public int onBlockPlaced(World par1World, int par2, int par3, int par4,int par5, float par6, float par7, float par8, int par9) {
		this.x = x;
		this.y = y;
		this.z = z;
		return par9;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z,int id, int meta) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te != null && te instanceof ISidedInventory) {
			ISidedInventory inventory = (ISidedInventory)te;
			
			for (int i = 0; i < inventory.getSizeInventory(); i++){
				ItemStack stack = inventory.getStackInSlotOnClosing(i);
				
				if (stack != null){
					float spawnX = x + world.rand.nextFloat();
					float spawnY = y + world.rand.nextFloat();
					float spawnZ = z + world.rand.nextFloat();
					
					EntityItem droppedItem = new EntityItem(world, spawnX, spawnY, spawnZ, stack);
					
					float mult = 0.05F;
					
					droppedItem.motionX = (-0.5F + world.rand.nextFloat()) * mult;
					droppedItem.motionY = (4 + world.rand.nextFloat()) * mult;
					droppedItem.motionZ = (-0.5F + world.rand.nextFloat())* mult;
					
					world.spawnEntityInWorld(droppedItem);
				}
			}
		}
		super.breakBlock(world, x, y, z, id, meta);
	}
}
