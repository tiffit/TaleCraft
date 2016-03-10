package de.longor.talecraft.blocks;

import net.minecraft.nbt.NBTTagCompound;

public interface TCIBlockCommandReceiver {
	void commandReceived(String command, NBTTagCompound data);
}
