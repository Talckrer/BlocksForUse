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

import org.apache.commons.io.FilenameUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import Core.client.Interfaces.Button;
import Core.client.Interfaces.Folder;
import Core.client.Interfaces.GuiColor;
import Core.client.Interfaces.Info;
import Core.client.Interfaces.MovableSelectionMP3Gui;
import Core.client.Interfaces.containers.ContainerMP3Player;
import Core.client.sounds.Playlist;
import Core.client.sounds.SoundLoader;
import Core.client.sounds.Sounds;
import Core.handlers.PacketHandler;
import Core.modBFU.BlocksForUse;
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
		if (!Sounds.files.isEmpty() && Sounds.canRetrieveInfo(isPlayingIndex)){
			durationInt = Integer.parseInt(Sounds.getInfo(isPlayingIndex, "durationInt"));
		}
		isPlaying = Sounds.fileRunning;
		
		actions.add("Play/Open folder");
		actions.add("Open with default program");
		actions.add("Rename");
		actions.add("Delete");
		actions.add("Move");
		actions.add("Make a folder/file");
		actions.add("Make a playlist");
		actions.add("Select a playlist");
		actions.add("Add to playlist");
		actions.add("Remove from playlist");
		CurrentAction = 0;
		CurrentActionPlaylist = 0;
		
		betweenActions.add("Paste");
		betweenActions.add("Rename");
		betweenActions.add("Giving name");
		
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
	private static final ResourceLocation textureRenameBar = new ResourceLocation("bfu", "textures/gui/MP3PlayerGuiRenameBar.png");
	private static final ResourceLocation textureTools = new ResourceLocation("bfu", "textures/gui/MP3PlayerGuiTools.png");
	private static final ResourceLocation textureToolsHighlight = new ResourceLocation("bfu", "textures/gui/MP3PlayerGuiToolsHighlight.png");
	private static final ResourceLocation textureBlack = new ResourceLocation("bfu", "textures/gui/MP3PlayerGuiBlack.png");
	
	
	private static final ResourceLocation textureOpenFolder = new ResourceLocation("bfu", "textures/gui/Texts/Open folder.png");
	private static final ResourceLocation texturePlay = new ResourceLocation("bfu", "textures/gui/Texts/Play.png");
	private static final ResourceLocation textureOpenDefault = new ResourceLocation("bfu", "textures/gui/Texts/Open with default program.png");
	private static final ResourceLocation textureRename = new ResourceLocation("bfu", "textures/gui/Texts/Rename.png");
	private static final ResourceLocation textureDelete = new ResourceLocation("bfu", "textures/gui/Texts/Delete.png");
	private static final ResourceLocation textureMove = new ResourceLocation("bfu", "textures/gui/Texts/Move.png");
	private static final ResourceLocation textureAddToPlaylist = new ResourceLocation("bfu", "textures/gui/Texts/Add to playlist.png");
	private static final ResourceLocation textureRemove = new ResourceLocation("bfu", "textures/gui/Texts/Remove.png");
	
	
	private ArrayList<Button> buttons;
	
	public EntityPlayer player;
	public World world;
	
	private File ChangableFile;
	
	public int CurrentAction;
	private int CurrentActionPlaylist;
	
	public boolean canActivate = false;
	public boolean canChange = true;
	public boolean canCancel = false;
	
	private boolean showTools = false;
	
	
	private boolean tools_file = true;
	private int tools_fileIndex = 0;
	private int tools_mouseX = 0;
	private int tools_mouseY = 0;
	
	private boolean whileGivingName = false;
	private String whileGivingNameString = "";
	
	private static ArrayList<String> actions = new ArrayList<String>();
	private static ArrayList<String> betweenActions = new ArrayList<String>();
	
	private ItemStack stack;
	public int durationInt;
	private boolean draggingDisplay = false;
	
	public boolean isPlaying;//for buttons and playing
	public int isPlayingIndex = 0;
	
	public ArrayList<MovableSelectionMP3Gui> musicTabs = new ArrayList<MovableSelectionMP3Gui>();
	public ArrayList<MovableSelectionMP3Gui> folderTabs = new ArrayList<MovableSelectionMP3Gui>();
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
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
		((GuiButton)buttonList.get(2)).enabled = canChange;
		((GuiButton)buttonList.get(3)).enabled = canActivate;
		((GuiButton)buttonList.get(4)).enabled = canCancel;
		((GuiButton)buttonList.get(5)).enabled = (SoundLoader.SelectedPlaylist != null);
		
		if (whileGivingName){
			Minecraft.getMinecraft().getTextureManager().bindTexture(textureRenameBar);
			drawTexturedModalRect(guiLeft, guiTop-40, 0, 0, 256, 40);
			
			if (Info.isSecond){
				drawTexturedModalRect(guiLeft+6+fontRenderer.getStringWidth(whileGivingNameString), guiTop-16, 0, 0, 4, 1);
			}
		}
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft - 128, guiTop, 0, 0, 256, 256);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(texturePart2);
		drawTexturedModalRect(guiLeft + 128, guiTop, 0, 0, 256, 256);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(textureVerticalBar);
		drawTexturedModalRect(guiLeft + 128 + 238, guiTop + 43, 0, 0, 8, 201);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(textureBottomCover);
		drawTexturedQuadFit(guiLeft+128, guiTop+244, 256, 40, 0.9);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(textureProgressBar);
		drawTexturedModalRect(guiLeft + 49 - 128, guiTop + 202, 0, 0, 160, 8);
		

		Minecraft.getMinecraft().getTextureManager().bindTexture(textureProgressThing);
		if (isDragging){
			drawTexturedQuadFit(guiLeft + 49 - 128 + Measure - 6, guiTop + 200, 12, 12, 0.8);
		}else{
			if (Sounds.fileRunning || (isPlayingIndex < Sounds.files.size() && Sounds.canRetrieveInfo(isPlayingIndex))){
				drawTexturedQuadFit(guiLeft + 49 - 128 + (Math.round((double)Info.SecondsPlayed / durationInt*160 - 6)), guiTop + 200, 12, 12, 0.8);
			}
		}
		
		if (buttons != null && !buttons.isEmpty()){
			if (buttons.get(1).DoDraw){
				Minecraft.getMinecraft().getTextureManager().bindTexture(textureButton2);
				drawTexturedQuadFit(guiLeft + 93 - 128, guiTop + 228, 37, 20, 0.8);
			}
			if (buttons.get(4).DoDraw){
				Minecraft.getMinecraft().getTextureManager().bindTexture(textureButton5);
				drawTexturedQuadFit(guiLeft + 93 - 128, guiTop + 228, 37, 20, 0.8);
			}
			if (isPlaying){
				buttons.get(4).appear();//5 on pause
				buttons.get(1).disappear();//2 on play
			}else{
				buttons.get(1).appear();
				buttons.get(4).disappear();
			}
		}
		
		if (Info.isShuffle){
			Minecraft.getMinecraft().getTextureManager().bindTexture(textureShuffleOn);
			drawTexturedQuadFit(guiLeft+223-128, guiTop+133, 16, 16, 0.9);
		}else{
			Minecraft.getMinecraft().getTextureManager().bindTexture(textureShuffleOff);
			drawTexturedQuadFit(guiLeft+223-128, guiTop+133, 16, 16, 0.9);
		}
		
		if (Info.isRepeat){
			Minecraft.getMinecraft().getTextureManager().bindTexture(textureRepeatOn);
			drawTexturedQuadFit(guiLeft+203-128, guiTop+133, 16, 16, 0.9);
		}else{
			Minecraft.getMinecraft().getTextureManager().bindTexture(textureRepeatOff);
			drawTexturedQuadFit(guiLeft+203-128, guiTop+133, 16, 16, 0.9);
		}
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		for (Button button : buttons){
			if (button.DoDraw){
				drawTexturedModalRect(guiLeft + button.x, guiTop + button.y, button.textureX, button.textureY, button.sizeX, button.sizeY);
			}
		}
		
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(textureProgressThing);
		drawTexturedQuadFit(guiLeft + 128 + 236, guiTop + 37 + MeasureVertical, 12, 12, 0.9);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(textureBlueBar);
		drawTexturedQuadFit(guiLeft + 128 + 13, guiTop + 11, 233, 32, 0.8);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(textureBack);
		drawTexturedQuadFit(guiLeft + 128 + 14, guiTop + 12, 32, 32, 0.9);
		
		if (showTools){
			GL11.glColor4d(1, 1, 1, 0.6);
			Minecraft.getMinecraft().getTextureManager().bindTexture(textureBlack);
			drawTexturedQuadFit(guiLeft-128, guiTop, 512, 300, 0.97, 180);
			drawTexturedModalRect(guiLeft-128, guiTop, 0, 0, 512, 300);
			GL11.glColor4d(1, 1, 1, 1);
			
			Minecraft.getMinecraft().getTextureManager().bindTexture(textureTools);
			drawTexturedQuadFit(tools_mouseX, tools_mouseY, 80, 96, 0.98);
			
			int index = 0;
			if (isPointInRegion(tools_mouseX-guiLeft, tools_mouseY-guiTop, 80, 6*16, x, y)){
				index = (y - tools_mouseY) / 16;
			}else{
				showTools = false;
			}
				
			Minecraft.getMinecraft().getTextureManager().bindTexture(textureToolsHighlight);
			drawTexturedQuadFit(tools_mouseX, tools_mouseY+index*16, 80, 15, 0.99);
			if (index == 5){
				drawTexturedQuadFit(tools_mouseX, tools_mouseY+81, 80, 15, 0.99);
			}
			
		}
		
		
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
		
		fontRenderer.drawSplitString(SoundLoader.folderLoaded, guiLeft-128+15, guiTop+115, 225, 0x010101);
		fontRenderer.drawString("Selected playlist: " + (SoundLoader.SelectedPlaylist != null ? SoundLoader.SelectedPlaylist.name : ""), guiLeft-128+15, guiTop+90, 0x010101);
		fontRenderer.drawString("MP3 player", guiLeft - fontRenderer.getStringWidth("MP3 player") / 2, guiTop+12, 0x010101);
		fontRenderer.drawSplitString(getSecondsPlayedAsString(durationInt), guiLeft + 122 - fontRenderer.getStringWidth(getSecondsPlayedAsString(durationInt)), guiTop + 176, 200, 0x151515);
		fontRenderer.drawString(isDragging ? "00:00" : getSecondsPlayedAsString(Info.SecondsPlayed), guiLeft + 14 - 128, guiTop + 202, 0x151515);
		fontRenderer.drawString(isDragging ? getSecondsPlayedAsString(durationInt) : getSecondsPlayedAsString(durationInt - Info.SecondsPlayed), guiLeft + 216 - 128, guiTop + 202, 0x151515);
		if (isDragging){
			fontRenderer.drawString(getSecondsPlayedAsString(convertMeasureToTime(Measure, durationInt)), guiLeft + 52 - 128 + Measure - 16, guiTop + 180, 0x151515);
		}
		
		if (canChange){
			if (SoundLoader.PlaylistLoaded){
				fontRenderer.drawString(CurrentAction == 0 ? "Play" : "Remove", guiLeft-128+18, guiTop+45, 0x010101);
			}else{
				fontRenderer.drawString(actions.get(CurrentAction), guiLeft-128+18, guiTop+45, 0x010101);	
			}
		}else{
			if (CurrentAction == 4){//move "paste"
				fontRenderer.drawString(betweenActions.get(0), guiLeft-128+18, guiTop+45, 0x010101);
			}
			if (CurrentAction == 2){//rename "rename"
				fontRenderer.drawString(betweenActions.get(1), guiLeft-128+18, guiTop+45, 0x010101);
			}
			if (CurrentAction == 5 || CurrentAction == 6){//giving name (1.Folder, 2.Playlist)
				fontRenderer.drawString(betweenActions.get(2), guiLeft-128+18, guiTop+45, 0x010101);
			}
		}
		if (whileGivingName){
			fontRenderer.drawString(whileGivingNameString, guiLeft+6, guiTop-24, 0x010101);
		}
		
		if (showTools){
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glColor4d(1, 1, 1, 1);
			
			for (Object button : buttonList){
				((GuiButton)button).drawButton = false;
			}
			
			if (SoundLoader.PlaylistLoaded){//playlist loaded at the moment
				
				Minecraft.getMinecraft().getTextureManager().bindTexture(texturePlay);
				drawTexturedQuadFit(tools_mouseX, tools_mouseY, 80, 16, 2);
				
				Minecraft.getMinecraft().getTextureManager().bindTexture(textureRemove);
				drawTexturedQuadFit(tools_mouseX, tools_mouseY+16, 80, 16, 2);
				
			}else{//normal explorer
				if (tools_file){//file
					
					Minecraft.getMinecraft().getTextureManager().bindTexture(texturePlay);
					drawTexturedQuadFit(tools_mouseX, tools_mouseY, 80, 16, 2);
					
					Minecraft.getMinecraft().getTextureManager().bindTexture(textureOpenDefault);
					drawTexturedQuadFit(tools_mouseX, tools_mouseY+16, 80, 16, 2);
					
					Minecraft.getMinecraft().getTextureManager().bindTexture(textureRename);
					drawTexturedQuadFit(tools_mouseX, tools_mouseY+32, 80, 16, 2);
					
					Minecraft.getMinecraft().getTextureManager().bindTexture(textureDelete);
					drawTexturedQuadFit(tools_mouseX, tools_mouseY+48, 80, 16, 2);
					
					Minecraft.getMinecraft().getTextureManager().bindTexture(textureMove);
					drawTexturedQuadFit(tools_mouseX, tools_mouseY+64, 80, 16, 2);
					
					Minecraft.getMinecraft().getTextureManager().bindTexture(textureAddToPlaylist);
					drawTexturedQuadFit(tools_mouseX, tools_mouseY+80, 80, 16, 2);
					
				}else{//folder
					Minecraft.getMinecraft().getTextureManager().bindTexture(textureOpenFolder);
					drawTexturedQuadFit(tools_mouseX, tools_mouseY, 80, 16, 2);
					
					Minecraft.getMinecraft().getTextureManager().bindTexture(textureOpenDefault);
					drawTexturedQuadFit(tools_mouseX, tools_mouseY+16, 80, 16, 2);
					
					Minecraft.getMinecraft().getTextureManager().bindTexture(textureRename);
					drawTexturedQuadFit(tools_mouseX, tools_mouseY+32, 80, 16, 2);
					
					Minecraft.getMinecraft().getTextureManager().bindTexture(textureDelete);
					drawTexturedQuadFit(tools_mouseX, tools_mouseY+48, 80, 16, 2);
				}
			}
		}else{//not show tools
			for (Object button : buttonList){
				((GuiButton)button).drawButton = true;
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
		drawTexturedQuadFit(guiLeft+128+x + 8, guiTop+y + 12, 16, 16, 0.7);
		
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
		buttonList.add(new GuiButton(2, guiLeft+128+150, guiTop+18, 80, 20, "Now playing"));
		buttonList.add(new GuiButton(3, guiLeft-128+18, guiTop+62, 80, 20, "Change action"));
		buttonList.add(new GuiButton(4, guiLeft-128+102, guiTop+62, 60, 20, "Activate"));
		buttonList.add(new GuiButton(5, guiLeft-128+172, guiTop+62, 60, 20, "Cancel"));
		buttonList.add(new GuiButton(6, guiLeft-128+180, guiTop+86, 60, 20, "Play"));
		
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
	
	
	
	public void buttonAction(String action){
		if (action.equals("toggle play")){
			if (isPlaying){
				buttonAction("pause");
			}else{
				if (Sounds.startFrame == 0){
					buttonAction("start play");
				}else{
					buttonAction("resume");
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
				buttonAction("start play");
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
				buttonAction("start play");
			}else{
				isPlaying = false;
			}
		}
		if (action.equals("back")){
			Info.MP3PlayerIndexToOpen = isPlayingIndex;
			SoundLoader.ButtonClicked(true, false, -1);
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
								buttonAction("toggle play");//toggle isPlaying
								break;
							case 3:
								buttonAction("stop");
								break;
							case 1:
								buttonAction("previous");
								break;
							case 4:
								buttonAction("next");
								break;
							case 6:
								buttonAction("back");
								break;
							case 7:
								buttonAction("shuffle");
								break;
							case 8:
								buttonAction("repeat");
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
		
		if (showTools){//tools
			if (SoundLoader.PlaylistLoaded){//playlist loaded
				
			}else{//normal explorer
				if (tools_file){
					if (isPointInRegion(tools_mouseX-guiLeft, tools_mouseY-guiTop, 80, 6*16, x, y)){
						int index = (y - tools_mouseY) / 16;
						actionsFile(musicTabs.get(tools_fileIndex).file, index, tools_fileIndex);
						showTools = false;
					}
				}else{//folder
					if (isPointInRegion(tools_mouseX-guiLeft, tools_mouseY-guiTop, 80, 6*16, x, y)){
						int index = (y - tools_mouseY) / 16;
						actionsFolder(folderTabs.get(tools_fileIndex).folder, index, tools_fileIndex);
						showTools = false;
					}
				}
			}
			
		}else{//normal
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
						for (MovableSelectionMP3Gui bar : folderTabs){
							if (isPointInRegion(128+13, bar.guiY, 225, 32, x, y)){
								if (keyBoardButton == 0){//left click
									if (canChange){
										if (CurrentAction == 1){//open with default program
											actionsFolder(bar.folder, 1, bar.ID-1);
											
										}else if (CurrentAction == 2){//rename
											actionsFolder(bar.folder, 2, bar.ID-1);
											
										}else if (CurrentAction == 3){//delete
											actionsFolder(bar.folder, 3, bar.ID-1);
											
										}else{//play
											actionsFolder(bar.folder, 0, bar.ID-1);
											
										}
									}else{
										actionsFolder(bar.folder, 0, bar.ID-1);
									}
								}else{//right click
									showTools = !showTools;
									tools_file = false;
									tools_fileIndex = bar.ID-1;
									tools_mouseX = x;
									tools_mouseY = y;
								}
								
							}
						}
						for (MovableSelectionMP3Gui bar : musicTabs){
							if (isPointInRegion(128+13, bar.guiY, 225, 32, x, y)){
								if (keyBoardButton == 0){//left click
									if (canChange){
										if (!SoundLoader.PlaylistLoaded){
											File file = SoundLoader.musicFiles.get(bar.ID-1);
											
											if (CurrentAction == 1){//open with default program
												actionsFile(file, 1, bar.ID-1);
												
											}else if (CurrentAction == 2){//rename
												actionsFile(file, 2, bar.ID-1);
												
											}else if (CurrentAction == 3){//delete
												actionsFile(file, 3, bar.ID-1);
												
											}else if (CurrentAction == 4){//move
												actionsFile(file, 4, bar.ID-1);
												
											}else if (CurrentAction == 7){//select a playlist
												Playlist playlist = new Playlist(file, file.getName());
												if (getExtension(file).equals("txt") && Playlist.isPlaylist(file)){
													SoundLoader.SelectedPlaylist = playlist;
													System.out.println("Selected playlist: "+SoundLoader.SelectedPlaylist.name);
												}
											}else if (CurrentAction == 8){//Add a song to playlist
												actionsFile(file, 5, bar.ID-1);
												
											}else if (CurrentAction == 9){//Remove a song from playlist
												if (SoundLoader.SelectedPlaylist != null){
													SoundLoader.SelectedPlaylist.removeSong(file);
												}
											}else{
												actionsFile(file, 0, bar.ID-1);
											}
										}else{
											if (CurrentAction == 4){//remove
												if (SoundLoader.SelectedPlaylist != null){
													SoundLoader.SelectedPlaylist.removeSong(bar.ID-1);
													SoundLoader.loadListForGui(false);
												}
											}else{//play
												Sounds.stopPlaying();
												Sounds.files = SoundLoader.SelectedPlaylist.songs;
												isPlayingIndex = bar.ID-1;
												Info.MP3PlayerIndexToOpen = bar.ID-1;
												Info.SecondsPlayed = 0;
												if (Sounds.canRetrieveInfo(isPlayingIndex)){
													durationInt = Integer.parseInt(Sounds.getInfo(isPlayingIndex, "durationInt"));
												}
												isPlaying = true;
												if (!Sounds.files.isEmpty()){
													Sounds.playRecord(bar.ID-1);
												}
											}
										}
										
									}
								}else{//right click
									showTools = !showTools;
									tools_file = true;
									tools_fileIndex = bar.ID-1;
									tools_mouseX = x;
									tools_mouseY = y;
								}
								
								
							}
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
	
	
	
	
	
	
	public void drawTexturedQuadFit(double x, double y, double width, double height, double zLevel, int alpha){
		Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + height, zLevel, 0,1);
        tessellator.addVertexWithUV(x + width, y + height, zLevel, 1, 1);
        tessellator.addVertexWithUV(x + width, y + 0, zLevel, 1,0);
        tessellator.addVertexWithUV(x + 0, y + 0, zLevel, 0, 0);
        if (alpha != 255){
        	tessellator.setColorRGBA(20, 20, 20, alpha);
        }
        tessellator.draw();
        tessellator.setColorOpaque(255, 255, 255);
	}
	
	public void drawTexturedQuadFit(double x, double y, double width, double height, double zLevel){
		drawTexturedQuadFit(x, y, width, height, zLevel, 255);
	}
	
	
	private static String getSecondsPlayedAsString(int seconds){
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
		case 1://open folder
			try{
				if (Desktop.isDesktopSupported()){
					Desktop.getDesktop().open(new File(SoundLoader.folderLoaded));
				}
			}catch (IOException e){
				e.printStackTrace();
			}
			break;
		case 2://now playing
			if (Sounds.file != null){
				SoundLoader.folderLoaded = SoundLoader.removeLastThing(Sounds.file.getAbsolutePath());
				SoundLoader.PlaylistLoaded = false;
				canCancel = false;
				canChange = true;
				canActivate = false;
				if (CurrentAction == 5 || CurrentAction == 6){//Activatable actions
					canActivate = true;
				}
				SoundLoader.loadListForGui(false);
			}
			break;
		case 3://change
			if (!SoundLoader.PlaylistLoaded){
				if (CurrentAction != actions.size()-1){
					CurrentAction++;
				}else{
					CurrentAction = 0;
				}
				
				if (CurrentAction == 5 || CurrentAction == 6){//Activatable actions
					canActivate = true;
				}else{
					canActivate = false;
				}
			}else{
				if (CurrentAction == 4){
					CurrentAction = 0;
				}else{
					CurrentAction = 4;
				}
			}
			
			break;
		case 4://activate
			if (!canChange){//you want to move loaded file here
				if (CurrentAction == 4){//move
					System.out.println("Moving file: " + ChangableFile.getAbsolutePath());
					if (ChangableFile.renameTo(new File(SoundLoader.folderLoaded+BlocksForUse.BackSlash+ChangableFile.getName()))){
						System.out.println("Moving was successful");
					}else{
						System.out.println(GuiColor.RED + "Moving failed");
					}
					canChange = true;
					canCancel = false;
					canActivate = false;
					SoundLoader.loadListForGui(false);
				}
				if (CurrentAction == 2){//ready to rename
					if (ChangableFile.isDirectory() || !getExtension(ChangableFile).equals("")){
						if (ChangableFile.renameTo(new File(SoundLoader.folderLoaded+BlocksForUse.BackSlash+whileGivingNameString))){
							player.addChatMessage(GuiColor.GREEN + "Successful");
						}else{
							player.addChatMessage(GuiColor.RED + "Failed");
						}
						canCancel = false;
						canChange = true;
						canActivate = false;
						if (CurrentAction == 5 || CurrentAction == 6){//Activatable actions
							canActivate = true;
						}
					}else{
						player.addChatMessage("You must have a file extension, for example .mp3 or .txt");
					}
				}
				if (CurrentAction == 5){//ready to make a folder
					File file = new File(SoundLoader.folderLoaded+BlocksForUse.BackSlash+whileGivingNameString);
					if (getExtension(file).equals("")){
						file.mkdir();
					}else{
						try {
							file.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					SoundLoader.loadListForGui(false);
					canCancel = false;
					canChange = true;
					canActivate = false;
					if (CurrentAction == 5 || CurrentAction == 6){//Activatable actions
						canActivate = true;
					}
				}
				if (CurrentAction == 6){//ready to make a playlist
					File file = new File(SoundLoader.folderLoaded+BlocksForUse.BackSlash+whileGivingNameString+".txt");
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
					SoundLoader.loadListForGui(false);
					canCancel = false;
					canChange = true;
					canActivate = false;
					if (CurrentAction == 5 || CurrentAction == 6){//Activatable actions
						canActivate = true;
					}
				}
				whileGivingName = false;
				whileGivingNameString = "";
			}else{
				System.out.println(CurrentAction);
				if (CurrentAction == 5){//make a folder
					canChange = false;
					canCancel = true;
					whileGivingName = true;
				}
				if (CurrentAction == 6){//make a playlist
					canChange = false;
					canCancel = true;
					whileGivingName = true;
				}

			}
			break;
		case 5://cancel
			whileGivingName = false;
			whileGivingNameString = "";
			canChange = true;
			canCancel = false;
			canActivate = false;
			if (CurrentAction == 5 || CurrentAction == 6){//Activatable actions
				canActivate = true;
			}
			break;
		case 6://play playlist
			Sounds.stopPlaying();
			Sounds.files = SoundLoader.SelectedPlaylist.songs;
			isPlayingIndex = 0;
			Info.MP3PlayerIndexToOpen = 0;
			Info.SecondsPlayed = 0;
			if (Sounds.canRetrieveInfo(isPlayingIndex)){
				durationInt = Integer.parseInt(Sounds.getInfo(isPlayingIndex, "durationInt"));
			}
			isPlaying = true;
			if (!Sounds.files.isEmpty()){
				Sounds.playRecord(0);
			}
			break;
		}
		
	}
	
	@Override
	public void handleKeyboardInput() {
		
		if (whileGivingName && Keyboard.getEventKeyState()){
			if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE || Keyboard.getEventKey() == Keyboard.KEY_LSHIFT){
				super.handleKeyboardInput();
			}else if (Keyboard.getEventKey() == Keyboard.KEY_BACK){
				if (whileGivingNameString.length() > 0){
			    	if (whileGivingNameString.length() == 1){
						whileGivingNameString = "";
					}else{
						whileGivingNameString = whileGivingNameString.substring(0, whileGivingNameString.length()-1);
					}
				}
			}else{
				String ch = Character.toString(Keyboard.getEventCharacter());
				whileGivingNameString = whileGivingNameString + ch;
			}
		}else{
			super.handleKeyboardInput();
		}
	}

	
	
	public static String getExtension(File file){
		return FilenameUtils.getExtension(file.getName());
	}
	
	private void actionsFile(File file, int actionIndex, int fileIndex){
		switch (actionIndex){
		case 0://open file/playlist
			if (getExtension(file).equals("txt")){
				Playlist playlist = new Playlist(file, file.getName());
				
				if (Playlist.isPlaylist(file)){
					SoundLoader.SelectedPlaylist = playlist;
					System.out.println("Selected playlist: "+SoundLoader.SelectedPlaylist.name);
					SoundLoader.ButtonClicked(false, true, fileIndex);
				}
			}else{
				SoundLoader.ButtonClicked(false, false, fileIndex);
			}
			break;
			
		case 1://open with default
			try{
				if (Desktop.isDesktopSupported()){
					Desktop.getDesktop().open(file);
				}
			}catch (IOException e){
				e.printStackTrace();
			}
			break;
			
		case 2://rename
			CurrentAction = 2;
			canChange = false;
			canCancel = true;
			canActivate = true;
			whileGivingName = true;
			ChangableFile = SoundLoader.musicFiles.get(fileIndex);
			whileGivingNameString = ChangableFile.getName();
			break;
			
		case 3://delete
			file.delete();
			SoundLoader.loadListForGui(false);
			break;
			
		case 4://move
			CurrentAction = 4;
			canChange = false;
			canCancel = true;
			canActivate = true;
			ChangableFile = SoundLoader.musicFiles.get(fileIndex);
			break;
			
		case 5://add to playlist
			player.addChatMessage("Adding: "+file.getName());
			if (SoundLoader.SelectedPlaylist != null){
				SoundLoader.SelectedPlaylist.addSong(file);
			}
			break;
		}
	}
	
	private void actionsFolder(Folder folder, int actionIndex, int fileIndex){
		File file = new File(folder.fullPath);
		
		switch (actionIndex){
		case 0://open file/folder/playlist
			SoundLoader.ButtonClicked(true, false, fileIndex);
			break;
			
		case 1://open with default
			try{
				if (Desktop.isDesktopSupported()){
					Desktop.getDesktop().open(file);
				}
			}catch (IOException e){
				e.printStackTrace();
			}
			break;
			
		case 2://rename
			CurrentAction = 2;
			canChange = false;
			canCancel = true;
			canActivate = true;
			whileGivingName = true;
			ChangableFile = file;
			whileGivingNameString = ChangableFile.getName();
			break;
			
		case 3://delete
			if (file.listFiles().length != 0){
				player.addChatMessage(GuiColor.RED + "Error: Directory not empty");
			}else{
				file.delete();
			}
			SoundLoader.loadListForGui(false);
			break;
	}
}
	
}
