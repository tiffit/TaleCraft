package de.longor.talecraft.client.gui.qad;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.minecraft.util.ResourceLocation;

import com.google.common.collect.Lists;

import de.longor.talecraft.client.gui.vcui.VCUIRenderer;

public class QADPanel extends QADRectangularComponent implements QADComponentContainer {
	private int x;
	private int y;
	private int width;
	private int height;
	private int backgroundColor;
	private List<QADComponent> components;
	
	public boolean enabled;
	public boolean visible;
	public boolean focused;
	public boolean ignoreOuterEvents;
	
	public QADPanel() {
		components = Lists.newArrayList();
		enabled = true;
		visible = true;
		ignoreOuterEvents = false;
		backgroundColor = 0;
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

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setSize(int w, int h) {
		this.width = w;
		this.height = h;
	}
	
	public boolean canResize() {
		return true;
	}
	
	public void setWidth(int w) {
		width = w;
	}
	
	public void setHeight(int h) {
		height = h;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public void setBackgroundColor(int i) {
		backgroundColor = i;
	}
	
	@Override
	public <T extends QADComponent> T addComponent(T c) {
		components.add(c);
		return c;
	}
	
	@Override
	public void draw(int localMouseX, int localMouseY, float partialTicks, VCUIRenderer renderer) {
		if(!visible)
			return;
		
		if(renderer.getOffsetY()+y > renderer.getHeight()) {
			return;
		}
		
		if(renderer.getOffsetY()+y+height < 0) {
			return;
		}
		
		if(backgroundColor == 2) {
			;// no background
		} else if (backgroundColor == 1) {
			boolean inside = localMouseX >= 0 && localMouseY >= 0 && localMouseX < this.width && localMouseY < this.height;
			renderer.bindTexture(null);
			renderer.drawRectangle(x, y, x+width, y+height, inside ? 0x307F7F7F : 0x1F7F7F7F);
		} else if (backgroundColor == 3) {
			renderer.bindTexture(null);
			renderer.drawRectangle(x, y, x+width, y+height, 0x7F000000);
		} else if(backgroundColor != 0) {
			renderer.bindTexture(null);
			renderer.drawRectangle(x, y, x+width, y+height, backgroundColor);
		} else {
			ResourceLocation optionsBackground = new ResourceLocation("textures/gui/options_background.png");
			renderer.bindTexture(optionsBackground);
			renderer.drawTexturedModalRectangle(x, y, 0, 0, -width, -height, 0xFF888888);
			renderer.bindTexture(null);
		}
		
		// renderer.drawRectangle(0, 0, 64, 64, backgroundColor);
		
		if(!components.isEmpty()) {
			renderer.offset(x, y);
			for(QADComponent component : components) {
				component.draw(localMouseX-component.getX(), localMouseY-component.getY(), partialTicks, renderer);
			}
			renderer.offset(-x, -y);
		}
	}

	@Override
	public void onMouseClicked(int localMouseX, int localMouseY, int mouseButton) {
		if(!enabled) return;
		
		if(!isPointInside(localMouseX+x, localMouseY+y) && ignoreOuterEvents) {
			return;
		}
		
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
		
		if(!isPointInside(localMouseX+x, localMouseY+y) && ignoreOuterEvents) {
			return;
		}
		
		for(QADComponent component : components) {
			component.onMouseReleased(localMouseX-component.getX(), localMouseY-component.getY(), state);
		}
	}
	
	@Override
	public void onMouseClickMove(int localMouseX, int localMouseY, int clickedMouseButton, long timeSinceLastClick) {
		if(!enabled) return;
		
		if(!isPointInside(localMouseX+x, localMouseY+y) && ignoreOuterEvents) {
			return;
		}
		
		for(QADComponent component : components) {
			component.onMouseClickMove(localMouseX-component.getX(), localMouseY-component.getY(), clickedMouseButton, timeSinceLastClick);
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
			tooltip = component.getTooltip(mouseX-x, mouseY-y);
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
	
}
