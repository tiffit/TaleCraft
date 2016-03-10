package de.longor.talecraft.client.gui.qad;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;

import com.google.common.collect.Lists;

import de.longor.talecraft.client.gui.vcui.VCUIRenderer;

public abstract class QADComponent {
	private String componentName;
	private List<String> tooltip;
	
	public QADComponent setTooltip(String...strings) {
		tooltip = Lists.newArrayList(strings);
		return this;
	}
	
	public QADComponent setTooltip(List<String> strings) {
		tooltip = strings;
		return this;
	}
	
	public List<String> getTooltip() {
		return tooltip;
	}
	
	public QADComponent setName(String name) {
		this.componentName = name;
		return this;
	}
	
	public String getName() {
		return this.componentName;
	}
	
	public abstract QADEnumComponentClass getComponentClass();
	
	public abstract int getX();
	public abstract int getY();
	
	public abstract void setX(int x);
	public abstract void setY(int y);
	public abstract void setPosition(int x, int y);
	
	public abstract void draw(int localMouseX, int localMouseY, float partialTicks, VCUIRenderer renderer);
	
	public abstract void onMouseClicked(int localMouseX, int localMouseY, int mouseButton);
	public abstract void onMouseReleased(int localMouseX, int localMouseY, int state);
	public abstract void onMouseClickMove(int localMouseX, int localMouseY, int clickedMouseButton, long timeSinceLastClick);
	public abstract void onKeyTyped(char typedChar, int typedCode);
	public abstract void onTickUpdate();
	
	public abstract boolean isPointInside(int mouseX, int mouseY);
	
	public List<String> getTooltip(int mouseX, int mouseY) {
		return isPointInside(mouseX, mouseY) ? tooltip : null;
	}
	
    public void playPressSound(float pitch) {
    	SoundHandler soundHandlerIn = Minecraft.getMinecraft().getSoundHandler();
    	ResourceLocation pressSound = new ResourceLocation("gui.button.press");
        soundHandlerIn.playSound(PositionedSoundRecord.create(pressSound, pitch));
    }
    
    public abstract boolean transferFocus();
    
	public abstract boolean isFocused();
	
	public abstract void removeFocus();
	
}
