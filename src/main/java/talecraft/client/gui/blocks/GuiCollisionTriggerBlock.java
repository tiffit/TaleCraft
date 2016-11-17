package talecraft.client.gui.blocks;

import net.minecraft.util.math.BlockPos;
import talecraft.client.gui.invoke.BlockInvokeHolder;
import talecraft.client.gui.invoke.InvokePanelBuilder;
import talecraft.client.gui.qad.QADGuiScreen;
import talecraft.client.gui.qad.QADLabel;
import talecraft.tileentity.CollisionTriggerBlockTileEntity;

public class GuiCollisionTriggerBlock extends QADGuiScreen {
	CollisionTriggerBlockTileEntity tileEntity;

	public GuiCollisionTriggerBlock(CollisionTriggerBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}

	@Override
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();
		addComponent(new QADLabel("Collision Trigger @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
		InvokePanelBuilder.build(this, this, 2, 16, tileEntity.getCollisionStartInvoke(), new BlockInvokeHolder(position, "collisionStartTrigger"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);
		InvokePanelBuilder.build(this, this, 2, 16+2+20, tileEntity.getCollisionStopInvoke(), new BlockInvokeHolder(position, "collisionStopTrigger"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);

		// TODO: Add entityFilter input+apply

	}



}
