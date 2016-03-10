package de.longor.talecraft.script.wrappers.block;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.script.wrappers.IObjectWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class BlockObjectWrapper implements IObjectWrapper {
	private Block block;
	
	public BlockObjectWrapper(Block block) {
		this.block = block;
	}
	
	public String getName() {
		return block.getLocalizedName();
	}
	
	public String getInternalName() {
		return block.getUnlocalizedName();
	}
	
	public BlockStateObjectWrapper getDefaultBlockState() {
		return new BlockStateObjectWrapper(block.getDefaultState());
	}
	
	public Block internal() {
		return block;
	}
	
	public boolean equals(Object obj) {
		return block.equals(obj);
	}
	
	public int hashCode() {
		return block.hashCode();
	}
	
	@Override
	public List<String> getOwnPropertyNames() {
		return TaleCraft.globalScriptManager.getOwnPropertyNames(this);
	}
	
}
