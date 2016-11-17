package talecraft.clipboard;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

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
