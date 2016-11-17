package talecraft.voxelator.params;

import talecraft.voxelator.BrushParameter;
import talecraft.voxelator.Voxelator;
import talecraft.voxelator.Voxelator.ActionFactory;

public final class ActionBrushParameter extends BrushParameter {
	
	@Override public BPType getType() {
		return BPType.xACTION;
	}
	
	public ActionBrushParameter(String name) {
		super(name);
	}
	
	public ActionFactory getDefault() {
		return Voxelator.actions.get("replace");
	}
}
