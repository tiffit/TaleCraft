package de.longor.talecraft.client.gui.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.util.tileentity.ClockBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.EmitterBlockTileEntity;
import de.longor.talecraft.client.ClientNetworkHandler;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADComponent;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.client.gui.qad.QADScrollPanel;
import de.longor.talecraft.client.gui.qad.QADTextField;
import de.longor.talecraft.network.StringNBTCommand;
import de.longor.talecraft.util.GObjectTypeHelper;

public class GuiEmitterBlock extends QADGuiScreen {
	EmitterBlockTileEntity tileEntity;
	
	QADTextField particleTypeTextField;
	
	public GuiEmitterBlock(EmitterBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}
	
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();
		addComponent(new QADLabel("Emitter Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
		
		int[] colp = new int[4];
		int[] colw = new int[4];
		
		colp[0] = 2;
		colw[0] = 100;
		colp[1] = colp[0] + colw[0] + 4;
		colw[1] = 80;
		colp[2] = colp[1] + colw[1] + 4;
		colw[2] = 80;
		colp[3] = colp[2] + colw[2] + 4;
		colw[3] = 80;
		
		int[] rowp = new int[9]; // row positions
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
		
		addComponent(QADFACTORY.createLabel("Particle Type"     , colp[0], rowp[0]+sro));
		addComponent(QADFACTORY.createLabel("Spawn Count"       , colp[0], rowp[1]+sro));
		
		addComponent(QADFACTORY.createLabel("X"       , colp[1] + colw[1]/2, rowp[2]+sro));
		addComponent(QADFACTORY.createLabel("Y"       , colp[2] + colw[2]/2, rowp[2]+sro));
		addComponent(QADFACTORY.createLabel("Z"       , colp[3] + colw[3]/2, rowp[2]+sro));
		
		addComponent(QADFACTORY.createLabel("Offset"            , colp[0], rowp[3]+sro));
		addComponent(QADFACTORY.createLabel("Offset Randomize"  , colp[0], rowp[4]+sro));
		addComponent(QADFACTORY.createLabel("Velocity"          , colp[0], rowp[5]+sro));
		addComponent(QADFACTORY.createLabel("Velocity Randomize", colp[0], rowp[6]+sro));
		
		addComponent(QADFACTORY.createLabel("Actions", colp[0], rowp[8]+sro));
		
		particleTypeTextField = QADFACTORY.createTextField(tileEntity.getParticleType(), colp[1], rowp[0]+tfc, colw[1]);
		particleTypeTextField.setTooltip("The particle type to emit.");
		addComponent(particleTypeTextField);
		
		addComponent(QADFACTORY.createButton("?", colp[2], rowp[0], 20, new Runnable() {
			@Override public void run() {
				displayGuiScreen(new GuiEmitterBlockParticleTypes(GuiEmitterBlock.this));
			}
		}));
		
		final QADTextField spawnCountTextField = QADFACTORY.createNumberTextField(tileEntity.getSpawnCount(), colp[1], rowp[1]+tfc, colw[1], 1000, 0);
		spawnCountTextField.setTooltip("How many particles to spawn per tick.");
		addComponent(spawnCountTextField);
		
		final QADTextField offsetXTextField = QADFACTORY.createTextField(tileEntity.getOffsetX(), colp[1], rowp[3]+tfc, colw[1]);
		final QADTextField offsetYTextField = QADFACTORY.createTextField(tileEntity.getOffsetY(), colp[2], rowp[3]+tfc, colw[2]);
		final QADTextField offsetZTextField = QADFACTORY.createTextField(tileEntity.getOffsetZ(), colp[3], rowp[3]+tfc, colw[3]);
		offsetXTextField.setTooltip("Offset from the emitter-block","on the X-Axis.");
		offsetYTextField.setTooltip("Offset from the emitter-block","on the Y-Axis.");
		offsetZTextField.setTooltip("Offset from the emitter-block","on the Z-Axis.");
		addComponent(offsetXTextField);
		addComponent(offsetYTextField);
		addComponent(offsetZTextField);
		
		final QADTextField offsetRandXTextField = QADFACTORY.createTextField(tileEntity.getOffsetRandX(), colp[1], rowp[4]+tfc, colw[1]);
		final QADTextField offsetRandYTextField = QADFACTORY.createTextField(tileEntity.getOffsetRandY(), colp[2], rowp[4]+tfc, colw[2]);
		final QADTextField offsetRandZTextField = QADFACTORY.createTextField(tileEntity.getOffsetRandZ(), colp[3], rowp[4]+tfc, colw[3]);
		offsetRandXTextField.setTooltip("Random offset multiplicator","on the X-Axis.");
		offsetRandYTextField.setTooltip("Random offset multiplicator","on the Y-Axis.");
		offsetRandZTextField.setTooltip("Random offset multiplicator","on the Z-Axis.");
		addComponent(offsetRandXTextField);
		addComponent(offsetRandYTextField);
		addComponent(offsetRandZTextField);
		
		final QADTextField velocityXTextField = QADFACTORY.createTextField(tileEntity.getVelocityX(), colp[1], rowp[5]+tfc, colw[1]);
		final QADTextField velocityYTextField = QADFACTORY.createTextField(tileEntity.getVelocityY(), colp[2], rowp[5]+tfc, colw[2]);
		final QADTextField velocityZTextField = QADFACTORY.createTextField(tileEntity.getVelocityZ(), colp[3], rowp[5]+tfc, colw[3]);
		velocityXTextField.setTooltip("Initial velocity on the X-Axis.");
		velocityYTextField.setTooltip("Initial velocity on the Y-Axis.");
		velocityZTextField.setTooltip("Initial velocity on the Z-Axis.");
		addComponent(velocityXTextField);
		addComponent(velocityYTextField);
		addComponent(velocityZTextField);
		
		final QADTextField velocityRandXTextField = QADFACTORY.createTextField(tileEntity.getVelocityRandX(), colp[1], rowp[6]+tfc, colw[1]);
		final QADTextField velocityRandYTextField = QADFACTORY.createTextField(tileEntity.getVelocityRandY(), colp[2], rowp[6]+tfc, colw[2]);
		final QADTextField velocityRandZTextField = QADFACTORY.createTextField(tileEntity.getVelocityRandZ(), colp[3], rowp[6]+tfc, colw[3]);
		velocityRandXTextField.setTooltip("Random velocity multiplicator on X-Axis.","0 to disable.");
		velocityRandYTextField.setTooltip("Random velocity multiplicator on Y-Axis.","0 to disable.");
		velocityRandZTextField.setTooltip("Random velocity multiplicator on Z-Axis.","0 to disable.");
		addComponent(velocityRandXTextField);
		addComponent(velocityRandYTextField);
		addComponent(velocityRandZTextField);
		
		QADButton applyButton = QADFACTORY.createButton("Apply", colp[1], rowp[8], colw[1], null);
		applyButton.setEnabled(true);
		applyButton.setAction(new Runnable() {
			@Override public void run() {
				NBTTagCompound commandComp = new NBTTagCompound();
				commandComp.setString("var_type", particleTypeTextField.getText());
				commandComp.setInteger("var_spawnCount", parseInt(spawnCountTextField.getText(),tileEntity.getSpawnCount(), 1, 1000));
				
				commandComp.setFloat("var_offsetX", parseFloat(offsetXTextField.getText(), tileEntity.getOffsetX(), -128, +128));
				commandComp.setFloat("var_offsetY", parseFloat(offsetYTextField.getText(), tileEntity.getOffsetY(), -128, +128));
				commandComp.setFloat("var_offsetZ", parseFloat(offsetZTextField.getText(), tileEntity.getOffsetZ(), -128, +128));
				
				commandComp.setFloat("var_offsetRandX", parseFloat(offsetRandXTextField.getText(), tileEntity.getOffsetRandX(), 0, +128));
				commandComp.setFloat("var_offsetRandY", parseFloat(offsetRandYTextField.getText(), tileEntity.getOffsetRandY(), 0, +128));
				commandComp.setFloat("var_offsetRandZ", parseFloat(offsetRandZTextField.getText(), tileEntity.getOffsetRandZ(), 0, +128));
				
				commandComp.setFloat("var_velocityX", parseFloat(velocityXTextField.getText(), tileEntity.getVelocityX(), -10, +10));
				commandComp.setFloat("var_velocityY", parseFloat(velocityYTextField.getText(), tileEntity.getVelocityY(), -10, +10));
				commandComp.setFloat("var_velocityZ", parseFloat(velocityZTextField.getText(), tileEntity.getVelocityZ(), -10, +10));
				
				commandComp.setFloat("var_velocityRandX", parseFloat(velocityRandXTextField.getText(), tileEntity.getVelocityRandX(), 0, +2));
				commandComp.setFloat("var_velocityRandY", parseFloat(velocityRandYTextField.getText(), tileEntity.getVelocityRandY(), 0, +2));
				commandComp.setFloat("var_velocityRandZ", parseFloat(velocityRandZTextField.getText(), tileEntity.getVelocityRandZ(), 0, +2));
				
				// Final
				commandComp.setString("command", "set_vars");

				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, commandComp));
				GuiEmitterBlock.this.mc.displayGuiScreen(null);
			}
		});
		addComponent(applyButton);
		
		QADButton toggleButton = QADFACTORY.createButton("Toggle", colp[2], rowp[8], colw[2], null);
		toggleButton.setEnabled(false);
		addComponent(toggleButton);
		
		QADButton resetButton = QADFACTORY.createButton(EnumChatFormatting.RED+"Reset", colp[3], rowp[8], colw[3], null);
		resetButton.setEnabled(false);
		addComponent(resetButton);
		
		
	}
	
	
	
	public class GuiEmitterBlockParticleTypes extends QADGuiScreen {
		private QADScrollPanel panel;
		
		public GuiEmitterBlockParticleTypes(GuiEmitterBlock guiEmitterBlock) {
			this.setBehind(guiEmitterBlock);
			this.returnScreen = guiEmitterBlock;
		}
		
		public void buildGui() {
			panel = new QADScrollPanel();
			panel.setPosition(0, 0);
			panel.setSize(200, 200);
			this.addComponent(panel);
			
			final int rowHeight = 20;
			
			List<String> names = GObjectTypeHelper.getParticleNameList();
			names.sort(String.CASE_INSENSITIVE_ORDER);
			panel.setViewportHeight(names.size() * rowHeight + 2);
			panel.allowLeftMouseButtonScrolling = true;
			
			int yOff = 1;
			for(final String string : names) {
				QADButton component = null;
				
				component = QADFACTORY.createButton(string, 2, yOff, 200 - 8, null);
				component.simplified = true;
				component.textAlignment = 0;
				
				if(string.endsWith("_")) {
					component.setEnabled(false);
				} else {
					component.setAction( new Runnable() {
						final String pt = string;
						@Override public void run() {
							particleTypeTextField.setText(pt);
							displayGuiScreen(GuiEmitterBlockParticleTypes.this.getBehind());
						}
					});
				}
				
				panel.addComponent(component);
				yOff += rowHeight;
			}
		}
		
		public void layoutGui() {
			panel.setSize(this.width, this.height);
		}
		
	}
	
}
