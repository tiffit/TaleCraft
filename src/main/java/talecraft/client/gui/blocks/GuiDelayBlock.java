package talecraft.client.gui.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import talecraft.TaleCraft;
import talecraft.client.ClientNetworkHandler;
import talecraft.client.gui.invoke.BlockInvokeHolder;
import talecraft.client.gui.invoke.InvokePanelBuilder;
import talecraft.client.gui.qad.QADGuiScreen;
import talecraft.client.gui.qad.QADLabel;
import talecraft.client.gui.qad.QADSlider;
import talecraft.client.gui.qad.model.DefaultSliderModel;
import talecraft.network.packets.StringNBTCommandPacket;
import talecraft.tileentity.DelayBlockTileEntity;

public class GuiDelayBlock extends QADGuiScreen {
	DelayBlockTileEntity tileEntity;
	QADSlider slider;

	public GuiDelayBlock(DelayBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}

	@Override
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();

		addComponent(new QADLabel("Delay Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
		InvokePanelBuilder.build(this, this, 2, 16, tileEntity.getInvoke(), new BlockInvokeHolder(position, "triggerInvoke"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);

		final int maximum = 20 * 10;
		slider = addComponent(new QADSlider(new DefaultSliderModel(tileEntity.getDelayValue(), maximum)));
		slider.setWidth(width-2-2);
		slider.setX(2);
		slider.setY(16+2+20+2);
		slider.setTooltip("ylock","Delay in ticks.","Max: "+maximum);
		slider.setSliderAction(new Runnable() {
			@Override public void run() {
				int newValue = (int) (slider.getSliderValue() * maximum);
				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "set");
				commandData.setInteger("delay", MathHelper.clamp(newValue, 1, maximum));
				TaleCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
			}
		});
	}

	@Override
	public void layoutGui() {
		slider.setWidth(width-2-2);
	}

}
