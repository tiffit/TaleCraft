package tiffit.talecraft.ingamescripting;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import de.longor.talecraft.client.gui.qad.QADEnumComponentClass;
import de.longor.talecraft.client.gui.qad.QADRectangularComponent;
import de.longor.talecraft.client.gui.qad.QADTextField;
import de.longor.talecraft.client.gui.qad.model.DefaultTextFieldModel;
import de.longor.talecraft.client.gui.vcui.VCUIRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;


public class QADScriptField extends QADRectangularComponent {

	int x;
	int y;
	int width;
	int height;
	boolean isFocused;
	FontRenderer font;
	List<String> content;
	int selected;
	
	public QADScriptField(FontRenderer font, int x, int y, int width, int height, List<String> content){
		this.font = font;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.content = content;
	}
	
	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public boolean canResize() {
		return false;
	}

	@Override
	public void setWidth(int newWidth) {
		width = newWidth;
	}

	@Override
	public void setHeight(int newHeight) {
		height = newHeight;
	}

	@Override
	public void setSize(int newWidth, int newHeight) {
		setWidth(newWidth);
		setHeight(newHeight);
	}

	@Override
	public QADEnumComponentClass getComponentClass() {
		return QADEnumComponentClass.INPUT;
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
		setX(x);
		setY(y);
	}

	@Override
	public void draw(int localMouseX, int localMouseY, float partialTicks, VCUIRenderer renderer) {
		for(int i = 0; i < content.size(); i++){
			font.drawString(content.get(i), x + 5, y + 5 + (i * 10), 0xffffff);
		}
	}

	@Override
	public void onMouseClicked(int localMouseX, int localMouseY, int mouseButton) {
		
	}

	@Override
	public void onMouseReleased(int localMouseX, int localMouseY, int state) {
		
	}

	@Override
	public void onMouseClickMove(int localMouseX, int localMouseY, int clickedMouseButton, long timeSinceLastClick) {
	}

	@Override
	public void onKeyTyped(char typedChar, int typedCode) {
		String line = content.size() == 0 ? "" : content.get(selected);
		System.out.println("Key: " + typedCode);
		if(GuiScreen.isCtrlKeyDown() || GuiScreen.isAltKeyDown()) return;
		if(typedCode == 14){
			if(line.length() > 0) line = line.substring(0, line.length() - 1);
			finalizeKey(line);
			return;
		}
		if(typedCode == 28){
			selected++;
			return;
		}
		line += typedChar;
		finalizeKey(line);
	}
	
	private void finalizeKey(String line){
		if(content.size() <= selected + 1){
			content.add(line);
		}else{
			content.set(selected, line);
		}
	}

	@Override
	public void onTickUpdate() {
		
	}

	@Override
	public boolean isPointInside(int mouseX, int mouseY) {
		if(mouseX < x || mouseY < y) return false;
		if(mouseX > x + width || mouseY > y + height) return false;
		return true;
	}

	@Override
	public boolean isFocused() {
		return isFocused;
	}

	@Override
	public boolean transferFocus() {
		if(isFocused) {
			isFocused = false;
			return false;
		} else {
			isFocused = true;
			return true;
		}
	}

	@Override
	public void removeFocus() {
		isFocused = false;
	}

	

}
