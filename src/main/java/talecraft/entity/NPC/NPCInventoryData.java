package talecraft.entity.NPC;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

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
	
	private List<NPCDrop> drops;
	
	public static class NPCDrop{
		
		public ItemStack stack;
		public float chance;
		
		public NPCDrop(ItemStack stack, float chance){
			this.stack = stack;
			this.chance = chance;
		}
		
		public NBTTagCompound toNBT(){
			NBTTagCompound tag = new NBTTagCompound();
			tag.setTag("stack", stack.writeToNBT(new NBTTagCompound()));
			tag.setFloat("chance", chance);
			return tag;
		}
		
		public static NPCDrop fromNBT(NBTTagCompound tag){
			return new NPCDrop(new ItemStack(tag.getCompoundTag("stack")), tag.getFloat("chance"));
		}
	}
	
	public NPCInventoryData(){
		drops = Lists.newArrayList();
	}
	
	public List<NPCDrop> getDrops(){
		return drops;
	}
	
	public int getDropsSize(){
		return drops.size();
	}
	
	public NPCDrop getDrop(int index){
		return drops.get(index);
	}
	
	public void addDrop(NPCDrop drop){
		drops.add(drop);
	}
	
	public void removeDrop(int index){
		drops.remove(index);
	}
	
	public void removeDrop(NPCDrop stack){
		drops.remove(stack);
	}
	
	public void clearDrops(){
		drops.clear();
	}
	
	public void setDrops(List<NPCDrop> drops){
		this.drops = drops;
	}
	
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
		NBTTagCompound drops = new NBTTagCompound();
		int drop_size = this.drops.size();
		drops.setInteger("size", drop_size);
		for(int i = 0; i < drop_size; i++){
			drops.setTag("drop_" + i, this.drops.get(i).toNBT());
		}
		tag.setTag("drops", drops);
		return tag;
	}
	
	public static NPCInventoryData fromNBT(NBTTagCompound tag){
		NPCInventoryData data = new NPCInventoryData();
		for(EntityEquipmentSlot slot : EntityEquipmentSlot.values()){
			if(tag.hasKey(slot.toString())) data.setItem(slot, new ItemStack(tag.getCompoundTag(slot.toString())));
		}
		NBTTagCompound drops = tag.getCompoundTag("drops");
		for(int i = 0; i < drops.getInteger("size"); i++){
			data.drops.add(NPCDrop.fromNBT(drops.getCompoundTag("drop_" + i)));
		}
		return data;
	}
	
	
	
	public static List<ItemStack> getAcceptableItems(EntityEquipmentSlot slot){
		List<ItemStack> items = Lists.newArrayList();
		
		Iterator<Item> iter = Item.REGISTRY.iterator();
		while(iter.hasNext()) {
			Item item = iter.next();
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
