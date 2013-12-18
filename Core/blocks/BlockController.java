package Core.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import Core.ModBFU.BlocksForUse;
import Core.TileEntities.TileEntityController;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockController extends BlockContainer {

	private TileEntityController TEController;

	public BlockController(int id) {
		super(id, Material.iron);
		setUnlocalizedName(BlockInfo.CONTROLLER_BLOCK_UNLOCALIZED_NAME);
		setHardness(60);
		setCreativeTab(BlocksForUse.tabBFUThings);
		setStepSound(soundMetalFootstep);
	}
	
	private int x = 0;
	private int y = 0;
	private int z = 0;
	
	@Override
    public TileEntity createNewTileEntity(World world) {
		if (x != 0 && y != 0 && z != 0){
			return new TileEntityController(world, x, y, z);
		}else{
			return new TileEntityController();
		}
        
    }
	
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int par5, float par6, float par7, float par8, int par9) {
		TEController = (TileEntityController)world.getBlockTileEntity(x, y, z);
		this.x = x;
		this.y = y;
		this.z = z;
		return par9;
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y,int z, EntityPlayer player, int par6, float par7,float par8, float par9) {
		if (!world.isRemote){
			FMLNetworkHandler.openGui(player, BlocksForUse.instance, 0, world, x, y, z);
		}
		return true;
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
		TEController = (TileEntityController)world.getBlockTileEntity(x, y, z);
		if (!world.isRemote){
			Blocks.getIDRegistry().onBreakBlock(TEController.getID());
		}
		
		
		super.breakBlock(world, x, y, z, id, meta);
	}

}
