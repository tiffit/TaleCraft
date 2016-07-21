package tiffit.talecraft.client.gui;

import de.longor.talecraft.TCSoundHandler.SoundEnum;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.ClientNetworkHandler;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.client.gui.qad.QADScrollPanel;
import de.longor.talecraft.client.gui.qad.QADTickBox;
import de.longor.talecraft.client.gui.qad.QADTickBox.TickBoxModel;
import de.longor.talecraft.network.StringNBTCommandPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import tiffit.talecraft.tileentity.CameraBlockTileEntity;
import tiffit.talecraft.tileentity.MusicBlockTileEntity;

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
