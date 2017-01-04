package talecraft.blocks.world;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import talecraft.blocks.TCITriggerableBlock;
import talecraft.invoke.EnumTriggerState;

public class SpikeBlock extends TCWorldBlock implements TCITriggerableBlock{
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static final PropertyBool ACTIVE = PropertyBool.create("active");
	private static final Logger LOGGER = LogManager.getLogger();
	
	public SpikeBlock(){
		super();
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP).withProperty(ACTIVE, true));
	}
	
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
        EnumFacing enumfacing = placer.getHorizontalFacing();
        if(hitY == 0f) enumfacing = EnumFacing.UP;
        else if(hitY == 1f) enumfacing = EnumFacing.DOWN;
        try{
            return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, enumfacing).withProperty(ACTIVE, true);
        }
        catch (IllegalArgumentException var11){
            if (!worldIn.isRemote){
                LOGGER.warn(String.format("Invalid spike block data @ " + pos.toString() + " in " + worldIn.provider.getDimension()));

                if (placer instanceof EntityPlayer){
                    ((EntityPlayer)placer).sendMessage(new TextComponentString("Invalid spike block data!"));
                }
            }

            return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, 0, placer).withProperty(FACING, EnumFacing.UP).withProperty(ACTIVE, true);
        }
    }
	
	@Override
	protected BlockStateContainer createBlockState() {
	    return new BlockStateContainer(this, new IProperty[] { FACING, ACTIVE });
	}
	
	@Deprecated
	@Override
	public IBlockState getStateFromMeta(int meta) {
		int altmeta = meta;
		if(meta >= 6){
			altmeta -= 6;
		}
		EnumFacing direction = EnumFacing.values()[altmeta];
		boolean active = meta >= 6;
	    return getDefaultState().withProperty(FACING, direction).withProperty(ACTIVE, active);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
	    EnumFacing facing = state.getValue(FACING);
	    int meta = facing.ordinal();
	    if(state.getValue(ACTIVE)) meta +=6;
	    return meta;
	}
	
	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}
	
	@Override
    public boolean isOpaqueCube(IBlockState state){
        return false;
    }

	@Override
    public boolean isFullCube(IBlockState state){
        return false;
    }
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Deprecated
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
	}

	@Deprecated
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1D, 0.3D, 1D);
	}

	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return true;
	}
	
	@Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn){
        if(state.getValue(ACTIVE)) entityIn.attackEntityFrom(DamageSource.CACTUS, 2.5F);
    }

	@Override
	public void trigger(World world, BlockPos position, EnumTriggerState trigger) {
		boolean active = trigger.getBooleanValue();
		IBlockState bs = world.getBlockState(position);
		world.setBlockState(position, bs.withProperty(ACTIVE, active));
	}
}
