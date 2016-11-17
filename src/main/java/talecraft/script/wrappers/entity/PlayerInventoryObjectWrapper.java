package talecraft.script.wrappers.entity;

import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;
import talecraft.TaleCraft;
import talecraft.script.wrappers.IObjectWrapper;
import talecraft.script.wrappers.item.ItemStackObjectWrapper;

public class PlayerInventoryObjectWrapper implements IObjectWrapper{

	InventoryPlayer inv;
	
	public PlayerInventoryObjectWrapper(InventoryPlayer inv){
		this.inv = inv;
	}
	
	@Override
	public Object internal() {
		return inv;
	}
	
	public boolean isHeldNull(){
		return inv.getCurrentItem() == null;
	}
	
	public void clearHeldItem(){
		inv.setInventorySlotContents(inv.currentItem, null);
	}
	
	public ItemStackObjectWrapper getItemAt(int index){
		return new ItemStackObjectWrapper(inv.getStackInSlot(index));
	}
	
	public ItemStackObjectWrapper[] getOffHandInventory(){
		return ItemStackObjectWrapper.createArray(inv.offHandInventory);
	}
	
	public ItemStackObjectWrapper[] getMainInventory(){
		return ItemStackObjectWrapper.createArray(inv.mainInventory);
	}
	
	public ItemStackObjectWrapper[] getArmorInventory(){
		return ItemStackObjectWrapper.createArray(inv.armorInventory);
	}
	
	public void setCurrentItem(int slot){
		inv.currentItem = slot;
	}
	
	public int getHeldSlot(){
		return inv.currentItem;
	}
	
	public int getFirstEmptySlot(){
		return inv.getFirstEmptyStack();
	}

	@Override
	public List<String> getOwnPropertyNames() {
		return TaleCraft.globalScriptManager.getOwnPropertyNames(this);
	}

}
