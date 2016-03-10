package de.longor.talecraft.blocks.util;

import java.util.Random;

import de.longor.talecraft.blocks.TCBlock;
import de.longor.talecraft.blocks.TCITriggerableBlock;
import de.longor.talecraft.invoke.EnumTriggerState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RedstoneActivatorBlock extends TCBlock implements TCITriggerableBlock {
    public static final PropertyBool POWERED = PropertyBool.create("powered");
	
	public RedstoneActivatorBlock() {
		super();
        setDefaultState(this.blockState.getBaseState().withProperty(POWERED, Boolean.valueOf(false)));
	}
	
    public boolean canProvidePower()
    {
        return true;
    }
    
    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return ((Boolean)state.getValue(POWERED)).booleanValue() ? 15 : 0;
    }
    
	@Override
	public void trigger(World world, BlockPos position, EnumTriggerState triggerState) {
		if (world.isRemote)
    		return;
		
		switch(triggerState) {
		case ON:
			world.setBlockState(position, this.getDefaultState().withProperty(POWERED, Boolean.valueOf(true)));
		break;
		case OFF:
			world.setBlockState(position, this.getDefaultState().withProperty(POWERED, Boolean.valueOf(false)));
		break;
		case INVERT:
			if (((Boolean)world.getBlockState(position).getValue(POWERED)).booleanValue()) {
				world.setBlockState(position, this.getDefaultState().withProperty(POWERED, Boolean.valueOf(false)));
			} else {
				world.setBlockState(position, this.getDefaultState().withProperty(POWERED, Boolean.valueOf(true)));
			}
		break;
		case IGNORE:
			world.setBlockState(position, this.getDefaultState().withProperty(POWERED, Boolean.valueOf(true)));
		break;
		default:
			world.setBlockState(position, this.getDefaultState().withProperty(POWERED, Boolean.valueOf(true)));
		break;
		}
	}
    
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(POWERED, Boolean.valueOf((meta & 1) > 0));
    }
    
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;

        if (((Boolean)state.getValue(POWERED)).booleanValue())
        {
            i |= 1;
        }

        return i;
    }
    
    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {POWERED});
    }
    
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(POWERED, Boolean.valueOf(false));
    }
	
	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }
	
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }
	
}
