package talecraft.network.packets;

import java.util.List;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.entity.NPC.dialogue.NPCDialogue;
import talecraft.network.handlers.client.DialogueOpenPacketHandler;

public class DialogueOpenPacket implements IMessage {
	public NBTTagCompound tag;
	
	public DialogueOpenPacket() {
	}
	
	public DialogueOpenPacket(String main_dialogue, UUID id, List<NPCDialogue> dialogue, UUID uuid){
		tag = new NBTTagCompound();
		NBTTagList tl = new NBTTagList();
		for(NPCDialogue d : dialogue){
			tl.appendTag(d.getNBT());
		}
		tag.setTag("dialogue_list", tl);
		tag.setString("main", main_dialogue);
		tag.setUniqueId("id", id);
		tag.setUniqueId("npcid", uuid);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		tag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, tag);
	}
	
	public static class Handler implements IMessageHandler<DialogueOpenPacket, IMessage> {
		
		@Override
		public IMessage onMessage(DialogueOpenPacket message, MessageContext ctx) {
			DialogueOpenPacketHandler.handle(message);
			return null;
		}
	}
}
