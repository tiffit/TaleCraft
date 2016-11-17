package talecraft.voxelator;

import talecraft.voxelator.params.*;

public abstract class BrushParameter {
	public static final BrushParameter[] NO_PARAMETERS = new BrushParameter[0];
	
	public BrushParameter(String name) {
		this.name = name;
	}
	
	// name
	private String name;
	public String getName() {
		return this.name;
	}
	
	// type
	
	// The type of the parameter...
	public static enum BPType {
		// primitives
		INTEGER, FLOAT, BOOLEAN,
		
		// collections
		LIST,
		
		// minecrafts
		BLOCKSTATE,
		
		// talecraft
		xSHAPE, xFILTER, xACTION;
	}
	
	// Method to get type.
	public abstract BrushParameter.BPType getType();
	
	// Methods for quick casting...
	
	public IntegerBrushParameter asIntegerParameter() {
		return (IntegerBrushParameter) this;
	}
	
	public FloatBrushParameter asFloatParameter() {
		return (FloatBrushParameter) this;
	}
	
	public BlockstateBrushParameter asBlockstateParameter() {
		return (BlockstateBrushParameter) this;
	}
	
	public BooleanBrushParameter asBooleanParameter() {
		return (BooleanBrushParameter) this;
	}
	
	public ListBrushParameter asListParameter() {
		return (ListBrushParameter) this;
	}
	
}
