package de.longor.talecraft.client.gui.qad;

public abstract class QADRectangularComponent extends QADComponent {
	public abstract int getWidth();
	public abstract int getHeight();
	
	public abstract boolean canResize();
	public abstract void setWidth(int newWidth);
	public abstract void setHeight(int newHeight);
	public abstract void setSize(int newWidth, int newHeight);
	
	public void setBounds(int newX, int newY, int newWidth, int newHeight) {
		setPosition(newX, newY);
		if(canResize()) {
			setSize(newWidth, newHeight);
		}
	}
}
