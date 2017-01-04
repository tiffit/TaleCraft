package talecraft.tileentity;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import talecraft.TaleCraft;
import talecraft.blocks.TCTileEntity;
import talecraft.invoke.IInvoke;
import talecraft.util.GObjectTypeHelper;

public class EmitterBlockTileEntity extends TCTileEntity{
	private static final int[] NULL_PARAMS = new int[8];
	NBTTagCompound emitterData;
	boolean state = false;

	public EmitterBlockTileEntity() {
		// setup default data
		emitterData = new NBTTagCompound();
		emitterData.setInteger("var_spawnCount", 1);
		emitterData.setString("var_type", "heart");
		emitterData.setFloat("var_offsetX", 0);
		emitterData.setFloat("var_offsetY", 0);
		emitterData.setFloat("var_offsetZ", 0);
		emitterData.setFloat("var_offsetRandX", 1.5f);
		emitterData.setFloat("var_offsetRandY", 1.5f);
		emitterData.setFloat("var_offsetRandZ", 1.5f);
		emitterData.setFloat("var_velocityX", 0f);
		emitterData.setFloat("var_velocityY", 0f);
		emitterData.setFloat("var_velocityZ", 0f);
		emitterData.setFloat("var_velocityRandX", 0f);
		emitterData.setFloat("var_velocityRandY", 0f);
		emitterData.setFloat("var_velocityRandZ", 0f);

		// and turn it on! :D
		state = true;
	}

	@Override
	public void getInvokes(List<IInvoke> invokes) {
		// none
	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 0.5f;
		color[1] = 0.125f;
		color[2] = 0f;
	}

	@Override
	public String getName() {
		return "EmitterBlock@" + getPos();
	}

	@Override
	public void init() {

	}

	@Override
	public void readFromNBT_do(NBTTagCompound comp) {
		emitterData = comp.getCompoundTag("emitter");
		state = comp.getBoolean("active");
	}

	@Override
	public NBTTagCompound writeToNBT_do(NBTTagCompound comp) {
		if(emitterData != null)
			comp.setTag("emitter", emitterData);

		comp.setBoolean("active", state);
		return comp;
	}

	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		if(command.equals("trigger")) {
			// toggle state
			state ^= true;
			world.notifyBlockUpdate(this.pos, world.getBlockState(pos), world.getBlockState(pos), 0); //TODO Confirm
			return;
		}

		if(command.equals("set_vars")) {
			data.removeTag("command");

			if(emitterData == null)
				emitterData = new NBTTagCompound();

			emitterData.merge(data);
			world.notifyBlockUpdate(this.pos, world.getBlockState(pos), world.getBlockState(pos), 0);
			return;
		}

		super.commandReceived(command, data);
	}

	@Override
	public void update() {
		super.update();
		if(emitterData == null) return;
		if(!world.isRemote) return;
		if(!state) return;

		float var_offsetX = emitterData.getFloat("var_offsetX");
		float var_offsetY = emitterData.getFloat("var_offsetY");
		float var_offsetZ = emitterData.getFloat("var_offsetZ");
		float var_offsetRandX = emitterData.getFloat("var_offsetRandX");
		float var_offsetRandY = emitterData.getFloat("var_offsetRandY");
		float var_offsetRandZ = emitterData.getFloat("var_offsetRandZ");
		float var_velocityX = emitterData.getFloat("var_velocityX");
		float var_velocityY = emitterData.getFloat("var_velocityY");
		float var_velocityZ = emitterData.getFloat("var_velocityZ");
		float var_velocityRandX = emitterData.getFloat("var_velocityRandX");
		float var_velocityRandY = emitterData.getFloat("var_velocityRandY");
		float var_velocityRandZ = emitterData.getFloat("var_velocityRandZ");

		int spawnCount = emitterData.getInteger("var_spawnCount");

		if(spawnCount < 1)
			spawnCount = 1;

		if(Boolean.FALSE.booleanValue()) {
			// For testing
			var_offsetRandX = 16;
			var_offsetRandZ = 16;
			spawnCount = 100;

			var_velocityY = 1;
		}

		EnumParticleTypes particleType = null;

		try {
			particleType = GObjectTypeHelper.findParticleType(emitterData.getString("var_type"));
		} catch (IllegalArgumentException e) {
			particleType = EnumParticleTypes.CLOUD;
		}

		int[] params = null;

		if(emitterData.hasKey("params")) {
			params = emitterData.getIntArray("params");
		} else {
			params = NULL_PARAMS;
		}


		for(int i = 0; i < spawnCount; i++) {
			float xCoord = getPos().getX() + 0.5f + var_offsetX + ((TaleCraft.random.nextFloat()*2-1)*var_offsetRandX);
			float yCoord = getPos().getY() + 0.5f + var_offsetY + ((TaleCraft.random.nextFloat()*2-1)*var_offsetRandY);
			float zCoord = getPos().getZ() + 0.5f + var_offsetZ + ((TaleCraft.random.nextFloat()*2-1)*var_offsetRandZ);
			float xVelocity = var_velocityRandX == 0 ? var_velocityX : (var_velocityX*var_velocityRandX);
			float yVelocity = var_velocityRandY == 0 ? var_velocityY : (var_velocityY*var_velocityRandY);
			float zVelocity = var_velocityRandZ == 0 ? var_velocityZ : (var_velocityZ*var_velocityRandZ);

			world.spawnParticle(particleType, xCoord, yCoord, zCoord, xVelocity, yVelocity, zVelocity, params);
		}
	}

	public float getOffsetX() {
		return emitterData != null ? emitterData.getFloat("var_offsetX") : 0f;
	}

	public float getOffsetY() {
		return emitterData != null ? emitterData.getFloat("var_offsetY") : 0f;
	}

	public float getOffsetZ() {
		return emitterData != null ? emitterData.getFloat("var_offsetZ") : 0f;
	}

	public float getOffsetRandX() {
		return emitterData != null ? emitterData.getFloat("var_offsetRandX") : 0f;
	}

	public float getOffsetRandY() {
		return emitterData != null ? emitterData.getFloat("var_offsetRandY") : 0f;
	}

	public float getOffsetRandZ() {
		return emitterData != null ? emitterData.getFloat("var_offsetRandZ") : 0f;
	}

	public float getVelocityX() {
		return emitterData != null ? emitterData.getFloat("var_velocityX") : 0f;
	}

	public float getVelocityY() {
		return emitterData != null ? emitterData.getFloat("var_velocityY") : 0f;
	}

	public float getVelocityZ() {
		return emitterData != null ? emitterData.getFloat("var_velocityZ") : 0f;
	}

	public float getVelocityRandX() {
		return emitterData != null ? emitterData.getFloat("var_velocityRandX") : 0f;
	}

	public float getVelocityRandY() {
		return emitterData != null ? emitterData.getFloat("var_velocityRandY") : 0f;
	}

	public float getVelocityRandZ() {
		return emitterData != null ? emitterData.getFloat("var_velocityRandZ") : 0f;
	}

	public int getSpawnCount() {
		return emitterData != null ? emitterData.getInteger("var_spawnCount") : 1;
	}

	public String getParticleType() {
		return emitterData != null ? emitterData.getString("var_type") : EnumParticleTypes.CLOUD.name();
	}

	public void setActive(boolean onoff) {
		this.state = onoff;
		this.world.notifyBlockUpdate(this.pos, world.getBlockState(pos), world.getBlockState(pos), 0); //TODO Confirm
	}

	public void toggleActive() {
		this.state ^= true;
		this.world.notifyBlockUpdate(this.pos, world.getBlockState(pos), world.getBlockState(pos), 0); //TODO Confirm
	}

}
