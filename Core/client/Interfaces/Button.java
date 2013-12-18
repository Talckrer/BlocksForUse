package Core.client.Interfaces;

import net.minecraft.util.ResourceLocation;

public class Button {
	
	/**
     * @Info Makes a button
     * @Args id, x, y, sizeX, sizeY, textureX, textureY, DoDraw, ClickEnabled
     */
	public Button(int id, int x, int y, int sizeX, int SizeY, int textureX, int textureY, boolean DoDraw, boolean clickWork){
		this.setId(id);
		this.setX(x);
		this.setY(y);
		this.setSizeX(sizeX);
		this.setSizeY(SizeY);
		this.setTextureX(textureX);
		this.setTextureY(textureY);
		this.setDoDraw(DoDraw);
		this.setClickEnabled(clickWork);
	}
	
	int id;
	int x;
	int y;
	int sizeX;
	int sizeY;
	int textureX;
	int textureY;
	boolean ClickEnabled = true;
	boolean DoDraw = true;
	
	
	public void setId(int id) {
		this.id = id;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}

	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}

	public void setTextureX(int textureX) {
		this.textureX = textureX;
	}

	public void setTextureY(int textureY) {
		this.textureY = textureY;
	}
	
	public void setClickEnabled(boolean isEnabled){
		this.ClickEnabled = isEnabled;
	}
	
	public void setDoDraw(boolean isEnabled){
		this.DoDraw = isEnabled;
	}
	
	public void disappear(){
		this.DoDraw = false;
		this.ClickEnabled = false;
	}
	
	public void appear(){
		this.DoDraw = true;
		this.ClickEnabled = true;
	}

	
	
}
