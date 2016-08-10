package de.longor.talecraft.voxelator.params;

import de.longor.talecraft.voxelator.BrushParameter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public final class BlockstateBrushParameter extends BrushParameter {
	private final IBlockState _default;
	
	@Override public BPType getType() {
		return BPType.BLOCKSTATE;
	}
	
	public BlockstateBrushParameter(String name, IBlockState _default) {
		super(name);
		this._default = _default;
	}
	
	public BlockstateBrushParameter(String name, Block _default) {
		super(name);
		this._default = _default.getDefaultState();
	}
	
	public IBlockState getDefault() {
		return this._default;
	}
}
