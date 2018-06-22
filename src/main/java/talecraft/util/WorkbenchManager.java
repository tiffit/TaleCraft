package talecraft.util;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import talecraft.blocks.world.WorkbenchBlock;

public class WorkbenchManager extends ArrayList<IRecipe> {

	private static final long serialVersionUID = -7624110812743587186L;

	public NBTTagCompound toNBT(){
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("size", size());
		for(int i = 0; i < size(); i++){
			tag.setTag("recipe_" + i, recipeToNBT(get(i)));
		}
		return tag;
	}
	
	public static WorkbenchManager fromNBT(NBTTagCompound tag){
		WorkbenchManager wrk = new WorkbenchManager();
		for(int i = 0; i < tag.getInteger("size"); i++){
			wrk.add(recipeFromNBT(tag.getCompoundTag("recipe_" + i)));
		}
		return wrk;
	}
	
	public static NBTTagCompound recipeToNBT(IRecipe recipe){
		if(recipe instanceof ShapedRecipes){
			return shapedToNBT((ShapedRecipes) recipe);
		}else{
			return new NBTTagCompound();
		}
	}
	
	public static IRecipe recipeFromNBT(NBTTagCompound tag){
		if(tag.getString("type").equals("shaped")){
			return shapedFromNBT(tag);
		}else{
			return null;
		}
	}
	
	private static NBTTagCompound shapedToNBT(ShapedRecipes shaped){
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("type", "shaped");
		tag.setInteger("width", shaped.recipeWidth);
		tag.setInteger("height", shaped.recipeHeight);
		for(int i = 0; i < 9; i++){
			ItemStack stack = shaped.recipeItems.get(i);
			NBTTagCompound stackTag = new NBTTagCompound();
			if(stack != null){
				stack.writeToNBT(stackTag);
			}
			tag.setTag("item_" + i, stackTag);
		}
		tag.setTag("output", shaped.getRecipeOutput().writeToNBT(new NBTTagCompound()));
		return tag;
	}
	
	private static ShapedRecipes shapedFromNBT(NBTTagCompound tag){
		int width = tag.getInteger("width");
		int height = tag.getInteger("height");
		ItemStack[] items = new ItemStack[9];
		for(int i = 0; i < 9; i++){
			NBTTagCompound stackTag = tag.getCompoundTag("item_" + i);
			if(stackTag.hasNoTags()){
				items[i] = null;
				continue;
			}else{
				items[i] = new ItemStack(stackTag);
			}
		}
		ItemStack output = new ItemStack(tag.getCompoundTag("output"));
		return new ShapedRecipes(width, height, items, output);
	}
	
	public static WorkbenchManager getInstance(){
		return WorkbenchBlock.recipes;
	}
	
}
