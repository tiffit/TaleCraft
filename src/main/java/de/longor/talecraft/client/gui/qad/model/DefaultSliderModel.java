package de.longor.talecraft.client.gui.qad.model;

import net.minecraft.util.MathHelper;
import de.longor.talecraft.client.gui.qad.QADSlider;
import de.longor.talecraft.client.gui.qad.QADSlider.SliderModel;

public class DefaultSliderModel implements SliderModel<Integer> {
	private int maximum;
	private int current;
	private float value;
	
	public DefaultSliderModel(int initial, int maximum) {
		this.current = initial;
		this.maximum = maximum;
		this.value = (float)current / (float)maximum;
	}
	
	@Override
	public void setSliderValue(float newSliderValue) {
		value = Math.round(newSliderValue * maximum) / (float)maximum;
		current = MathHelper.clamp_int((int)(newSliderValue*maximum+0.5f), 0, maximum);
	}
	
	@Override
	public void setValue(Integer value) {
		this.current = value.intValue();
		
		if(current < 0) current = 0;
		if(current > maximum) current = maximum;
	}
	
	@Override
	public Integer getValue() {
		return Integer.valueOf(current);
	}
	
	@Override
	public String getValueAsText() {
		return Integer.toString(current);
	}
	
	public float getSliderValue() {
		return value;
	}
}