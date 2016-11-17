package talecraft.voxelator.params;

import talecraft.voxelator.BrushParameter;

public final class IntegerBrushParameter extends BrushParameter {
	private final int min;
	private final int max;
	private final int step;
	private final int _default;
	
	@Override public BPType getType() {
		return BPType.INTEGER;
	}
	
	public IntegerBrushParameter(String name, int minimum, int maximum, int step, int _default) {
		super(name);
		this.min = minimum;
		this.max = maximum;
		this.step = step;
		this._default = _default;
	}
	
	public IntegerBrushParameter(String name, int minimum, int maximum, int _default) {
		super(name);
		this.min = minimum;
		this.max = maximum;
		this.step = 1;
		this._default = _default;
	}
	
	public int getMinimum() {
		return this.min;
	}
	
	public int getMaximum() {
		return this.max;
	}
	
	public int getStep() {
		return this.step;
	}
	
	public int getDefault() {
		return this._default;
	}
}
