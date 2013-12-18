package Core.client.Interfaces;

import Core.TileEntities.TileEntityDiscWriter;
import Core.items.ItemInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerDiscWriter extends Container {
	
	private TileEntityDiscWriter discWriter;
	
	public ContainerDiscWriter(InventoryPlayer invPlayer, TileEntityDiscWriter writer){
		discWriter = writer;
		
		for (int x = 0; x < 9; x++){
			addSlotToContainer(new Slot(invPlayer, x, 16 + 18 * x, 149));
		}
		
		for (int y = 0; y < 3; y++){
			for (int x = 0; x < 9; x++){
				addSlotToContainer(new Slot(invPlayer, x + y * 9 + 9, 16 + 18 * x, 91 + y * 18));
			}
		}
		
		addSlotToContainer(new Slot(discWriter, 0, 59, 52));
		addSlotToContainer(new Slot(discWriter, 1, 115, 52));
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int i) {
		Slot slot = getSlot(i);
		
		if (slot != null && slot.getHasStack()){
			ItemStack stack = slot.getStack();
			ItemStack result = stack.copy();
			
			
			System.out.println("StackID: "+(stack.itemID)+" MP3Player: "+(ItemInfo.MP3PLAYER_ID+256)+" DiscID: "+(ItemInfo.DISC_ID+256));
			
			if (stack.itemID == ItemInfo.DISC_ID+256){
				if (!mergeItemStack(stack, 37, 38, false)){
					return null;
				}
			}else if (stack.itemID == ItemInfo.MP3PLAYER_ID+256){
				if (!mergeItemStack(stack, 36, 37, false)){
					return null;
				}
			}else{
				if (!mergeItemStack(stack, 0, 36, false)){
					return null;
				}
			}
			
			if (stack.stackSize == 0){
				slot.putStack(null);
			}else{
				slot.onSlotChanged();
			}
			
			slot.onPickupFromSlot(player, stack);
			
			return result;
		}
		
		return null;
	}
	
	public TileEntityDiscWriter getTileEntity(){
		return discWriter;
	}

}
