package talecraft.client.gui.blocks;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import talecraft.TaleCraft;
import talecraft.container.WorkbenchContainer;
import talecraft.network.packets.WorkbenchCraftingPacket;

public class GuiWorkbench extends GuiContainer {

	private static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/crafting_table.png");
	
	private WorkbenchContainer container;
	
	public GuiWorkbench(WorkbenchContainer container) {
		super(container);
		this.container = container;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		if(Minecraft.getMinecraft().player.isCreative()){
			this.buttonList.add(new GuiButton(0, this.guiLeft + xSize + 5, this.guiTop, 100, 20,"Add Recipe"));
			this.buttonList.add(new GuiButton(1, this.guiLeft + xSize + 5, this.guiTop + 30, 100, 20,"Remove Recipe"));
			this.buttonList.add(new GuiButton(2, this.guiLeft + xSize + 5, this.guiTop + 60, 100, 20,"Clear Recipes"));
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(CRAFTING_TABLE_GUI_TEXTURES);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		this.fontRenderer.drawString("Workbench", 28, 6, 4210752);
		this.fontRenderer.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button.id == 0 || button.id == 1){
			boolean empty = true;
			NonNullList<Ingredient> ingredients = NonNullList.create();
			for(int i = 0; i < container.craftMatrix.getSizeInventory(); i++){
				if(!container.craftMatrix.getStackInSlot(i).isEmpty()){
					empty = false;
				}
				ingredients.add(Ingredient.fromStacks(container.craftMatrix.getStackInSlot(i)));
			}
			if(!empty){
				if(!container.getSlot(0).getStack().isEmpty()){
					if(button.id == 0){
						TaleCraft.network.sendToServer(new WorkbenchCraftingPacket(new ShapedRecipes(null, 3, 3, ingredients, container.getSlot(0).getStack()), true));
					}else{
						TaleCraft.network.sendToServer(new WorkbenchCraftingPacket(new ShapedRecipes(null, 3, 3, ingredients, container.getSlot(0).getStack()), false));
					}
				}
			}
		}else{
			TaleCraft.network.sendToServer(new WorkbenchCraftingPacket());
		}
	}

}
