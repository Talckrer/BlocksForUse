package Core.client.Interfaces;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import Core.items.ItemInfo;

public class SlotDisc extends Slot {

	public SlotDisc(IInventory inventory, int id, int x, int y) {
		super(inventory, id, x, y);
	}
	
	
	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return itemstack.itemID == ItemInfo.DISC_ID + 256;
	}

}
