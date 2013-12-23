package Core.client.Interfaces.guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import Core.client.Interfaces.Info;
import Core.client.Interfaces.containers.ContainerDiscWriter;
import Core.client.sounds.SoundLoader;
import Core.client.sounds.Sounds;
import Core.handlers.PacketHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiDiscWriter extends GuiContainer {

	public GuiDiscWriter(ContainerDiscWriter container) {
		super(container);
		
		this.container = container;
		this.player = player;
		xSize = 195;
		ySize = 178;
	}

	private ResourceLocation texture = new ResourceLocation("bfu", "textures/gui/DiscWriter.png");
	
	private ContainerDiscWriter container;
	private EntityPlayer player;
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1, 1, 1, 1);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		drawTexturedModalRect(guiLeft+58, guiTop+51, 0, 178, 18, 18);
		drawTexturedModalRect(guiLeft+114, guiTop+51, 18, 178, 18, 18);
		
		
		
		fontRenderer.drawString("Disc writer", guiLeft+98-fontRenderer.getStringWidth("Disc writer")/2, guiTop+16, 0x010101);
	}

	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.clear();
		
		buttonList.add(new GuiButton(0, guiLeft + 57, guiTop + 72, 50, 20, "Write"));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		
		if (button.id == 0 && !Sounds.files.isEmpty()){
			Info.TempDiscWriterString = Sounds.getFile(Info.MP3PlayerIndexToOpen).getAbsolutePath();
			
			ItemStack stack = container.getTileEntity().getStackInSlot(1);
			
	        stack.stackTagCompound = new NBTTagCompound();
			stack.stackTagCompound.setTag("Path", new NBTTagCompound());
			
	        stack.stackTagCompound.getCompoundTag("Path").setString("Path", Info.TempDiscWriterString);
	        stack.stackTagCompound.getCompoundTag("Path").setString("PathShort", SoundLoader.getLastThing(Info.TempDiscWriterString));
	        
			PacketHandler.sendButtonPacket(1, 0);
		}
	}
}
