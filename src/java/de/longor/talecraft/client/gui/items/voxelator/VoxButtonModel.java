package de.longor.talecraft.client.gui.items.voxelator;

import de.longor.talecraft.client.gui.qad.QADButton.ButtonModel;
import de.longor.talecraft.voxelator.params.BooleanBrushParameter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class VoxButtonModel implements ButtonModel {

	private boolean current;
	private final String name;
	private final NBTTagCompound tag;
	
	public VoxButtonModel(NBTTagCompound compound, BooleanBrushParameter param) {
		name = param.getName();
		if(compound.hasKey(name))current = compound.getBoolean(name);
		this.tag = compound;
	}

	@Override
	public void onClick() {
		current = !current;
		tag.setBoolean(name, current);
	}

	@Override
	public String getText() {
		return name + ": " + (current ? "true" : "false");
	}

	@Override
	public ResourceLocation getIcon() {return null;}

	@Override
	public void setText(String newText) {}

	@Override
	public void setIcon(ResourceLocation newIcon) {}

}
