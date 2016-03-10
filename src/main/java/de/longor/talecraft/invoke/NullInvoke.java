package de.longor.talecraft.invoke;

import net.minecraft.nbt.NBTTagCompound;

public class NullInvoke implements IInvoke {
	public static final String TYPE = "NullInvoke";
	public static final NullInvoke instance = new NullInvoke();
	
	private NullInvoke() {
		// private stub constructor
	}
	
	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public void getColor(float[] color_out) {
		color_out[0] = 0.1f;
		color_out[1] = 0.1f;
		color_out[2] = 0.1f;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		
	}
	
}
