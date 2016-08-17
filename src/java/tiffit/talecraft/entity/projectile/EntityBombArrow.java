package tiffit.talecraft.entity.projectile;

import de.longor.talecraft.TaleCraftItems;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import tiffit.talecraft.client.render.RenderBombArrow;

public class EntityBombArrow extends EntityArrow {

	public EntityBombArrow(World worldIn) {
		super(worldIn);
	}

	public EntityBombArrow(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}

	public EntityBombArrow(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	@Override
	protected ItemStack getArrowStack() {
		return new ItemStack(TaleCraftItems.bombArrow);
	}
	
	@Override
	protected void onHit(RayTraceResult result) {
		super.onHit(result);
		if(!worldObj.isRemote){
			EntityBomb bomb = new EntityBomb(worldObj, this.posX, this.posY, this.posZ);
			bomb.setFuse(0);
			worldObj.spawnEntityInWorld(bomb);
			setDead();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static class EntityBombArrowRenderFactory implements IRenderFactory{
		@Override
		public Render createRenderFor(RenderManager manager) {
			return new RenderBombArrow(manager);
		}
	}

}
