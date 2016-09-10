package de.longor.talecraft.client.gui.items.voxelator;

import java.util.HashMap;

import de.longor.talecraft.client.gui.qad.QADSlider.SliderModel;
import de.longor.talecraft.voxelator.params.FloatBrushParameter;
import de.longor.talecraft.voxelator.params.IntegerBrushParameter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class FloatSliderModel implements SliderModel<Float> {

	private final float min;
	private final float max;
	private float current;
	private float value;
	private final String name;
	private NBTTagCompound tag;
	
	public FloatSliderModel(NBTTagCompound compound, FloatBrushParameter param) {
		tag = compound;
		min = param.getMinimum();
		max = param.getMaximum();
		name = param.getName();
		setValue(param.getDefault());
		if(compound.hasKey(name))current = compound.getFloat(name);
	}

	@Override
	public void setValue(Float value) {
		current = value;
		tag.setFloat(name, value);
	}

	@Override
	public Float getValue() {
		return current;
	}

	@Override
	public String getValueAsText() {
		return name + ": " + current;
	}

	@Override
	public void setSliderValue(float sliderValue) {
		this.value = sliderValue;
		setValue(Float.valueOf(ItemStack.DECIMALFORMAT.format(sliderValue*(max-min) + min)));
	}

	@Override
	public float getSliderValue() {
		return value;
	}

}
