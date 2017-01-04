package talecraft.client.gui.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import talecraft.TaleCraft;
import talecraft.client.ClientNetworkHandler;
import talecraft.client.gui.qad.QADButton;
import talecraft.client.gui.qad.QADFACTORY;
import talecraft.client.gui.qad.QADGuiScreen;
import talecraft.client.gui.qad.QADLabel;
import talecraft.client.gui.qad.QADSlider;
import talecraft.client.gui.qad.model.DefaultSliderModel;
import talecraft.network.packets.StringNBTCommandPacket;
import talecraft.tileentity.LightBlockTileEntity;

public class GuiLightBlock extends QADGuiScreen {
	LightBlockTileEntity tileEntity;

	public GuiLightBlock(LightBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
		this.setDoesPauseGame(false);
	}

	@Override
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();
		addComponent(new QADLabel("Light @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));

		final QADSlider slider = addComponent(new QADSlider(new DefaultSliderModel(tileEntity.getLightValue(), 15)));
		slider.setX(2);
		slider.setY(16);
		slider.setSliderAction(new Runnable() {
			@Override public void run() {
				int newValue = (int) (slider.getSliderValue() * 16);
				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "set");
				commandData.setInteger("lightValue", MathHelper.clamp(newValue, 0, 15));
				TaleCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
			}
		});

		QADButton buttonToggle = QADFACTORY.createButton("Toggle", 2, 40, 100, new Runnable() {
			@Override public void run() {
				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "toggle");
				TaleCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
			}
		});
		addComponent(buttonToggle);

		QADButton buttonEnable = QADFACTORY.createButton("Enable", 2 + 100 + 2, 40, 50, new Runnable() {
			@Override public void run() {
				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "on");
				TaleCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
			}
		});
		addComponent(buttonEnable);

		QADButton buttonDisable = QADFACTORY.createButton("Disable", 2 + 100 + 2 + 50 + 2, 40, 50, new Runnable() {
			@Override public void run() {
				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "off");
				TaleCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
			}
		});
		addComponent(buttonDisable);
	}

}
