package de.longor.talecraft.client.gui.blocks;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.util.tileentity.ImageHologramBlockTileEntity;
import de.longor.talecraft.client.ClientNetworkHandler;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADComponent;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.client.gui.qad.QADTextField;
import de.longor.talecraft.network.StringNBTCommand;

public class GuiImageHologramBlock extends QADGuiScreen {
	ImageHologramBlockTileEntity tileEntity;
	
	public GuiImageHologramBlock(ImageHologramBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}
	
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();
		addComponent(new QADLabel("Image Hologram Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
		
		int[] colp = new int[4];
		int[] colw = new int[4];
		int[] rowp = new int[7];
		
		colp[0] = 2;
		colw[0] = 100;
		colp[1] = colp[0] + colw[0] + 4;
		colw[1] = 80;
		colp[2] = colp[1] + colw[1] + 4;
		colw[2] = 80;
		colp[3] = colp[2] + colw[2] + 4;
		colw[3] = 80;
		
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
		
		addComponent(QADFACTORY.createLabel("Texture Path",colp[0], rowp[0]+sro));
		
		addComponent(QADFACTORY.createLabel("X"       , colp[1] + colw[1]/2, rowp[2]+sro));
		addComponent(QADFACTORY.createLabel("Y"       , colp[2] + colw[2]/2, rowp[2]+sro));
		addComponent(QADFACTORY.createLabel("Z"       , colp[3] + colw[3]/2, rowp[2]+sro));
		
		addComponent(QADFACTORY.createLabel("Offset"  , colp[0], rowp[3]+sro));
		addComponent(QADFACTORY.createLabel("Rotation", colp[0], rowp[4]+sro));
		addComponent(QADFACTORY.createLabel("Size"    , colp[0], rowp[5]+sro));
		
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
		
		final QADTextField rotationPitchTextField = QADFACTORY.createTextField(tileEntity.getHologramPitch(), colp[1], rowp[4]+tfc, colw[1]);
		final QADTextField rotationYawTextField = QADFACTORY.createTextField(tileEntity.getHologramYaw(), colp[2], rowp[4]+tfc, colw[2]);
		rotationPitchTextField.setTooltip("Rotation forward/backward. (Pitch)");
		rotationYawTextField.setTooltip  ("Rotation left/right. (Yaw)");
		addComponent(rotationPitchTextField);
		addComponent(rotationYawTextField);
		
		final QADTextField textureWidthTextField = QADFACTORY.createTextField(tileEntity.getHologramWidth(), colp[1], rowp[5]+tfc, colw[1]);
		final QADTextField textureHeightTextField = QADFACTORY.createTextField(tileEntity.getHologramHeight(), colp[2], rowp[5]+tfc, colw[2]);
		textureWidthTextField .setTooltip("Width of the hologram.");
		textureHeightTextField.setTooltip("Height of the hologram.");
		addComponent(textureWidthTextField);
		addComponent(textureHeightTextField);
		
		QADButton applyButton = QADFACTORY.createButton("Apply", colp[1], rowp[6], colw[1], null);
		applyButton.setEnabled(true);
		applyButton.setAction(new Runnable() {
			@Override public void run() {
				NBTTagCompound commandComp = new NBTTagCompound();
				
				commandComp.setString("var_texture", texturePathTextField.getText());
				
				commandComp.setFloat("var_offsetX", parseFloat(offsetXTextField.getText(), tileEntity.getHologramOffsetX(), -128, +128));
				commandComp.setFloat("var_offsetY", parseFloat(offsetYTextField.getText(), tileEntity.getHologramOffsetY(), -128, +128));
				commandComp.setFloat("var_offsetZ", parseFloat(offsetZTextField.getText(), tileEntity.getHologramOffsetZ(), -128, +128));
				
				commandComp.setFloat("var_pitch", parseFloat(rotationPitchTextField.getText(), tileEntity.getHologramPitch(), -360, +360));
				commandComp.setFloat("var_yaw", parseFloat(rotationYawTextField.getText(), tileEntity.getHologramYaw(), -360, +360));
				
				commandComp.setFloat("var_width", parseFloat(textureWidthTextField.getText(), tileEntity.getHologramWidth(), -1000, 1000));
				commandComp.setFloat("var_height", parseFloat(textureHeightTextField.getText(), tileEntity.getHologramHeight(), -1000, 1000));
				
				// Final
				commandComp.setString("command", "set_vars");

				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, commandComp));
				GuiImageHologramBlock.this.mc.displayGuiScreen(null);
			}
		});
		addComponent(applyButton);
		
	}
	
	

}
