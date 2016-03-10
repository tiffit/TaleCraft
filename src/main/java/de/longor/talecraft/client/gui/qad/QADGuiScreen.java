package de.longor.talecraft.client.gui.qad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftBlocks;
import de.longor.talecraft.client.gui.vcui.VCUIComponent;
import de.longor.talecraft.client.gui.vcui.VCUIRenderer;
import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

/**
 * A class that is to be used to simplify GUIScreen creation.
 **/
@SideOnly(Side.CLIENT)
public class QADGuiScreen extends GuiScreen implements QADComponentContainer {
	public static final VCUIRenderer instance = new VCUIRenderer();
	private ArrayList<QADComponent> components;
	private GuiScreen behindScreen;
	private boolean shouldPauseGame;
	private boolean shouldRelayout;
	private boolean shouldDebugRender;
	protected GuiScreen returnScreen;
	
	public QADGuiScreen() {
		super.allowUserInput = false;
		shouldPauseGame = true;
		shouldRelayout = true;
		shouldDebugRender = false;
		components = null;
		behindScreen = null;
	}
	
	public void buildGui() {
		// dont do anything, let the extended classes do their thing!
	}
	
	public void layoutGui() {
		
	}
	
	public void updateGui() {
		
	}
	
	/** ********************************* **/
	/**                                   **/
	/** Everything following is final and **/
	/** should not be overriden.          **/
	/**                                   **/
	/** ********************************* **/
	/****/
	
	@Override
    public final boolean doesGuiPauseGame() {
        return shouldPauseGame;
    }
	
	public final void setDoesPauseGame(boolean flag) {
		this.shouldPauseGame = flag;
	}
	
	@Override
    public final void initGui() {
		// TaleCraft.logger.info("Gui.init() -> " + this.getClass().getName());
    	Keyboard.enableRepeatEvents(true);
		
    	if(components == null) {
    		components = Lists.newArrayList();
    		
    		try {
    			buildGui();
    		} catch(Throwable e) {
    			e.printStackTrace();
    		}
    	}
    	
    	// XXX: Untested. Might lead to crash. Leaving it in for now.
    	if(this.behindScreen != null) {
    		this.behindScreen.width = this.width;
    		this.behindScreen.height = this.height;
    		this.behindScreen.initGui();
    	}
    	
    	layoutGui();
    }
	
	@Override
    public final void onGuiClosed() {
		// TaleCraft.logger.info("Gui.close() -> " + this.getClass().getName());
		Keyboard.enableRepeatEvents(false);
		
		if(behindScreen != null) {
			TaleCraft.proxy.asClient().sheduleClientTickTask(new Runnable() {
				@Override public void run() {
					mc.displayGuiScreen(behindScreen);
				}
			});
		}
		
    }
	
	@Override
    protected final void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		try {
			for(QADComponent component : components) {
				component.onMouseClicked(mouseX-component.getX(), mouseY-component.getY(), mouseButton);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	@Override
    protected final void mouseReleased(int mouseX, int mouseY, int state) {
		try {
			for(QADComponent component : components) {
				component.onMouseReleased(mouseX-component.getX(), mouseY-component.getY(), state);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	@Override
    protected final void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		for(QADComponent component : components) {
			component.onMouseClickMove(mouseX-component.getX(), mouseY-component.getY(), clickedMouseButton, timeSinceLastClick);
		}
    }
	
	@Override
	public final void keyTyped(char typedChar, int typedCode) {
		if(typedCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(returnScreen);
			mc.inGameHasFocus = returnScreen == null;
			return;
		}
		
		if(typedCode == Keyboard.KEY_F1) {
			this.shouldDebugRender ^= true;
			return;
		}
		
		if(typedCode == Keyboard.KEY_TAB) {
			// TODO: Continue working on the 'transfer focus' thing.
			// Commented out because it doesn't work.
			// transferFocus();
			return;
		}
		
		for(QADComponent component : components) {
			component.onKeyTyped(typedChar, typedCode);
		}
	}
	
	@Override
    public final void updateScreen() {
		if(components == null) {
			initGui();
			return;
		}
		
		if(shouldRelayout) {
			layoutGui();
			shouldRelayout = false;
		}
		
		this.updateGui();
		
		for(QADComponent component : components) {
			component.onTickUpdate();
		}
    }
	
	@Override
    public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
		// If there is a 'behind' screen, draw it first so it appears in the background.
		if(behindScreen != null) {
			behindScreen.drawScreen(-9999, -9999, partialTicks);
		}
		
		// ...
		instance.setCurrentScreen(this, this.zLevel, this.fontRendererObj, this.itemRender);
		instance.drawDefaultBackground();
		
		// Draw all components.
		for(QADComponent component : components) {
			component.draw(mouseX-component.getX(), mouseY-component.getY(), partialTicks, instance);
		}
		
		if(shouldDebugRender) {
			QADGuiScreenDebugRenderer.debugRender(this, components, instance, mouseX, mouseY, partialTicks);
		}
		
		drawCustom(mouseX, mouseY, partialTicks, instance);
		
		// Check for tooltips, and draw them if necessary.
		for(QADComponent component : components) {
			if(component.isPointInside(mouseX, mouseY)) {
				List<String> text = component.getTooltip(mouseX, mouseY);
				
				if(text != null) {
					int yPos = mouseY;
					
					if(text.get(0).equalsIgnoreCase("ylock")) {
						int add = component instanceof QADRectangularComponent ? ((QADRectangularComponent) component).getHeight() : 20;
						yPos = component.getY()+add+fontRendererObj.FONT_HEIGHT*2;
						text = text.subList(1, text.size()-1);
					}
					
					this.drawHoveringText(text, mouseX, yPos);
					break;
				}
			}
		}
		
		// Debug: Draw cursor position marker.
		if(Boolean.TRUE.booleanValue() && Mouse.isInsideWindow()) {
			final int color = 0x1ACCEEFF;
			instance.drawHorizontalLine(0, width, mouseY, color);
			instance.drawVerticalLine(mouseX, -1, height, color);
		}
		
    }
	
	public void drawCustom(int mouseX, int mouseY, float partialTicks, VCUIRenderer instance2) {
		
	}
	
	public final void setBehind(GuiScreen behind) {
		behindScreen = behind;
	}
	
	public final GuiScreen getBehind() {
		return behindScreen;
	}
	
	public final void displayGuiScreen(GuiScreen guiScreen) {
		mc.displayGuiScreen(guiScreen);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getCenterX() {
		return width / 2;
	}
	
	public int getCenterY() {
		return height / 2;
	}
	
	public final void relayout() {
		this.shouldRelayout = true;
	}
	
	public <T extends QADComponent> T addComponent(T component) {
		if(components == null)
			components = Lists.newArrayList();
		this.components.add(component);
		return component;
	}
	
	// FOLLOWING ARE UTILITY METHODS!
	public static final int parseInt(String string, int original, int min, int max) {
		try {
			int i = 0;
			
			if(string.startsWith("0x"))
				i = Integer.parseInt(string.toLowerCase(), 16);
			if(string.startsWith("0b"))
				i = Integer.parseInt(string.toLowerCase(), 2);
			else
				i = Integer.parseInt(string);
			
			if(i < min)
				throw new NumberFormatException(i+" is smaller than "+min+".");
			if(i > max)
				throw new NumberFormatException(i+" is bigger than "+max+".");
			
			return i;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return original;
		}
	}
	
	public static final float parseFloat(String string, float original, float min, float max) {
		try {
			float f = Float.parseFloat(string);
			
			if(f < min)
				throw new NumberFormatException(f+" is smaller than "+min+".");
			if(f > max)
				throw new NumberFormatException(f+" is bigger than "+max+".");
			
			return f;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return original;
		}
	}

	@Override
	public Collection<QADComponent> getComponents() {
		return components;
	}

	@Override
	public void removeAllComponents() {
		components.clear();
	}
	
	public void resetGuiScreen() {
		components = null;
	}

	@Override
	public QADComponent getComponentByName(String name) {
		for(QADComponent component : components) {
			if(name.equals(component.getName()))
				return component;
		}
		return null;
	}
	
	public void transferFocus() {
		Iterator<QADComponent> iterator = components.iterator();
		boolean unfocusRest = false;
		
		while(iterator.hasNext()) {
			QADComponent current = iterator.next();
			
			if(unfocusRest) {
				current.removeFocus();
				continue;
			}
			
			if(current.isFocused()) {
				if(current.transferFocus()) {
					// stay
				} else {
					// move to next
					if(iterator.hasNext()) {
						iterator.next().transferFocus();
					} else {
						components.get(0).transferFocus();
						return;
					}
				}
				unfocusRest = true;
			}
		}
	}
	
}
