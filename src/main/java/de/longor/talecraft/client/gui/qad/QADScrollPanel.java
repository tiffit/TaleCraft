package de.longor.talecraft.client.gui.qad;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import scala.languageFeature.higherKinds;

import com.google.common.collect.Lists;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.gui.vcui.VCUIRenderer;

public class QADScrollPanel extends QADRectangularComponent implements QADComponentContainer {
	private int x;
	private int y;
	private int width;
	private int height;
	private List<QADComponent> components;
	
	private int viewportPosition; // position of the view
	private int viewportHeight; // height of the content
	
	public boolean enabled;
	public boolean visible;
	public boolean focused;
	public boolean allowLeftMouseButtonScrolling;
	
	private int backgroundColor = 3;
	
	public QADScrollPanel() {
		components = Lists.newArrayList();
		enabled = true;
		visible = true;
		
		viewportPosition = 0;
		viewportHeight = 300;
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
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
	
	public boolean canResize() {
		return true;
	}

	@Override
	public void setWidth(int newWidth) {
		this.width = newWidth;
	}

	@Override
	public void setHeight(int newHeight) {
		this.height = newHeight;
		this.viewportPosition = MathHelper.clamp_int(viewportPosition, 0, height);
	}
	
	public void setViewportHeight(int height) {
		this.viewportHeight = height;
		this.viewportPosition = MathHelper.clamp_int(viewportPosition, 0, height);
	}
	
	public int getViewportPosition() {
		return viewportPosition;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setSize(int w, int h) {
		this.width = w;
		this.height = h;
		this.viewportPosition = MathHelper.clamp_int(viewportPosition, 0, height);
	}
	
	public boolean getDoesViewportFit() {
		return height > viewportHeight;
	}
	
	public <T extends QADComponent> T addComponent(T component) {
		components.add(component);
		return component;
	}
	
	@Override
	public void draw(int localMouseX, int localMouseY, float partialTicks, VCUIRenderer renderer) {
		if(!visible)
			return;
		
		boolean viewport = height < viewportHeight;
		
		if(backgroundColor == 2) {
			;// no background
		} else if (backgroundColor == 1) {
			boolean inside = localMouseX >= 0 && localMouseY >= 0 && localMouseX < this.width && localMouseY < this.height;
			renderer.bindTexture(null);
			renderer.drawRectangle(x, y, x+width, y+height, inside ? 0x307F7F7F : 0x1F7F7F7F);
		} else if (backgroundColor == 3) {
			renderer.bindTexture(null);
			renderer.drawRectangle(x, y, x+width, y+height, 0x80000000);
		} else if(backgroundColor != 0) {
			renderer.bindTexture(null);
			renderer.drawRectangle(x, y, x+width, y+height, backgroundColor);
		} else {
			ResourceLocation optionsBackground = new ResourceLocation("textures/gui/options_background.png");
			renderer.bindTexture(optionsBackground);
			renderer.drawTexturedModalRectangle(x, y, 0, 0, -width, -height, 0xFF888888);
			renderer.bindTexture(null);
		}
		
		localMouseY += viewportPosition;
		
		renderer.pushScissor(x,y,width,height);
		
		renderer.offset(x, y - (viewport?viewportPosition:0));
		for(QADComponent component : components) {
			component.draw(localMouseX-component.getX(), localMouseY-component.getY(), partialTicks, renderer);
		}
		renderer.offset(-x, -y+(viewport?viewportPosition:0));
		renderer.popScissor();
		
		if(viewport) {
			renderer.drawGradientRectangle(x, y, x+width-4, y+8, 0xFF000000, 0);
			renderer.drawGradientRectangle(x, y+height-8, x+width-4, y+height, 0, 0xFF000000);
		}
		
		// draw scroll point
		if(viewport) {
			// compute what part of the view we are looking at right now
			float scrollStart = viewportPosition;
			int height = this.height;
			
			if(viewportPosition + height >= viewportHeight) {
				viewportPosition = viewportHeight - height;
			}
			
			float scrollEnd = viewportPosition + height;
			
			if(scrollEnd >= viewportHeight) {
				scrollEnd = viewportHeight;
			}
			
			int __scST = (int) ((scrollStart / viewportHeight) * height);
			int __scED = (int) ((scrollEnd / viewportHeight) * height);
			
			// bar
			renderer.drawRectangle(x+width-4, y, x+width, y+this.height, 0x80000000);
			
			// slide
			renderer.drawRectangle(x+width-4, y+__scST, x+width, y+__scED, 0xFF888888);
			renderer.drawRectangle(x+width-4, y+__scST, x+width-1, y+__scED-1, 0xFFFFFFFF);
		}
	}

	@Override
	public void onMouseClicked(int localMouseX, int localMouseY, int mouseButton) {
		if(!enabled) return;
		
		localMouseY += viewportPosition;
		
		for(QADComponent component : components) {
			component.onMouseClicked(localMouseX-component.getX(), localMouseY-component.getY(), mouseButton);
			
			if(component.isFocused()) {
				focused = true;
			}
		}
	}
	
	@Override
	public void onMouseReleased(int localMouseX, int localMouseY, int state) {
		if(!enabled) return;
		
		localMouseY += viewportPosition;
		
		for(QADComponent component : components) {
			component.onMouseReleased(localMouseX-component.getX(), localMouseY-component.getY(), state);
			
			if(component.isFocused()) {
				focused = true;
			}
		}
	}
	
	@Override
	public void onMouseClickMove(int localMouseX, int localMouseY, int clickedMouseButton, long timeSinceLastClick) {
		if(!enabled) return;
		
		boolean mouseButtonValid = false;
		
		if(allowLeftMouseButtonScrolling)
			mouseButtonValid = true;
		else if(clickedMouseButton != 0)
			mouseButtonValid = true;
		else
			mouseButtonValid = false;
		
		if(mouseButtonValid && isPointInside(localMouseX+x, localMouseY+y)) {
			float normalized = ((float)localMouseY / (float)height); // 0..1
			float scaled = (normalized * viewportHeight);
			
			float scrollStart = viewportPosition;
			float scrollEnd = viewportPosition + height;
			
			float __scST = (scrollStart / (float)viewportHeight) * (float)height;
			float __scED = (scrollEnd / (float)viewportHeight) * (float)height;
			
			float barHeight = (__scED - __scST) * ((float)viewportHeight/(float)height);
			float barHalfHeight = barHeight / 2f;
			
			viewportPosition = (int) (scaled - barHalfHeight); // 0..viewH
			
			if(viewportPosition < 0) {
				viewportPosition = 0;
			}
			
			if(viewportPosition + height >= viewportHeight) {
				viewportPosition = viewportHeight - height;
			}
		}
		
		for(QADComponent component : components) {
			component.onMouseClickMove(localMouseX-component.getX(), localMouseY-component.getY(), clickedMouseButton, timeSinceLastClick);
			
			if(component.isFocused()) {
				focused = true;
			}
		}
	}
	
	@Override
	public void onKeyTyped(char typedChar, int typedCode) {
		if(!enabled) return;
		
		for(QADComponent component : components) {
			component.onKeyTyped(typedChar, typedCode);
		}
	}
	
	@Override
	public void onTickUpdate() {
		if(!enabled) return;
		
		if(height > viewportHeight) {
			viewportPosition = 0;
		}
		
		for(QADComponent component : components) {
			component.onTickUpdate();
		}
	}
	
	@Override
	public boolean isPointInside(int mouseX, int mouseY) {
		int localMouseX = mouseX - x;
		int localMouseY = mouseY - y;
		return localMouseX >= 0 && localMouseY >= 0 && localMouseX < this.width && localMouseY < this.height;
	}
	
	public List<String> getTooltip(int mouseX, int mouseY) {
		List<String> tooltip = null;
		
		for(QADComponent component : components) {
			tooltip = component.getTooltip(mouseX-x, mouseY-y+viewportPosition);
			if(tooltip != null) break;
		}
		
		return tooltip;
	}

	@Override
	public Collection<QADComponent> getComponents() {
		return components;
	}

	@Override
	public QADComponent getComponentByName(String name) {
		for(QADComponent component : components) {
			if(name.equals(component.getName()))
				return component;
		}
		return null;
	}

	@Override
	public void removeAllComponents() {
		components.clear();
	}

	@Override
	public QADEnumComponentClass getComponentClass() {
		return QADEnumComponentClass.CONTAINER;
	}
	
	@Override
	public boolean transferFocus() {
		if(components.size() == 0) {
			return false;
		}
		
		if(!focused) {
			focused = true;
		}
		
		Iterator<QADComponent> iterator = components.iterator();
		boolean unfocusRest = false;
		
		while(iterator.hasNext()) {
			QADComponent current = iterator.next();
			
			if(unfocusRest) {
				current.removeFocus();
				continue;
			}
			
			if(current.isFocused()) {
				if(current.transferFocus()) {
					// stay
				} else {
					// move to next
					if(iterator.hasNext()) {
						iterator.next().transferFocus();
					} else {
						return false;
					}
				}
				unfocusRest = true;
			}
		}
		
		return false;
	}

	@Override
	public boolean isFocused() {
		return focused;
	}

	@Override
	public void removeFocus() {
		// we dont have a focus
		focused = false;
		
		for(QADComponent component : components) {
			component.removeFocus();
		}
	}
	
	public void setBackground(int background) {
		this.backgroundColor = background;
	}
	
}
