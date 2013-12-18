package Core.items;

import java.io.File;
import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import Core.ModBFU.BlocksForUse;
import Core.blocks.BlockInfo;
import Core.client.Interfaces.GuiColor;
import Core.client.Interfaces.Info;
import Core.client.sounds.SoundLoader;
import Core.client.sounds.Sounds;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDisc extends Item {
	

	public ItemDisc(int id) {
		super(id);
		setMaxStackSize(1);
		setCreativeTab(BlocksForUse.tabDisks);
	}
	
	@SideOnly(Side.CLIENT)
	private Icon iconDisc;
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		iconDisc = register.registerIcon(BlockInfo.TEXTURE_LOCATION + ":" + ItemInfo.DISC_TEXTURE);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int dmg) {
		return iconDisc;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean addExtraInformation) {
		super.addInformation(stack, player, list, addExtraInformation);
		
		String path = "";
		try{
			NBTTagCompound compound = stack.getTagCompound();
			path = compound.getCompoundTag("Path").getString("Path");
			if (new File(path).exists()){
				list.add(GuiColor.GREEN + "Exists");
			}else{
				list.add(GuiColor.RED + "Doesn't exist");
			}
			
		}catch (NullPointerException e){
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world,EntityPlayer player) {
		if (!player.isSneaking()){
			if (world.isRemote){
				String path = stack.stackTagCompound.getCompoundTag("Path").getString("Path");
				
				SoundLoader.folderLoaded = SoundLoader.removeLastThing(path);
				SoundLoader.loadListForGui(true);
				Info.SecondsPlayed = 0;
				for (int i = 0; i < Sounds.files.size(); i++){
					File file = Sounds.files.get(i);
					if (file.getAbsolutePath().equals(path)){
						Info.MP3PlayerIndexToOpen = i;
						if (Info.GuiMp3Player != null){
							Info.GuiMp3Player.isPlayingIndex = i;
						}
						player.addChatMessage("Now playing: "+file.getName());
						Sounds.playRecord(i);
					}
				}
			}
		}else{
			if (!world.isRemote){
				player.addChatMessage("Stopping any records from playing...");
			}else{
				BlocksForUse.sounds.stopPlaying();
			}
			
		}
		
		return stack;
	}
	
	@Override
	public String getItemDisplayName(ItemStack stack) {
		String path = "";
		try{
			NBTTagCompound compound = stack.getTagCompound();
			path = compound.getCompoundTag("Path").getString("PathShort");
			if (path.equals("")){
				return "Unknown disc";
			}
			return path;
			
		}catch (NullPointerException e){
			return "Unknown disc";
		}
		
	}
	
	@Override
	public void onCreated(ItemStack stack, World par2World,	EntityPlayer par3EntityPlayer) {
		stack.stackTagCompound = new NBTTagCompound();
	}
	
}
