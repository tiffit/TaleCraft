package talecraft.tileentity;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import talecraft.TaleCraft;
import talecraft.TaleCraftSounds.SoundEnum;
import talecraft.blocks.TCTileEntity;
import talecraft.invoke.EnumTriggerState;
import talecraft.invoke.IInvoke;
import talecraft.network.packets.SoundsPacket;

public class MusicBlockTileEntity extends TCTileEntity {
	private SoundEnum sound;
	private boolean mute;
	private boolean repeat;
	private int repeat_delay;

	public MusicBlockTileEntity() {
		sound = SoundEnum.EFFECT1;
		mute = false;
		repeat = false;
		repeat_delay = 0;
	}

	@Override
	public void getInvokes(List<IInvoke> invokes) {
	}
	
	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		if(command.equals("sound")){
			sound = SoundEnum.valueOf(data.getString("sound"));
			mute = data.getBoolean("mute");
			repeat = data.getBoolean("repeat");
			repeat_delay = data.getInteger("repeat_delay");
			world.notifyBlockUpdate(this.pos, world.getBlockState(pos), world.getBlockState(pos), 0);
			return;
		}
		super.commandReceived(command, data);
		
	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 1.0f;
		color[1] = 1.0f;
		color[2] = 0.8f;
	}

	@Override
	public String getName() {
		return "MusicBlock@"+this.getPos();
	}

	@Override
	public void readFromNBT_do(NBTTagCompound comp) {
		sound = SoundEnum.values()[comp.getInteger("sound")];
		mute = comp.getBoolean("mute");
		repeat = comp.getBoolean("repeat");
		repeat_delay = comp.getInteger("repeat_delay");
	}

	@Override
	public NBTTagCompound writeToNBT_do(NBTTagCompound comp) {
		comp.setInteger("sound", sound.ordinal());
		comp.setBoolean("mute", mute);
		comp.setBoolean("repeat", repeat);
		comp.setInteger("repeat_delay", repeat_delay);
		return comp;
	}

	public SoundEnum getSound() {
		return sound;
	}
	
	public boolean isMute(){
		return mute;
	}
	
	public boolean isRepeat(){
		return repeat;
	}
	
	public int repeatDelay(){
		return repeat_delay;
	}

	public void trigger(EnumTriggerState triggerState){
		if(world.isRemote) return;
		SoundsPacket packet = null;
		if(mute){
			packet = new SoundsPacket();
		}else{
			packet = new SoundsPacket(getSound(), isRepeat(), repeatDelay(), true);
		}
		TaleCraft.network.sendToDimension(packet, world.provider.getDimension());
	}
	
	
}
