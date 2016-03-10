package de.longor.talecraft;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TaleCraftTabs {
	// empty stub method for 'touching' the class
	public static final void init(){}
	
    public static CreativeTabs tab_TaleCraftTab = new CreativeTabs("talecraftTab") {
		@Override
		public Item getTabIconItem() {
			return Items.book;
		}
		
	    @SideOnly(Side.CLIENT)
	    public void displayAllReleventItems(List items) {
	    	// Add the useful items from 'Vanilla' ...
			items.add(new ItemStack(Blocks.command_block));
			items.add(new ItemStack(Blocks.mob_spawner));
			items.add(new ItemStack(Blocks.barrier));
			
			// etc. etc.
			super.displayAllReleventItems(items);
		}
	};
	
    public static CreativeTabs tab_TaleCraftDecorationTab = new CreativeTabs("talecraftDecoTab") {
		@Override
		public Item getTabIconItem() {
			return Items.dye;
		}
		
	    @SideOnly(Side.CLIENT)
	    public int getIconItemDamage() {
	        return (int) (((double)Minecraft.getSystemTime() / 100D) % 16);
	    }
	    
	    @SideOnly(Side.CLIENT)
	    public ItemStack getIconItemStack() {
	    	// We don't care about memory usage! :D
	        return new ItemStack(this.getTabIconItem(), 1, this.getIconItemDamage());
	    }
		
	    @SideOnly(Side.CLIENT)
	    public void displayAllReleventItems(List items) {
	    	super.displayAllReleventItems(items);
		}
	};
	
}
