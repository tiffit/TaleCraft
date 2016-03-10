package de.longor.talecraft.client.gui.qad;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import de.longor.talecraft.client.gui.qad.QADLabel.LabelModel;
import de.longor.talecraft.client.gui.qad.model.DefaultLabelModel;
import de.longor.talecraft.client.gui.vcui.VCUIRenderer;

public class QADBoxLabel extends QADLabel {
	int boxWidth = 0;
	int boxHeight = 0;
	int valign = 1;
	int halign = 1;
	
	public QADBoxLabel(String text, int x, int y, int width, int height) {
		super(text, x, y);
		this.x = x;
		this.y = y;
		this.boxWidth = width;
		this.boxHeight = height;
	}

	public void setAlign(int h, int v) {
		this.halign = h;
		this.valign = v;
	}

	@Override
	public void draw(int localMouseX, int localMouseY, float partialTicks, VCUIRenderer renderer) {
		int normFontHeight = renderer.getFontRenderer().fr.FONT_HEIGHT;
		int drawFontHeight = renderer.getFontRenderer().fr.FONT_HEIGHT;
		
		if(this.fontHeight != -1) {
			drawFontHeight = this.fontHeight;
			renderer.getFontRenderer().fr.FONT_HEIGHT = drawFontHeight;
		}
		
		lastKnownWidth = renderer.getFontRenderer().stringWidth(model.getText());
		lastKnownHeight = renderer.getFontRenderer().fr.FONT_HEIGHT;
		
		int dx = x;
		int dy = y;
		
		switch (halign) {
			case 0: break;
			case 1: dx = boxWidth/2 - lastKnownWidth/2; break;
			case 2: dx = boxWidth - lastKnownWidth - 1; break;
			default: break;
		}
		
		switch (valign) {
			case 0: break;
			case 1: dy = boxHeight/2 - lastKnownHeight/2; break;
			case 2: dy = boxHeight - lastKnownHeight - 1; break;
			default: break;
		}
		
		renderer.drawString(model.getText(), dx, dy, model.getColor(), shadow);
		
		renderer.getFontRenderer().fr.FONT_HEIGHT = normFontHeight;
	}
    
	@Override
	public boolean isPointInside(int mouseX, int mouseY) {
		int localMouseX = mouseX - x;
		int localMouseY = mouseY - y;
		return localMouseX >= 0 && localMouseY >= 0 && localMouseX < boxWidth && localMouseY < boxHeight;
	}

	@Override
	public int getWidth() {
		return this.boxWidth;
	}

	@Override
	public int getHeight() {
		return this.boxHeight;
	}
	
	public boolean canResize() {
		return true;
	}
	
	@Override
	public void setWidth(int newWidth) {
		boxWidth = newWidth;
	}

	@Override
	public void setHeight(int newHeight) {
		boxHeight = newHeight;
	}

	@Override
	public void setSize(int newWidth, int newHeight) {
		boxWidth = newWidth;
		boxHeight = newHeight;
	}
}
