package Core.tileEntities;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import Core.blocks.Blocks;
import Core.handlers.PacketHandler;
import Core.items.IWrenchable;
import Core.items.ItemInfo;

public class TileEntityController extends TileEntity implements ISidedInventory, IWrenchable {
	
	
	public TileEntityController(World world, int x, int y, int z) {
		items = new ItemStack[3];
		FreeIDs = new ArrayList<Integer>();
		EnergyStorage = 0;
		if (world != null){
			getData(0, world, x, y, z);
			getData(1, world, x, y, z);
		}
		songIDs = new int[27];
	}
	
	
	public TileEntityController() {
		items = new ItemStack[3];
		FreeIDs = new ArrayList();
		EnergyStorage = 0;
		songIDs = new int[27];
	}
	
	private ItemStack[] items;
	private int[] songIDs; 
	private int ID = 0;
	private int EnergyStorage;
	private int EnergyCapacity = 100000;
	private static ArrayList<Integer> FreeIDs;
	private int timer = 0;
	private int count = 0;
	
	@Override
	public boolean canUpdate() {
		return true;
	}
	
	private int specialCount = 0;
	public int getID(){
		return ID;
	}
	
	public void setID(int ID){
		this.ID = ID;
	}
	
	public void getData(int i, World world, int x, int y, int z){
		if (!world.isRemote){
			ID = Blocks.getIDRegistry().getNewID();
		}else{
			PacketHandler.sendRequestPacketToServer(0, i, x, y, z);
		}
	}
	
	
	
	/*public int registerSlotID(int x, int y, int z, World world){
		
	}*/
	
	public int getEnergyStorage(){
		return EnergyStorage;
	}
	
	public int getEnergyCapacity(){
		return EnergyCapacity;
	}
	
	public void setEnergy(int amount){
		EnergyStorage = amount;
	}
	
	public void  addEnergy(int amount){
		if (EnergyCapacity - EnergyStorage > amount){
			EnergyStorage += amount;
		}else{
			EnergyStorage = EnergyCapacity;
		}
	}
	
	public void removeEnergy(int amount){
		if (EnergyStorage <= amount){
			EnergyStorage = 0;
		}else{
			EnergyStorage -= amount;
		}
	}

	@Override
	public int getSizeInventory() {
		return items.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return items[i];
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
		ItemStack stack = items[i];
		items[i] = null;
		return stack;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		items[i] = itemstack;
		
	}

	@Override
	public String getInvName() {
		return "BFUStorageController";
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
		return entityplayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) <= 64;
	}

	@Override
	public void openChest() {
		
	}

	@Override
	public void closeChest() {
		
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return itemstack.itemID == ItemInfo.DISC_ID + 256;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		int[] slots = new int[]{0,1,2};
		if (side == 0){
			return null;
		}else{
			return slots;
		}
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemstack, int side) {
		if (side != 0 && itemstack.itemID == ItemInfo.DISC_ID + 256){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
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
		compound.setInteger("Energy", EnergyStorage);
		compound.setByte("ID", (byte)ID);
		compound.setTag("Items", items);
		compound.setIntArray("SongIDs", songIDs);
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
		
		EnergyStorage = compound.getInteger("Energy");
		ID = compound.getByte("ID");
		songIDs = compound.getIntArray("SongIDs");
	}


	@Override
	public boolean IsWrenchable(World world, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		return true;
	}




	
}
