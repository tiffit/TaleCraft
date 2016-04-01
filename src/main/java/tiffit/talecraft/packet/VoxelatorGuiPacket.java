package tiffit.talecraft.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tiffit.talecraft.client.gui.voxelator.VoxelatorGui;

public class VoxelatorGuiPacket implements IMessage {
	
	public VoxelatorGuiPacket() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public static class Handler implements IMessageHandler<VoxelatorGuiPacket, IMessage> {

		@Override
		public IMessage onMessage(VoxelatorGuiPacket message, MessageContext ctx) {
			Minecraft mc = Minecraft.getMinecraft();
			InventoryPlayer inv = mc.thePlayer.inventory;
			int currentItem = inv.currentItem;
			NBTTagCompound tag = inv.getCurrentItem().getTagCompound();
			mc.displayGuiScreen(new VoxelatorGui(currentItem, tag));
			return null;
		}
	}
}
