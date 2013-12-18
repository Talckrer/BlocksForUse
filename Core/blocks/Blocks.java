package Core.blocks;

import net.minecraft.block.Block;
import Core.TileEntities.ControllerIDs;
import Core.TileEntities.TileEntityController;
import Core.TileEntities.TileEntityDiscWriter;
import Core.TileEntities.TileEntityWeather;
import Core.TileEntities.TileEntityWire;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Blocks {
    
    public static Block weather;
    public static Block CalindoriumOre;
    public static Block Controller;
    public static Block Wire;
    public static Block DiscWriter;
    public static ControllerIDs idControllerRegistry;
    
    
    public static void init() {
        
        weather = new blockWeather(BlockInfo.WEATHER_BLOCK_ID);
        GameRegistry.registerBlock(weather, BlockInfo.WEATHER_BLOCK_KEY);
        CalindoriumOre = new blockOreCalindorium(BlockInfo.CALINDORIUM_BLOCK_ID);
        GameRegistry.registerBlock(CalindoriumOre, BlockInfo.CALINDORIUM_BLOCK_KEY);
        Controller = new BlockController(BlockInfo.CONTROLLER_BLOCK_ID);
//        GameRegistry.registerBlock(Controller, BlockInfo.CONTROLLER_BLOCK_KEY);
        
        idControllerRegistry = new ControllerIDs();
        
        Wire = new BlockWire(BlockInfo.WIRE_BLOCK_ID);
        GameRegistry.registerBlock(Wire, BlockInfo.WIRE_BLOCK_KEY);
        DiscWriter = new BlockDiscWriter(BlockInfo.DISC_WRITER_BLOCK_ID);
        GameRegistry.registerBlock(DiscWriter, BlockInfo.DISC_WRITER_BLOCK_KEY);
    }
    
    public static void addNames() {
        LanguageRegistry.addName(weather, BlockInfo.WEATHER_BLOCK_NAME);
        LanguageRegistry.addName(CalindoriumOre, BlockInfo.CALINDORIUM_BLOCK_NAME);
//        LanguageRegistry.addName(Controller, BlockInfo.CONTROLLER_BLOCK_NAME);
        LanguageRegistry.addName(Wire, BlockInfo.WIRE_BLOCK_NAME);
        LanguageRegistry.addName(DiscWriter, BlockInfo.DISC_WRITER_BLOCK_NAME);
    }
    
    public static void registerTileEntities(){
        
        GameRegistry.registerTileEntity(TileEntityWeather.class, BlockInfo.WEATHER_BLOCK_TE_KEY);
//        GameRegistry.registerTileEntity(TileEntityController.class, BlockInfo.CONTROLLER_BLOCK_TE_KEY);
        GameRegistry.registerTileEntity(TileEntityWire.class, BlockInfo.WIRE_BLOCK_TE_KEY);
        GameRegistry.registerTileEntity(TileEntityDiscWriter.class, BlockInfo.DISC_WRITER_BLOCK_TE_KEY);
    }
    
    
    public static ControllerIDs getIDRegistry(){
    	return idControllerRegistry;
    }
}
