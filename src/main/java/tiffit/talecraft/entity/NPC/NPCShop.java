package tiffit.talecraft.entity.NPC;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class NPCShop implements IMerchant {
	
	private EntityPlayer player;
	private EntityNPC npc;
	private MerchantRecipeList trades;

	public NPCShop(EntityNPC npc){
		this.npc = npc;
		this.trades = new MerchantRecipeList();
	}
	
	@Override
	public void setCustomer(EntityPlayer player) {
		this.player = player;
	}

	@Override
	public EntityPlayer getCustomer() {
		return player;
	}

	@Override
	public MerchantRecipeList getRecipes(EntityPlayer player) {
		return trades;
	}

	@Override
	public void setRecipes(MerchantRecipeList trades) {
		this.trades = trades;
	}

	@Override
	public void useRecipe(MerchantRecipe recipe) {
		/**When the specific recipe is used, lets not do anything right now*/
	}

	@Override
	public void verifySellingItem(ItemStack stack) {
		/**This will play the yes or no "sounds", for now lets not do that*/
//        if (!npc.worldObj.isRemote && npc.livingSoundTime > -npc.getTalkInterval() + 20){
//            npc.livingSoundTime = -npc.getTalkInterval();
//
//            if (stack != null){
//                npc.playSound(SoundEvents.ENTITY_VILLAGER_YES, 1.0F, (npc.getRNG().nextFloat() - npc.getRNG().nextFloat()) * 0.2F + 1.0F);
//            }
//            else{
//                npc.playSound(SoundEvents.ENTITY_VILLAGER_NO, 1.0F, (npc.getRNG().nextFloat() - npc.getRNG().nextFloat()) * 0.2F + 1.0F);
//            }
//        }
	}

	@Override
	public ITextComponent getDisplayName() {
		return npc.getDisplayName();
	}
	
	public NBTTagCompound toNBT(){
		return trades.getRecipiesAsTags();
	}
	
	public static NPCShop getShop(NBTTagCompound tag, EntityNPC npc){
		NPCShop shop = new NPCShop(npc);
		shop.setRecipes(new MerchantRecipeList(tag));
		return shop;
	}

}
