package Core.TileEntities;

import Core.items.ItemInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityDiscWriter extends TileEntity implements ISidedInventory{
	
	public TileEntityDiscWriter(){
		items = new ItemStack[2];
	}
	
	public TileEntityDiscWriter(World world, int x, int y, int z){
		items = new ItemStack[2];
		this.x = x;
		this.y = y;
		this.z = z;
	}
	

	private int x;
	private int y;
	private int z;
	private ItemStack[] items;
	
	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return items[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int count) {
		ItemStack itemstack = getStackInSlot(slot);
		
		if (itemstack != null){
			if (itemstack.stackSize <= count){
				setInventorySlotContents(slot, null);
			}else{
				itemstack = itemstack.splitStack(count);
				onInventoryChanged();
			}
		}
		return itemstack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		items[slot] = stack;
	}

	@Override
	public String getInvName() {
		return "Disc writer";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void openChest() {
		
	}

	@Override
	public void closeChest() {
		
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (stack.itemID == ItemInfo.DISC_ID+256 && slot == 1){
			return true;
		}
		if (stack.itemID == ItemInfo.MP3PLAYER_ID+256 && slot == 0){
			return true;
		}
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		int[] idsSide = {0};
		int[] idsTop = {0, 1};
		int[] idsBottom = {1};
		switch (side){
		case 0:
			return idsBottom;
		case 1:
			return idsTop;
		case 2:
		case 3:
		case 4:
		case 5:
			return idsSide;
		}
		return null;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		if (stack.itemID == ItemInfo.DISC_ID+256 && slot == 1 && side == 1){
			return true;
		}
		if (stack.itemID == ItemInfo.MP3PLAYER_ID+256 && slot == 0 && side == 1){
			return true;
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		if (slot == 1 && side == 0){
			return true;
		}
		return false;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		NBTTagList items = new NBTTagList();
		
		for (int i = 0; i < getSizeInventory(); i++){
			ItemStack stack = getStackInSlot(i);
			
			if (stack != null){
				NBTTagCompound item = new NBTTagCompound();
				item.setByte("Slot", (byte)i);
				stack.writeToNBT(item);
				items.appendTag(item);
			}
		}
		compound.setTag("Items", items);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		NBTTagList items = compound.getTagList("Items");
		
		for (int i = 0; i < items.tagCount(); i++){
			NBTTagCompound item = (NBTTagCompound)items.tagAt(i);
			int slot = item.getByte("Slot");
			if (slot >= 0 && slot < getSizeInventory()){
				setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
			}
		}
	}
	

}
