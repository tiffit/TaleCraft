package tiffit.talecraft.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tiffit.talecraft.blocks.world.WorkbenchBlock;
import tiffit.talecraft.util.WorkbenchManager;

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
								ItemStack grid1 = rec.recipeItems[i];
								ItemStack grid2 = cur.recipeItems[i];
								if((grid1 == null && grid2 != null) || (grid1 != null && grid2 == null)){
									equal = false;
									break;
								}
								if(!ItemStack.areItemStacksEqual(grid1, grid2)){
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
