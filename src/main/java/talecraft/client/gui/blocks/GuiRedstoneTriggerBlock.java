package talecraft.client.gui.blocks;

import net.minecraft.util.math.BlockPos;
import talecraft.client.gui.invoke.BlockInvokeHolder;
import talecraft.client.gui.invoke.InvokePanelBuilder;
import talecraft.client.gui.qad.QADGuiScreen;
import talecraft.client.gui.qad.QADLabel;
import talecraft.tileentity.RedstoneTriggerBlockTileEntity;

public class GuiRedstoneTriggerBlock extends QADGuiScreen {
	RedstoneTriggerBlockTileEntity tileEntity;

	public GuiRedstoneTriggerBlock(RedstoneTriggerBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}

	@Override
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();

		addComponent(new QADLabel("Redstone Trigger @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
		InvokePanelBuilder.build(this, this, 2, 16, tileEntity.getInvokeOn(),  new BlockInvokeHolder(position, "triggerInvokeOn"),  InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);
		InvokePanelBuilder.build(this, this, 2, 42, tileEntity.getInvokeOff(), new BlockInvokeHolder(position, "triggerInvokeOff"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);
	}

}
