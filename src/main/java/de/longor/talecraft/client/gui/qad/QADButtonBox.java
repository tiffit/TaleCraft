package de.longor.talecraft.client.gui.qad;

public class QADButtonBox extends QADScrollPanel {
	
	public QADButtonBox(QADButton...buttons) {
		int iy = 0;
		int hb = 20;
		
		for(QADButton button : buttons) {
			addComponent(button);
			
			button.y = iy;
			iy += button.height;
		}
		
		this.setViewportHeight(iy);
	}
	
}
