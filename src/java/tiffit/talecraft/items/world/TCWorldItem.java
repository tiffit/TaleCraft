package tiffit.talecraft.items.world;

import de.longor.talecraft.TaleCraftTabs;
import net.minecraft.item.Item;

public class TCWorldItem extends Item {

	public TCWorldItem() {
        this.setCreativeTab(TaleCraftTabs.tab_TaleCraftWorldTab);
        this.setMaxStackSize(64);
	}
	
	@Override
	public boolean isFull3D() {
		return true;
	}

}
