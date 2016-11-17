package talecraft.entity.NPC;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerNPCMerchant extends ContainerMerchant {

	public ContainerNPCMerchant(InventoryPlayer playerInventory, IMerchant merchant, World worldIn) {
		super(playerInventory, merchant, worldIn);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		return null;
	}

}
