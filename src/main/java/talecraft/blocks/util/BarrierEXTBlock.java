package talecraft.blocks.util;

import java.util.List;

import net.minecraft.block.BlockBarrier;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import talecraft.TaleCraftTabs;

public class BarrierEXTBlock extends BlockBarrier {
	public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 15);

	public BarrierEXTBlock() {
		super();
		this.setCreativeTab(TaleCraftTabs.tab_TaleCraftTab);
		this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, Integer.valueOf(0)));
	}

	/*@SideOnly(Side.CLIENT) TODO
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
		if(ClientProxy.isInBuildMode())
			return new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)pos.getY() + this.maxY, (double)pos.getZ() + this.maxZ);
		else
			return null;
	}*/
	
	@Deprecated
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity, boolean p_185477_7_) {
		int type = state.getValue(TYPE).intValue();
		boolean collide = false;

		switch(type) {
		case 0: // Everything
			collide = true;
			break;

		case 1: // ONLY players
			collide |= collidingEntity instanceof EntityPlayer;
			break;

		case 2: // ALL living
			collide |= collidingEntity instanceof EntityLivingBase;
			break;

		case 3: // ALL living EXCEPT player
			collide |= collidingEntity instanceof EntityLiving;
			break;

		case 4: // ALL monsters
			if(collidingEntity instanceof EntityLiving) {
				EntityLiving living = (EntityLiving) collidingEntity;

				for(Object targetTask : living.targetTasks.taskEntries) {
					if(targetTask instanceof EntityAIFindEntityNearestPlayer) {
						collide |= true;
						break;
					}
				}
			}
			break;

		case 5: // ALL villagers
			collide |= collidingEntity instanceof EntityVillager;
			break;

		case 6: // ALL items
			collide |= collidingEntity instanceof EntityItem;
			break;

		default: // Everything.
			collide = true;
			break;
		}

		if(collide) {
			super.addCollisionBoxToList(state, worldIn, pos, mask, list, collidingEntity, p_185477_7_);
		}

	}

	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for (int j = 0; j < 7; ++j) {
			list.add(new ItemStack(this, 1, j));
		}
	}

	@Deprecated
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(TYPE, Integer.valueOf(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE).intValue();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {TYPE});
	}

}
