package Core.config;

import java.io.File;

import net.minecraftforge.common.Configuration;
import Core.ModBFU.BlocksForUse;
import Core.blocks.BlockInfo;
import Core.items.ItemInfo;

public class ConfigHandler {

    
        public static void init(File file){
            
           Configuration config = new Configuration(file);
           config.load();
           
           
           BlockInfo.WEATHER_BLOCK_ID = config.getBlock(BlockInfo.WEATHER_BLOCK_KEY, BlockInfo.WEATHER_BLOCK_DEFAULT).getInt();
           BlockInfo.CALINDORIUM_BLOCK_ID = config.getBlock(BlockInfo.CALINDORIUM_BLOCK_KEY, BlockInfo.CALINDORIUM_BLOCK_DEFAULT).getInt();
           BlockInfo.CONTROLLER_BLOCK_ID = config.getBlock(BlockInfo.CONTROLLER_BLOCK_KEY, BlockInfo.CONTROLLER_BLOCK_DEFAULT).getInt();
           BlockInfo.STORAGE_BLOCK_ID = config.getBlock(BlockInfo.STORAGE_BLOCK_KEY, BlockInfo.STORAGE_BLOCK_DEFAULT).getInt();
           BlockInfo.WIRE_BLOCK_ID = config.getBlock(BlockInfo.WIRE_BLOCK_KEY, BlockInfo.WIRE_BLOCK_DEFAULT).getInt();
           BlockInfo.DISC_WRITER_BLOCK_ID = config.getBlock(BlockInfo.DISC_WRITER_BLOCK_KEY, BlockInfo.DISC_WRITER_BLOCK_DEFAULT).getInt();
           
           ItemInfo.WRENCH_ID = config.getItem(ItemInfo.WRENCH_KEY, ItemInfo.WRENCH_DEFAULT).getInt() - 256;
           ItemInfo.DISC_ID = config.getItem(ItemInfo.DISC_KEY, ItemInfo.DISC_DEFAULT).getInt() - 256;
           ItemInfo.CALINDORIUM_ID = config.getItem(ItemInfo.CALINDORIUM_KEY, ItemInfo.CALINDORIUM_DEFAULT).getInt() - 256;
           ItemInfo.SIDES_ID = config.getItem(ItemInfo.SIDES_KEY, ItemInfo.SIDES_DEFAULT).getInt() - 256;
           ItemInfo.MP3PLAYER_ID = config.getItem(ItemInfo.MP3PLAYER_KEY, ItemInfo.MP3PLAYER_DEFAULT).getInt() - 256;
           
           BlocksForUse.addID(BlockInfo.CONTROLLER_BLOCK_ID);
           BlocksForUse.addID(BlockInfo.STORAGE_BLOCK_ID);
           BlocksForUse.addID(BlockInfo.WIRE_BLOCK_ID);
           
           config.save();
           
        }
    
}
