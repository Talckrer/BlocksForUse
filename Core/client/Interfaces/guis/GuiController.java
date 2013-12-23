package Core.client.Interfaces.guis;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import Core.client.Interfaces.containers.ContainerController;
import Core.handlers.PacketHandler;
import Core.tileEntities.TileEntityController;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiController extends GuiContainer {
	
	private static TileEntityController controller;

	public GuiController(InventoryPlayer invPlayer, TileEntityController controller) {
		super(new ContainerController(invPlayer, controller));
		xSize = 229;
		ySize = 242;
		this.controller = controller;
	}
	
	private static final ResourceLocation texture = new ResourceLocation("bfu", "textures/gui/ControllerGui.png");

	
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1, 1, 1, 1);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		int pixelsHigh = (int)Math.floor((double)((float)controller.getEnergyStorage() / controller.getEnergyCapacity()) * 136);
		drawTexturedModalRect(guiLeft + 15, guiTop + 91 + (136 - pixelsHigh), 229, 0, 16, pixelsHigh);
		
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRenderer.drawString("Controller", 72, 14, 0x404040);
		fontRenderer.drawString("ID : " + controller.getID(), 180, 30, 0x404040);
		if (x > guiLeft + 15 && x < guiLeft + 15 + 16 && y > guiTop + 91 && y < guiTop + 91 + 136){
			drawHoverString("Energy " + controller.getEnergyStorage() + "/" + controller.getEnergyCapacity(), x, y);
		}
	}
	
	protected void  drawHoverString(String text, int x, int y){
		List list = Arrays.asList(text);
		drawHoveringText(list , x - guiLeft, y - guiTop, fontRenderer);
	}
	
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.clear();
		
		buttonList.add(new GuiButton(0, guiLeft + 95, guiTop + 53, 100, 20, "Add energy"));
		
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		PacketHandler.sendButtonPacket(0, button.id);
	}

}
