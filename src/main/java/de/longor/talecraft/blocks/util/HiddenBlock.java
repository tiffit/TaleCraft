package de.longor.talecraft.blocks.util;

import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftTabs;
import de.longor.talecraft.blocks.TCBlock;

public class HiddenBlock extends TCBlock {
	
	public HiddenBlock() {
		super();
	}
	
    public int getRenderType() {
        return -1;
    }
	
	public boolean isFullCube() {
		return false;
	}
	
    public boolean isOpaqueCube() {
        return false;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public float getAmbientOcclusionLightValue()
    {
        return 1.0F;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
    	return TaleCraft.proxy.isBuildMode() ? super.collisionRayTrace(worldIn, pos, start, end) : null;
    }
	
	@Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
    	if(TaleCraft.proxy.isBuildMode())
    		return new AxisAlignedBB(pos.getX(),pos.getY(),pos.getZ(),pos.getX()+1,pos.getY()+1,pos.getZ()+1);
    	else
    		return null;
    }
    
	@Override
    public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
    {
    	return true;
    }

    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return true;
    }
    
}
