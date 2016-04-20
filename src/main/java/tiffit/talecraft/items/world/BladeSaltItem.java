package tiffit.talecraft.items.world;

import java.util.List;
import java.util.Random;

import de.longor.talecraft.TaleCraftTabs;
import de.longor.talecraft.items.TCItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * He is really, really, salty.
 *  http://steamcommunity.com/profiles/76561198070351387 (BladeMaster)
 */
public class BladeSaltItem extends Item {

	public BladeSaltItem(){
		if((new Random()).nextBoolean()) setCreativeTab(TaleCraftTabs.tab_TaleCraftWorldTab);
	}
	
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced){
		tooltip.add("http://steamcommunity.com/profiles/76561198070351387");
	}
	
}
