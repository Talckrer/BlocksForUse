package Core.handlers;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import Core.items.Items;
import cpw.mods.fml.common.registry.GameRegistry;

public class CraftingHandler {
	
	public static void Init(){
		GameRegistry.addShapedRecipe(new ItemStack(Items.disc, 1), " x ", " # ", "///", "x", Item.redstone, "#", Block.glass, "/", Block.stoneSingleSlab);
	}
}
