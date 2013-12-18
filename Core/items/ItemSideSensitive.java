package Core.items;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import Core.ModBFU.BlocksForUse;

public class ItemSideSensitive extends Item {

	public ItemSideSensitive(int id) {
		super(id);
		setCreativeTab(BlocksForUse.tabBFUThings);
		setUnlocalizedName(ItemInfo.SIDES_UNLOCALIZED_NAME);
		
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY,float hitZ) {
		if (!world.isRemote){
			System.out.println("Reading a block");
			if (world.blockExists(x, y, z)){
				if (world.blockHasTileEntity(x, y, z)){
					TileEntity te = world.getBlockTileEntity(x, y, z);
					if (te instanceof ISidedInventory ){
						try{
							ArrayList Inputs = new ArrayList<Integer>(6);
							ArrayList Outputs = new ArrayList<Integer>(6);
							int[] bottomSlots = null;
							int[] sideSlots = null;
							int[] topSlots = null;
							if (((ISidedInventory) te).getAccessibleSlotsFromSide(0) != null){
								bottomSlots = ((ISidedInventory) te).getAccessibleSlotsFromSide(0);
							}
							if (((ISidedInventory) te).getAccessibleSlotsFromSide(1) != null){
								topSlots = ((ISidedInventory) te).getAccessibleSlotsFromSide(1);
							}
							if (((ISidedInventory) te).getAccessibleSlotsFromSide(2) != null){
								sideSlots = ((ISidedInventory) te).getAccessibleSlotsFromSide(2);
							}
							
							
							String InfoToDisplay = "";
							
							
							if (bottomSlots != null){
								InfoToDisplay = "";
								for (int i : bottomSlots){
									InfoToDisplay += ("," + i);
								}
								player.addChatMessage("Accessable bottom slots: " + InfoToDisplay);
							}

							if (topSlots != null){
								InfoToDisplay = "";
								for (int i : topSlots){
									InfoToDisplay += ("," + i);
								}
								player.addChatMessage("Accessable top slots: " + InfoToDisplay);
							}
							
							if (sideSlots != null){
								InfoToDisplay = "";
								for (int i : sideSlots){
									InfoToDisplay += ("," + i);
								}
								player.addChatMessage("Accessable side slots: " + InfoToDisplay);
							}
							
						}finally{
							
						}
					}else if (te instanceof IInventory) {
						player.addChatMessage("This tile entity has normal inventory(every side is allowed for everything)");
					}else{
						player.addChatMessage("This tile entity doesn't have an inventory");
					}
				}else{
					player.addChatMessage("This block doesn't have a tile entity");
				}
			}
		}
		
		
		
		return true;
	}


}
