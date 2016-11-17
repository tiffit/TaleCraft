package talecraft.client.gui.items.voxelator;

import net.minecraft.nbt.NBTTagCompound;
import talecraft.client.gui.qad.QADSlider.SliderModel;
import talecraft.voxelator.params.IntegerBrushParameter;

public class IntegerSliderModel implements SliderModel<Integer> {

	private final int min;
	private final int max;
	private int current;
	private float value;
	private final String name;
	private NBTTagCompound tag;
	
	public IntegerSliderModel(NBTTagCompound compound, IntegerBrushParameter param) {
		this.tag = compound;
		min = param.getMinimum();
		max = param.getMaximum();
		name = param.getName();
		setValue(param.getDefault());
		if(compound.hasKey(name))current = compound.getInteger(name);
	}

	@Override
	public void setValue(Integer value) {
		current = value;
		tag.setInteger(name, value);
	}

	@Override
	public Integer getValue() {
		return current;
	}

	@Override
	public String getValueAsText() {
		return name + ": " + current;
	}

	@Override
	public void setSliderValue(float sliderValue) {
		this.value = sliderValue;
		setValue((int)(sliderValue*(max+min) + min));
	}

	@Override
	public float getSliderValue() {
		return value;
	}

}
