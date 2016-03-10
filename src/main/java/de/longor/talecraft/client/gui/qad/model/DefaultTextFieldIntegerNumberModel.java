package de.longor.talecraft.client.gui.qad.model;

import java.text.NumberFormat;

import org.ietf.jgss.Oid;

import com.google.common.base.Predicate;

import de.longor.talecraft.client.gui.qad.QADTextField;
import de.longor.talecraft.client.gui.qad.QADTextField.TextFieldModel;

public final class DefaultTextFieldIntegerNumberModel implements TextFieldModel {
	private Predicate<Integer> validatorPredicate = new Predicate<Integer>() {
		@Override public boolean apply(Integer input) {
			return true;
		}
	};
	
	private String text;
	private boolean valid;
	private int value;
	
	public DefaultTextFieldIntegerNumberModel(int value) {
		this.value = value;
		this.text = Integer.toString(value);
		this.valid = validatorPredicate.apply(Integer.valueOf(value));
	}
	
	public DefaultTextFieldIntegerNumberModel() {
		this.value = 0;
		this.text = "0";
		this.valid = validatorPredicate.apply(Integer.valueOf(value));
	}
	
	@Override public void setText(String text) {
		this.text = text;
		
		int oldValue = value;
		try {
			value = Integer.parseInt(text);
			valid = true;
			
			if(!validatorPredicate.apply(Integer.valueOf(value))) {
				throw new NumberFormatException("Value did not pass validator predicate.");
			}
		} catch (NumberFormatException e) {
			value = oldValue;
			valid = false; // :(
		}
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
	
	@Override public void setTextColor(int color) {
		// nope
	}
	
	@Override public int getTextColor() {
		return valid ? 0xFFFFFFFF : 0xFFFF7070;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValidatorPredicate(Predicate<Integer> validator) {
		this.validatorPredicate = validator;
	}
	
}