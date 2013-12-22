package Core.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import Core.client.Interfaces.ContainerController;
import Core.client.Interfaces.ContainerDiscWriter;
import Core.client.Interfaces.ContainerMP3Player;
import Core.client.Interfaces.GuiController;
import Core.client.Interfaces.GuiDiscWriter;
import Core.client.Interfaces.GuiMP3Player;
import Core.modBFU.BlocksForUse;
import Core.tileEntities.TileEntityController;
import Core.tileEntities.TileEntityDiscWriter;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class GuiHandler implements IGuiHandler {
	
	public GuiHandler(){
		NetworkRegistry.instance().registerGuiHandler(BlocksForUse.instance, this);
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case 0:
				TileEntity te = world.getBlockTileEntity(x, y, z);
				if (te != null && te instanceof TileEntityController) {
					return new ContainerController(player.inventory, (TileEntityController)te);
				}
				break;
			case 1:
				return new ContainerMP3Player();
			case 2:
				TileEntity te1 = world.getBlockTileEntity(x, y, z);
				if (te1 != null && te1 instanceof TileEntityDiscWriter) {
					return new ContainerDiscWriter(player.inventory, (TileEntityDiscWriter) te1);
				}
				break;
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case 0:
				TileEntity te = world.getBlockTileEntity(x, y, z);
				if (te != null && te instanceof TileEntityController) {
					return new GuiController(player.inventory, (TileEntityController)te);
				}
				break;
			case 1:
				return new GuiMP3Player(player, world);
			case 2:
				TileEntity te1 = world.getBlockTileEntity(x, y, z);
				if (te1 != null && te1 instanceof TileEntityDiscWriter) {
					return new GuiDiscWriter(new ContainerDiscWriter(player.inventory, (TileEntityDiscWriter) te1));
				}
				break;
				
		}
	
		return null;
	}

}
