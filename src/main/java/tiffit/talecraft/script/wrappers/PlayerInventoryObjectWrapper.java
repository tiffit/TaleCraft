package tiffit.talecraft.script.wrappers;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.script.wrappers.IObjectWrapper;
import de.longor.talecraft.script.wrappers.item.ItemStackObjectWrapper;
import net.minecraft.entity.player.InventoryPlayer;

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
		return new ItemStackObjectWrapper(inv.armorInventory[index]);
	}
	
	public int getHeldSlot(){
		return inv.currentItem;
	}

	@Override
	public List<String> getOwnPropertyNames() {
		return TaleCraft.globalScriptManager.getOwnPropertyNames(this);
	}

}
