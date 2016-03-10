package de.longor.talecraft.blocks.util;

import java.util.List;

import de.longor.talecraft.TaleCraftTabs;
import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.block.BlockBarrier;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BarrierEXTBlock extends BlockBarrier {
    public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 15);
	
	public BarrierEXTBlock() {
		super();
		this.setCreativeTab(TaleCraftTabs.tab_TaleCraftTab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, Integer.valueOf(0)));
	}
	
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
    	if(ClientProxy.isInBuildMode())
    		return new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)pos.getY() + this.maxY, (double)pos.getZ() + this.maxZ);
    	else
    		return null;
    }
    
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
    	int type = ((Integer)state.getValue(TYPE)).intValue();
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
    		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    	}
    	
    }
	
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        for (int j = 0; j < 7; ++j)
        {
            list.add(new ItemStack(itemIn, 1, j));
        }
    }
	
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(TYPE, Integer.valueOf(meta));
    }
	
	@Override
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(TYPE)).intValue();
    }
	
	@Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {TYPE});
    }
    
}
