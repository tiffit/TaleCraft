package talecraft.script.wrappers.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import talecraft.TaleCraft;
import talecraft.script.wrappers.IObjectWrapper;
import talecraft.script.wrappers.nbt.CompoundTagWrapper;
import talecraft.util.Vec3f;
import talecraft.util.Vec3i;

public class EntityObjectWrapper implements IObjectWrapper {
	private Entity entity;

	public EntityObjectWrapper(Entity entity) {
		this.entity = entity;
	}

	@Override
	public Entity internal() {
		return entity;
	}

	@Override
	public List<String> getOwnPropertyNames() {
		return TaleCraft.globalScriptManager.getOwnPropertyNames(this);
	}

	public String getName() {
		return entity.getName();
	}

	public Vec3f getPosition() {
		return new Vec3f(entity.getPositionVector());
	}

	public Vec3i getBlockPosition() {
		return new Vec3i(entity.getPosition());
	}

	public void setPosition(BlockPos blockPos) {
		double x = blockPos.getX()+0.5;
		double y = blockPos.getY();
		double z = blockPos.getZ()+0.5;
		entity.setPositionAndUpdate(x, y, z);
	}

	public void setPositionAndRotation(BlockPos blockPos, double yaw, double pitch) {
		double x = blockPos.getX()+0.5;
		double y = blockPos.getY();
		double z = blockPos.getZ()+0.5;
		entity.setPositionAndRotation(x, y, z, (float)yaw, (float)pitch);
	}

	public void setPosition(double x, double y, double z) {
		entity.setPositionAndUpdate(x, y, z);
	}

	public void setPositionAndRotation(double x, double y, double z, double yaw, double pitch) {
		entity.setPositionAndRotation(x, y, z, (float)yaw, (float)pitch);
	}

	public void setRotation(float yaw, float pitch) {
		entity.rotationYaw = yaw % 360.0F;
		entity.rotationPitch = pitch % 360.0F;
	}

	public float getYaw() {
		return entity.rotationYaw;
	}

	public float getPitch() {
		return entity.rotationPitch;
	}

	public void playSound(SoundEvent sound, float volume, float pitch) {
		entity.playSound(sound, volume, pitch);
	}

	public void playSound(SoundEvent sound, float volume) {
		entity.playSound(sound, volume, 1f);
	}

	public void playSound(SoundEvent sound) {
		entity.playSound(sound, 1f, 1f);
	}

	public void addVelocity(float x, float y, float z) {
		entity.addVelocity(x, y, z);
		entity.velocityChanged = true;
	}

	public void setVelocity(float x, float y, float z) {
		entity.motionX = x;
		entity.motionY = y;
		entity.motionZ = z;
		entity.velocityChanged = true;
	}

	public void setAlwaysRenderNameTag(boolean flag) {
		entity.setAlwaysRenderNameTag(flag);
	}

	public void setOnFire(int seconds) {
		entity.setFire(seconds);
	}

	public void setInvisible(boolean flag) {
		entity.setInvisible(flag);
	}

	public void setSilent(boolean flag) {
		entity.setSilent(flag);
	}

	public void mount(EntityObjectWrapper rider) {
		entity.startRiding(rider.entity);
	}
	
	public int getID(){
		return entity.getEntityId();
	}
	
	public UUID getUUID(){
		return entity.getUniqueID();
	}

	//	public void attackEntity(DamageSource source, float damage) {
	//
	//	}

	public void kill() {
		// TODO: This might not work correctly... ?
		entity.attackEntityFrom(DamageSource.GENERIC, 1000000F);
	}
	
	public EntityLivingObjectWrapper getAsLiving(){
		return new EntityLivingObjectWrapper((EntityLiving) entity);
	}

	/**
	 * This function is seriosly powerful, as you can change ANY entities NBT-Data with it.
	 * Hell, you can even transform one entity into another (In theory! Dont do it!).
	 **/
	public void merge(CompoundTagWrapper tagwrap) {
		NBTTagCompound tagCompound = new NBTTagCompound();
		entity.writeToNBT(tagCompound);
		tagCompound.merge(tagwrap.internal());
		entity.readFromNBT(tagCompound);
	}

	public static final List<EntityObjectWrapper> transform(List<Entity> entities) {
		List<EntityObjectWrapper> out = new ArrayList<EntityObjectWrapper>(entities.size());

		for(Entity entity : entities) {
			out.add(transform(entity));
		}

		return out;
	}

	public static final EntityObjectWrapper transform(Entity entity) {
		if(entity instanceof EntityLiving) {
			return new EntityLivingObjectWrapper((EntityLiving)entity);
		}

		return new EntityObjectWrapper(entity);
	}
	
	public PlayerObjectWrapper getClosestPlayer(double distance){
		return new PlayerObjectWrapper(entity.world.getClosestPlayerToEntity(entity, distance));
	}

}
