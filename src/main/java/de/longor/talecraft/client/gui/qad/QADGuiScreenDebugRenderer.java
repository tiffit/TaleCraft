package de.longor.talecraft.client.gui.qad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.longor.talecraft.client.gui.vcui.VCUIRenderer;

public class QADGuiScreenDebugRenderer {
	
	public static void debugRender(
			QADGuiScreen screen, Collection<QADComponent> components, VCUIRenderer instance,
			int mouseX, int mouseY, float partialTicks
	) {
		instance.drawLineRectangle(0,1,screen.width,screen.height-1,QADEnumComponentClass.CONTAINER.color);
		draw(components, instance);
	}
	
	private static void draw(Collection<QADComponent> components, VCUIRenderer instance) {
		for(QADComponent component : components) {
			instance.offset(+component.getX(), +component.getY());
			int color = component.getComponentClass().color;
			
			// In case this is a container, draw child components!
			if(component instanceof QADComponentContainer) {
				int yOff = 0;
				if(component instanceof QADScrollPanel) {
					yOff = -((QADScrollPanel) component).getViewportPosition();
				}
				instance.offset(0, +yOff);
				draw(((QADComponentContainer) component).getComponents(), instance);
				instance.offset(0, -yOff);
			}
			
			// If this is a rectangular component, draw it as a rectangle!
			if(component instanceof QADRectangularComponent) {
				QADRectangularComponent rect = (QADRectangularComponent) component;
				instance.drawLineRectangle(0,0,rect.getWidth(),rect.getHeight(),color);
			} else {
				// Not a rectangular component? Draw as a tiny box.
				instance.drawLineRectangle(0,0,2,2,color);
			}
			
			instance.offset(-component.getX(), -component.getY());
		}
	}

}
