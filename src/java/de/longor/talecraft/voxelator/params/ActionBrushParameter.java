package de.longor.talecraft.voxelator.params;

import de.longor.talecraft.voxelator.BrushParameter;
import de.longor.talecraft.voxelator.Voxelator;
import de.longor.talecraft.voxelator.Voxelator.ActionFactory;

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
