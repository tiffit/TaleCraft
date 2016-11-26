package talecraft.network.packets;

import java.util.List;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import talecraft.entity.NPC.EntityNPC;
import talecraft.entity.NPC.dialogue.NPCDialogue;
import talecraft.entity.NPC.dialogue.NPCDialogue.NPCDialogueOption;
import talecraft.script.wrappers.entity.NPCObjectWrapper;

public class NPCDialogueOptionPacket implements IMessage {

	private UUID id;
	private String dialogue;
	private String option;
	private UUID npcid;

	public NPCDialogueOptionPacket() {
	}

	public NPCDialogueOptionPacket(UUID id, String dialogue, String option, UUID npcid) {
		this.id = id;
		this.dialogue = dialogue;
		this.option = option;
		this.npcid = npcid;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		id = UUID.fromString(ByteBufUtils.readUTF8String(buf));
		dialogue = ByteBufUtils.readUTF8String(buf);
		option = ByteBufUtils.readUTF8String(buf);
		npcid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, id.toString());
		ByteBufUtils.writeUTF8String(buf, dialogue);
		ByteBufUtils.writeUTF8String(buf, option);
		ByteBufUtils.writeUTF8String(buf, npcid.toString());
	}

	public static class Handler implements IMessageHandler<NPCDialogueOptionPacket, IMessage> {

		@Override
		public IMessage onMessage(NPCDialogueOptionPacket message, MessageContext ctx) {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			EntityNPC npc = (EntityNPC) server.getEntityFromUuid(message.npcid);
			List<NPCDialogue> dialogues = NPCObjectWrapper.created_dialogues_map.get(message.id);
			for(NPCDialogue dlog : dialogues){
				if(dlog.getName().equals(message.dialogue)){
					for(NPCDialogueOption opt : dlog.getOptions()){
						if(opt.option.equals(message.option)){
							opt.handleClickServer(npc);
							break;
						}
					}
					break;
				}
			}
			return null;
		}
	}
}
