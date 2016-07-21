package tiffit.talecraft.items.world;

import de.longor.talecraft.TaleCraftTabs;
import de.longor.talecraft.items.TCItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
