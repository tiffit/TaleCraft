package tiffit.talecraft.packet;

import java.util.ArrayList;
import java.util.List;

import de.longor.talecraft.client.gui.items.GuiDecorator;
import de.longor.talecraft.decorator.Decoration;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DecoratorGuiPacket implements IMessage {
	NBTTagCompound tag;
	
	public DecoratorGuiPacket() {
	}
	
	public DecoratorGuiPacket(List<Decoration> decor, NBTTagCompound tag){
		NBTTagList tl = new NBTTagList();
		for(Decoration d : decor){
			tl.appendTag(new NBTTagString(d.name()));
		}
		tag.setTag("decorations_list", tl);
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

	public static class Handler implements IMessageHandler<DecoratorGuiPacket, IMessage> {
		@Override
		public IMessage onMessage(DecoratorGuiPacket message, MessageContext ctx) {
			List<String> list = new ArrayList<String>();
			NBTTagList tl = message.tag.getTagList("decorations_list", 8);
			for(int i = 0; i < tl.tagCount(); i++){
				list.add(tl.getStringTagAt(i));
			}
			Minecraft.getMinecraft().displayGuiScreen(new GuiDecorator(list, message.tag));
			return null;
		}
	}
}
