package de.longor.talecraft.client.gui.qad;

import de.longor.talecraft.client.gui.vcui.VCUIRenderer;

public abstract class QADDrawable extends QADComponent {
	int x;
	int y;
	
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
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public QADEnumComponentClass getComponentClass() {
		return QADEnumComponentClass.VISUAL;
	}
	
	@Override
	public void draw(int localMouseX, int localMouseY, float partialTicks, VCUIRenderer renderer) {
		paint(localMouseX, localMouseY, partialTicks, renderer);
	}
	
	public abstract void paint(int localMouseX, int localMouseY, float partialTicks, VCUIRenderer renderer);
	
	@Override
	public void onMouseClicked(int localMouseX, int localMouseY, int mouseButton) {}

	@Override
	public void onMouseReleased(int localMouseX, int localMouseY, int state) {}

	@Override
	public void onMouseClickMove(int localMouseX, int localMouseY, int clickedMouseButton, long timeSinceLastClick) {}

	@Override
	public void onKeyTyped(char typedChar, int typedCode) {}

	@Override
	public void onTickUpdate() {}

	@Override
	public boolean isPointInside(int mouseX, int mouseY) {return false;}
	
	@Override
	public boolean transferFocus() {
		return false;
	}

}
