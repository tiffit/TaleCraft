package de.longor.talecraft.client.gui.blocks;

import java.util.ArrayList;

import net.minecraft.util.BlockPos;
import de.longor.talecraft.blocks.util.tileentity.CollisionTriggerBlockTileEntity;
import de.longor.talecraft.client.gui.invoke.BlockInvokeHolder;
import de.longor.talecraft.client.gui.invoke.InvokePanelBuilder;
import de.longor.talecraft.client.gui.qad.QADComponent;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;

public class GuiCollisionTriggerBlock extends QADGuiScreen {
	CollisionTriggerBlockTileEntity tileEntity;
	
	public GuiCollisionTriggerBlock(CollisionTriggerBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}
	
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();
		addComponent(new QADLabel("Collision Trigger @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
		InvokePanelBuilder.build(this, this, 2, 16, tileEntity.getCollisionStartInvoke(), new BlockInvokeHolder(position, "collisionStartTrigger"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);
		InvokePanelBuilder.build(this, this, 2, 16+2+20, tileEntity.getCollisionStopInvoke(), new BlockInvokeHolder(position, "collisionStopTrigger"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);
		
		// TODO: Add entityFilter input+apply
		
	}
	
	
	
}
