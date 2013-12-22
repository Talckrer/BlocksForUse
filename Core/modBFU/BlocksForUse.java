package Core.modBFU;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.filechooser.FileSystemView;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import Core.blocks.Blocks;
import Core.blocks.IDsinNetwork;
import Core.client.Interfaces.ClipBoardManager;
import Core.client.Interfaces.Info;
import Core.client.sounds.SoundLoader;
import Core.client.sounds.Sounds;
import Core.handlers.ConfigHandler;
import Core.handlers.CraftingHandler;
import Core.handlers.GuiHandler;
import Core.handlers.PacketHandler;
import Core.handlers.ThreadHandler;
import Core.items.Items;
import Core.proxies.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = ModInformation.ID, name = ModInformation.NAME, version = ModInformation.VERSION)
@NetworkMod(channels = {ModInformation.CHANNEL}, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)

public class BlocksForUse {
    
    @Instance("BlocksForUse")
    public static BlocksForUse instance;
    
    @SidedProxy(clientSide = "Core.proxies.ClientProxy", serverSide = "Core.proxies.CommonProxy")
    public static CommonProxy proxy;
    
    public static IDsinNetwork IDs;
    
    public static Sounds sounds;
    public static Info info;
    public static ClipBoardManager clipManager;
    
    public static char BackSlash;
    
    public static CreativeTabs tabDisks = new CreativeTabs("tabDiscs") {
        public ItemStack getIconItemStack() {
        	return new ItemStack(Items.disc, 1, 0);
        }
    };
    public static CreativeTabs tabBFUThings = new CreativeTabs("tabBFUThings") {
        public ItemStack getIconItemStack() {
        	return new ItemStack(Blocks.Controller, 1, 0);
        }
    };
    
    public static void addID(int id){
    	IDs.addID(id);
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	clipManager = new ClipBoardManager();
    	sounds = new Sounds();
    	info = new Info();
    	ThreadHandler.Init();
    	IDs = new IDsinNetwork();
    	ConfigHandler.init(event.getSuggestedConfigurationFile());
    	
    	String path = event.getSuggestedConfigurationFile().getAbsolutePath();
        
    	BackSlash = path.charAt(2);
    	info.MusicPath = "";//+BackSlash+"Development"+BackSlash+"forge"+BackSlash+"mcp"+BackSlash+"jars"+BackSlash+"mods"+BackSlash+"BFUMusic";
    	System.out.println(info.MusicPath);
        
    	FileSystemView viewer = new FileSystemView() {
			@Override
			public File createNewFolder(File containingDir) throws IOException {
				return null;
			}
		};
    	
    	File[] roots = File.listRoots();
    	for(File file : roots){
    	    SoundLoader.drives.add(file);
    	    SoundLoader.driveNames.add(viewer.getSystemDisplayName(file));
    	    System.out.println("Added drive: "+file.getAbsolutePath());
    	}
    	SoundLoader.folderLoaded = info.MusicPath;
        proxy.initSounds();
        proxy.initRenderers();
        
    }
    

	@EventHandler
    public void load(FMLInitializationEvent event){
		LanguageRegistry.instance().addStringLocalization("itemGroup.tabDiscs", "en_US", "Disks");
		LanguageRegistry.instance().addStringLocalization("itemGroup.tabBFUThings", "en_US", "BFU Things");
        Blocks.init();
        Blocks.addNames();
        Blocks.registerTileEntities();
//        Items.init();
//        Items.addNames();
        CraftingHandler.Init();
        new GuiHandler();
    }
    
    
    @EventHandler
    public void modsLoaded(FMLPostInitializationEvent event){
        
    }
    
    
    public static void writeReadme(File readme, String text){
    	byte[] bytes = text.getBytes();
    	try{
    		FileOutputStream stream = new FileOutputStream(readme);
    		stream.write(bytes);
    	}catch (FileNotFoundException e){
    		System.out.println("Readme file not found");
    	}catch (IOException i){
    		System.out.println("Unable to write readme");
    	}finally{
    		
    	}
    	
    }
    
    public final static String readmeText = "Author: Talckrer\n\nThis mod allows you to play MP3s in Minecraft.\nJust put them in the music";
    
}
