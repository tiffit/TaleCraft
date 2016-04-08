package tiffit.talecraft.tileentity;

import java.util.List;

import de.longor.talecraft.blocks.TCTileEntity;
import de.longor.talecraft.invoke.BlockTriggerInvoke;
import de.longor.talecraft.invoke.EnumTriggerState;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.invoke.Invoke;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class SpikeBlockTileEntity extends TCTileEntity {
	IInvoke activateInvoke;
	IInvoke deactivateInvoke;

	public boolean active;

	public float damage;

	public SpikeBlockTileEntity() {
		activateInvoke = BlockTriggerInvoke.ZEROINSTANCE;
		deactivateInvoke = BlockTriggerInvoke.ZEROINSTANCE;

		active = false;
		damage = 3.5f;
	}

	@Override
	public void init() {
	}

	@Override
	public void writeToNBT_do(NBTTagCompound compound){
		compound.setBoolean("active", active);
		compound.setFloat("damage", damage);
		compound.setTag("activateInvoke", IInvoke.Serializer.write(activateInvoke));
		compound.setTag("deactivateInvoke", IInvoke.Serializer.write(deactivateInvoke));
	}

	@Override
	public void readFromNBT_do(NBTTagCompound compound) {
		active = compound.getBoolean("active");
		damage = compound.getFloat("damage");

		activateInvoke = IInvoke.Serializer.read(compound.getCompoundTag("activateInvoke"));
		deactivateInvoke = IInvoke.Serializer.read(compound.getCompoundTag("deactivateInvoke"));
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		NBTTagCompound comp = pkt.getNbtCompound();
		readFromNBT_do(comp);
	}

	@Override
	public Packet<?> getDescriptionPacket() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		this.writeToNBT(nbttagcompound);
		return new SPacketUpdateTileEntity(this.pos, 3, nbttagcompound);
	}
	
	public void activate(){
		active = true;
		Invoke.invoke(activateInvoke, this, null, EnumTriggerState.ON);
	}
	
	public void deactivate(){
		active = false;
		Invoke.invoke(deactivateInvoke, this, null, EnumTriggerState.OFF);
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void commandReceived(String command, NBTTagCompound data) {

		if("activate".equals(command)) {
			activate();
		}

		if("deactivate".equals(command)) {
			deactivate();
		}

		worldObj.notifyBlockUpdate(this.pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 0); //TODO Confirm

		super.commandReceived(command, data);
	}

	public String getStateAsString() {
		return "[" + active + " | " + active + ", " + damage+"]";
	}

	public boolean isClockRunning() {
		return active;
	}

	@Override
	public String getName() {
		return "ClockBlock@"+pos;
	}

	@Override
	public void getInvokes(List<IInvoke> invokes) {
		invokes.add(activateInvoke);
		invokes.add(deactivateInvoke);
	}

	public IInvoke getStartInvoke() {
		return activateInvoke;
	}

	public IInvoke getStopInvoke() {
		return deactivateInvoke;
	}

	//	@Override
	//	public void getInvokesAsDataCompounds(List<NBTTagCompound> invokes) {
	//		invokes.add(clockInvoke);
	//	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 0.5f;
		color[1] = 0.1f;
	}

}
