package de.longor.talecraft.client.gui.qad.model;

import java.text.NumberFormat;

import com.google.common.base.Predicate;

import de.longor.talecraft.client.gui.qad.QADTextField;
import de.longor.talecraft.client.gui.qad.QADTextField.TextFieldModel;

public final class DefaultTextFieldDecimalNumberModel implements TextFieldModel {
	private Predicate<Double> validatorPredicate = new Predicate<Double>() {
		@Override public boolean apply(Double input) {
			return true;
		}
	};
	
	private String text;
	private boolean valid;
	private double value;
	
	public DefaultTextFieldDecimalNumberModel(double value, Predicate<Double> validator) {
		this.value = value;
		this.text = Double.toString(value);
		this.validatorPredicate = validator;
		this.valid = true;
	}
	
	public DefaultTextFieldDecimalNumberModel(double value) {
		this.value = value;
		this.text = Double.toString(value);
		this.valid = true;
	}
	
	public DefaultTextFieldDecimalNumberModel() {
		this.value = 0.0;
		this.text = "0.0";
		this.valid = true;
	}
	
	@Override public void setText(String text) {
		this.text = text;
		
		double oldValue = value;
		try {
			value = Double.parseDouble(text);
			valid = true;
			
			if(!validatorPredicate.apply(Double.valueOf(value))) {
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
	
	public double getValue() {
		return value;
	}
	
	public void setValidatorPredicate(Predicate<Double> validator) {
		this.validatorPredicate = validator;
	}
	
}