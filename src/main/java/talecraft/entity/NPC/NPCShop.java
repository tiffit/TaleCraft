package talecraft.entity.NPC;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class NPCShop implements IMerchant {
	
	private List<NPCTrade> trades;
	private EntityPlayer player;
	private EntityNPC npc;

	public NPCShop(EntityNPC npc){
		this.npc = npc;
		this.trades = new ArrayList<NPCTrade>();
	}
	
	@Override
	public void setCustomer(EntityPlayer player) {
		this.player = player;
	}

	@Override
	public EntityPlayer getCustomer() {
		return player;
	}
	
	public List<NPCTrade> getTrades(){
		return trades;
	}

	@Override
	public MerchantRecipeList getRecipes(EntityPlayer player) {
		return toVanilla();
	}

	@Override
	public void setRecipes(MerchantRecipeList trades) {
		this.trades = fromVanilla(this, trades);
	}

	@Override
	public void useRecipe(MerchantRecipe recipe) {
		NPCTrade trade = NPCTrade.fromVanilla(this, recipe);
		trade.decrStock();
	}

	@Override
	public void verifySellingItem(ItemStack stack) {}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(TextFormatting.getTextWithoutFormattingCodes(npc.getDisplayName().getUnformattedText()));
	}
	
	public MerchantRecipeList toVanilla(){
		MerchantRecipeList list = new MerchantRecipeList();
		for(NPCTrade trade : trades){
			list.add(trade.toVanilla());
		}
		return list;
	}
	
	public static List<NPCTrade> fromVanilla(NPCShop shop, MerchantRecipeList trades){
		List<NPCTrade> list = new ArrayList<NPCTrade>();
		for(MerchantRecipe trade : trades){
			list.add(NPCTrade.fromVanilla(shop, trade));
		}
		return list;
	}
	
	public NBTTagCompound toNBT(){
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("size", trades.size());
		for(int i = 0; i < trades.size(); i++){
			tag.setTag("trade_" + i, trades.get(i).toNBT());
		}
		return tag;
	}
	
	public static NPCShop getShop(NBTTagCompound tag, EntityNPC npc){
		NPCShop shop = new NPCShop(npc);
		for(int i = 0; i < tag.getInteger("size"); i++){
			shop.trades.add(NPCTrade.fromNBT(tag.getCompoundTag("trade_" + i)));
		}
		return shop;
	}
	
	@Override
	public World getWorld() {
		return npc.getEntityWorld();
	}

	@Override
	public BlockPos getPos() {
		return npc.getPosition();
	}
	
	public static class NPCTrade{
	
		private ItemStack item1;
		private ItemStack item2;
		private int stock;
		
		public NPCTrade(ItemStack item1, ItemStack item2){
			this(item1, item2, -1);
		}
		
		public NPCTrade(ItemStack item1, ItemStack item2, int stock){
			this.item1 = item1;
			this.item2 = item2;
			this.stock = stock;
		}
		
		public ItemStack getBuying(){
			return item1;
		}
		
		public ItemStack getSelling(){
			return item2;
		}
		
		public int getStock(){
			return stock;
		}
		
		public void setStock(int stock){
			this.stock = stock;
		}
		
		public boolean inStock(){
			return stock > 0 || stock == -1;
		}
		
		public void decrStock(){
			if(inStock()) stock--;
		}
		
		public MerchantRecipe toVanilla(){
			ItemStack outOfStock = inStock() ? null : new ItemStack(Blocks.BARRIER);
			if(outOfStock != null) outOfStock.setStackDisplayName(TextFormatting.RED + "Out Of Stock!");
			return new NPCMerchantRecipe(this, outOfStock);
		}
		
		public static NPCTrade fromVanilla(NPCShop shop, MerchantRecipe recipe){
			NPCTrade trade = null;
			for(NPCTrade trd : shop.trades){
				if(ItemStack.areItemStacksEqual(trd.getBuying(), recipe.getItemToBuy()) && ItemStack.areItemStacksEqual(trd.getSelling(), recipe.getItemToSell())){
					trade = trd;
					break;
				}
			}
			if(trade == null) trade = new NPCTrade(recipe.getItemToBuy(), recipe.getItemToSell());
			return trade;
		}
		
		public NBTTagCompound toNBT(){
			NBTTagCompound tag = new NBTTagCompound();
			tag.setTag("item1", item1.writeToNBT(new NBTTagCompound()));
			tag.setTag("item2", item2.writeToNBT(new NBTTagCompound()));
			tag.setInteger("stock", stock);
			return tag;
		}
		
		public static NPCTrade fromNBT(NBTTagCompound tag){
			ItemStack item1 = new ItemStack(tag.getCompoundTag("item1"));
			ItemStack item2 = new ItemStack(tag.getCompoundTag("item2"));
			return new NPCTrade(item1, item2, tag.getInteger("stock"));
		}
		
	}
	
	public static class NPCMerchantRecipe extends MerchantRecipe{

		private NPCTrade npctrade;
		
		 public NPCMerchantRecipe(NPCTrade trade, ItemStack outOfStock){
		        super(trade.item1, outOfStock, trade.item2, 0, Integer.MAX_VALUE);
		        this.npctrade = trade;
		 }
		 
		 @Override
		public boolean isRecipeDisabled() {
			return !npctrade.inStock();
		}
	}



}
