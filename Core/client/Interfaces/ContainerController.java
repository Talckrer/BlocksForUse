package Core.client.Interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import Core.TileEntities.TileEntityController;
import Core.items.ItemInfo;

public class ContainerController extends Container {
	
	private TileEntityController controller;
	
	public ContainerController(InventoryPlayer invPlayer, TileEntityController controller){
		this.controller = controller;
		
		for (int x = 0; x < 9; x++){
			addSlotToContainer(new Slot(invPlayer, x, 50 + 18 * x, 218));
		}
		
		for (int y = 0; y < 3; y++){
			for (int x = 0; x < 9; x++){
				addSlotToContainer(new Slot(invPlayer, x + y * 9 + 9, 50 + 18 * x, 160 + y * 18));
			}
		}
		
		for (int x = 0; x < 3; x++){
			addSlotToContainer(new SlotDisc(controller, x, 17 + 18 * x, 55));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return controller.isUseableByPlayer(entityplayer);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int i) {
		Slot slot = getSlot(i);
		
		if (slot != null && slot.getHasStack()){
			ItemStack stack = slot.getStack();
			ItemStack result = stack.copy();
			
			if (i >= 36){
				if (!mergeItemStack(stack, 0, 36, false)){
					return null;
				}
			}else if(stack.itemID != ItemInfo.DISC_ID - 256 || !mergeItemStack(stack, 36, 36 + controller.getSizeInventory(), false)){
				return null;
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
	
	public TileEntityController getController(){
		return controller;
	}

}
