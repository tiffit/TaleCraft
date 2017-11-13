package talecraft.client.gui.qad;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import talecraft.client.gui.vcui.VCUIRenderer;

public class QADDropdownBox extends QADRectangularComponent {
	
	private boolean isVisible = true;
	private boolean isFocused = false;
	
	private boolean fieldbox_visible = true;
	private int fieldbox_width = 100;
	private int fieldbox_height = 20;
	private int fieldbox_x = 0;
	private int fieldbox_y = 0;
	private String fieldbox_text = "";

	private boolean dropbox_visible = false;
	private int dropbox_width = 100;
	private int dropbox_height = 22;
	private int dropbox_x = 0;
	private int dropbox_y = 20;
	
	private int dropbox_scroll = 0;
	
	private int dropbox_rowcount = 7;
	private int dropbox_rowheight = 20;
	// private int dropbox_highlight = -1;
	private ListModel dropbox_model = null;
	private String dropbox_filterstr = "";
	private ListModelItem dropbox_selected = null;
	
	private int color = 0x7F101010;
	
	public QADDropdownBox(ListModel model, ListModelItem selectedItem) {
		this.dropbox_model = model;
		setSelected(selectedItem);
	}
	
	public QADDropdownBox(ListModel model) {
		this(model, (ListModelItem)null);
	}
	
	public void setSelected(ListModelItem item){
		if(dropbox_model != null && dropbox_model.hasItems()) {
			if(item != null && dropbox_model.getItems().contains(item)) {
				this.fieldbox_text = item.getText();
				this.dropbox_selected = item;
				this.dropbox_model.onSelection(item);
			} else {
				ListModelItem itm = dropbox_model.getItems().get(0);
				this.fieldbox_text = itm.getText();
				this.dropbox_selected = itm;
			}
		}
	}
	
	@Override
	public void handleMouseInput() {
		int amount = Mouse.getEventDWheel()/120;
		if(amount < 0){
			dropbox_scroll += amount;
			if(dropbox_scroll < 0) dropbox_scroll = 0;
		} else if(amount > 0){
			dropbox_scroll += amount;
			if(dropbox_scroll > dropbox_model.getFilteredItems().size() - dropbox_rowcount) dropbox_scroll = dropbox_model.getFilteredItems().size() - dropbox_rowcount < 0 ? 0 : dropbox_model.getFilteredItems().size() - dropbox_rowcount;
		}
	}
	
	@Override
	public int getWidth() {
		return this.fieldbox_width;
	}

	@Override
	public int getHeight() {
		return this.fieldbox_height;
	}

	@Override
	public void setWidth(int newWidth) {
		setSize(newWidth, fieldbox_height);
	}
	
	public void setColor(int color){
		this.color = color;
	}

	@Override
	public void setHeight(int newHeight) {
		setSize(fieldbox_width, newHeight);
	}

	@Override
	public void setSize(int newWidth, int newHeight) {
		this.fieldbox_width = newWidth;
		this.fieldbox_height = newHeight;
		reFocus();
	}

	@Override
	public boolean canResize() {
		return true;
	}

	@Override
	public QADEnumComponentClass getComponentClass() {
		return QADEnumComponentClass.INPUT;
	}

	@Override
	public int getX() {
		return this.fieldbox_x;
	}

	@Override
	public int getY() {
		return this.fieldbox_y;
	}

	@Override
	public void setX(int x) {
		setPosition(x, fieldbox_y);
	}

	@Override
	public void setY(int y) {
		setPosition(fieldbox_x, y);
	}

	@Override
	public void setPosition(int x, int y) {
		this.fieldbox_x = x;
		this.fieldbox_y = y;
		reFocus();
	}

	@Override
	public void postDraw(int localMouseX, int localMouseY, float partialTicks, VCUIRenderer renderer){
		if(!(dropbox_visible && dropbox_model != null))return;
		if(!isVisible)
			return;

		renderer.drawRectangle(0, 0, renderer.getWidth(), renderer.getHeight(), 0xaa000000);
		boolean hover = isPointInside(localMouseX+getX(), localMouseY+getY());
		
		if(fieldbox_visible) {
			int color = -1;
			int hoverState = hover ? 1 : 0;
			
			if(isFocused)
				hoverState++;
			
			switch(hoverState) {
			case 0: color = this.color; break;
			case 1: color = 0xFF505030; break;
			case 2: color = 0xFF707030; break;
			default:color = 0xFF101010;break;
//				case 0: color = 0x7F101010; break;
//				case 1: color = 0xFF505030; break;
//				case 2: color = 0xFF707030; break;
//				default:color = 0xFF101010;break;
			}
			
			renderer.drawRectangle    (this.fieldbox_x, this.fieldbox_y, this.fieldbox_x+this.fieldbox_width, this.fieldbox_y+this.fieldbox_height, 0xFF000000);
			renderer.drawLineRectangle(this.fieldbox_x, this.fieldbox_y, this.fieldbox_x+this.fieldbox_width, this.fieldbox_y+this.fieldbox_height, color);
			
			renderer.pushScissor(this.fieldbox_x+1, this.fieldbox_y+1, this.fieldbox_width-2, this.fieldbox_height-2);
			
			
			int textColor = -1;
			String text = "";
			boolean cursor = true;
			
			if(isFocused) {
				text = dropbox_filterstr;
				
				if(dropbox_filterstr.isEmpty()) {
					text = fieldbox_text;
					textColor = 0x7F505050;
					cursor = false;
				}
			} else {
				text = fieldbox_text;
				cursor = false;
			}
			
			int strp = renderer.drawString(text, this.fieldbox_x+4, this.fieldbox_y+6, textColor, !cursor);
			
			if(isFocused) {
				if(!cursor) {
					strp = this.fieldbox_x+4;
				}
				
				// draw text cursor if focused
				renderer.drawRectangle(
						strp+0, this.fieldbox_y+14,
						strp+5, this.fieldbox_y+15,
						(System.currentTimeMillis()%1000) < 700 ? 0 : 0xFFFFFFFF
				);
			}
			
			renderer.popScissor();
		}
		

		
			renderer.drawRectangle(dropbox_x, dropbox_y, dropbox_x+dropbox_width, dropbox_y+dropbox_height, 0x7F000000);
			
			List<ListModelItem> items = dropbox_model.getFilteredItems();
			if(items != null) {
				int rows = Math.min(items.size(), this.dropbox_rowcount + this.dropbox_scroll);
				boolean hasIcons = dropbox_model.hasIcons();
				
				// hover highlight
				int hoverlight = hover?mouseToItemIndex(localMouseY+getY()):-1;
				// this.dropbox_highlight = hoverlight;
				
				// list render
				int yOffset = 0;
				int xOffset = hasIcons?(20+4):4;
				
				boolean light = false;
				for(int i = dropbox_scroll; i < rows; i++) {
					yOffset = (i * dropbox_rowheight) - (this.dropbox_scroll*dropbox_rowheight);
					light = i == hoverlight;
					
					ListModelItem item = items.get(i);
					
					int x = dropbox_x;
					int y = dropbox_y+yOffset;
					renderer.drawLineRectangle(x, y, x+dropbox_width, y+dropbox_rowheight, 0xFFFFFFFF);
					renderer.drawString(item.getText(), x+xOffset, y+6, light?0xFFfff266:0xFFFFFFFF, false);
					
					if(hasIcons) {
						int icox = x;
						int icoy = y;
						renderer.offset(icox, icoy);
						dropbox_model.drawIcon(renderer, partialTicks, light, item);
						renderer.offset(-icox, -icoy);
					}
				}
			}
	}
	
	@Override
	public void draw(int localMouseX, int localMouseY, float partialTicks, VCUIRenderer renderer) {
		if(dropbox_visible && dropbox_model != null)return;
		if(!isVisible)
			return;
		
		boolean hover = isPointInside(localMouseX+getX(), localMouseY+getY());
		
		if(fieldbox_visible) {
			int color = -1;
			int hoverState = hover ? 1 : 0;
			
			if(isFocused)
				hoverState++;
			
			switch(hoverState) {
			case 0: color = this.color; break;
			case 1: color = 0xFF505030; break;
			case 2: color = 0xFF707030; break;
			default:color = 0xFF101010;break;
//				case 0: color = 0x7F101010; break;
//				case 1: color = 0xFF505030; break;
//				case 2: color = 0xFF707030; break;
//				default:color = 0xFF101010;break;
			}
			
			renderer.drawRectangle    (this.fieldbox_x, this.fieldbox_y, this.fieldbox_x+this.fieldbox_width, this.fieldbox_y+this.fieldbox_height, 0xFF000000);
			renderer.drawLineRectangle(this.fieldbox_x, this.fieldbox_y, this.fieldbox_x+this.fieldbox_width, this.fieldbox_y+this.fieldbox_height, color);
			
			renderer.pushScissor(this.fieldbox_x+1, this.fieldbox_y+1, this.fieldbox_width-2, this.fieldbox_height-2);
			
			
			int textColor = -1;
			String text = "";
			boolean cursor = true;
			
			if(isFocused) {
				text = dropbox_filterstr;
				
				if(dropbox_filterstr.isEmpty()) {
					text = fieldbox_text;
					textColor = 0x7F505050;
					cursor = false;
				}
			} else {
				text = fieldbox_text;
				cursor = false;
			}
			
			int strp = renderer.drawString(text, this.fieldbox_x+4, this.fieldbox_y+6, textColor, !cursor);
			
			if(isFocused) {
				if(!cursor) {
					strp = this.fieldbox_x+4;
				}
				
				// draw text cursor if focused
				renderer.drawRectangle(
						strp+0, this.fieldbox_y+14,
						strp+5, this.fieldbox_y+15,
						(System.currentTimeMillis()%1000) < 700 ? 0 : 0xFFFFFFFF
				);
			}
			
			renderer.popScissor();
		}
		

	}
	
	@Override
	public boolean focusInput() {
		return dropbox_visible && dropbox_model != null;
	}

	@Override
	public void onMouseClicked(int localMouseX, int localMouseY, int mouseButton) {
	}

	@Override
	public void onMouseReleased(int localMouseX, int localMouseY, int state) {
		boolean inside = isPointInside(localMouseX+getX(), localMouseY+getY());
		boolean preFocus = isFocused;
		
		if(inside) {
			setFocused(true);
		} else {
			setFocused(false);
		}
		
		if(preFocus != isFocused) {
			if(dropbox_model != null) {
				dropbox_model.applyFilter(dropbox_filterstr);
				recalculateDropbox();
			}
			dropbox_filterstr = "";
		}
		
		if(isFocused && inside && dropbox_model != null) {
			List<ListModelItem> items = dropbox_model.getFilteredItems();
			if(items != null) {
				int hoverlight = mouseToItemIndex(localMouseY+getY());
				// this.dropbox_highlight = hoverlight;
				
				if(hoverlight != -1 && hoverlight < items.size()) {
					ListModelItem item = items.get(hoverlight);
					fieldbox_text = item.getText();
					dropbox_selected = item;
					dropbox_model.onSelection(item);
					setFocused(false);
				}
			}
		}
	}

	@Override
	public void onMouseClickMove(int localMouseX, int localMouseY, int clickedMouseButton, long timeSinceLastClick) {
		
	}

	@Override
	public void onKeyTyped(char typedChar, int typedCode) {
		if(isFocused) {
			
			if(typedCode == Keyboard.KEY_TAB) {
				// autocompletion?
				// Not necessary; see below.
				return;
			}
			
			if(typedCode == Keyboard.KEY_RETURN) {
				if(dropbox_model != null) {
					List<ListModelItem> items = dropbox_model.getFilteredItems();
					if(items.size() > 0) {
						int hl = 0; // XXX: dropbox_highlight? No.
						ListModelItem item = items.get(hl);
						fieldbox_text = item.getText();
						dropbox_selected = item;
						dropbox_model.onSelection(item);
						setFocused(false);
					}
				}
				
				return;
			}
			
			if(typedCode == Keyboard.KEY_BACK) {
				int len = dropbox_filterstr.length();
				if(len > 0) {
					dropbox_filterstr = dropbox_filterstr.substring(0,len-1);
					
					if(dropbox_model!=null) {
						dropbox_model.applyFilter(dropbox_filterstr);
						recalculateDropbox();
					}
				}
				
				return;
			}
			
			if(Character.isJavaIdentifierPart(typedChar) || Character.isWhitespace(typedChar)) {
				dropbox_filterstr += typedChar;
				
				if(dropbox_model!=null) {
					dropbox_model.applyFilter(dropbox_filterstr);
					recalculateDropbox();
					if(dropbox_model.getFilteredItems().size() > 0) {
						// dropbox_highlight = 0;
					}
				}
			}
			
		}
	}

	@Override
	public void onTickUpdate() {
	}

	@Override
	public boolean isPointInside(int mouseX, int mouseY) {
		boolean inside =
				mouseX >= this.fieldbox_x &&
				mouseY >= this.fieldbox_y &&
				mouseX <= this.fieldbox_x + this.fieldbox_width &&
				mouseY <= (dropbox_visible?(this.dropbox_y+this.dropbox_height):(this.fieldbox_y+this.fieldbox_height));
		return inside;
	}
	
	public int mouseToItemIndex(int mouseY) {
		if(mouseY < dropbox_y) {
			return -1;
		}
		if(mouseY >= dropbox_y+dropbox_height) {
			return -1;
		}
		
		mouseY -= dropbox_y;
		// mouseY -= dropbox_rowheight;
		
		return (mouseY/dropbox_rowheight) +  + dropbox_scroll;
	}

	@Override
	public boolean transferFocus() {
		return false;
	}

	@Override
	public boolean isFocused() {
		return isFocused;
	}

	@Override
	public void removeFocus() {
		isFocused = false;
		
	}
	
	private void reFocus() {
		boolean f = isFocused;
		setFocused(false);
		setFocused(f);
	}
	
	private void setFocused(boolean b) {
		if(isFocused == b) {
			// nothing to do
			return;
		}
		
		// Change focus...
		this.isFocused = b;
		
		recalculateDropbox();
	}
	
	private void recalculateDropbox() {
		this.dropbox_visible = false;
		if(!isFocused) {
			// dont continue if not focused!
			return;
		}
		
		this.dropbox_x = this.fieldbox_x;
		this.dropbox_y = this.fieldbox_y + this.fieldbox_height + 3;
		
		if(this.dropbox_model == null) {
			// no model!
			return;
		}
		
		if(!this.dropbox_model.hasItems()) {
			// empty model!
			return;
		}

		this.dropbox_scroll = 0;
		if(dropbox_filterstr != null && !dropbox_filterstr.trim().isEmpty())
			dropbox_model.applyFilter(dropbox_filterstr);
		
		final List<ListModelItem> items = this.dropbox_model.getFilteredItems();
		if(items == null) {
			return;
		}
		int itemCount = items.size();
		int itemHeight = dropbox_rowheight;
		
		int rows = Math.min(itemCount, this.dropbox_rowcount);
		
		this.dropbox_width = fieldbox_width;
		this.dropbox_height = rows * itemHeight;
		
		this.dropbox_visible = true;
	}
	
	/**
	 * @return The selected item, or null.
	 **/
	public final ListModelItem getSelected() {
		return dropbox_selected;
	}
	
	public static interface ListModel {
		/** Called when a item is selected. **/
		public void onSelection(ListModelItem selected);
		
		/** This should return true if getItemCount() > 0. **/
		public boolean hasItems();
		/** This should return the amount of items. **/
		public int getItemCount();
		/** This should return a full list of items. **/
		public List<ListModelItem> getItems();
		
		/** This should use the given string to filter the list of items. **/
		public void applyFilter(String filterString);
		/** This should return the filtered list (see applyFilter) of items. **/
		public List<ListModelItem> getFilteredItems();
		
		/** let this return true if drawIcon is implemented. **/
		public boolean hasIcons();
		/** implement this to draw icons (origin is at list item!). **/
		public void drawIcon(VCUIRenderer renderer, float partialTicks, boolean light, ListModelItem item);
	}
	
	// The interface for list items. Pretty simple, isnt it?
	public static interface ListModelItem {
		String getText();
	}
	
	// A basic implementation for ListModelItem.
	public static class StringItem implements ListModelItem {
		private final String text;
		
		public StringItem(final String text) {
			this.text = text;
		}
		
		@Override
		public final String getText() {
			return text;
		}
	}
	
}
