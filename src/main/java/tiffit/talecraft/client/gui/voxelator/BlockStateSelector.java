package tiffit.talecraft.client.gui.voxelator;

import java.util.List;

import de.longor.talecraft.client.gui.blocks.GuiEmitterBlock;
import de.longor.talecraft.client.gui.blocks.GuiEmitterBlock.GuiEmitterBlockParticleTypes;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADScrollPanel;
import de.longor.talecraft.util.GObjectTypeHelper;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStateSelector extends QADGuiScreen {
	private QADScrollPanel panel;
	private VoxelatorGui voxGui;
	private boolean add = true;
	private int index = 0;
	
	public BlockStateSelector(VoxelatorGui voxGui) {
		this.setBehind(voxGui);
		this.returnScreen = voxGui;
		this.voxGui = voxGui;
	}
	
	public BlockStateSelector(VoxelatorGui voxGui, boolean add, int index) {
		this.setBehind(voxGui);
		this.returnScreen = voxGui;
		this.voxGui = voxGui;
		this.add = add;
		this.index = index;
	}

	@Override
	public void buildGui() {
		panel = new QADScrollPanel();
		panel.setPosition(0, 0);
		panel.setSize(200, 200);
		this.addComponent(panel);

		final int rowHeight = 20;

		RegistryNamespacedDefaultedByKey<ResourceLocation, Block> blocks = Block.blockRegistry;
		panel.setViewportHeight(blocks.getKeys().size() * rowHeight + 2);
		panel.allowLeftMouseButtonScrolling = true;

		int yOff = 1;
		for(final Block block : blocks) {
			QADButton component = QADFACTORY.createButton(block.getLocalizedName(), 2, yOff, 200 - 8, null);
			component.simplified = true;
			component.textAlignment = 0;
			component.setAction( new Runnable() {
				@Override public void run() {
					if(add)voxGui.blocks.add(block);
					else voxGui.blocks.set(index, block);
					displayGuiScreen(getBehind());
				}
			});

			panel.addComponent(component);
			yOff += rowHeight;
		}
	}

	@Override
	public void layoutGui() {
		panel.setSize(this.width, this.height);
	}

}
