package de.longor.talecraft.voxelbrush;

public interface IShape {

	public String getName();
	@Override
	public String toString();

	/** Checks if the given world-position is inside the shape. **/
	public boolean isBlockInShape(int x, int y, int z);

	public int[] getBounds();

	public int getOffsetX();
	public int getOffsetY();
	public int getOffsetZ();

}
