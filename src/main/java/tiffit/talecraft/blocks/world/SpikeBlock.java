package tiffit.talecraft.blocks.world;

import de.longor.talecraft.TaleCraftTabs;
import de.longor.talecraft.blocks.TCBlock;
import de.longor.talecraft.blocks.util.tileentity.DelayBlockTileEntity;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tiffit.talecraft.tileentity.SpikeBlockTileEntity;


//TODO I need to redo ALL of this.
public class SpikeBlock extends TCBlock implements ITileEntityProvider{

    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1D, 0.9D, 1D);
    protected static final AxisAlignedBB COLLAABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1D, 0.9D, 1D);
	
	public SpikeBlock(){
		super();
		this.setCreativeTab(TaleCraftTabs.tab_TaleCraftWorldTab);
		this.setStepSound(SoundType.ANVIL);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new SpikeBlockTileEntity();
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}
	
	@Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn){
		SpikeBlockTileEntity te = (SpikeBlockTileEntity) worldIn.getTileEntity(pos);
        entityIn.attackEntityFrom(DamageSource.cactus, te.damage);
    }
	
	public AxisAlignedBB getSelectedBoundingBox(IBlockState blockState, World worldIn, BlockPos pos){
		return AABB;
	}

	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getCollisionBoundingBox(IBlockState worldIn, World pos, BlockPos state){
		return COLLAABB.offset(state);
	}

	public boolean isFullCube(IBlockState state){
		return false;
	}
	
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}

	
}
