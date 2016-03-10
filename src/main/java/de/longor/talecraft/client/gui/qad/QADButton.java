package de.longor.talecraft.client.gui.qad;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.realmsclient.dto.RealmsServer.McoServerComparator;

import de.longor.talecraft.client.gui.qad.model.DefaultButtonModel;
import de.longor.talecraft.client.gui.vcui.VCUIRenderer;
import de.longor.talecraft.client.render.renderers.EXTFontRenderer;
import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class QADButton extends QADRectangularComponent {
	public static interface ButtonModel {
		public void onClick();
		
		public String getText();
		public ResourceLocation getIcon(); // Can be null.
		
		public void setText(String newText);
		public void setIcon(ResourceLocation newIcon); // Can be null.
	}
	
	protected static final ResourceLocation buttonTextures = new ResourceLocation("minecraft:textures/gui/widgets.png");

	public static final ResourceLocation ICON_ADD = new ResourceLocation("talecraft:textures/gui/add.png");
	public static final ResourceLocation ICON_DELETE = new ResourceLocation("talecraft:textures/gui/delete.png");
	public static final ResourceLocation ICON_INVEDIT = new ResourceLocation("talecraft:textures/gui/invokeedit.png");
	public static final ResourceLocation ICON_PAUSE = new ResourceLocation("talecraft:textures/gui/pause.png");
	public static final ResourceLocation ICON_PLAY = new ResourceLocation("talecraft:textures/gui/play.png");
	public static final ResourceLocation ICON_STOP = new ResourceLocation("talecraft:textures/gui/stop.png");
	public static final ResourceLocation ICON_SAVE = new ResourceLocation("talecraft:textures/gui/save.png");
	public static final ResourceLocation ICON_NEW = new ResourceLocation("talecraft:textures/gui/new.png");
	
	public static final ResourceLocation ICON_EDITOR_TXT = new ResourceLocation("talecraft:textures/gui/file/editors/txt.png");
	public static final ResourceLocation ICON_EDITOR_NBT = new ResourceLocation("talecraft:textures/gui/file/editors/nbt.png");
	public static final ResourceLocation ICON_EDITOR_BIN = new ResourceLocation("talecraft:textures/gui/file/editors/bin.png");
	public static final ResourceLocation ICON_EDITOR_NIL = new ResourceLocation("talecraft:textures/gui/file/editors/none.png");
	
	Runnable clickRunnable = null;
	ButtonModel model;
	int x;
	int y;
	int width;
	int height;
	boolean enabled = true;
	boolean hovered = false;
	boolean focused = false;
	
	// STYLE OPTIONS
	/** 0 = Left, 1 = Center, 2 = Right **/
	public int textAlignment = 1;
	/** Simplified rendering for buttons. **/
	public boolean simplified = true;

	public QADButton(String text) {
		this.model = new DefaultButtonModel(text);
		
		this.x = 0;
		this.y = 0;
		this.width = 20;
		this.height = 20;
		
		this.enabled = true;
	}

	public QADButton(ResourceLocation resourceLocation) {
		this.model = new DefaultButtonModel(resourceLocation);
		
		this.x = 0;
		this.y = 0;
		this.width = 20;
		this.height = 20;
		
		this.enabled = true;
	}

	public QADButton(int x, int y, ResourceLocation resourceLocation) {
		this.model = new DefaultButtonModel(resourceLocation);
		
		this.x = x;
		this.y = y;
		this.width = 20;
		this.height = 20;
		
		this.enabled = true;
	}

	public QADButton(String string, ResourceLocation resourceLocation) {
		this.model = new DefaultButtonModel(string, resourceLocation);
		
		this.x = 0;
		this.y = 0;
		this.width = 20;
		this.height = 20;
		
		this.enabled = true;
	}
	
	public QADButton(int x, int y, int width, String text) {
		this.model = new DefaultButtonModel(text);

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = 20;

		this.enabled = true;
	}
	
	public QADButton(int x, int y, int width, ButtonModel model) {
		this.model = model;

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = 20;

		this.enabled = true;
	}

	public QADButton setAction(Runnable handler) {
		clickRunnable = handler;
		return this;
	}

	protected int getHoverState(boolean mouseOver)
	{
		byte b0 = 1; // Normal

		if (!this.enabled)
		{
			b0 = 0; // Disabled
		}
		else if (mouseOver)
		{
			b0 = 2; // Mouse Over Button
		}

		return b0;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getButtonWidth()
	{
		return this.width;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}
	
	public boolean canResize() {
		return true;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public void setHeight(int newHeight) {
		this.height = newHeight;
	}

	@Override
	public void setSize(int newWidth, int newHeight) {
		this.width = newWidth;
		this.height = newHeight;
	}

	@Override
	public void draw(int localMouseX, int localMouseY, float partialTicks, VCUIRenderer renderer) {
		
		// Culling on the Y-Axis
		if(renderer.getOffsetY()+y > renderer.getHeight()) {
			return;
		} else if(renderer.getOffsetY()+y+height < 0) {
			return;
		}
		
		// if (this.visible)
		{
			EXTFontRenderer fontrenderer = renderer.getFontRenderer();
			renderer.bindTexture(buttonTextures);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = localMouseX >= 0 && localMouseY >= 0 && localMouseX < this.width && localMouseY < this.height;
			int k = this.getHoverState(this.hovered);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);
			
			if(simplified) {
				int color = 0xFF101010;
				
				switch(k) {
				case 0: color = 0x7F101010; break;
				case 1: color = 0x7F505050; break;
				case 2: color = 0x7F707060; break;
				}
				
				renderer.drawRectangle(this.x, this.y, this.x+this.width, this.y+this.height, color);
            	renderer.drawLineRectangle(this.x, this.y, this.x+this.width, this.y+this.height, 0x7FFFFFFF);
			} else {
	            if(width < 256) {
	            	renderer.drawTexturedModalRectangle(this.x, this.y, 0, 46 + k * 20, this.width / 2, this.height);
	            	renderer.drawTexturedModalRectangle(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
	            } else {
	            	renderer.drawRectangle(this.x, this.y, this.x+this.width, this.y+this.height, 0x7F5F5F5F);
	            	renderer.drawLineRectangle(this.x, this.y, this.x+this.width, this.y+this.height, 0xFFFFFFFF);
	            }
			}
			
			// this.mouseDragged(mc, mouseX, mouseY);
			
			int fontColor = 14737632;

			if (!this.enabled)
			{
				fontColor = 10526880;
			}
			else if (this.hovered)
			{
				fontColor = 16777120;
			}

			int bx = this.x;
			
			ResourceLocation iconTexture = model.getIcon();
			
			if(iconTexture != null) {
				renderer.bindTexture(iconTexture);
				renderer.drawModalRectangleWithCustomSizedTexture(bx + 2, y + 2, 0, 0, 16, 16, 16, 16);
				
				// ! MODIFY X !
				bx += 2 + 16;
			}
			
			String text = model.getText();
			
			if(text == null || text.isEmpty()) {
				return;
			}
			
			int txtY = y + (height - ((fontrenderer.fr.FONT_HEIGHT+7)/2)) / 2;
			
			
			switch (textAlignment) {
				case 0: {//left
					int txtX = bx + 3;
					renderer.drawString(text, txtX, txtY, fontColor, true);
				} break;
				case 1: {//center
					int txtX = Math.min(bx + width / 2, this.x + width / 2);
					renderer.drawCenteredString(text, txtX, txtY, fontColor, true);
				} break;
				case 2: {//right
					int txtX = bx + width - 3;
					renderer.drawString(text, txtX, txtY, fontColor, true);
				} break;
			}
		}
	}

	@Override
	public void onMouseClicked(int i, int j, int mouseButton) {

	}

	@Override
	public void onMouseReleased(int localMouseX, int localMouseY, int state) {
		boolean hit = localMouseX >= 0 && localMouseY >= 0 && localMouseX < this.width && localMouseY < this.height;

		if(state != 0 || !hit)
			return;

		playPressSound(enabled ? 1f : 0.5f);
		
		model.onClick();
		
		if(clickRunnable != null)
			clickRunnable.run();
	}

	@Override
	public void onMouseClickMove(int i, int j, int clickedMouseButton, long timeSinceLastClick) {}

	@Override
	public void onKeyTyped(char typedChar, int typedCode) {}

	@Override
	public void onTickUpdate() {}

	@Override
	public boolean isPointInside(int mouseX, int mouseY) {
		int localMouseX = mouseX - x;
		int localMouseY = mouseY - y;
		return localMouseX >= 0 && localMouseY >= 0 && localMouseX < this.width && localMouseY < this.height;
	}

	public List<String> getTooltip(int mouseX, int mouseY) {
		return isPointInside(mouseX, mouseY) ? getTooltip() : null;
	}
	
	public QADButton setText(String newText) {
		this.model.setText(newText);
		return this;
	}

	public QADButton setIcon(ResourceLocation newIcon) {
		this.model.setIcon(newIcon);
		return this;
	}
	
	public QADButton setEnabled(boolean b) {
		this.enabled = b;
		return this;
	}
	
	public ButtonModel getModel() {
		return model;
	}
	
	public QADButton setModel(ButtonModel newModel) {
		this.model = newModel;
		return this;
	}

	@Override
	public QADEnumComponentClass getComponentClass() {
		return QADEnumComponentClass.ACTION;
	}
	
	@Override
	public boolean transferFocus() {
		if(focused) {
			focused = false;
			return false;
		} else {
			focused = true;
			return true;
		}
	}

	@Override
	public boolean isFocused() {
		return focused;
	}

	@Override
	public void removeFocus() {
		focused = false;
	}

}
