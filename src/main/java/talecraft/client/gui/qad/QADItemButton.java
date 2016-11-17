package talecraft.client.gui.qad;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import talecraft.client.gui.vcui.VCUIRenderer;
import talecraft.client.render.renderers.EXTFontRenderer;

public class QADItemButton extends QADButton {

	private ItemStack stack;
	
	public QADItemButton(int x, int y, int width, String text, ItemStack stack) {
		super(x, y, width, text);
		this.stack = stack;
	}
	
	public QADItemButton(String text, ItemStack stack) {
		super(text);
		this.stack = stack;
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

			}
			if(stack != null){
				renderer.drawItemStack(stack, x + 1, y + 1);
				GlStateManager.disableLighting();
			}

			// ! MODIFY X !
			if(iconTexture != null || stack != null){
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

	

}
