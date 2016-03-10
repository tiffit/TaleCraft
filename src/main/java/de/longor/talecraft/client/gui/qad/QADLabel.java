package de.longor.talecraft.client.gui.qad;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import de.longor.talecraft.client.gui.qad.model.DefaultLabelModel;
import de.longor.talecraft.client.gui.vcui.VCUIRenderer;

public class QADLabel extends QADRectangularComponent {
	public static interface LabelModel {
		public String getText();
		public int getTextLength();
		public int getColor();
	}
	
	LabelModel model = null;
	Runnable onClick = null;
	int x = 0;
	int y = 0;
	int fontHeight = -1;
	int lastKnownWidth = 0;
	int lastKnownHeight = 0;
	boolean shadow = true;
	
	public QADLabel(String text) {
		this.model = new DefaultLabelModel(text);
		this.x = 0;
		this.y = 0;
	}
	
	public QADLabel(String text, int x, int y) {
		this.model = new DefaultLabelModel(text);
		this.x = x;
		this.y = y;
	}
	
	public QADLabel(String text, int x, int y, int color) {
		this.model = new DefaultLabelModel(text, color);
		this.x = x;
		this.y = y;
	}
	
	public QADLabel(String text, int x, int y, boolean shadow) {
		this.model = new DefaultLabelModel(text);
		this.x = x;
		this.y = y;
		this.shadow = shadow;
	}
	
	public QADLabel(String text, int x, int y, int color, boolean shadow) {
		this.model = new DefaultLabelModel(text, color);
		this.x = x;
		this.y = y;
		this.shadow = shadow;
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

	@Override
	public void draw(int localMouseX, int localMouseY, float partialTicks, VCUIRenderer renderer) {
		int normFontHeight = renderer.getFontRenderer().fr.FONT_HEIGHT;
		int drawFontHeight = renderer.getFontRenderer().fr.FONT_HEIGHT;
		
		if(fontHeight != -1) {
			drawFontHeight = fontHeight;
			renderer.getFontRenderer().fr.FONT_HEIGHT = drawFontHeight;
		}
		
		// Culling on the Y-Axis
		if(renderer.getOffsetY()+y > renderer.getHeight()) {
			return;
		} else if(renderer.getOffsetY()+y+drawFontHeight+1 < 0) {
			return;
		}
		
		lastKnownWidth = renderer.getFontRenderer().stringWidth(model.getText());
		lastKnownHeight = renderer.getFontRenderer().fr.FONT_HEIGHT;
		renderer.drawString(model.getText(), x, y, model.getColor(), shadow);
		
		renderer.getFontRenderer().fr.FONT_HEIGHT = normFontHeight;
	}

	@Override
	public void onMouseClicked(int i, int j, int mouseButton) {}

	@Override
	public void onMouseReleased(int mouseX, int mouseY, int state) {
		if(isPointInside(mouseX+x, mouseY+y)) {
			if(onClick != null) {
				playPressSound(1f);
				onClick.run();
			}
		}
	}

	@Override
	public void onMouseClickMove(int i, int j, int clickedMouseButton, long timeSinceLastClick) {}

	@Override
	public void onKeyTyped(char typedChar, int typedCode) {}

	@Override
	public void onTickUpdate() {}
    
	@Override
	public boolean isPointInside(int mouseX, int mouseY) {
		if(lastKnownWidth == 0 || lastKnownHeight == 0)
			return false;
		
		int localMouseX = mouseX - x;
		int localMouseY = mouseY - y;
		return localMouseX >= 0 && localMouseY >= 0 && localMouseX < lastKnownWidth && localMouseY < lastKnownHeight;
	}
	
	public List<String> getTooltip(int mouseX, int mouseY) {
		return isPointInside(mouseX, mouseY) ? getTooltip() : null;
	}

	@Override
	public int getWidth() {
		return this.lastKnownWidth;
	}

	@Override
	public int getHeight() {
		return this.lastKnownHeight;
	}
	
	public boolean canResize() {
		return false;
	}
	
	@Override
	public void setWidth(int newWidth) {
		throw new UnsupportedOperationException("Cannot change string component's width as that is a property of the font-renderer.");
	}

	@Override
	public void setHeight(int newHeight) {
		throw new UnsupportedOperationException("Cannot change string component's height as that is a property of the font-renderer.");
	}

	@Override
	public void setSize(int newWidth, int newHeight) {
		throw new UnsupportedOperationException("Cannot change string component's size as that is a property of the font-renderer.");
	}

	public void setFontHeight(int height) {
		this.fontHeight = height;
	}
	
	public void setOnClickHandler(Runnable runnable) {
		this.onClick = runnable;
	}

	@Override
	public QADEnumComponentClass getComponentClass() {
		return QADEnumComponentClass.VISUAL;
	}
	
	@Override
	public boolean transferFocus() {
		return false;
	}

	@Override
	public boolean isFocused() {
		return false;
	}

	@Override
	public void removeFocus() {
		// dont do a thing
	}

}
