package talecraft.util;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneBrick.EnumType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BombExplosion{ //The same thing as a normal explosion, except it doesn't destroy all blocks
    /** whether or not the explosion sets fire to blocks around it */
    private final boolean isFlaming;
    /** whether or not this explosion spawns smoke particles */
    private final boolean isSmoking;
    private final Random explosionRNG;
    private final World worldObj;
    private final double explosionX;
    private final double explosionY;
    private final double explosionZ;
    private final Entity exploder;
    private final float explosionSize;
    private final List<BlockPos> affectedBlockPositions;
    private final Map<EntityPlayer, Vec3d> playerKnockbackMap;
    private final Vec3d position;
    private final Explosion explosion;

    public BombExplosion(World worldIn, Entity entityIn, double x, double y, double z){
        this.explosionRNG = new Random();
        this.affectedBlockPositions = Lists.<BlockPos>newArrayList();
        this.playerKnockbackMap = Maps.<EntityPlayer, Vec3d>newHashMap();
        this.worldObj = worldIn;
        this.exploder = entityIn;
        this.explosionSize = 1.5f;
        this.explosionX = x;
        this.explosionY = y;
        this.explosionZ = z;
        this.isFlaming = false;
        this.isSmoking = true;
        this.position = new Vec3d(explosionX, explosionY, explosionZ);
        this.explosion = new Explosion(worldIn, entityIn, x, y, z, explosionSize, isFlaming, isSmoking);
    }

    /**
     * Does the first part of the explosion (destroy blocks)
     */
    public void doExplosionA(){
        float f3 = this.explosionSize * 2.0F;
        int k1 = MathHelper.floor(this.explosionX - (double)f3 - 1.0D);
        int l1 = MathHelper.floor(this.explosionX + (double)f3 + 1.0D);
        int i2 = MathHelper.floor(this.explosionY - (double)f3 - 1.0D);
        int i1 = MathHelper.floor(this.explosionY + (double)f3 + 1.0D);
        int j2 = MathHelper.floor(this.explosionZ - (double)f3 - 1.0D);
        int j1 = MathHelper.floor(this.explosionZ + (double)f3 + 1.0D);
        List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB((double)k1, (double)i2, (double)j2, (double)l1, (double)i1, (double)j1));
        Vec3d vec3d = new Vec3d(this.explosionX, this.explosionY, this.explosionZ);

        for (int k2 = 0; k2 < list.size(); ++k2){
            Entity entity = (Entity)list.get(k2);

            if (!entity.isImmuneToExplosions()){
                double d12 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / (double)f3;

                if (d12 <= 1.0D){
                    double d5 = entity.posX - this.explosionX;
                    double d7 = entity.posY + (double)entity.getEyeHeight() - this.explosionY;
                    double d9 = entity.posZ - this.explosionZ;
                    double d13 = (double)MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);

                    if (d13 != 0.0D){
                        d5 = d5 / d13;
                        d7 = d7 / d13;
                        d9 = d9 / d13;
                        double d14 = (double)this.worldObj.getBlockDensity(vec3d, entity.getEntityBoundingBox());
                        double d10 = (1.0D - d12) * d14;
                        entity.attackEntityFrom(DamageSource.causeExplosionDamage(explosion), 7.5f);
                        double d11 = 1.0D;

                        if (entity instanceof EntityLivingBase){
                            d11 = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase)entity, d10);
                        }

                        entity.motionX += d5 * d11;
                        entity.motionY += d7 * d11;
                        entity.motionZ += d9 * d11;

                        if (entity instanceof EntityPlayer){
                            EntityPlayer entityplayer = (EntityPlayer)entity;

                            if (!entityplayer.isSpectator() && (!entityplayer.isCreative() || !entityplayer.capabilities.isFlying)){
                                this.playerKnockbackMap.put(entityplayer, new Vec3d(d5 * d10, d7 * d10, d9 * d10));
                            }
                        }
                    }
                }
            }
        }

        List<BlockPos> origBlocks = Lists.newArrayList();
        List<BlockPos> destroyBlocks = Lists.newArrayList();
        for(int sx = -1; sx <= 1; sx++){
        	for(int sy = -1; sy <= 1; sy++){
        		for(int sz = -1; sz <= 1; sz++){
        			BlockPos pos = new BlockPos(sx + explosionX, sy + explosionY, sz + explosionZ);
        			IBlockState state = worldObj.getBlockState(pos);
        			if(state != null && state.getBlock() == Blocks.STONEBRICK && state.getValue(BlockStoneBrick.VARIANT) == EnumType.CRACKED){
        				origBlocks.add(pos);
        			}
        		}
        	}
        }
        destroyBlocks.addAll(origBlocks);
        int added = origBlocks.size();
        int iterations = 0;
        while(added > 0 && iterations < 100){
        	iterations++;
        	List<BlockPos> currentIteration = Lists.newArrayList();
        	currentIteration.addAll(destroyBlocks);
        	added = 0;
            for(BlockPos pos : destroyBlocks){
            	added += iterate(pos, currentIteration, explosionX, explosionY, explosionZ);
            }
            destroyBlocks.clear();
            destroyBlocks.addAll(currentIteration);
        }
        
        for(BlockPos pos : destroyBlocks){
        	worldObj.setBlockToAir(pos);
        }
    }
    
    private int iterate(BlockPos pos, List<BlockPos> destroyBlocks, double oX, double oY, double oZ){
    	if(pos.distanceSq(oX, oY, oZ) > 32) {
    		return 0;
    	}
    	
    	BlockPos[] sides = new BlockPos[]{pos.up(), pos.down(), pos.north(), pos.south(), pos.east(), pos.west()};
    	int added = 0;
    	for(BlockPos side : sides){
    		IBlockState state = worldObj.getBlockState(side);
			if(state != null && state.getBlock() == Blocks.STONEBRICK && state.getValue(BlockStoneBrick.VARIANT) == EnumType.CRACKED){
				if(!destroyBlocks.contains(side)){
					destroyBlocks.add(side);
					added++;
				}
			}
    	}
    	return added;
    }

    /**
     * Does the second part of the explosion (sound, particles, drop spawn)
     */
    public void doExplosionB(boolean spawnParticles)
    {
        this.worldObj.playSound((EntityPlayer)null, this.explosionX, this.explosionY, this.explosionZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);

        if (this.explosionSize >= 2.0F && this.isSmoking)
        {
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D, new int[0]);
        }
        else
        {
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D, new int[0]);
        }

        if (this.isSmoking)
        {
            for (BlockPos blockpos : this.affectedBlockPositions)
            {
                IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if (spawnParticles)
                {
                    double d0 = (double)((float)blockpos.getX() + this.worldObj.rand.nextFloat());
                    double d1 = (double)((float)blockpos.getY() + this.worldObj.rand.nextFloat());
                    double d2 = (double)((float)blockpos.getZ() + this.worldObj.rand.nextFloat());
                    double d3 = d0 - this.explosionX;
                    double d4 = d1 - this.explosionY;
                    double d5 = d2 - this.explosionZ;
                    double d6 = (double)MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                    d3 = d3 / d6;
                    d4 = d4 / d6;
                    d5 = d5 / d6;
                    double d7 = 0.5D / (d6 / (double)this.explosionSize + 0.1D);
                    d7 = d7 * (double)(this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3F);
                    d3 = d3 * d7;
                    d4 = d4 * d7;
                    d5 = d5 * d7;
                    this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (d0 + this.explosionX) / 2.0D, (d1 + this.explosionY) / 2.0D, (d2 + this.explosionZ) / 2.0D, d3, d4, d5, new int[0]);
                    this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5, new int[0]);
                }

                if (iblockstate.getMaterial() != Material.AIR)
                {
                    if (block.canDropFromExplosion(explosion))
                    {
                        block.dropBlockAsItemWithChance(this.worldObj, blockpos, this.worldObj.getBlockState(blockpos), 1.0F / this.explosionSize, 0);
                    }

                    block.onBlockExploded(this.worldObj, blockpos, explosion);
                }
            }
        }

        if (this.isFlaming)
        {
            for (BlockPos blockpos1 : this.affectedBlockPositions)
            {
                if (this.worldObj.getBlockState(blockpos1).getMaterial() == Material.AIR && this.worldObj.getBlockState(blockpos1.down()).isFullBlock() && this.explosionRNG.nextInt(3) == 0)
                {
                    this.worldObj.setBlockState(blockpos1, Blocks.FIRE.getDefaultState());
                }
            }
        }
    }

    public Map<EntityPlayer, Vec3d> getPlayerKnockbackMap()
    {
        return this.playerKnockbackMap;
    }

    /**
     * Returns either the entity that placed the explosive block, the entity that caused the explosion or null.
     */
    public EntityLivingBase getExplosivePlacedBy()
    {
        return this.exploder == null ? null : (this.exploder instanceof EntityTNTPrimed ? ((EntityTNTPrimed)this.exploder).getTntPlacedBy() : (this.exploder instanceof EntityLivingBase ? (EntityLivingBase)this.exploder : null));
    }

    public void clearAffectedBlockPositions()
    {
        this.affectedBlockPositions.clear();
    }

    public List<BlockPos> getAffectedBlockPositions()
    {
        return this.affectedBlockPositions;
    }

    public Vec3d getPosition(){ return this.position; }
}
