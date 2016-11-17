package talecraft.items.world;

import net.minecraft.item.Item;
import talecraft.TaleCraftTabs;

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
