package de.longor.talecraft.script.wrappers.item;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.script.wrappers.IObjectWrapper;
import de.longor.talecraft.script.wrappers.nbt.CompoundTagWrapper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemStackObjectWrapper implements IObjectWrapper {
	private ItemStack stack;

	public ItemStackObjectWrapper(ItemStack stack) {
		this.stack = stack;
	}

	@Override
	public ItemStack internal() {
		return this.stack;
	}

	@Override
	public List<String> getOwnPropertyNames() {
		return TaleCraft.globalScriptManager.getOwnPropertyNames(this);
	}

	
	public ItemObjectWrapper getItem(){
		return new ItemObjectWrapper(stack.getItem());
	}

	public String getInternalName() {
		return stack.getUnlocalizedName();
	}

	public String getName() {
		return stack.getDisplayName();
	}

	public void setName(String name) {
		stack.setStackDisplayName(name);
	}

	public int getMaxStackSize() {
		return stack.getMaxStackSize();
	}

	public int getStackSize() {
		return stack.stackSize;
	}

	public void setStackSize(int size) {
		stack.stackSize = size;
	}
	
	public void incStackSize(int amount){
		setStackSize(getStackSize() + amount);
	}

	public boolean isStackable() {
		return stack.isStackable();
	}




	public int getDamage() {
		return stack.getItemDamage();
	}

	public void setDamage(int meta) {
		stack.setItemDamage(meta);
	}

	public boolean isDamaged() {
		return stack.isItemDamaged();
	}

	public boolean isEnchanted() {
		return stack.isItemEnchanted();
	}

	public void merge(CompoundTagWrapper tagwrap) {
		NBTTagCompound tagCompound = new NBTTagCompound();
		stack.writeToNBT(tagCompound);
		tagCompound.merge(tagwrap.internal());
		stack.readFromNBT(tagCompound);
	}

}
