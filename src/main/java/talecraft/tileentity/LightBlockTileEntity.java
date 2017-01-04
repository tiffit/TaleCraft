package talecraft.tileentity;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import talecraft.blocks.TCTileEntity;
import talecraft.invoke.IInvoke;

public class LightBlockTileEntity extends TCTileEntity {
	int lightValue;
	boolean lightActive;

	public LightBlockTileEntity() {
		this.lightValue = 15;
		this.lightActive = true;
	}

	public int getLightValue() {
		return lightActive ? lightValue : 0;
	}

	public void setLightValue(int value) {
		lightValue = value < 15 ? (value > 0 ? value : 0) : 15;
		world.notifyBlockUpdate(this.pos, world.getBlockState(pos), world.getBlockState(pos), 0); //TODO Confirm
	}

	public void setLightActive(boolean flag) {
		if(lightActive != flag) {
			lightActive = flag;
			world.notifyBlockUpdate(this.pos, world.getBlockState(pos), world.getBlockState(pos), 0); //TODO Confirm
		}
	}

	public void toggleLightActive() {
		lightActive ^= true;
		world.notifyBlockUpdate(this.pos, world.getBlockState(pos), world.getBlockState(pos), 0); //TODO Confirm
	}

	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		if(command.equals("toggle") || command.equals("trigger")) {
			setLightActive(!lightActive);
			return;
		}

		if(command.equals("on")) {
			setLightActive(true);
			return;
		}

		if(command.equals("off")) {
			setLightActive(false);
			return;
		}

		if(command.equals("set")) {
			setLightValue(data.getInteger("lightValue"));
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
		color[0] = 1f;
		color[1] = 1f;
		color[2] = 0.75f;
	}

	@Override
	public String getName() {
		return "LightBlock@"+getPos();
	}

	@Override
	public void readFromNBT_do(NBTTagCompound comp) {
		lightValue = comp.getInteger("lightValue");
		lightActive = comp.getBoolean("lightActive");

		if(world != null)
			world.checkLight(getPos());
	}

	@Override
	public NBTTagCompound writeToNBT_do(NBTTagCompound comp) {
		comp.setInteger("lightValue", lightValue);
		comp.setBoolean("lightActive", lightActive);
		return comp;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared() {
		return 128; // reduced render distance, as light blocks may be placed in VERY large amounts.
	}

}
