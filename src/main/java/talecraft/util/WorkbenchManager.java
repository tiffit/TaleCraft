package talecraft.util;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
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
			ItemStack[] stacks = shaped.recipeItems.get(i).getMatchingStacks();
			NBTTagCompound stackTag = new NBTTagCompound();
			for(ItemStack stack : stacks) {
				if(!stack.isEmpty()){
					stack.writeToNBT(stackTag);
				}
			}
			tag.setTag("item_" + i, stackTag);
		}
		tag.setTag("output", shaped.getRecipeOutput().writeToNBT(new NBTTagCompound()));
		return tag;
	}
	
	private static ShapedRecipes shapedFromNBT(NBTTagCompound tag){
		int width = tag.getInteger("width");
		int height = tag.getInteger("height");
		NonNullList<Ingredient> ingredients = NonNullList.create();
		for(int i = 0; i < 9; i++){
			NBTTagCompound stackTag = tag.getCompoundTag("item_" + i);
			if(stackTag.hasNoTags()){
				ingredients.add(Ingredient.fromStacks(ItemStack.EMPTY));
				continue;
			}else{
				ingredients.add(Ingredient.fromStacks(new ItemStack(stackTag)));
			}
		}
		ItemStack output = new ItemStack(tag.getCompoundTag("output"));
		return new ShapedRecipes(null, width, height, ingredients, output);
	}
	
	public static WorkbenchManager getInstance(){
		return WorkbenchBlock.recipes;
	}
	
}
