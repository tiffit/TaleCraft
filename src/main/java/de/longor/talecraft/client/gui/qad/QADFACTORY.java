package de.longor.talecraft.client.gui.qad;

import java.awt.Button;

import de.longor.talecraft.client.gui.qad.QADTextField.TextChangeListener;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class QADFACTORY {
	
	public static final QADLabel createLabel(Object object, int x, int y) {
		return new QADLabel(""+object, x, y);
	}
	
	public static final QADLabel createLabel(String text, int x, int y) {
		return new QADLabel(text, x, y);
	}
	
	public static final QADButton createButton(ResourceLocation icon, int x, int y, int width, Runnable action) {
		QADButton button = new QADButton(x, y, width, "");
		button.setAction(action);
		button.setIcon(icon);
		return button;
	}
	
	public static final QADButton createButton(String text, int x, int y, int width, Runnable action) {
		QADButton button = new QADButton(x, y, width, text);
		button.setAction(action);
		return button;
	}
	
	public static final QADButton createButton(String text, int x, int y, int width) {
		QADButton button = new QADButton(x, y, width, text);
		return button;
	}
	
	public static final QADTextField createTextField(String text, int x, int y, int width) {
		QADTextField textField = new QADTextField(Minecraft.getMinecraft().fontRendererObj, x, y, width, 16);
		textField.setText(text);
		return textField;
	}
	
	public static final QADTextField createTextField(int number, int x, int y, int width) {
		QADTextField textField = new QADTextField(Minecraft.getMinecraft().fontRendererObj, x, y, width, 16);
		textField.setText(Integer.toString(number));
		return textField;
	}
	
	public static final QADTextField createTextField(float number, int x, int y, int width) {
		QADTextField textField = new QADTextField(Minecraft.getMinecraft().fontRendererObj, x, y, width, 16);
		textField.setText(Float.toString(number));
		return textField;
	}
	
	public static final QADTextField createNumberTextField(int number, int x, int y, int width) {
		QADTextField textField = new QADTextField(Minecraft.getMinecraft().fontRendererObj, x, y, width, 16);
		textField.setText(Integer.toString(number));
		textField.textChangedListener = new TextChangeListener() {
			@Override public void call(QADTextField qadTextField, String text) {
				if(text.isEmpty()) {
					qadTextField.setText("0");
					return;
				}
				
				try {
					Integer.parseInt(text);
					qadTextField.setTextColor(0xFFFFFFFF);
				} catch (NumberFormatException e) {
					qadTextField.setTextColor(0xFFFF5555);
				}
			}
		};
		return textField;
	}
	
	public static final QADTextField createNumberTextField(int number, int x, int y, int width, final int MAX) {
		QADTextField textField = new QADTextField(Minecraft.getMinecraft().fontRendererObj, x, y, width, 16);
		textField.setText(Integer.toString(number));
		textField.textChangedListener = new TextChangeListener() {
			int max = MAX;
			
			@Override public void call(QADTextField qadTextField, String text) {
				if(text.isEmpty()) {
					qadTextField.setText("0");
					return;
				}
				
				try {
					int i = Integer.parseInt(text);
					
					if(i > max)
						qadTextField.setTextColor(0xFFFF5555);
					else
						qadTextField.setTextColor(0xFFFFFFFF);
				} catch (NumberFormatException e) {
					qadTextField.setTextColor(0xFFFF5555);
				}
			}
		};
		return textField;
	}
	
	public static final QADTextField createNumberTextField(int number, int x, int y, int width, final int MAX, final int MIN) {
		QADTextField textField = new QADTextField(Minecraft.getMinecraft().fontRendererObj, x, y, width, 16);
		textField.setText(Integer.toString(number));
		textField.textChangedListener = new TextChangeListener() {
			int max = MAX;
			int min = MIN;
			
			@Override public void call(QADTextField qadTextField, String text) {
				if(text.isEmpty()) {
					qadTextField.setText("0");
					return;
				}
				
				try {
					int i = Integer.parseInt(text);
					
					if(i > max)
						qadTextField.setTextColor(0xFFFF5555);
					else if(i < min)
						qadTextField.setTextColor(0xFFFF5555);
					else
						qadTextField.setTextColor(0xFFFFFFFF);
				} catch (NumberFormatException e) {
					qadTextField.setTextColor(0xFFFF5555);
				}
			}
		};
		return textField;
	}
	
}
