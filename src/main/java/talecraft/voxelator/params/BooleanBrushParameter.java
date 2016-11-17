package talecraft.voxelator.params;

import talecraft.voxelator.BrushParameter;

public final class BooleanBrushParameter extends BrushParameter {
	private final boolean _default;
	
	@Override public BPType getType() {
		return BPType.BOOLEAN;
	}
	
	public BooleanBrushParameter(String name, boolean _default) {
		super(name);
		this._default = _default;
	}
	
	public boolean getDefault() {
		return this._default;
	}
}
