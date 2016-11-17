package talecraft.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.client.gui.items.voxelator.GuiVoxelator;

public class VoxelatorGuiPacket implements IMessage {
	
	NBTTagCompound tag;
	
	public VoxelatorGuiPacket() {
	}
	
	public VoxelatorGuiPacket(NBTTagCompound tag){
		this.tag = tag;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		tag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, tag);
	}

	public static class Handler implements IMessageHandler<VoxelatorGuiPacket, IMessage> {
		@Override
		public IMessage onMessage(VoxelatorGuiPacket message, MessageContext ctx) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiVoxelator(message.tag));
			return null;
		}
	}
}
