package talecraft.client.gui.blocks;

import net.minecraft.util.math.BlockPos;
import talecraft.client.gui.invoke.BlockInvokeHolder;
import talecraft.client.gui.invoke.InvokePanelBuilder;
import talecraft.client.gui.qad.QADGuiScreen;
import talecraft.client.gui.qad.QADLabel;
import talecraft.tileentity.BlockUpdateDetectorTileEntity;

public class GuiUpdateDetectorBlock extends QADGuiScreen {
	BlockUpdateDetectorTileEntity tileEntity;

	public GuiUpdateDetectorBlock(BlockUpdateDetectorTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}

	@Override
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();
		addComponent(new QADLabel("Block Update Detector Trigger @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
		InvokePanelBuilder.build(this, this, 2, 16, tileEntity.getInvoke(), new BlockInvokeHolder(position, "detectorInvoke"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);
	}

}
