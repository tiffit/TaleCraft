package talecraft.entity.projectile;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import talecraft.client.entity.RenderBullet;

public class EntityBullet extends EntityThrowable {
	
	private float damage = 1f;
	private double distance = 50f;
	private BlockPos original = new BlockPos(0, 0, 0);
	
	public EntityBullet(World world) {
		super(world);
	}
	
	public EntityBullet(World world, double x, double y, double z) {
		super(world, x, y, z);
	}
	
	public EntityBullet(World world, EntityLivingBase thrower, float damage,
			double distance) {
		super(world, thrower);
		this.damage = damage;
		this.distance = distance;
		original = thrower.getPosition();
	}
	
	@Override
	protected void onImpact(RayTraceResult result) {
		if (world.isRemote)
			return;
		boolean kill = true;
		if (result.typeOfHit == Type.ENTITY) {
			Entity ent = result.entityHit;
			if (ent instanceof EntityPlayerMP) {
				kill = false;
			} else
				ent.attackEntityFrom(
						(new EntityDamageSource("arrow", getThrower())).setProjectile(),
						damage);
		}
		if (result.typeOfHit == Type.BLOCK) {
			if (!world.getBlockState(result.getBlockPos()).isFullBlock()) {
				kill = false;
			}
		}
		if (kill)
			setDead();
	}
	
	@Override
	protected float getGravityVelocity() {
		return 0;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if (getPosition().distanceSq(original) >= (distance * distance)
				&& !world.isRemote) {
			setDead();
		}
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setFloat("damage", damage);
		tag.setDouble("distance", distance);
		tag.setLong("original", original.toLong());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		damage = tag.getFloat("damage");
		distance = tag.getDouble("distance");
		original = BlockPos.fromLong(tag.getLong("double"));
	}
	

	public static class EntityBulletRenderFactory implements IRenderFactory {
		
		@Override
		public Render createRenderFor(RenderManager manager) {
			return new RenderBullet(manager);
		}
		
	}
	
}
