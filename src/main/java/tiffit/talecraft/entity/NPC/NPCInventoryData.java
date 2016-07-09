package tiffit.talecraft.entity.NPC;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockPumpkin;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.EntityEquipmentSlot.Type;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NPCInventoryData {

	private ItemStack mainHand;
	private ItemStack offHand;
	private ItemStack helmet;
	private ItemStack chestplate;
	private ItemStack leggings;
	private ItemStack boots;
	
	public ItemStack getItem(EntityEquipmentSlot slot){
		switch (slot){
		case CHEST:
			return chestplate;
		case FEET:
			return boots;
		case HEAD:
			return helmet;
		case LEGS:
			return leggings;
		case MAINHAND:
			return mainHand;
		case OFFHAND:
			return offHand;
		default:
			return null;
        }
	}
	
	public void setItem(EntityEquipmentSlot slot, ItemStack item){
		switch (slot){
		case CHEST:
			chestplate = item;
			return;
		case FEET:
			boots = item;
			return;
		case HEAD:
			helmet = item;
			return;
		case LEGS:
			leggings = item;
			return;
		case MAINHAND:
			mainHand = item;
			return;
		case OFFHAND:
			offHand = item;
			return;
        }
	}
	
	public NBTTagCompound toNBT(){
		NBTTagCompound tag = new NBTTagCompound();
		for(EntityEquipmentSlot slot : EntityEquipmentSlot.values()){
			if(getItem(slot) !=  null) tag.setTag(slot.toString(), getItem(slot).writeToNBT(new NBTTagCompound()));
		}
		return tag;
	}
	
	public static NPCInventoryData fromNBT(NBTTagCompound tag){
		NPCInventoryData data = new NPCInventoryData();
		for(EntityEquipmentSlot slot : EntityEquipmentSlot.values()){
			if(tag.hasKey(slot.toString())) data.setItem(slot, ItemStack.loadItemStackFromNBT(tag.getCompoundTag(slot.toString())));
		}
		return data;
	}
	
	
	
	public static List<ItemStack> getAcceptableItems(EntityEquipmentSlot slot){
		List<ItemStack> items = Lists.newArrayList();
		for(Item item : Item.REGISTRY){
			if(isAcceptable(slot, item)) items.add(new ItemStack(item));
		}
		return items;
	}
	
	public static boolean isAcceptable(EntityEquipmentSlot slot, Item item){
		if(slot.getSlotType() == Type.ARMOR){
			if(item instanceof ItemArmor){
				ItemArmor armor = (ItemArmor) item;
				if(armor.armorType == slot) return true;
				else if(slot == EntityEquipmentSlot.HEAD && item instanceof ItemBlock) return true;
				else return false;
			}else return false;
		}else{
			return true;
		}
	}
}
