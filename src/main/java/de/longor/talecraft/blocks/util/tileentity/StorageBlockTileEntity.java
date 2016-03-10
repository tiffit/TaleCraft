package de.longor.talecraft.blocks.util.tileentity;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftTabs;
import de.longor.talecraft.blocks.TCTileEntity;
import de.longor.talecraft.clipboard.ClipboardItem;
import de.longor.talecraft.invoke.IInvoke;

public class StorageBlockTileEntity extends TCTileEntity {
	ClipboardItem item;
	int[] bounds;
	
	@Override
	public void getInvokes(List<IInvoke> invokes) {
		// has none
	}
	
	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 0.25f;
		color[1] = 1.00f;
		color[2] = 0.75f;
	}
	
	@Override
	public String getName() {
		return "StorageBlock@"+pos;
	}
	
	public int[] getBounds() {
		return bounds;
	}
	
	public ClipboardItem getClipboard() {
		return item;
	}
	
	@Override
	public void init() {
		
	}
    
	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		if(command.equals("set")) {
			int[] bounds = data.getIntArray("bounds");
			
			this.bounds = new int[6];
			
			// min correction
			this.bounds[0] = Math.min(bounds[0], bounds[3]);
			this.bounds[1] = Math.min(bounds[1], bounds[4]);
			this.bounds[2] = Math.min(bounds[2], bounds[5]);
			
			// max correction
			this.bounds[3] = Math.max(bounds[0], bounds[3]);
			this.bounds[4] = Math.max(bounds[1], bounds[4]);
			this.bounds[5] = Math.max(bounds[2], bounds[5]);
			
			this.item = ClipboardItem.copyRegion(bounds, worldObj, getName(), this);
			worldObj.markBlockForUpdate(getPos());
			return;
		}
		
		if(command.equals("store")) {
			this.item = ClipboardItem.copyRegion(bounds, worldObj, getName(), this);
			worldObj.markBlockForUpdate(getPos());
			return;
		}
		
		if(command.equals("trigger")) {
			if(item == null) {
				TaleCraft.logger.error(getName() + this + "# does not have CLIPBOARD: " + item);
			}
			if(bounds == null) {
				TaleCraft.logger.error(getName() + this + "# does not have BOUNDS: " + bounds);
			}
			
			if(item != null && bounds != null) {
				BlockPos pos = new BlockPos(bounds[0], bounds[1], bounds[2]);
				ClipboardItem.pasteRegion(item, pos, worldObj, this);
			}
			return;
		}
		
		super.commandReceived(command, data);
	}
	
	@Override
	public void readFromNBT_do(NBTTagCompound comp) {
		if(comp.hasKey("clipboard", comp.getId())) {
			item = ClipboardItem.fromNBT(comp.getCompoundTag("clipboard"));
			bounds = comp.getIntArray("bounds");
		}
	}
	
	@Override
	public void writeToNBT_do(NBTTagCompound comp) {
		if(item != null && item.getData() != null) {
			comp.setTag("clipboard", item.getData());
			comp.setIntArray("bounds", bounds);
		}
	}
	
}
