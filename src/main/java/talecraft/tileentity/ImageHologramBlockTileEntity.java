package talecraft.tileentity;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import talecraft.blocks.TCTileEntity;
import talecraft.invoke.IInvoke;

public class ImageHologramBlockTileEntity extends TCTileEntity {
	NBTTagCompound holodata;
	boolean state;

	public ImageHologramBlockTileEntity() {
		state = true;
		holodata = new NBTTagCompound();
	}

	@Override
	public void getInvokes(List<IInvoke> invokes) {
		// none
	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 0;
		color[1] = 0.25f;
		color[2] = 0;
	}

	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		if(command.equals("trigger")) {
			state ^= true;
			world.notifyBlockUpdate(this.pos, world.getBlockState(pos), world.getBlockState(pos), 0); //TODO Confirm
			return;
		}

		if(command.equals("set_vars")) {
			data.removeTag("command");
			holodata.merge(data);
			world.notifyBlockUpdate(this.pos, world.getBlockState(pos), world.getBlockState(pos), 0); //TODO Confirm
			return;
		}

		super.commandReceived(command, data);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldRenderInPass(int pass) {
		return pass == 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return getHologramOffsetRelative()
			? new AxisAlignedBB(getPos().add(-999999999, -999999999, -999999999), getPos().add(999999999, 999999999, 999999999))
			: new AxisAlignedBB(getPos().add(-512, -512, -512), getPos().add(512, 512, 512));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared() {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public String getName() {
		return "ImageHologramBlock@"+getPos();
	}

	@Override
	public void init() {

	}

	@Override
	public void readFromNBT_do(NBTTagCompound comp) {
		holodata = comp.getCompoundTag("holodata");
		state = comp.getBoolean("state");
	}

	@Override
	public NBTTagCompound writeToNBT_do(NBTTagCompound comp) {
		comp.setTag("holodata", holodata);
		comp.setBoolean("state", state);
		return comp;
	}

	public void toggleActive() {
		state ^= true;
		this.world.notifyBlockUpdate(this.pos, world.getBlockState(pos), world.getBlockState(pos), 0); //TODO Confirm
	}

	public void setActive(boolean active) {
		state = active;
		this.world.notifyBlockUpdate(this.pos, world.getBlockState(pos), world.getBlockState(pos), 0); //TODO Confirm
	}

	public boolean isActive() {
		return state;
	}

	public String getTextureLocation() {
		return holodata.hasKey("var_texture") ? holodata.getString("var_texture") : "missingno";
	}

	public float getHologramOffsetX() {
		return holodata.hasKey("var_offsetX") ? holodata.getFloat("var_offsetX") : 0;
	}

	public float getHologramOffsetY() {
		return holodata.hasKey("var_offsetY") ? holodata.getFloat("var_offsetY") : 0;
	}

	public float getHologramOffsetZ() {
		return holodata.hasKey("var_offsetZ") ? holodata.getFloat("var_offsetZ") : 0;
	}

	public boolean getHologramOffsetRelative() {
		return holodata.hasKey("var_offsetRelative") ? holodata.getBoolean("var_offsetRelative") : false;
	}

	public float getHologramWidth() {
		return holodata.hasKey("var_width") ? holodata.getFloat("var_width") : 1;
	}

	public float getHologramHeight() {
		return holodata.hasKey("var_height") ? holodata.getFloat("var_height") : 1;
	}

	public float getHologramPitch() {
		return holodata.hasKey("var_pitch") ? holodata.getFloat("var_pitch") : 0;
	}

	public float getHologramYaw() {
		return holodata.hasKey("var_yaw") ? holodata.getFloat("var_yaw") : 0;
	}

	public float getHologramUscale() {
		return holodata.hasKey("var_uscale") ? holodata.getFloat("var_uscale") : 1f;
	}

	public float getHologramVscale() {
		return holodata.hasKey("var_vscale") ? holodata.getFloat("var_vscale") : 1f;
	}

	public int getHologramColor() {
		if(!holodata.hasKey("var_color")) {
			return 0xFFFFFFFF;
		}
		return holodata.getInteger("var_color");
	}

}
