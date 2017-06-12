package talecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import talecraft.TaleCraft;
import talecraft.network.packets.DoorPacket;

public class LockedDoorTileEntity extends TileEntity{

	public static enum DoorCorner implements IStringSerializable{
		BottomLeftX, BottomRightX, TopLeftX, TopRightX, BottomLeftZ, BottomRightZ, TopLeftZ, TopRightZ;

		@Override
		public String getName() {
			return toString().toLowerCase();
		}
		
		public boolean isZ(){
			return this.ordinal() > 3;
		}
	}
	
	public DoorCorner corner;
	public boolean isCorner;
	public boolean useSilverKey = true;
	
	public void setDoorCorner(DoorCorner corner){
		this.corner = corner;
		isCorner = corner == DoorCorner.BottomLeftX || corner == DoorCorner.BottomLeftZ;
		markDirty();
	}
	
	public void toggleKey(){
		useSilverKey = useSilverKey ? false : true;
		TaleCraft.network.sendToDimension(new DoorPacket(pos, useSilverKey), world.provider.getDimension());
		markDirty();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		corner = DoorCorner.values()[tag.getInteger("corner")];
		isCorner = tag.getBoolean("isCorner");
		useSilverKey = tag.getBoolean("useSilverKey");
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		tag.setInteger("corner", corner.ordinal());
		tag.setBoolean("isCorner", isCorner);
		tag.setBoolean("useSilverKey", useSilverKey);
		return tag;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);
		return new SPacketUpdateTileEntity(pos, 1, tag);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
		this.readFromNBT(pkt.getNbtCompound());
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		readFromNBT(tag);
	}
	

}
