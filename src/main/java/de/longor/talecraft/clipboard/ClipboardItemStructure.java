package de.longor.talecraft.clipboard;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.google.common.collect.Lists;

public class ClipboardItemStructure {
	final NBTTagCompound structureRoot;
	final NBTTagList structureObjects;
	
	public ClipboardItemStructure() {
		this.structureRoot = new NBTTagCompound();
		this.structureObjects = new NBTTagList();
		this.structureRoot.setTag("objects", structureObjects);
		
		// Force the list to contain compound tags.
		this.structureObjects.appendTag(new NBTTagCompound());
		this.structureObjects.removeTag(0);
	}
	
}
