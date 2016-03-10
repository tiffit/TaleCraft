package de.longor.talecraft.blocks.deco;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import de.longor.talecraft.TaleCraftBlocks;
import de.longor.talecraft.TaleCraftTabs;
import de.longor.talecraft.blocks.TCBlock;

public class BlankBlock extends TCBlock {
	public static final IProperty SUB = PropertyInteger.create("sub", 0, 15);
	public int blockLayer = 0;
	public boolean ignoreSimilarity = true;
	
	public BlankBlock(SoundType sound) {
		this.setCreativeTab(TaleCraftTabs.tab_TaleCraftDecorationTab);
		this.setDefaultState(this.blockState.getBaseState().withProperty(SUB, Integer.valueOf(0)));
		this.setStepSound(sound);
	}

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return false;
    }
	
	@Override
	public int damageDropped(IBlockState state) {
		return ((Integer)state.getValue(SUB)).intValue();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(SUB, Integer.valueOf(meta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return ((Integer)state.getValue(SUB)).intValue();
	}
	
	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] {SUB});
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
		for (int j = 0; j < 16; ++j) {
			list.add(new ItemStack(itemIn, 1, j));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return blockLayer == 0 ? EnumWorldBlockLayer.SOLID : EnumWorldBlockLayer.CUTOUT;
	}

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();
        
        if (this == TaleCraftBlocks.deco_glass_a) {
            if (worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock() != this) {
                return true;
            }
            
            if (block == this)
            {
                return false;
            }
        }
        
        return !this.ignoreSimilarity && block == this ? false : super.shouldSideBeRendered(worldIn, pos, side);
    }
	
	public boolean isFullCube() {
		return blockLayer == 0;
	}
	
    public boolean isOpaqueCube() {
        return blockLayer == 0;
    }
    
    public int getMobilityFlag()
    {
        return 2; // can be moved by pistons
    }
	
}
