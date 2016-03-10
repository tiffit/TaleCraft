package de.longor.talecraft.client.gui.qad;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumChatFormatting;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.gui.qad.QADTextField.*;

public class QADNumberTextField extends QADTextField implements TextChangeListener {
	public static final int COLOR_OKAY = 0xFFFFFFFF;
	public static final int COLOR_FAIL = 0xFFFF7F7F;
	
	private Number valueLast;
	private NumberType valueType;
	private double MIN;
	private double MAX;
	
	public QADNumberTextField(FontRenderer FNTREN, int xPos, int yPos, int width, int height, Number value, NumberType type) {
		super(FNTREN, xPos, yPos, width, height);
		super.textChangedListener = (TextChangeListener) this;
		
		this.valueLast = value;
		this.valueType = type;
		
		super.setText(this.valueLast.toString());
		super.setTextColor(COLOR_OKAY);
		
		this.MIN = Double.MIN_VALUE/2;
		this.MAX = Double.MAX_VALUE/2;
	}
	
	public QADNumberTextField(FontRenderer FNTREN, int xPos, int yPos, int width, int height, Number value) {
		this(FNTREN, xPos, yPos, width, height, value, NumberType.INTEGER);
	}
	
	public QADNumberTextField(int xPos, int yPos, int width, int height, Number value, NumberType valueType) {
		this(Minecraft.getMinecraft().fontRendererObj, xPos, yPos, width, height, value, valueType);
	}
	
	public QADNumberTextField(Number value, NumberType valueType) {
		this(Minecraft.getMinecraft().fontRendererObj, 0, 0, 20, 20, value, valueType);
	}
	
	@Override
	public void call(QADTextField qadTextField, String text) {
		try {
			switch(valueType) {
				case DECIMAL: valueLast = Double.parseDouble(text); break;
				case INTEGER: valueLast = Long.parseLong(text); break;
				default: break;
			}
			
			if(valueLast.doubleValue() > MAX) {
				throw new NumberFormatException("VALUE is bigger than MAX!");
			}
			
			if(valueLast.doubleValue() < MIN) {
				throw new NumberFormatException("VALUE is smaller than MIN!");
			}
			
			super.setTextColor(COLOR_OKAY); // :D
		} catch (NumberFormatException ex) {
			super.setTextColor(COLOR_FAIL); // D:
		}
	}
	
	public void setRange(double min, double max) {
		MIN = min;
		MAX = max;
	}
	
	public Number getValue() {
		return valueLast;
	}
	
	public static enum NumberType {
		INTEGER, DECIMAL;
	}
}
