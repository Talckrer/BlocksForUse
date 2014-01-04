package Core.client.Interfaces.guis;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import Core.client.Interfaces.Button;
import Core.client.Interfaces.Info;
import Core.client.Interfaces.MovableSelectionMP3Gui;
import Core.client.Interfaces.containers.ContainerMP3Player;
import Core.client.sounds.SoundLoader;
import Core.client.sounds.Sounds;
import Core.handlers.PacketHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMP3Player extends GuiContainer{
	
	public GuiMP3Player(EntityPlayer player, World world) {
		super(new ContainerMP3Player());
		xSize = 247;
		ySize = 213;
		
		buttons = new ArrayList<Button>(6);
		this.player = player;
		this.world = world;
		
		isPlayingIndex = Info.MP3PlayerIndexToOpen;
		Sounds.UpdateFile(isPlayingIndex);
		Info.GuiMp3Player = this;
		Info.player = player;
		if (!SoundLoader.musicFiles.isEmpty() && Sounds.canRetrieveInfo(isPlayingIndex)){
			durationInt = Integer.parseInt(Sounds.getInfo(isPlayingIndex, "durationInt"));
		}
		isPlaying = Sounds.fileRunning;
		
		SoundLoader.loadListForGui(false);
	}


	private void addButton(Button button) {
		buttons.add(button);
	}


	private static final ResourceLocation texture = new ResourceLocation("bfu", "textures/gui/MP3PlayerGui.png");
	private static final ResourceLocation texturePart2 = new ResourceLocation("bfu", "textures/gui/MP3PlayerGuiPart2.png");
	private static final ResourceLocation textureButton2 = new ResourceLocation("bfu", "textures/gui/MP3PlayerGuiButton2.png");
	private static final ResourceLocation textureButton5 = new ResourceLocation("bfu", "textures/gui/MP3PlayerGuiButton5.png");
	private static final ResourceLocation textureProgressBar = new ResourceLocation("bfu", "textures/gui/MP3PlayerGuiProgressBar.png");
	private static final ResourceLocation textureProgressThing = new ResourceLocation("bfu", "textures/gui/MP3PlayerGuiProgressThing.png");
	private static final ResourceLocation textureDragging = new ResourceLocation("bfu", "textures/gui/MP3PlayerGuiDraggingDisplay.png");
	private static final ResourceLocation textureBack = new ResourceLocation("bfu", "textures/gui/MP3PlayerGuiBack.png");
	private static final ResourceLocation textureVerticalBar = new ResourceLocation("bfu", "textures/gui/MP3PlayerGuiVerticalBar.png");
	private static final ResourceLocation textureBlueBar = new ResourceLocation("bfu", "textures/gui/MP3PlayerGuiBlueBar.png");
	private static final ResourceLocation textureMovableBar = new ResourceLocation("bfu", "textures/gui/MP3PlayerGuiMovableBar.png");
	private static final ResourceLocation textureMusicIcon = new ResourceLocation("bfu", "textures/gui/MP3PlayerGuiMusicIcon.png");
	private static final ResourceLocation textureFolderIcon = new ResourceLocation("bfu", "textures/gui/MP3PlayerGuiFolderIcon.png");
	private static final ResourceLocation textureBottomCover = new ResourceLocation("bfu", "textures/gui/MP3PlayerGuiBottomCover.png");
	private static final ResourceLocation textureButtonAllMusicFiles = new ResourceLocation("bfu", "textures/gui/MP3PlayerGuiButtonAllMusicFiles.png");
	private static final ResourceLocation textureShuffleOff = new ResourceLocation("bfu", "textures/gui/MP3PlayerGuiShuffleOff.png");
	private static final ResourceLocation textureShuffleOn = new ResourceLocation("bfu", "textures/gui/MP3PlayerGuiShuffleOn.png");
	private static final ResourceLocation textureRepeatOff = new ResourceLocation("bfu", "textures/gui/MP3PlayerGuiRepeatOff.png");
	private static final ResourceLocation textureRepeatOn = new ResourceLocation("bfu", "textures/gui/MP3PlayerGuiRepeatOn.png");
	
	
	private ArrayList<Button> buttons;
	
	public EntityPlayer player;
	public World world;
	
	private ItemStack stack;
	public int durationInt;
	private boolean draggingDisplay = false;
	
	public boolean isPlaying;//for buttons and playing
	public int isPlayingIndex = 0;
	
	public ArrayList<MovableSelectionMP3Gui> musicTabs = new ArrayList<MovableSelectionMP3Gui>();
	public ArrayList<MovableSelectionMP3Gui> folderTabs = new ArrayList<MovableSelectionMP3Gui>();
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1, 1, 1, 1);
		
		if (SoundLoader.folderLoaded.equals("")){
			((GuiButton)buttonList.get(0)).enabled = false;
		}else{
			((GuiButton)buttonList.get(0)).enabled = true;
		}
		
		if (Sounds.file != null){
			((GuiButton)buttonList.get(1)).enabled = true;
		}else{
			((GuiButton)buttonList.get(1)).enabled = false;
		}
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft - 128, guiTop, 0, 0, 256, 256);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(texturePart2);
		drawTexturedModalRect(guiLeft + 128, guiTop, 0, 0, 256, 256);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(textureVerticalBar);
		drawTexturedModalRect(guiLeft + 128 + 238, guiTop + 43, 0, 0, 8, 201);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(textureBottomCover);
		drawTexturedQuadFit(guiLeft+128, guiTop+244, 256, 40, 1.1);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(textureProgressBar);
		drawTexturedModalRect(guiLeft + 49 - 128, guiTop + 202, 0, 0, 160, 8);
		

		Minecraft.getMinecraft().getTextureManager().bindTexture(textureProgressThing);
		if (isDragging){
			drawTexturedQuadFit(guiLeft + 49 - 128 + Measure - 6, guiTop + 200, 12, 12, 1);
		}else{
			if (Sounds.fileRunning || (isPlayingIndex < Sounds.files.size() && Sounds.canRetrieveInfo(isPlayingIndex))){
				drawTexturedQuadFit(guiLeft + 49 - 128 + (Math.round((double)Info.SecondsPlayed / /*Integer.parseInt(Sounds.getInfo(isPlayingIndex, "durationInt"))durationInt * 160))*/durationInt*160 - 6)), guiTop + 200, 12, 12, 1);
			}
		}
		
		if (buttons != null && !buttons.isEmpty()){
			if (buttons.get(1).DoDraw){
				Minecraft.getMinecraft().getTextureManager().bindTexture(textureButton2);
				drawTexturedQuadFit(guiLeft + 93 - 128, guiTop + 228, 37, 20, 1);
			}
			if (buttons.get(4).DoDraw){
				Minecraft.getMinecraft().getTextureManager().bindTexture(textureButton5);
				drawTexturedQuadFit(guiLeft + 93 - 128, guiTop + 228, 37, 20, 1);
			}
			if (isPlaying){
				buttons.get(4).appear();//5 on pause
				buttons.get(1).disappear();//2 on play
			}else{
				buttons.get(1).appear();
				buttons.get(4).disappear();
			}
			
			if (Info.isShuffle){
				Minecraft.getMinecraft().getTextureManager().bindTexture(textureShuffleOn);
				drawTexturedQuadFit(guiLeft+223-128, guiTop+133, 16, 16, 1.1);
			}else{
				Minecraft.getMinecraft().getTextureManager().bindTexture(textureShuffleOff);
				drawTexturedQuadFit(guiLeft+223-128, guiTop+133, 16, 16, 1.1);
			}
			
			if (Info.isRepeat){
				Minecraft.getMinecraft().getTextureManager().bindTexture(textureRepeatOn);
				drawTexturedQuadFit(guiLeft+203-128, guiTop+133, 16, 16, 1.1);
			}else{
				Minecraft.getMinecraft().getTextureManager().bindTexture(textureRepeatOff);
				drawTexturedQuadFit(guiLeft+203-128, guiTop+133, 16, 16, 1.1);
			}
			
			Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
			for (Button button : buttons){
				if (button.DoDraw){
					drawTexturedModalRect(guiLeft + button.x, guiTop + button.y, button.textureX, button.textureY, button.sizeX, button.sizeY);
				}
			}
			
			
			Minecraft.getMinecraft().getTextureManager().bindTexture(textureProgressThing);
			drawTexturedQuadFit(guiLeft + 128 + 236, guiTop + 37 + MeasureVertical, 12, 12, 1.1);
			
			Minecraft.getMinecraft().getTextureManager().bindTexture(textureBlueBar);
			drawTexturedQuadFit(guiLeft + 128 + 13, guiTop + 11, 233, 32, 1);
			
			Minecraft.getMinecraft().getTextureManager().bindTexture(textureBack);
			drawTexturedQuadFit(guiLeft + 128 + 14, guiTop + 12, 32, 32, 1.1);
			
			int TabsCombined = folderTabs.size() + musicTabs.size();
			int Location = 0;
			int Move = (int) (((float)MeasureVertical / 201) * (TabsCombined * 40 - 201 > -1 ? TabsCombined * 40 - 201 : 0));
			if (!folderTabs.isEmpty()){
				for (int i1 = 1; i1 < folderTabs.size()+1; i1++){
					MovableSelectionMP3Gui bar = folderTabs.get(i1 - 1);
					int yPlace = 43 + Location - Move;
					bar.guiY = yPlace;
					bar.doDraw = false;
					bar.ID = i1;
					if (yPlace > 11 && yPlace < 239){
						bar.doDraw = true;
					}
					Location += 40;
				}
				for (MovableSelectionMP3Gui bar : folderTabs){
					if (bar.doDraw){
						drawMovableTab(true, bar.Name, 13, bar.guiY);
					}
				}
			}
			if (!musicTabs.isEmpty()){
				for (int i1 = 1; i1 < musicTabs.size()+1; i1++){
					MovableSelectionMP3Gui bar = musicTabs.get(i1 - 1);
					int yPlace = 43 + Location - Move;
					bar.guiY = yPlace;
					bar.doDraw = false;
					bar.ID = i1;
					if (yPlace > 11 && yPlace < 239){
						bar.doDraw = true;
					}
					Location += 40;
				}
				for (MovableSelectionMP3Gui bar : musicTabs){
					if (bar.doDraw){
						drawMovableTab(false, bar.Name, 13, bar.guiY);
					}
				}
			}
			
			if (!Sounds.files.isEmpty() && isPlayingIndex < Sounds.files.size()){
				int width = 0;
				List<String> list = fontRenderer.listFormattedStringToWidth(Sounds.getFile(isPlayingIndex).getName(), 230);
				for (int a = 0; a < list.size(); a++){
					String string = list.get(a);
					width = fontRenderer.getStringWidth(string);
					fontRenderer.drawString(string, guiLeft - width / 2, guiTop + 161 + a * 9, 0x151515);
				}
			}
			
			fontRenderer.drawSplitString(SoundLoader.folderLoaded, guiLeft-128+15, guiTop+95, 225, 0x010101);
			fontRenderer.drawString("MP3 player", guiLeft - fontRenderer.getStringWidth("MP3 player") / 2, guiTop+12, 0x010101);
			fontRenderer.drawSplitString(getSecondsPlayedAsString(durationInt), guiLeft + 122 - fontRenderer.getStringWidth(getSecondsPlayedAsString(durationInt)), guiTop + 176, 200, 0x151515);
			fontRenderer.drawString(isDragging ? "00:00" : getSecondsPlayedAsString(Info.SecondsPlayed), guiLeft + 14 - 128, guiTop + 202, 0x151515);
			fontRenderer.drawString(isDragging ? getSecondsPlayedAsString(durationInt) : getSecondsPlayedAsString(durationInt - Info.SecondsPlayed), guiLeft + 216 - 128, guiTop + 202, 0x151515);
			if (isDragging){
				fontRenderer.drawString(getSecondsPlayedAsString(convertMeasureToTime(Measure, durationInt)), guiLeft + 52 - 128 + Measure - 16, guiTop + 180, 0x151515);
			}
			

		}
	}
	/**
	 * 
	 * @param isFolder
	 * @param name
	 * @param x compared to GuiLeft
	 * @param y
	 */
	private void drawMovableTab(boolean isFolder, String name, int x, int y){
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glScalef(1, 1, 1);
		Minecraft.getMinecraft().getTextureManager().bindTexture(textureMovableBar);
		drawTexturedQuadFit(guiLeft+128+x, guiTop+y, 225, 40, 0.1);
		Minecraft.getMinecraft().getTextureManager().bindTexture(isFolder ? textureFolderIcon : textureMusicIcon);
		drawTexturedQuadFit(guiLeft+128+x + 8, guiTop+y + 12, 16, 16, 0.99);
		
//		if (isFolder){
//			Minecraft.getMinecraft().getTextureManager().bindTexture(textureButtonAllMusicFiles);
//			drawTexturedQuadFit(x+198, y+5, 40, 30, 2);
//		}
		
		int size = fontRenderer.listFormattedStringToWidth(name, isFolder ? 158 : 180).size();
		switch (size){
		case 1:
			fontRenderer.drawSplitString(name, guiLeft+128+x+36, guiTop+y+17, isFolder ? 158 : 180, 0x010101);
			break;
		case 2:
			fontRenderer.drawSplitString(name, guiLeft+128+x+36, guiTop+y+12, isFolder ? 158 : 180, 0x010101);
			break;
		case 3:
			fontRenderer.drawSplitString(name, guiLeft+128+x+36, guiTop+y+8, isFolder ? 158 : 180, 0x010101);
			break;
		case 4:
			fontRenderer.drawSplitString(name, guiLeft+128+x+36, guiTop+y+3, isFolder ? 158 : 180, 0x010101);
			break;
		}
		
		
		
	}
	
	
	@Override
	public void onGuiClosed() {
		
		super.onGuiClosed(); 
	}
	
	
	
	@Override
	public void initGui() {
		super.initGui();
		
		buttonList.clear();
		buttonList.add(new GuiButton(1, guiLeft+128+56, guiTop+18, 80, 20, "Open folder"));
		buttonList.add(new GuiButton(2, guiLeft+128+156, guiTop+18, 80, 20, "Now playing"));
		
		buttons = new ArrayList<Button>();
		addButton(new Button(1, -73, 228, 37, 20, 0, 0, false, true));//previous
		addButton(new Button(2, -36, 228, 37, 20, 0, 0, false, true));//play
		addButton(new Button(3, 1, 228, 37, 20, 0, 0, false, true));//stop
		addButton(new Button(4, 38, 228, 37, 20, 0, 0, false, true));//next
		addButton(new Button(5, -36, 228, 37, 20, 0, 0, false, true));//pause
		addButton(new Button(6, 128 + 14, 12, 32, 32, 0, 0, false, true));//back
		addButton(new Button(7, 223-128, 133, 16, 16, 0, 0, false, true));//Shuffle
		addButton(new Button(8, 203-128, 133, 16, 16, 0, 0, false, true));//repeat
		
	}
	
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	
	
	public void Action(String action){
		if (action.equals("toggle play")){
			if (isPlaying){
				Action("pause");
			}else{
				if (Sounds.startFrame == 0){
					Action("start play");
				}else{
					Action("resume");
				}
			}
			isPlaying = !isPlaying;
		}
		if (action.equals("start play")){
			Sounds.playRecord(isPlayingIndex);
		}
		if (action.equals("stop")){
			isPlaying = false;
			Info.SecondsPlayed = 0;
			Sounds.stopPlaying();
			continuePlaying = false;
		}
		if (action.equals("pause")){
			if (Sounds.fileRunning){
				Sounds.UpdateFile(isPlayingIndex);
				Sounds.pauseRecord(isPlayingIndex);
				if (Sounds.canRetrieveInfo(isPlayingIndex)){
					Sounds.startFrame = Math.round(((float)Info.SecondsPlayed / durationInt * Integer.parseInt(Sounds.getInfo(isPlayingIndex, "TotalFrames"))));
				}
			}
			
		}
		if (action.equals("resume")){
			player.addChatMessage("Resuming: " + Sounds.file.getName());
			Sounds.resumeRecord();
		}
		if (action.equals("previous")){
			Sounds.stopPlaying();
			if (Info.isShuffle){
				isPlayingIndex = Math.round((float)Math.random() * Sounds.getList().size());
			}else{
				if (isPlayingIndex == 0){
					isPlayingIndex = Sounds.getList().size() - 1;
				}else{
					isPlayingIndex -= 1;
				}
			}
			
			Info.SecondsPlayed = 0;
			if (Sounds.canRetrieveInfo(isPlayingIndex)){
				durationInt = Integer.parseInt(Sounds.getInfo(isPlayingIndex, "durationInt"));
			}
			Sounds.UpdateFile(isPlayingIndex);
			
			Info.MP3PlayerIndexToOpen = isPlayingIndex;
			
			if (isPlaying){
				Action("start play");
			}else{
				isPlaying = false;
			}
		}
		if (action.equals("next")){
			Sounds.stopPlaying();
			if (Info.isShuffle){
				isPlayingIndex = Math.round((float)Math.random() * Sounds.getList().size()-1);
			}else{
				if (isPlayingIndex == Sounds.getList().size() - 1){
					isPlayingIndex = 0;
				}else{
					isPlayingIndex += 1;
				}
			}
			
			
			Info.SecondsPlayed = 0;
			if (Sounds.canRetrieveInfo(isPlayingIndex)){
				durationInt = Integer.parseInt(Sounds.getInfo(isPlayingIndex, "durationInt"));
			}
			Sounds.UpdateFile(isPlayingIndex);
			
			Info.MP3PlayerIndexToOpen = isPlayingIndex;
			
			if (isPlaying){
				Action("start play");
			}else{
				isPlaying = false;
			}
		}
		if (action.equals("back")){
			Info.MP3PlayerIndexToOpen = isPlayingIndex;
			SoundLoader.ButtonClicked(true, -1);
		}
		if (action.equals("shuffle")){
			Info.isShuffle = !Info.isShuffle;
		}
		if (action.equals("repeat")){
			Info.isRepeat = !Info.isRepeat;
		}
	}
	
	private void checkButton(int x, int y) {
		if (buttons != null && !buttons.isEmpty()){
			for (Button button : buttons){
				if (isPointInRegion(button.x, button.y, button.sizeX, button.sizeY, x, y)){
					if (button.ClickEnabled){
						switch (button.id) {
							case 5:
							case 2:
								Action("toggle play");//toggle isPlaying
								break;
							case 3:
								Action("stop");
								break;
							case 1:
								Action("previous");
								break;
							case 4:
								Action("next");
								break;
							case 6:
								Action("back");
								break;
							case 7:
								Action("shuffle");
								break;
							case 8:
								Action("repeat");
						}
						PacketHandler.sendButtonPacket(1, button.id);
					}
					
				}
			}
		}
	}
	
	private boolean isDragging = false;
	private int Measure = 0;
	
	public boolean continuePlaying = false;
	
	private boolean isDraggingVertical = false;
	public int MeasureVertical = 0;
	
	
	@Override
	protected void mouseClicked(int x, int y, int keyBoardButton) {
		super.mouseClicked(x, y, keyBoardButton);
		
		checkButton(x, y);
		if (isPointInRegion(49 - 128, 202, 160, 8, x, y)){//song bar
			Measure = x - guiLeft - (49 - 128);
			isDragging = true;
			continuePlaying = isPlaying;
			Sounds.stopPlaying();
		}else{
			if (isPointInRegion(128 + 243, 43, 8, 201, x, y)){//scrolling bar
				MeasureVertical = y - guiTop - (43);
				isDraggingVertical = true;	
			}else{
				if (isPointInRegion(128+13, 43, 225, 201, x, y)){
//					if (isPointInRegion(128+188, 43, 40, 201, x, y)){
//						for (MovableSelectionMP3Gui bar : folderTabs){
//							if (isPointInRegion(128+198, bar.guiY+5, 40, 30, x, y)){
//								Sounds.files = SoundLoader.folders.get(SoundLoader.folderLoaded).AllMusicFiles;
//								Info.MP3PlayerIndexToOpen = 0;
//								Info.SecondsPlayed = 0;
//								isPlayingIndex = 0;
//								continuePlaying = true;
//								isPlaying = true;
//								Sounds.stopPlaying();
//								Sounds.playRecord(Info.MP3PlayerIndexToOpen);
//							}
//						}
//					}
					for (MovableSelectionMP3Gui bar : folderTabs){
						if (isPointInRegion(128+13, bar.guiY, 225, 32, x, y)){
							SoundLoader.ButtonClicked(true, bar.ID-1);
						}
					}
					for (MovableSelectionMP3Gui bar : musicTabs){
						if (isPointInRegion(128+13, bar.guiY, 225, 32, x, y)){
							SoundLoader.ButtonClicked(false, bar.ID-1);
						}
					}
					
				}
			}
			
		}
		
	}

	@Override
	protected void mouseClickMove(int x, int y, int lastButtonClicked, long timeSinceMouseClick) {
		super.mouseClickMove(x, y, lastButtonClicked, timeSinceMouseClick);
		
		if (isDragging){
			Measure = x - guiLeft - (49 - 128);
			if (Measure > 160){
				Measure = 160;
			}
			if (Measure < 0){
				Measure = 0;
			}
			draggingDisplay = true;
		}
		if (isDraggingVertical){
			MeasureVertical = y - guiTop - (43);
			if (MeasureVertical > 201){
				MeasureVertical = 201;
			}
			if (MeasureVertical < 0){
				MeasureVertical = 0;
			}
		}
	}
	
	@Override
	protected void mouseMovedOrUp(int par1, int par2, int par3) {
		super.mouseMovedOrUp(par1, par2, par3);
		
		if (isDragging){
			Info.SecondsPlayed = convertMeasureToTime(Measure, durationInt);
			if (Sounds.canRetrieveInfo(isPlayingIndex)){
				Sounds.startFrame = Math.round(((float)Info.SecondsPlayed / durationInt * Integer.parseInt(Sounds.getInfo(isPlayingIndex, "TotalFrames"))));
			}
			if (continuePlaying){
				Sounds.playRecord(isPlayingIndex);
			}
		}
		isDragging = false;
		isDraggingVertical = false;
		
		draggingDisplay = true;
	}
	
	
	
	
	
	
	public static void drawTexturedQuadFit(double x, double y, double width, double height, double zLevel){
		Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + height, zLevel, 0,1);
        tessellator.addVertexWithUV(x + width, y + height, zLevel, 1, 1);
        tessellator.addVertexWithUV(x + width, y + 0, zLevel, 1,0);
        tessellator.addVertexWithUV(x + 0, y + 0, zLevel, 0, 0);
        tessellator.draw();
        GL11.glColor4f(1, 1, 1, 1);
	}
	
	
	private String getSecondsPlayedAsString(int seconds){
		String finalString = "";
		if (seconds / 60 < 10){
			finalString += "0";
		}
		finalString += seconds / 60;
		finalString += ":";
		if (seconds % 60 < 10){
			finalString += "0";
		}
		finalString += seconds % 60;
		
		return finalString;
	}
	
	private int convertMeasureToTime(int Measure, int duration){
		return Math.round((float)Measure / 160 * duration);
	}
	
	public void UpdateLists(){
		folderTabs = Info.folderTabs;
		musicTabs = Info.musicTabs;
	}
	
	/*@Override
	public void handleKeyboardInput() {
		super.handleKeyboardInput();
		
		System.out.println("Key number: "+Keyboard.getEventKey());
		if (Keyboard.getEventKeyState()){
			int c = Keyboard.getEventKey() + 1;
			System.out.println(c);
			if (c > 0 && c < 10){
				if (Keyboard.isKeyDown(42)){
					SoundLoader.ButtonClicked(false, c);
				}else{
					SoundLoader.ButtonClicked(true, c);
				}
				
			}
		}
	}*/
	
	@Override
	protected void drawHoveringText(List list, int x, int y, FontRenderer font) {
		if (isPointInRegion(223-128, 133, 16, 16, x, y)){
			list.add("Shuffle: " + (Info.isShuffle ? "On" : "Off"));
		}
		if (isPointInRegion(203-128, 133, 16, 16, x, y)){
			list.add("Repeat song: " + (Info.isRepeat ? "On" : "Off"));
		}
		
		super.drawHoveringText(list, x, y, font);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id){
		case 1:
			try{
				if (Desktop.isDesktopSupported()){
					Desktop.getDesktop().open(new File(SoundLoader.folderLoaded));
				}
			}catch (IOException e){
				e.printStackTrace();
			}
			break;
		case 2:
			if (Sounds.file != null){
				SoundLoader.folderLoaded = SoundLoader.removeLastThing(Sounds.file.getAbsolutePath());
			}
			break;
		}
		
	}
}
