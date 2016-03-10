package de.longor.talecraft.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

/**
 * This material defines a invisible, indestructible, immovable block.
 **/
public class TCAdminiumMaterial extends Material {
	/** The one and only instance of this material. **/
	public static final Material instance = new TCAdminiumMaterial();

	public TCAdminiumMaterial() {
		super(MapColor.airColor);
		setImmovableMobility();
	}

    public boolean isLiquid()
    {
        return false;
    }

    public boolean isSolid()
    {
        return true;
    }

    public boolean blocksLight()
    {
        return true;
    }

    public boolean blocksMovement()
    {
        return true;
    }

    public boolean getCanBurn()
    {
        return false;
    }

    public boolean isReplaceable()
    {
        return false;
    }

    public boolean isOpaque()
    {
        return true;
    }

    public boolean isToolNotRequired()
    {
        return false;
    }
    
}
