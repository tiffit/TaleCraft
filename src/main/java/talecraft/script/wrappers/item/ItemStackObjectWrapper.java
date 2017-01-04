package talecraft.script.wrappers.item;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import talecraft.TaleCraft;
import talecraft.script.wrappers.IObjectWrapper;
import talecraft.script.wrappers.nbt.CompoundTagWrapper;

public class ItemStackObjectWrapper implements IObjectWrapper {
	private ItemStack stack;

	public ItemStackObjectWrapper(ItemStack stack) {
		this.stack = stack;
	}
	
	public static ItemStackObjectWrapper[] createArray(NonNullList<ItemStack> stacks){
		ItemStackObjectWrapper[] newList = new ItemStackObjectWrapper[stacks.size()];
		for(int i = 0; i < stacks.size(); i++){
			newList[i] = new ItemStackObjectWrapper(stacks.get(i));
		}
		return newList;
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
		return stack.getCount();
	}

	public void setStackSize(int size) {
		stack.setCount(size);
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
		stack = new ItemStack(tagCompound);
	}

	public CompoundTagWrapper getNBT(){
		return new CompoundTagWrapper(stack.getTagCompound());
	}
	
	public boolean hasNBT(){
		return stack.hasTagCompound();
	}
	
	public void setNBT(CompoundTagWrapper nbt){
		stack.setTagCompound(nbt.internal());
	}
	
}
