package tiffit.talecraft.client.gui;

import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import net.minecraft.util.math.BlockPos;
import tiffit.talecraft.tileentity.CameraBlockTileEntity;

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
