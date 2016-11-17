package talecraft.client.gui.blocks;

import net.minecraft.util.math.BlockPos;
import talecraft.client.gui.qad.QADGuiScreen;
import talecraft.client.gui.qad.QADLabel;
import talecraft.tileentity.CameraBlockTileEntity;

public class GuiCameraBlock extends QADGuiScreen {
	private CameraBlockTileEntity te;

	public GuiCameraBlock(CameraBlockTileEntity tileEntity) {
		this.te = tileEntity;
	}

	@Override
	public void buildGui() {
		removeAllComponents();
		BlockPos position = te.getPosition();
		addComponent(new QADLabel("Camera Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
	}
}
