package Core.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import Core.ModBFU.ModInformation;
import Core.TileEntities.TileEntityController;
import Core.TileEntities.TileEntityDiscWriter;
import Core.client.Interfaces.ContainerController;
import Core.client.Interfaces.ContainerDiscWriter;
import Core.client.Interfaces.Info;
import Core.client.sounds.SoundLoader;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {
	

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        ByteArrayDataInput reader = ByteStreams.newDataInput(packet.data);
        
        EntityPlayer entityPlayer = (EntityPlayer)player;
        
        byte packetID = reader.readByte();
        
        switch (packetID){
	        case 0:
	        	byte InterfaceID = reader.readByte();
	        	byte buttonID = reader.readByte();
	        	switch(InterfaceID){
		        	case 0:   //Controller
		        		switch (buttonID){
			        		case 0:   //ButtonAddEnergy
			        			Container container = entityPlayer.openContainer;
			        			if (container != null && container instanceof ContainerController){
			        				TileEntityController controller = ((ContainerController)container).getController();
			        				controller.addEnergy(5000);
			        			}
			        			break;
		        		}
		        		break;
		        	case 1:  //Disc Writer
		        		String path = reader.readLine();
		        		Container container = entityPlayer.openContainer;
		        		if (container != null && container instanceof ContainerDiscWriter){
	        				TileEntityDiscWriter discWriter = ((ContainerDiscWriter)container).getTileEntity();
	        				
	        				ItemStack stack = discWriter.getStackInSlot(1);
	        				NBTTagCompound compound = stack.getTagCompound();
	        				
	        		        stack.stackTagCompound = new NBTTagCompound();
	        				stack.stackTagCompound.setTag("Path", new NBTTagCompound());
	        				
	        		        stack.stackTagCompound.getCompoundTag("Path").setString("Path", path);
	        		        stack.stackTagCompound.getCompoundTag("Path").setString("PathShort", SoundLoader.getLastThing(path));
	        		        
	        			}
		        		break;
	        	}
	        	break;
	        case 50: //ID Request to server
	        	int blockID = reader.readInt();
	        	int dataType = reader.readInt();
	        	int x = reader.readInt();
	        	int y = reader.readInt();
	        	int z = reader.readInt();
	        	if (blockID == 0){ //controller
	        		if (entityPlayer.worldObj.getBlockTileEntity(x, y, z) != null && entityPlayer.worldObj.getBlockTileEntity(x, y, z) instanceof TileEntityController){
	        			TileEntityController controller = (TileEntityController)entityPlayer.worldObj.getBlockTileEntity(x, y, z);
	        			if (dataType == 0){   //id
	        				this.sendRequestPacketToClients(blockID, dataType, x, y, z, controller.getID());
	        			}else if(dataType == 1){  //energy
	        				this.sendRequestPacketToClients(blockID, dataType, x, y, z, controller.getEnergyStorage());
	        			}
	        			
	        		}
	        	}
	        	
	        	break;
	        	
	        case 51: //ID send to all players
	        	int blockID1 = reader.readInt();
	        	int dataType1 = reader.readInt();
	        	int x1 = reader.readInt();
	        	int y1 = reader.readInt();
	        	int z1 = reader.readInt();
	        	int value = reader.readInt();
	        	if (blockID1 == 0){  //controller
	        		if (entityPlayer.worldObj.getBlockTileEntity(x1, y1, z1) != null && entityPlayer.worldObj.getBlockTileEntity(x1, y1, z1) instanceof TileEntityController){
	        			TileEntityController controller = (TileEntityController)entityPlayer.worldObj.getBlockTileEntity(x1, y1, z1);
	        			if (dataType1 == 0){ //id
	        				controller.setID(value);
	        			}else if(dataType1 == 1){ //energy
	        				controller.setEnergy(value);
	        			}
	        			
	        		}
	        	}
	        	
	        	
        }
    }
    
    public static void sendButtonPacket(int InterfaceID, int buttonID){
    	ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    	DataOutputStream dataStream = new DataOutputStream(byteStream);
    	
    	try{
    		dataStream.writeByte(0);
    		dataStream.writeByte(InterfaceID);
    		dataStream.writeByte(buttonID);
    		if (InterfaceID == 1){//Disc writer
    			dataStream.writeBytes(Info.TempDiscWriterString);
    		}
    		
    		PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(ModInformation.CHANNEL, byteStream.toByteArray()));
    	}catch(IOException ex){
    		System.err.append("Failed to send Interface: " + InterfaceID + " button: " + buttonID + " packet");
    	}
    }
    
    public static void sendRequestPacketToServer(int blockID, int dataType, int x, int y, int z){
    	ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    	DataOutputStream dataStream = new DataOutputStream(byteStream);
    	
    	try{
    		dataStream.writeByte(50);
    		dataStream.writeInt(blockID);
    		dataStream.writeInt(dataType);
    		dataStream.writeInt(x);
    		dataStream.writeInt(y);
    		dataStream.writeInt(z);
    		
    		PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(ModInformation.CHANNEL, byteStream.toByteArray()));
    	}catch(IOException ex){
    		System.err.append("Failed sending IDRequest " + x + " : " + y + " : " + z + " packet from client to server");
    	}
    }
    
    public static void sendRequestPacketToClients(int blockID, int dataType, int x, int y, int z, int value){
    	ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    	DataOutputStream dataStream = new DataOutputStream(byteStream);
    	
    	try{
    		dataStream.writeByte(51);
    		dataStream.writeInt(blockID);
    		dataStream.writeInt(dataType);
    		dataStream.writeInt(x);
    		dataStream.writeInt(y);
    		dataStream.writeInt(z);
    		dataStream.writeInt(value);
    		
    		PacketDispatcher.sendPacketToAllPlayers(PacketDispatcher.getPacket(ModInformation.CHANNEL, byteStream.toByteArray()));
    	}catch(IOException ex){
    		System.err.append("Failed sending Request " + value + " packet from server to client");
    	}
    }
    
    

}
