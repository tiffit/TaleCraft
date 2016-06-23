package tiffit.talecraft.client.gui.voxelator;

import java.util.ArrayList;
import java.util.List;

import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADComponent;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADScrollPanel;
import de.longor.talecraft.client.gui.qad.layout.QADListLayout;
import de.longor.talecraft.util.Vec2i;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;

public class BlockStateSelector extends QADGuiScreen {
	private VoxelatorGui voxGui;
	private QADScrollPanel panel;
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
		RegistryNamespacedDefaultedByKey<ResourceLocation, Block> blocks = Block.REGISTRY;
		for(final Block block : blocks) {
			QADButton component = new QADButton(block.getLocalizedName());
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
		}
		panel.setLayout(new QADListLayout());
		addComponent(panel);
	}
	@Override
	public void layoutGui(){
		panel.setSize(this.width, this.height);
	}

}
