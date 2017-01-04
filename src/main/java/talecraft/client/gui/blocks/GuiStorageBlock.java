package talecraft.client.gui.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import talecraft.TaleCraft;
import talecraft.client.ClientNetworkHandler;
import talecraft.client.gui.qad.QADFACTORY;
import talecraft.client.gui.qad.QADGuiScreen;
import talecraft.client.gui.qad.QADLabel;
import talecraft.items.WandItem;
import talecraft.network.packets.StringNBTCommandPacket;
import talecraft.tileentity.StorageBlockTileEntity;

public class GuiStorageBlock extends QADGuiScreen {
	private StorageBlockTileEntity tileEntity;

	public GuiStorageBlock(StorageBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}

	@Override
	public void buildGui() {
		final BlockPos position = tileEntity.getPos();
		addComponent(new QADLabel("Storage Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));

		addComponent(QADFACTORY.createButton("Set Region & Store", 2, 16 + (22*0), 100, new Runnable() {
			@Override
			public void run() {
				int[] bounds = WandItem.getBoundsFromPlayerOrNull(mc.player);

				if(bounds == null)
					return;

				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "set");
				commandData.setIntArray("bounds", bounds);
				TaleCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
				GuiStorageBlock.this.mc.displayGuiScreen(null);
			}
		}));

		addComponent(QADFACTORY.createButton("Store", 2, 16+(22*1), 100, new Runnable() {
			@Override
			public void run() {
				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "store");
				TaleCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
				GuiStorageBlock.this.mc.displayGuiScreen(null);
			}
		}));

		addComponent(QADFACTORY.createButton("Trigger (Paste)", 2, 16+(22*2), 100, new Runnable() {
			@Override
			public void run() {
				String commandString = ClientNetworkHandler.makeBlockCommand(position);
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "trigger");
				TaleCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
				GuiStorageBlock.this.mc.displayGuiScreen(null);
			}
		}));

	}

}
