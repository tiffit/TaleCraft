package de.longor.talecraft.client.gui.qad.model;

import de.longor.talecraft.client.gui.qad.QADTextField;
import de.longor.talecraft.client.gui.qad.QADTextField.TextFieldModel;

public final class DefaultTextFieldModel implements TextFieldModel {
	private String text;
	private int color;
	
	public DefaultTextFieldModel(String text) {
		this.text = text;
		this.color = 0xFFFFFFFF;
	}
	
	public DefaultTextFieldModel() {
		this.text = "[null]";
		this.color = 0xFFFFFFFF;
	}
	
	@Override public void setText(String text) {
		this.text = text;
	}
	@Override public String getText() {
		return this.text;
	}
	@Override public int getTextLength() {
		return this.text.length();
	}
	@Override public char getCharAt(int i) {
		return this.text.charAt(i);
	}

	@Override
	public void setTextColor(int color) {
		this.color = color;
	}

	@Override
	public int getTextColor() {
		return color;
	}
}