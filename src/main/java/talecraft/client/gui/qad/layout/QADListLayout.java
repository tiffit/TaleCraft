package talecraft.client.gui.qad.layout;

import java.util.List;

import talecraft.client.gui.qad.QADComponent;
import talecraft.client.gui.qad.QADComponentContainer;
import talecraft.client.gui.qad.QADLayoutManager;
import talecraft.client.gui.qad.QADRectangularComponent;
import talecraft.util.Vec2i;

/*
	QADLayoutManager that arranges components in a list.
*/
public class QADListLayout implements QADLayoutManager {
	private int rowHeight;
	private int vgap;
	private int hgap;
	private double widthMultiplier;
	/*
		No row-height given, using '20'.
	*/
	public QADListLayout() {
		this(20);
	}
	
	public QADListLayout(int rowHeight) {
		this(1D, 20);
	}
	
	public QADListLayout(double widthMultiplier, int rowHeight){
		this(rowHeight, 2, 1, widthMultiplier);
	}
	
	public QADListLayout(int rowHeight, int vgap, int hgap, double widthMultiplier){
		this.rowHeight = rowHeight;
		this.vgap = vgap;
		this.hgap = hgap;
		this.widthMultiplier = widthMultiplier;
	}
	
	@Override
	public void layout(QADComponentContainer container, List<QADComponent> components, Vec2i newContainerSize) {
		
		int width = container.getContainerWidth() - (hgap*2);
		int height = rowHeight;
		
		
		// How much Y gets increased with every component.
		int yIncrement = vgap + height;
		
		// Current Y position to place a component at.
		int currentY = 0 + vgap;
		for(QADComponent component : components) {
			int yIncrementAlt = 0;
			// Is this a rectangular component/a component with size?
			if(component instanceof QADRectangularComponent) {
				QADRectangularComponent rectComp = (QADRectangularComponent) component;
				int maxW = Math.max(width , rectComp.getWidth ());
				int maxH = Math.max(height, rectComp.getHeight());
				yIncrementAlt = maxH + vgap;
				
				if( rectComp.canResize() ) {
					// We can resize the component;
					// so we do that!
					component.setX(hgap);
					component.setY(currentY);
					rectComp.setSize((int) (maxW*widthMultiplier), maxH);
				} else {
					// can't resize, center it in the row.
					// XXX: Add feature to let row height vary per component?
					
					rectComp.setX( (width / 2) - (rectComp.getWidth ()/2) + hgap);
					rectComp.setY( (maxH  / 2) - (rectComp.getHeight()/2) + currentY);
				}
			} else {
				// Component has no size; place it and hope it fits.
				component.setX(hgap);
				component.setY(currentY);
			}
			
			// In the small case that the layout of a sub-container got dirty,
			// go and rebuild its layout (if needed!).
			if(component instanceof QADComponentContainer) {
				if(((QADComponentContainer)component).isLayoutDirty())
					((QADComponentContainer)component).forceRebuildLayout();
			}
			
			currentY += Math.max(yIncrement, yIncrementAlt);
		}
		
		// Set new container size.
		newContainerSize.y = currentY + vgap;
	}
	
}
