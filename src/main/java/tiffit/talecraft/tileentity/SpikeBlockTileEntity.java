package tiffit.talecraft.tileentity;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.JsonParseException;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.TCTileEntity;
import de.longor.talecraft.invoke.BlockTriggerInvoke;
import de.longor.talecraft.invoke.EnumTriggerState;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.invoke.Invoke;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import tiffit.talecraft.packet.DoorPacket;
import tiffit.talecraft.packet.SpikePacket;

public class SpikeBlockTileEntity extends TCTileEntity {
	private boolean active;
	private float damage;

	public SpikeBlockTileEntity() {
		active = false;
		damage = 5f;
	}

	@Override
	public void update(){
		
	}
	
	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 0;
	}
	
	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		if(command.equals("trigger")) {
			active = true;
			TaleCraft.network.sendToDimension(new SpikePacket(pos, active), worldObj.provider.getDimension());
			markDirty();
			return;
		}
		super.commandReceived(command, data);
	}

	@Override
	public void getInvokes(List<IInvoke> invokes) {
		// none
	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 0.90f;
		color[1] = 0.85f;
		color[2] = 0.50f;
	}

	@Override
	public String getName() {
		return "SpikeBlock@"+this.getPos();
	}

	@Override
	public void readFromNBT_do(NBTTagCompound comp) {
		this.active = comp.getBoolean("active");
	}

	@Override
	public void writeToNBT_do(NBTTagCompound comp) {
		comp.setBoolean("active", active);
	}

	public boolean getActive(){
		return active;
	}
	
	public void setActive(boolean bool){
		active = bool;
		if(!this.worldObj.isRemote){
			TaleCraft.network.sendToDimension(new SpikePacket(pos, active), worldObj.provider.getDimension());
			markDirty();
		}
	}
	
	public float getDamage(){
		return damage;
	}

}
