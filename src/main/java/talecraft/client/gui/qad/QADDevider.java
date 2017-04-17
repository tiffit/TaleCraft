package talecraft.client.gui.qad;

import talecraft.client.gui.vcui.VCUIRenderer;

public class QADDevider extends QADComponent {

	private int pos;
	private boolean vertical;
	
	public QADDevider(int pos, boolean vertical) {
		this.pos = pos;
		this.vertical = vertical;
	}
	
	@Override
	public QADEnumComponentClass getComponentClass() {
		return QADEnumComponentClass.VISUAL;
	}

	@Override
	public int getX() {
		return pos;
	}

	@Override
	public int getY() {
		return pos;
	}

	@Override
	public void setX(int x) {
		this.pos = x;
	}

	@Override
	public void setY(int y) {
		this.pos = y;
	}

	@Override
	public void setPosition(int x, int y) {
		this.pos = x;
	}

	@Override
	public void draw(int localMouseX, int localMouseY, float partialTicks, VCUIRenderer renderer) {
		if(vertical)renderer.drawLineRectangle(pos, 0, pos+1, renderer.getHeight(), 0xff555555);
		else renderer.drawLineRectangle(0, pos, renderer.getWidth(), pos, 0xff555555);
	}

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
	public boolean isPointInside(int mouseX, int mouseY) {
		return false;
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
	}

}
