package talecraft.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.blocks.world.WorkbenchBlock;
import talecraft.util.WorkbenchManager;

public class WorkbenchCraftingPacket implements IMessage {

	boolean add;
	boolean clear;
	IRecipe recipe;
	
	public WorkbenchCraftingPacket() {
		clear = true;
	}
	
	public WorkbenchCraftingPacket(IRecipe recipe, boolean add) {
		this.add = add;
		this.clear = false;
		this.recipe = recipe;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		if(buf.readBoolean()){
			clear = true;
		}else{
			clear = false;
			add = buf.readBoolean();
			NBTTagCompound tag = ByteBufUtils.readTag(buf);
			recipe = WorkbenchManager.recipeFromNBT(tag);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		if(clear){
			buf.writeBoolean(true);
		}else{
			buf.writeBoolean(false);
			buf.writeBoolean(add);
			ByteBufUtils.writeTag(buf, WorkbenchManager.recipeToNBT(recipe));
		}
	}

	public static class Handler implements IMessageHandler<WorkbenchCraftingPacket, IMessage> {

		@Override
		public IMessage onMessage(WorkbenchCraftingPacket message, MessageContext ctx) {
			if(message.clear){
				WorkbenchManager.getInstance().clear();
				return null;
			}
			IRecipe recipe = message.recipe;
			if(message.add){
				WorkbenchBlock.recipes.add(recipe);
			}else{
				for(IRecipe frec : WorkbenchManager.getInstance()){
					if(frec instanceof ShapedRecipes && recipe instanceof ShapedRecipes){
						ShapedRecipes rec = (ShapedRecipes) frec;
						ShapedRecipes cur = (ShapedRecipes) recipe;
						if(ItemStack.areItemStacksEqual(rec.getRecipeOutput(), cur.getRecipeOutput())){
							boolean equal = true;
							for(int i = 0; i < 9; i++){
								Ingredient grid1 = rec.recipeItems.get(i);
								Ingredient grid2 = cur.recipeItems.get(i);
								if((grid1 == Ingredient.EMPTY && grid2 != Ingredient.EMPTY) || (grid1 != Ingredient.EMPTY && grid2 == Ingredient.EMPTY)){
									equal = false;
									break;
								}
								if(!grid1.equals(grid2)){
									equal = false;
									break;
								}
							}
							if(equal){
								WorkbenchManager.getInstance().remove(frec);
								break;
							}
						}
					}
				}
			}
			return null;
		}
	}
}
