package Core.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import Core.ModBFU.BlocksForUse;
import Core.TileEntities.TileEntityController;
import Core.blocks.BlockInfo;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMP3Player extends Item {

	public ItemMP3Player(int id) {
		super(id);
		setUnlocalizedName(ItemInfo.MP3PLAYER_UNLOCALIZED_NAME);
		setCreativeTab(BlocksForUse.tabDisks);
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote){
			if (player.isSneaking()){
				if (world.blockHasTileEntity(x, y, z) && world.getBlockTileEntity(x, y, z) instanceof TileEntityController){
					TileEntityController controller = (TileEntityController) world.getBlockTileEntity(x, y, z);
					
					player.addChatMessage("Setting ID " + controller.getID());
					
					try{
						stack.getTagCompound().getInteger("ID");
					}catch (NullPointerException e){
						stack.stackTagCompound = new NBTTagCompound();
					}
					
					stack.getTagCompound().setInteger("ID", controller.getID());
				}
			}else{
				this.onItemRightClick(stack, world, player);
			}
		}
		return true;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!world.isRemote){
			FMLNetworkHandler.openGui(player, BlocksForUse.instance, 1, world, (int)player.posX, (int)player.posY, (int)player.posZ);
		}
		
		
		return stack;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean addExtraInformation) {
		
		list.add("Doesn't depend on the server");
		try{
			list.add("ID: " + stack.getTagCompound().getInteger("ID"));
		}catch (NullPointerException e){
			list.add("ID: unlinked");
		}
		
		super.addInformation(stack, player, list, addExtraInformation);
	}
	
	@Override
	public void onCreated(ItemStack stack, World par2World, EntityPlayer par3EntityPlayer) {
		stack.stackTagCompound = new NBTTagCompound();
	}
	
	@SideOnly(Side.CLIENT)
	private Icon iconMP3Player;
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		iconMP3Player = register.registerIcon(BlockInfo.TEXTURE_LOCATION + ":" + ItemInfo.MP3PLAYER_TEXTURE);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1) {
		return iconMP3Player;
	}

}
