package de.longor.talecraft.client.render.renderers;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EXTFontRenderer {
	public FontRenderer fr;
	
	public EXTFontRenderer(FontRenderer fr) {
		if(fr == null)
			throw new IllegalArgumentException("'fr' may not be null.");
		
		this.fr = fr;
	}
	
	public EXTFontRenderer() {
		this.fr = null;
	}

	public void setFontRenderer(FontRenderer fr) {
		if(fr == null)
			throw new IllegalArgumentException("'fr' may not be null.");
		
		this.fr = fr;
	}
	
	/**
	 * This is the ultimate string drawing function.
	 *
	 * @param str The string to draw. May contain formatting.
	 * @param x The top-left corner of the box.
	 * @param y The top-left corner of the box.
	 * @param width The width of the box.
	 * @param height The height of the box.
	 * @param color The color fo the box. Default is 0xFFFFFFFF (white).
	 * @param shadow Should a shadow be drawn?
	 * @param align Horizontal Alignment of the string in the box. 0=left, 1=center, 2=right.
	 * @param valign  Vertical Alignment of the string in the box. 0=top, 1=center, 2=bottom.
	 **/
	public int drawStringInBox(String str, int x, int y, int width, int height, int color, boolean shadow, int align, int valign) {
		int str_w = fr.getStringWidth(str);
		int str_h = fr.FONT_HEIGHT;
		
		int finalX = x;
		int finalY = y;
		
		switch(align) {
		case 2: finalX = x + (width    -  str_w); break;
		case 1: finalX = x + (width/2) - (str_w/2); break;
		default: case 0: finalX = x; break;
		}
		
		switch(valign) {
		case 2: finalY = y + (height    -  str_h); break;
		case 1: finalY = y + (height/2) - (str_h/2); break;
		default: case 0: finalY = y; break;
		}
		
		return fr.drawString(str, finalX, finalY, color, shadow);
	}
	
	// STR X Y COLOR SHADOW
	
	public int drawString(String str, int x, int y, int color, boolean shadow) {
		return fr.drawString(str, x, y, color, shadow);
	}
	
	public int drawCenteredString(String str, int x, int y, int color, boolean shadow) {
		int width = fr.getStringWidth(str);
		x -= width / 2;
		return fr.drawString(str, x, y, color, shadow);
	}
	
	// STR X Y COLOR
	
	public int drawString(String str, int x, int y, int color) {
		return fr.drawString(str, x, y, color);
	}
	
	public int drawCenteredString(String str, int x, int y, int color) {
		int width = fr.getStringWidth(str);
		x -= width / 2;
		return fr.drawString(str, x, y, color);
	}
	
	// STR X Y
	
	public int drawString(String str, int x, int y) {
		return fr.drawString(str, x, y, 0xFFFFFFFF);
	}
	
	public int drawCenteredString(String str, int x, int y) {
		int width = fr.getStringWidth(str);
		x -= width / 2;
		return fr.drawString(str, x, y, 0xFFFFFFFF);
	}
	
	public int stringWidth(String str) {
		return fr.getStringWidth(str);
	}
	
}
