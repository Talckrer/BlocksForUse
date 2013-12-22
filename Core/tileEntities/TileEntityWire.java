package Core.tileEntities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import Core.items.IWrenchable;
import Core.modBFU.BlocksForUse;

public class TileEntityWire extends TileEntity implements IWrenchable {
	
	public TileEntityWire(World world){
		sides = 0;
		ctrlX = 0;
		ctrlY = 0;
		ctrlZ = 0;
		timer = 0;
	}
	
	private Byte sides;
	
	private int ctrlX;
	private int ctrlY;
	private int ctrlZ;
	private int timer;
	
	@Override
	public void updateEntity() {
		System.out.println("Updating tileentityWire");
		if (timer >= 10){
			timer = 0;
			ctrlX = this.xCoord;
			ctrlY = this.yCoord;
			ctrlZ = this.zCoord;
			System.out.println("Coordinates " + ctrlX + " : " + ctrlY + " : " + ctrlZ);
		
			sides = 0;
			int side;
			
			for (int i = 0; i < 6; i++){
				ctrlX = this.xCoord;
				ctrlY = this.yCoord;
				ctrlZ = this.zCoord;
				side = i;
				switch(i){
				case 0:
					ctrlX += 1;
					break;
				case 1:
					ctrlX -= 1;
					break;
				case 2:
					ctrlY += 1;
					break;
				case 3:
					ctrlY -= 1;
					break;
				case 4:
					ctrlZ += 1;
					break;
				case 5:
					ctrlZ -= 1;
					break;
				}
				System.out.println("Checking " + ctrlX + " : " + ctrlY + " : " + ctrlZ);
				if (worldObj.blockExists(ctrlX, ctrlY, ctrlZ)){
					System.out.println("Block exists");
					for (int element : BlocksForUse.IDs.getList()){
						System.out.println(element);
					}
					System.out.println(worldObj.getBlockId(ctrlX, ctrlY, ctrlZ));
					if (BlocksForUse.IDs.getList().contains(worldObj.getBlockId(ctrlX, ctrlY, ctrlZ)) ){
						System.out.println("Wire Connected to side " + side);
						sides = (byte) (sides | (1 << side));
					}
				}
				System.out.println("Sides are now" +  Byte.toString(sides));
				
				}
			
			
		}else{
			timer += 1;
		}
		
		
		
		
	}
	
	@Override
	public boolean canUpdate() {
		return true;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		compound.setByte("sides", sides);
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		sides = compound.getByte("sides");
	}

	@Override
	public boolean IsWrenchable(World world, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		return true;
	}
	
}
