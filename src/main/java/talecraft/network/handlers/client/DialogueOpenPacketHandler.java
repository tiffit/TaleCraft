package talecraft.network.handlers.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import talecraft.client.gui.entity.npc.GuiNPCDialogue;
import talecraft.entity.NPC.dialogue.NPCDialogue;
import talecraft.network.packets.DialogueOpenPacket;

public class DialogueOpenPacketHandler{
	
	public static void handle(DialogueOpenPacket message){
		String main = message.tag.getString("main");
		NPCDialogue maindialogue = null;
		List<NPCDialogue> dialogues = new ArrayList<NPCDialogue>();
		NBTTagList list = message.tag.getTagList("dialogue_list", 10);
		for(int i = 0; i < list.tagCount(); i++){
			NBTTagCompound comp = list.getCompoundTagAt(i);
			NPCDialogue dialogue = NPCDialogue.fromNBT(comp);
			dialogues.add(dialogue);
			if(dialogue.getName().equals(main))maindialogue = dialogue;
		}
		Minecraft.getMinecraft().displayGuiScreen(new GuiNPCDialogue(maindialogue, message.tag.getUniqueId("id"), dialogues,  message.tag.getUniqueId("npcid")));
	}
}
