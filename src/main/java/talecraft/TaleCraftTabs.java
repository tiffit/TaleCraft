package talecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TaleCraftTabs {
	// Empty stub method for 'touching' the class
	public static final void init() {}

	public static CreativeTabs tab_TaleCraftTab = new CreativeTabs("talecraftTab") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(TaleCraftItems.filler);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void displayAllRelevantItems(NonNullList<ItemStack> items) {
			// Add useful items from 'Vanilla'
			items.add(new ItemStack(Blocks.COMMAND_BLOCK));
			items.add(new ItemStack(Blocks.MOB_SPAWNER));
			items.add(new ItemStack(Blocks.BARRIER));
			items.add(new ItemStack(Blocks.STRUCTURE_BLOCK));
			super.displayAllRelevantItems(items);
		}
	};

	public static CreativeTabs tab_TaleCraftDecorationTab = new CreativeTabs("talecraftDecoTab") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Items.DYE, 1, this.getIconItemDamage());
		}
		
		@SideOnly(Side.CLIENT)
		public int getIconItemDamage() {
			return (int) ((Minecraft.getSystemTime() / 100D) % 16);
		}
	};
	
	public static CreativeTabs tab_TaleCraftWorldTab = new CreativeTabs("talecraftWorldTab") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(TaleCraftItems.goldKey);
		}
	};
	
	public static CreativeTabs tab_TaleCraftWeaponTab = new CreativeTabs("talecraftWeaponTab") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(TaleCraftItems.bomb);
		}
	};

}
