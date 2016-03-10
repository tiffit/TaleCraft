package de.longor.talecraft.client.gui.vcui;

import java.util.Stack;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import de.longor.talecraft.client.ClientResources;
import de.longor.talecraft.client.render.renderers.EXTFontRenderer;
import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class VCUIRenderer {
	EXTFontRenderer fontRenderer = new EXTFontRenderer();
	FontRenderer nativeFontRenderer;
	RenderItem itemRender;
	GuiScreen guiScreen;
	Minecraft minecraft;

	float zLevel = 0;
	int offsetX = 0;
	int offsetY = 0;
	int uiWidth = 0;
	int uiHeight = 0;

	public void setCurrentScreen(GuiScreen guiScreen, float zLevel, FontRenderer fontRendererObj, RenderItem itemRender) {
		this.minecraft = guiScreen.mc;
		this.guiScreen = guiScreen;

		this.zLevel = zLevel;
		this.uiWidth = guiScreen.width;
		this.uiHeight = guiScreen.height;

		this.fontRenderer.setFontRenderer(fontRendererObj);
		this.nativeFontRenderer = fontRendererObj;
		this.itemRender = itemRender;

		this.offsetX = 0;
		this.offsetY = 0;
	}

	public int getWidth() {
		return uiWidth;
	}

	public int getHeight() {
		return uiHeight;
	}




	public void drawDefaultBackground() {
		guiScreen.drawDefaultBackground();
	}

	public void offset(int offX, int offY) {
		this.offsetX += offX;
		this.offsetY += offY;
	}

	public void drawHorizontalLine(int startX, int endX, int y, int color) {
		do_drawHorizontalLine(startX+offsetX, endX+offsetX, y+offsetY, color);
	}

	public void drawVerticalLine(int x, int startY, int endY, int color) {
		do_drawVerticalLine(x+offsetX, startY+offsetY, endY+offsetY, color);
	}

	public void drawRectangle(int left, int top, int right, int bottom, int color) {
		do_drawRect(left+offsetX, top+offsetY, right+offsetX, bottom+offsetY, color);
	}

	public void drawGradientRectangle(int left, int top, int right, int bottom, int startColor, int endColor) {
		do_drawGradientRect(left+offsetX, top+offsetY, right+offsetX, bottom+offsetY, startColor, endColor);
	}

	public void drawLineRectangle(int left, int top, int right, int bottom, int color) {
		int swapvar;
		
		if (left < right) {
			swapvar = left;
			left = right;
			right = swapvar;
		}
		
		if (top < bottom) {
			swapvar = top;
			top = bottom;
			bottom = swapvar;
		}
		
		left += offsetX;
		top += offsetY;
		right += offsetX;
		bottom += offsetY;
		
		do_drawRect(left, top, right, top+1, color);
		do_drawRect(left, bottom, right, bottom-1, color);
		do_drawRect(left, top, left-1, bottom, color);
		do_drawRect(right, top, right+1, bottom, color);
		
		/*
		float error = 0.375f;
		float ix = left   + error;
		float iy = top    + error;
		float ax = right  - error;
		float ay = bottom - error;
		
		float a = (float)(color >> 24 & 255) / 255.0F;
		float r = (float)(color >> 16 & 255) / 255.0F;
		float g = (float)(color >> 8 & 255) / 255.0F;
		float b = (float)(color & 255) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(r, g, b, a);
		
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3f(ix, ay, zLevel);
		GL11.glVertex3f(ax, ay, zLevel);
		GL11.glVertex3f(ax, ay, zLevel);
		GL11.glVertex3f(ax, iy, zLevel);
		GL11.glVertex3f(ax, iy, zLevel);
		GL11.glVertex3f(ix, iy, zLevel);
		GL11.glVertex3f(ix, iy, zLevel);
		GL11.glVertex3f(ix, ay, zLevel);
		GL11.glEnd();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		//*/
	}

	public void drawItemStack(ItemStack item, int x, int y) {
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableColorMaterial();
		GlStateManager.enableLighting();
		itemRender.renderItemIntoGUI(item, x+offsetX, y+offsetY);
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		RenderHelper.enableStandardItemLighting();
	}

	public int drawString(String text, int x, int y, int color, boolean shadow) {
		return fontRenderer.drawString(text, x+offsetX, y+offsetY, color, shadow);
	}

	public int drawString(String text, int x, int y, boolean shadow) {
		return fontRenderer.drawString(text, x+offsetX, y+offsetY, 0xFFFFFFFF, shadow);
	}

	public int drawString(String text, int x, int y, int color) {
		return fontRenderer.drawString(text, x+offsetX, y+offsetY, color, false);
	}

	public int drawString(String text, int x, int y) {
		return fontRenderer.drawString(text, x+offsetX, y+offsetY, 0xFFFFFFFF, false);
	}

	public int drawStringWithShadow(String text, int x, int y, int color) {
		return fontRenderer.drawString(text, x+offsetX, y+offsetY, color, true);
	}

	public int drawStringWithShadow(String text, int x, int y) {
		return fontRenderer.drawString(text, x+offsetX, y+offsetY, 0xFFFFFFFF, true);
	}

	public int drawCenteredString(String str, int x, int y, int color, boolean shadow) {
		int width = fontRenderer.stringWidth(str);
		x -= width / 2;
		return fontRenderer.drawString(str, x + offsetX, y + offsetY, color, shadow);
	}

	public EXTFontRenderer getFontRenderer() {
		return fontRenderer;
	}

	public void bindTexture(ResourceLocation resource) {
		minecraft.getTextureManager().bindTexture(resource == null ? ClientResources.texColorWhite : resource);
	}

	public void drawTexturedModalRectangle(int x, int y, int textureX, int textureY, int width, int height, int color) {
		do_drawTexturedModalRect(x+offsetX, y+offsetY, textureX, textureY, width, height, color);
	}

	public void drawTexturedModalRectangle(int x, int y, int textureX, int textureY, int width, int height) {
		do_drawTexturedModalRect(x+offsetX, y+offsetY, textureX, textureY, width, height);
	}

	public void drawTexturedModalRectangle(float xCoord, float yCoord, int minU, int minV, int maxU, int maxV) {
		do_drawTexturedModalRect(xCoord, yCoord, minU, minV, maxU, maxV);
	}

	public void drawTexturedModalRectangle(int xCoord, int yCoord, TextureAtlasSprite textureSprite, int width, int height) {
		do_drawTexturedModalRect(xCoord+offsetX, yCoord+offsetY, textureSprite, width, height);
	}

	public void drawModalRectangleWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
		do_drawModalRectWithCustomSizedTexture(x+offsetX, y+offsetY, u, v, width, height, textureWidth, textureHeight);
	}

	public void drawScaledCustomSizeModalRectangle(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
		do_drawScaledCustomSizeModalRect(x+offsetX, y+offsetY, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
	}





	/********************************************************/
	/**                                                    **/
	/** These methods where copied from vanilla Minecraft, **/
	/** because the creator of this mod was very lazy.     **/
	/**                                                    **/
	/********************************************************/

	/****/
	private void do_drawHorizontalLine(int startX, int endX, int y, int color)
	{
		if (endX < startX)
		{
			int i1 = startX;
			startX = endX;
			endX = i1;
		}

		do_drawRect(startX, y, endX + 1, y + 1, color);
	}

	private void do_drawVerticalLine(int x, int startY, int endY, int color)
	{
		if (endY < startY)
		{
			int i1 = startY;
			startY = endY;
			endY = i1;
		}

		do_drawRect(x, startY + 1, x + 1, endY, color);
	}

	private void do_drawRect(int left, int top, int right, int bottom, int color)
	{
		int j1;

		if (left < right)
		{
			j1 = left;
			left = right;
			right = j1;
		}

		if (top < bottom)
		{
			j1 = top;
			top = bottom;
			bottom = j1;
		}

		float f3 = (float)(color >> 24 & 255) / 255.0F;
		float f = (float)(color >> 16 & 255) / 255.0F;
		float f1 = (float)(color >> 8 & 255) / 255.0F;
		float f2 = (float)(color & 255) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(f, f1, f2, f3);
		worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldrenderer.pos((double)left, (double)bottom, 0.0D).endVertex();
		worldrenderer.pos((double)right, (double)bottom, 0.0D).endVertex();
		worldrenderer.pos((double)right, (double)top, 0.0D).endVertex();
		worldrenderer.pos((double)left, (double)top, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	private void do_drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor)
	{
		float f = (float)(startColor >> 24 & 255) / 255.0F;
		float f1 = (float)(startColor >> 16 & 255) / 255.0F;
		float f2 = (float)(startColor >> 8 & 255) / 255.0F;
		float f3 = (float)(startColor & 255) / 255.0F;
		float f4 = (float)(endColor >> 24 & 255) / 255.0F;
		float f5 = (float)(endColor >> 16 & 255) / 255.0F;
		float f6 = (float)(endColor >> 8 & 255) / 255.0F;
		float f7 = (float)(endColor & 255) / 255.0F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldrenderer.color(f1, f2, f3, f);
		worldrenderer.pos((double)right, (double)top, (double)this.zLevel).endVertex();
		worldrenderer.pos((double)left, (double)top, (double)this.zLevel).endVertex();
		worldrenderer.color(f5, f6, f7, f4);
		worldrenderer.pos((double)left, (double)bottom, (double)this.zLevel).endVertex();
		worldrenderer.pos((double)right, (double)bottom, (double)this.zLevel).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}

	private void do_drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
		do_drawTexturedModalRect(x, y, textureX, textureY, width, height, 1, 1, 1, 1);
	}

	private void do_drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height, int color) {
		float a = (float)(color >> 24 & 255) / 255.0F;
		float r = (float)(color >> 16 & 255) / 255.0F;
		float g = (float)(color >> 8 & 255) / 255.0F;
		float b = (float)(color & 255) / 255.0F;
		do_drawTexturedModalRect(x, y, textureX, textureY, width, height, r, g, b, a);
	}

	private void do_drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height, float r, float g, float b, float a) {
		float f = 0.00390625F;

		boolean repeat = false;

		if(width < 0) {
			width = -width;
			repeat = true;
		}
		if(height < 0) {
			height = -height;
			repeat = true;
		}

		int L = x;
		int T = y;
		int R = x+width;
		int B = y+height;
		float z = this.zLevel;

		if(repeat) {
			f *= 8;
		}

		float u0 = (textureX +      0) * f;
		float v0 = (textureY +      0) * f;
		float u1 = (textureX +  width) * f;
		float v1 = (textureY + height) * f;

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldrenderer.color(r, g, b, a);
		//worldrenderer.setBrightness(0xFF); //TODO FIX
		worldrenderer.pos(L, B, z).tex(u0, v1).endVertex();
		worldrenderer.pos(R, B, z).tex(u1, v1).endVertex();
		worldrenderer.pos(R, T, z).tex(u1, v0).endVertex();
		worldrenderer.pos(L, T, z).tex(u0, v0).endVertex();
		tessellator.draw();
	}

	private void do_drawTexturedModalRect(float xCoord, float yCoord, int minU, int minV, int maxU, int maxV)
	{
		float f2 = 0.00390625F;
		float f3 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldrenderer.pos((double)(xCoord + 0.0F), (double)(yCoord + (float)maxV), (double)this.zLevel).tex((double)((float)(minU + 0) * f2), (double)((float)(minV + maxV) * f3)).endVertex();												
		worldrenderer.pos((double)(xCoord + (float)maxU), (double)(yCoord + (float)maxV), (double)this.zLevel).tex((double)((float)(minU + maxU) * f2), (double)((float)(minV + maxV) * f3)).endVertex();
		worldrenderer.pos((double)(xCoord + (float)maxU), (double)(yCoord + 0.0F), (double)this.zLevel).tex( (double)((float)(minU + maxU) * f2), (double)((float)(minV + 0) * f3)).endVertex();
		worldrenderer.pos((double)(xCoord + 0.0F), (double)(yCoord + 0.0F), (double)this.zLevel).tex((double)((float)(minU + 0) * f2), (double)((float)(minV + 0) * f3)).endVertex();
		tessellator.draw();
	}

	private void do_drawTexturedModalRect(int xCoord, int yCoord, TextureAtlasSprite textureSprite, int width, int height)
	{
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldrenderer.pos(xCoord + 0	, yCoord + height	, this.zLevel).tex(textureSprite.getMinU(), textureSprite.getMaxV()).endVertex();
		worldrenderer.pos(xCoord + width, yCoord + height	, this.zLevel).tex(textureSprite.getMaxU(), textureSprite.getMaxV()).endVertex();
		worldrenderer.pos(xCoord + width, yCoord + 0		, this.zLevel).tex(textureSprite.getMaxU(), textureSprite.getMinV()).endVertex();
		worldrenderer.pos(xCoord + 0	, yCoord + 0		, this.zLevel).tex(textureSprite.getMinU(), textureSprite.getMinV()).endVertex();
		tessellator.draw();
	}

	private void do_drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight)
	{
		float f4 = 1.0F / textureWidth;
		float f5 = 1.0F / textureHeight;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		//worldrenderer.setColorOpaque_I(0xFFFFFFFF); //TODO FIX
		worldrenderer.pos((double)x, (double)(y + height), 0.0D).tex( (double)(u * f4), (double)((v + (float)height) * f5)).endVertex();
		worldrenderer.pos((double)(x + width), (double)(y + height), 0.0D).tex( (double)((u + (float)width) * f4), (double)((v + (float)height) * f5)).endVertex();
		worldrenderer.pos((double)(x + width), (double)y, 0.0D).tex( (double)((u + (float)width) * f4), (double)(v * f5)).endVertex();
		worldrenderer.pos((double)x, (double)y, 0.0D).tex( (double)(u * f4), (double)(v * f5)).endVertex();
		tessellator.draw();
	}

	private void do_drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight)
	{
		float f4 = 1.0F / tileWidth;
		float f5 = 1.0F / tileHeight;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldrenderer.pos((double)x, (double)(y + height), 0.0D).tex( (double)(u * f4), (double)((v + (float)vHeight) * f5)).endVertex();
		worldrenderer.pos((double)(x + width), (double)(y + height), 0.0D).tex( (double)((u + (float)uWidth) * f4), (double)((v + (float)vHeight) * f5)).endVertex();
		worldrenderer.pos((double)(x + width), (double)y, 0.0D).tex( (double)((u + (float)uWidth) * f4), (double)(v * f5)).endVertex();
		worldrenderer.pos((double)x, (double)y, 0.0D).tex( (double)(u * f4), (double)(v * f5)).endVertex();
		tessellator.draw();
	}

	Stack<ScissorFrame> scissorStack = new Stack<ScissorFrame>();

	public void pushScissor(int x, int y, int width, int height) {
		x += offsetX;
		y += offsetY + 1;

		if(scissorStack.isEmpty()) {
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
		}

		float factorX = MathHelper.ceiling_float_int((float)Display.getWidth() / (float)minecraft.currentScreen.width);
		float factorY = MathHelper.ceiling_float_int((float)Display.getHeight() / (float)minecraft.currentScreen.height);

		y = minecraft.currentScreen.height - y - height;

		ScissorFrame frame = new ScissorFrame();
		frame.x = (int) (x * factorX);
		frame.y = (int) (y * factorY);
		frame.w = (int) (width * factorX);
		frame.h = (int) (height * factorY);
		frame.use();
		
		scissorStack.push(frame);
	}

	public void popScissor() {
		scissorStack.pop();
		
		if(scissorStack.isEmpty()) {
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		} else {
			scissorStack.peek().use();
		}
	}

	private class ScissorFrame {
		int x;
		int y;
		int w;
		int h;

		void use() {
			GL11.glScissor(x, y, w, h);
		}
	}

	public float getOffsetX() {
		return this.offsetX;
	}

	public float getOffsetY() {
		return this.offsetY;
	}

	public float getZLevel() {
		return this.zLevel;
	}

}
