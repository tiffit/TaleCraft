package de.longor.talecraft.client.gui.qad;

import com.sun.media.sound.ModelAbstractChannelMixer;

import de.longor.talecraft.client.gui.qad.model.DefaultTickBoxModel;
import de.longor.talecraft.client.gui.vcui.VCUIRenderer;

public class QADTickBox extends QADRectangularComponent {
	public static interface TickBoxModel {
		public void setState(boolean newState);
		public boolean getState();
		public void toggleState();
	}
	
	int x = 0;
	int y = 0;
	int w = 0;
	int h = 0;
	boolean isFocused;
	TickBoxModel model;
	
	public QADTickBox(int x, int y, int w, int h, TickBoxModel model) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.model = model == null ? new DefaultTickBoxModel() : model;
	}
	
	public QADTickBox(int x, int y, TickBoxModel model) {
		this.x = x;
		this.y = y;
		this.w = 14;
		this.h = 14;
		this.model = model == null ? new DefaultTickBoxModel() : model;
	}
	
	public QADTickBox(TickBoxModel model) {
		this.x = 0;
		this.y = 0;
		this.w = 14;
		this.h = 14;
		this.model = model == null ? new DefaultTickBoxModel() : model;
	}
	
	public QADTickBox(int x, int y) {
		this.x = x;
		this.y = y;
		this.w = 14;
		this.h = 14;
		this.model = new DefaultTickBoxModel();
	}
	
	public QADTickBox(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.model = new DefaultTickBoxModel();
	}
	
	public QADTickBox setModel(TickBoxModel newModel) {
		if(newModel == null)
			throw new IllegalArgumentException("'newModel' must not be null!");
		
		this.model = newModel;
		return this;
	}
	
	public TickBoxModel getModel() {
		return model;
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
		renderer.drawRectangle(x, y, x+w, y+h, 0xFF7F7F7F);
		renderer.drawRectangle(x+1, y+1, x+w-1, y+h-1, 0xFF000000);
		
		if(model.getState()) {
			renderer.drawRectangle(x+3, y+3, x+w-3, y+h-3, 0xFFF0F0F0);
		}
	}
	
	@Override
	public void onMouseClicked(int localMouseX, int localMouseY, int mouseButton) {
		if(localMouseX > 0 && localMouseY > 0 && localMouseX < w && localMouseY < h) {
			model.toggleState();
			playPressSound(model.getState() ? 1f : 0.8f);
		}
	}
	
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
		return mouseX > x && mouseY > y && mouseX-x < w && mouseY-y < h;
	}

	@Override
	public int getWidth() {
		return w;
	}

	@Override
	public int getHeight() {
		return h;
	}
	
	public boolean canResize() {
		return true;
	}

	@Override
	public void setWidth(int newWidth) {
		w = newWidth;
	}

	@Override
	public void setHeight(int newHeight) {
		h = newHeight;
	}

	@Override
	public void setSize(int newWidth, int newHeight) {
		w = newWidth;
		h = newHeight;
	}

	@Override
	public QADEnumComponentClass getComponentClass() {
		return QADEnumComponentClass.INPUT;
	}
	
	public boolean getState() {
		return model.getState();
	}
	
	@Override
	public boolean transferFocus() {
		if(isFocused) {
			isFocused = false;
			return false;// jump to next focus
		} else {
			isFocused = true;
			return true;// stay in focus
		}
	}

	@Override
	public boolean isFocused() {
		return isFocused;
	}

	@Override
	public void removeFocus() {
		isFocused = false;
	}
	
}
