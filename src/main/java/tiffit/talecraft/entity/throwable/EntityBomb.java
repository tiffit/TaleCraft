package tiffit.talecraft.entity.throwable;

import de.longor.talecraft.TaleCraftItems;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import tiffit.talecraft.util.BombExplosion;

public class EntityBomb extends EntityThrowable{

	int explosion_delay = 60;
	boolean should_play_sound = true;
	
    public EntityBomb(World world){
        super(world);
    }

    public EntityBomb(World world, EntityLivingBase thrower){
        super(world, thrower);
    }

    public EntityBomb(World world, double x, double y, double z){
        super(world, x, y, z);
    }
	
	@Override
    protected void onImpact(RayTraceResult result){
        if (result.entityHit != null){
            explode(); //explode on impact
        }
        if(!worldObj.isRemote){
        	this.setVelocity(0, 0, 0);
        }
    }
	
	@Override
	public void onUpdate(){
		super.onUpdate();
		if(should_play_sound){
			worldObj.playSound(posX, posY, posZ, SoundEvents.entity_tnt_primed, SoundCategory.AMBIENT, 100f, 1f, false);
			should_play_sound = false;
		}
		explosion_delay--;
		this.createRunningParticles();
		if(explosion_delay <= 0){
			if(!worldObj.isRemote) explode();
			else worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, posX, posY, posZ, 0, 0, 0, null);
		}
		worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX, posY + 1, posZ, 0, 0, 0, null);
		if(explosion_delay <= 20) worldObj.spawnParticle(EnumParticleTypes.FLAME, posX, posY + 1, posZ, 0, 0, 0, null);
	}
	
	private void explode(){
		if(!worldObj.isRemote){
			BombExplosion explosion = new BombExplosion(worldObj, this, posX, posY, posZ);
			explosion.doExplosionA();
			explosion.doExplosionB(true);
			setDead();
		}
	}
	
	 public void writeEntityToNBT(NBTTagCompound tag){
	        super.writeEntityToNBT(tag);
	        tag.setInteger("explosion_delay", explosion_delay);
	    }

	    public void readEntityFromNBT(NBTTagCompound tag){
	       super.readEntityFromNBT(tag);
	       explosion_delay = tag.getInteger("explosion_delay");
	    }
	
	
	public static class EntityBombRenderFactory implements IRenderFactory{

		@Override
		public Render createRenderFor(RenderManager manager) {
			Minecraft mc = Minecraft.getMinecraft();
			return new RenderSnowball(manager, TaleCraftItems.bomb, mc.getRenderItem());
		}
		
	}

}
