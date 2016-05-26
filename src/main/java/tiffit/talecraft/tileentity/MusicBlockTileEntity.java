package tiffit.talecraft.tileentity;

import java.util.List;

import de.longor.talecraft.TCSoundHandler.SoundEnum;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.TCTileEntity;
import de.longor.talecraft.invoke.EnumTriggerState;
import de.longor.talecraft.invoke.IInvoke;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import tiffit.talecraft.packet.SoundsMutePacket;

public class MusicBlockTileEntity extends TCTileEntity {
	private SoundEnum sound;
	private boolean mute;

	public MusicBlockTileEntity() {
		sound = SoundEnum.EFFECT1;
		mute = false;
	}

	@Override
	public void getInvokes(List<IInvoke> invokes) {
	}
	
	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		if(command.equals("sound")){
			sound = SoundEnum.valueOf(data.getString("sound"));
			mute = data.getBoolean("mute");
			worldObj.notifyBlockUpdate(this.pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 0);
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
	}

	@Override
	public void writeToNBT_do(NBTTagCompound comp) {
		comp.setInteger("sound", sound.ordinal());
		comp.setBoolean("mute", mute);
	}

	public SoundEnum getSound() {
		return sound;
	}
	
	public boolean isMute(){
		return mute;
	}

	public void trigger(EnumTriggerState triggerState){
		if(mute){
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();
			TaleCraft.network.sendToAllAround(new SoundsMutePacket(), new TargetPoint(worldObj.provider.getDimension(), x, y, z, 16D));
		}else{
			this.worldObj.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), sound.getSoundEvent(), SoundCategory.MASTER, 1F, 1F);
		}
	}
	
	
}
