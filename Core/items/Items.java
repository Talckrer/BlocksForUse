package Core.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Items {
    
    public static Item wrench;
    public static Item disc;
    public static Item calindorium;
    public static Item sides;
    public static Item mp3player;
    
    
    public static void init() {
        
        wrench = new ItemWrench(ItemInfo.WRENCH_ID);
        GameRegistry.registerItem(wrench, ItemInfo.WRENCH_KEY);
        disc = new ItemDisc(ItemInfo.DISC_ID);
        GameRegistry.registerItem(disc, ItemInfo.DISC_KEY);
        calindorium = new ItemCalindorium(ItemInfo.CALINDORIUM_ID);
        GameRegistry.registerItem(calindorium, ItemInfo.CALINDORIUM_KEY);
        sides = new ItemSideSensitive(ItemInfo.SIDES_ID);
        GameRegistry.registerItem(sides, ItemInfo.SIDES_KEY);
        mp3player = new ItemMP3Player(ItemInfo.MP3PLAYER_ID);
        GameRegistry.registerItem(mp3player, ItemInfo.MP3PLAYER_KEY);
    }
    
    public static void addNames(){
        
        LanguageRegistry.addName(wrench, ItemInfo.WRENCH_NAME);
        LanguageRegistry.addName(new ItemStack(disc, 1), "Unknown");
        LanguageRegistry.addName(calindorium, ItemInfo.CALINDORIUM_NAME);
        LanguageRegistry.addName(sides, ItemInfo.SIDES_NAME);
        LanguageRegistry.addName(mp3player, ItemInfo.MP3PLAYER_NAME);
    }
}
