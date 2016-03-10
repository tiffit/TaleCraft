package de.longor.talecraft.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftBlocks;
import de.longor.talecraft.client.gui.vcui.VCUIComponent;
import de.longor.talecraft.client.gui.vcui.VCUIRenderer;
import de.longor.talecraft.proxy.ClientProxy;
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
public class TCGuiScreen extends GuiScreen {
	public static final VCUIRenderer instance = new VCUIRenderer();
	private VCUIComponent root;
	
	public TCGuiScreen() {
		super.allowUserInput = false;
		root = new VCUIComponent();
		buildGui(root);
	}
	
	public void buildGui(VCUIComponent root) {
		// dont do anything, let the extended classes do their thing!
	}
	
	@Override
    public boolean doesGuiPauseGame() {
        return true;
    }
	
	/** ********************************* **/
	/**                                   **/
	/** Everything following is final and **/
	/** should not be overriden.          **/
	/**                                   **/
	/** ********************************* **/
	
	@Override /****/
    public final void initGui() {
		TaleCraft.logger.info("Gui.init() -> " + this.getClass().getName());
    	layoutGui();
    }
	
	private final void layoutGui() {
		root.position.x = 0;
		root.position.y = 0;
		root.size.x = width;
		root.size.y = height;
		
		if(root.layoutManager != null) {
			root.layoutManager.accept(root);
		}
	}
	
	@Override
    public final void onGuiClosed() {
		TaleCraft.logger.info("Gui.close() -> " + this.getClass().getName());
    }
	
	@Override
    protected final void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    	root.mouseClicked(mouseX, mouseY, mouseButton);
    }
	
	@Override
    protected final void mouseReleased(int mouseX, int mouseY, int state) {
    	root.mouseReleased(mouseX, mouseY, state);
    }
	
	@Override
    protected final void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    	root.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
	
	@Override
	public final void keyTyped(char typedChar, int typedCode) {
		if(typedCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(null);
			mc.inGameHasFocus = true;
			return;
		}
		
		root.keyTyped(typedChar, typedCode);
	}
	
	@Override
    public final void updateScreen() {
		root.update();
    }
	
	@Override
    public final void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
		instance.setCurrentScreen(this, this.zLevel, this.fontRendererObj, this.itemRender);
		instance.drawDefaultBackground();
		
		if(Mouse.isInsideWindow() && Boolean.TRUE.booleanValue()) {
			final int color = 0x1ACCEEFF;
			instance.drawHorizontalLine(0, width, mouseY, color);
			instance.drawVerticalLine(mouseX, -1, height, color);
		}
		
		if(Boolean.FALSE.booleanValue()) {
			final int H = 128;
			instance.drawGradientRectangle(0, 0, 		width, H, 0xFF000000, 0);
			instance.drawGradientRectangle(0, height-H, width, height, 0, 0xFF000000);
    	}
		
    	root.draw();
    }
	
}
