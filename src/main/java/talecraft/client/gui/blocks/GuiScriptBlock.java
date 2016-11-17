package talecraft.client.gui.blocks;

import net.minecraft.util.math.BlockPos;
import talecraft.client.gui.invoke.BlockInvokeHolder;
import talecraft.client.gui.invoke.InvokePanelBuilder;
import talecraft.client.gui.qad.QADGuiScreen;
import talecraft.client.gui.qad.QADLabel;
import talecraft.tileentity.ScriptBlockTileEntity;

public class GuiScriptBlock extends QADGuiScreen {
	ScriptBlockTileEntity tileEntity;

	public GuiScriptBlock(ScriptBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}

	@Override
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();
		addComponent(new QADLabel("Script Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
		InvokePanelBuilder.build(this, this, 2, 16, tileEntity.getInvoke(), new BlockInvokeHolder(position, "scriptInvoke"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOW_SCRIPTFILE | InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOW_SCRIPTEMBEDDED);
	}
}
