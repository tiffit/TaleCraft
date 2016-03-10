package de.longor.talecraft.blocks;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TCBlock extends Block {
	
	protected TCBlock() {
		super(TCAdminiumMaterial.instance);
		setDefaultState(getBlockState().getBaseState());
        setResistance(6000001.0F);
		setBlockUnbreakable();
		setTickRandomly(false);
		setStepSound(Block.soundTypeStone);
		setCreativeTab(TaleCraftTabs.tab_TaleCraftTab);
	}
	
    public int getRenderType()
    {
        return 3;
    }
    
	@Override
    public boolean isFullCube()
    {
        return true;
    }
	
	@Override
    public boolean isOpaqueCube()
    {
        return true;
    }
	
}
