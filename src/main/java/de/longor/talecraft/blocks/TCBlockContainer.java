package de.longor.talecraft.blocks;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TCBlockContainer extends BlockContainer {
	
	protected TCBlockContainer() {
		super(TCAdminiumMaterial.instance);
		setDefaultState(getBlockState().getBaseState());
		setCreativeTab(TaleCraftTabs.tab_TaleCraftTab);
		setBlockUnbreakable();
        setResistance(6000001.0F);
		setStepSound(Block.soundTypeStone);
		setTickRandomly(false);
		setLightOpacity(0);
        disableStats();
        translucent = true;
	}
	
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }
	
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
    	worldIn.removeTileEntity(pos);
    }
	
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return false;
    }
    
	@Override
    public int getRenderType() {
        return 2;
    }
    
	@Override
    public boolean isFullCube() {
        return false;
    }
    
	@Override
    public boolean isOpaqueCube() {
        return false;
    }
	
    public int tickRate(World worldIn) {
        return 1;
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
	
	@Override
    @SideOnly(Side.CLIENT)
    public float getAmbientOcclusionLightValue()
    {
        return 1.0F;
    }
	
	@Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {}
    
}
