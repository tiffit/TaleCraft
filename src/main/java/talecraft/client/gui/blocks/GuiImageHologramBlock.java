package talecraft.client.gui.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import talecraft.TaleCraft;
import talecraft.client.ClientNetworkHandler;
import talecraft.client.gui.qad.QADButton;
import talecraft.client.gui.qad.QADFACTORY;
import talecraft.client.gui.qad.QADGuiScreen;
import talecraft.client.gui.qad.QADLabel;
import talecraft.client.gui.qad.QADTextField;
import talecraft.client.gui.qad.QADTickBox;
import talecraft.client.gui.qad.model.DefaultTickBoxModel;
import talecraft.network.packets.StringNBTCommandPacket;
import talecraft.tileentity.ImageHologramBlockTileEntity;

public class GuiImageHologramBlock extends QADGuiScreen {
	ImageHologramBlockTileEntity tileEntity;

	public GuiImageHologramBlock(ImageHologramBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}

	@Override
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();
		addComponent(new QADLabel("Image Hologram Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));

		int[] colp = new int[5];
		int[] colw = new int[5];
		int[] rowp = new int[9];

		colp[0] = 2;
		colw[0] = 100;
		colp[1] = colp[0] + colw[0] + 4;
		colw[1] = 80;
		colp[2] = colp[1] + colw[1] + 4;
		colw[2] = 80;
		colp[3] = colp[2] + colw[2] + 4;
		colw[3] = 80;
		colp[4] = colp[3] + colw[3] + 4;
		colw[4] = 22;

		int rowh = 20; // row height
		int tfc = 2;
		int sro = 6; // string row offset
		rowp[0] = 16;
		rowp[1] = rowp[0] + rowh + 0;
		rowp[2] = rowp[1] + rowh + 0;
		rowp[3] = rowp[2] + rowh + 0;
		rowp[4] = rowp[3] + rowh + 0;
		rowp[5] = rowp[4] + rowh + 0;
		rowp[6] = rowp[5] + rowh + 0;
		rowp[7] = rowp[6] + rowh + 0;
		rowp[8] = rowp[7] + rowh + 0;

		addComponent(QADFACTORY.createLabel("Texture Path",colp[0], rowp[0]+sro));

		addComponent(QADFACTORY.createLabel("X"       , colp[1] + colw[1]/2, rowp[2]+sro));
		addComponent(QADFACTORY.createLabel("Y"       , colp[2] + colw[2]/2, rowp[2]+sro));
		addComponent(QADFACTORY.createLabel("Z"       , colp[3] + colw[3]/2, rowp[2]+sro));

		addComponent(QADFACTORY.createLabel("Offset"  , colp[0], rowp[3]+sro));
		addComponent(QADFACTORY.createLabel("Rotation", colp[0], rowp[4]+sro));
		addComponent(QADFACTORY.createLabel("Size"    , colp[0], rowp[5]+sro));
		addComponent(QADFACTORY.createLabel("UV-Scale", colp[0], rowp[6]+sro));

		addComponent(QADFACTORY.createLabel("Color"   , colp[0], rowp[7]+sro));

		final QADTextField texturePathTextField = QADFACTORY.createTextField(tileEntity.getTextureLocation(), colp[1], rowp[0]+tfc, colw[1]+colw[2]+colw[3]+6);
		texturePathTextField.setTooltip("The path of the texture to display.");
		addComponent(texturePathTextField);

		final QADTextField offsetXTextField = QADFACTORY.createTextField(tileEntity.getHologramOffsetX(), colp[1], rowp[3]+tfc, colw[1]);
		final QADTextField offsetYTextField = QADFACTORY.createTextField(tileEntity.getHologramOffsetY(), colp[2], rowp[3]+tfc, colw[2]);
		final QADTextField offsetZTextField = QADFACTORY.createTextField(tileEntity.getHologramOffsetZ(), colp[3], rowp[3]+tfc, colw[3]);
		offsetXTextField.setTooltip("Offset of the hologram from","this block on the X-Axis.");
		offsetYTextField.setTooltip("Offset of the hologram from","this block on the Y-Axis.");
		offsetZTextField.setTooltip("Offset of the hologram from","this block on the Z-Axis.");
		addComponent(offsetXTextField);
		addComponent(offsetYTextField);
		addComponent(offsetZTextField);
		
		final QADTickBox offsetRelativeTickbox = new QADTickBox(colp[4], rowp[3]+tfc, new DefaultTickBoxModel(tileEntity.getHologramOffsetRelative()));
		offsetRelativeTickbox.setTooltip("If ticked the hologram is rendered","relative to the player.");
		addComponent(offsetRelativeTickbox);
		
		final QADTextField rotationPitchTextField = QADFACTORY.createTextField(tileEntity.getHologramPitch(), colp[1], rowp[4]+tfc, colw[1]);
		final QADTextField rotationYawTextField = QADFACTORY.createTextField(tileEntity.getHologramYaw(), colp[2], rowp[4]+tfc, colw[2]);
		rotationPitchTextField.setTooltip("Rotation forward/backward. (Pitch)");
		rotationYawTextField.setTooltip  ("Rotation left/right. (Yaw)");
		addComponent(rotationPitchTextField);
		addComponent(rotationYawTextField);

		final QADTextField textureWidthTextField = QADFACTORY.createTextField(tileEntity.getHologramWidth(), colp[1], rowp[5]+tfc, colw[1]);
		final QADTextField textureHeightTextField = QADFACTORY.createTextField(tileEntity.getHologramHeight(), colp[2], rowp[5]+tfc, colw[2]);
		textureWidthTextField .setTooltip("Width of the hologram.","If negative, texture is tiled over surface horizontally.");
		textureHeightTextField.setTooltip("Height of the hologram.","If negative, texture is tiled over surface vertically.");
		addComponent(textureWidthTextField);
		addComponent(textureHeightTextField);

		final QADTextField textureUscaleTextField = QADFACTORY.createTextField(tileEntity.getHologramUscale(), colp[1], rowp[6]+tfc, colw[1]);
		final QADTextField textureVscaleTextField = QADFACTORY.createTextField(tileEntity.getHologramVscale(), colp[2], rowp[6]+tfc, colw[2]);
		textureUscaleTextField.setTooltip("Texture X-scale of the hologram.");
		textureVscaleTextField.setTooltip("Texture Y-scale of the hologram.");
		addComponent(textureUscaleTextField);
		addComponent(textureVscaleTextField);

		final QADTextField colorTextField = QADFACTORY.createTextField(tileEntity.getHologramColor(), colp[1], rowp[7]+tfc, colw[1]);
		colorTextField.setTooltip(
				"Color of the hologram as hexadecimal ARGB integer.",
				"Example: FFFFFFFF = White",
				"Example: 7F00FFFF = Transparent Cyan"
		);
		addComponent(colorTextField);
		colorTextField.setEnabled(false);

		QADButton applyButton = QADFACTORY.createButton("Apply", colp[1], rowp[8], colw[1], null);
		applyButton.setEnabled(true);
		applyButton.setAction(new Runnable() {
			@Override public void run() {
				NBTTagCompound commandComp = new NBTTagCompound();

				commandComp.setString("var_texture", texturePathTextField.getText());

				commandComp.setFloat("var_offsetX", parseFloat(offsetXTextField.getText(), tileEntity.getHologramOffsetX(), -128, +128));
				commandComp.setFloat("var_offsetY", parseFloat(offsetYTextField.getText(), tileEntity.getHologramOffsetY(), -128, +128));
				commandComp.setFloat("var_offsetZ", parseFloat(offsetZTextField.getText(), tileEntity.getHologramOffsetZ(), -128, +128));
				commandComp.setBoolean("var_offsetRelative", offsetRelativeTickbox.getState());

				commandComp.setFloat("var_pitch", parseFloat(rotationPitchTextField.getText(), tileEntity.getHologramPitch(), -360, +360));
				commandComp.setFloat("var_yaw", parseFloat(rotationYawTextField.getText(), tileEntity.getHologramYaw(), -360, +360));

				commandComp.setFloat("var_width", parseFloat(textureWidthTextField.getText(), tileEntity.getHologramWidth(), -1000, 1000));
				commandComp.setFloat("var_height", parseFloat(textureHeightTextField.getText(), tileEntity.getHologramHeight(), -1000, 1000));

				commandComp.setFloat("var_uscale", parseFloat(textureUscaleTextField.getText(), tileEntity.getHologramUscale(), -1000, 1000));
				commandComp.setFloat("var_vscale", parseFloat(textureVscaleTextField.getText(), tileEntity.getHologramVscale(), -1000, 1000));

				commandComp.setInteger("var_color", parseInt(colorTextField.getText(), tileEntity.getHologramColor(), Integer.MIN_VALUE, Integer.MAX_VALUE));

				// Final
				commandComp.setString("command", "set_vars");

				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				TaleCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandComp));
				GuiImageHologramBlock.this.mc.displayGuiScreen(null);
			}
		});
		addComponent(applyButton);

	}



}
