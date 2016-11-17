package talecraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import talecraft.TaleCraftTabs;

public abstract class TCBlock extends Block {

	protected TCBlock() {
		super(TCAdminiumMaterial.instance);
		setResistance(6000001.0F);
		setBlockUnbreakable();
		setTickRandomly(false);
		setSoundType(SoundType.STONE);
		setCreativeTab(TaleCraftTabs.tab_TaleCraftTab);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return true;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return true;
	}

}
