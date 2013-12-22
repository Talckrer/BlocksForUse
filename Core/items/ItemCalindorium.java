package Core.items;

import java.util.List;
import java.util.Random;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import Core.blocks.BlockInfo;
import Core.modBFU.BlocksForUse;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCalindorium extends Item {

	public ItemCalindorium(int id) {
		super(id);
		setCreativeTab(BlocksForUse.tabBFUThings);
		setUnlocalizedName(ItemInfo.CALINDORIUM_UNLOCALIZED_NAME);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack,EntityPlayer player, List list, boolean addExtraInformation) {
		list.add("A bit poisonous when it's in players inventory");
		super.addInformation(stack, player, list, addExtraInformation);
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		itemIcon = register.registerIcon(BlockInfo.TEXTURE_LOCATION + ":" + ItemInfo.CALINDORIUM_TEXTURE);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world,Entity entity, int par4, boolean par5) {
		if (Random1of20()){
			((EntityLivingBase)entity).addPotionEffect(new PotionEffect(9, 200, 4));
		}
	}
	
	Random random = new Random();
	int result = 0;
	
	public boolean Random1of20(){
		result = random.nextInt(20);
		if (result == 0){
			return true;
		}else{
			return false;
		}
	
	}

}
