package talecraft.network;

import static net.minecraftforge.fml.relauncher.Side.CLIENT;
import static net.minecraftforge.fml.relauncher.Side.SERVER;
import static talecraft.TaleCraft.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.relauncher.Side;
import talecraft.entity.NPC.dialogue.NPCDialogue.NPCDialogueOption;
import talecraft.network.packets.DecoratorGuiPacket;
import talecraft.network.packets.DecoratorPacket;
import talecraft.network.packets.DialogueOpenPacket;
import talecraft.network.packets.DoorPacket;
import talecraft.network.packets.FadePacket;
import talecraft.network.packets.ForceF1Packet;
import talecraft.network.packets.GunReloadPacket;
import talecraft.network.packets.NPCDataPacket;
import talecraft.network.packets.NPCDataUpdatePacket;
import talecraft.network.packets.NPCDialogueOptionPacket;
import talecraft.network.packets.NPCOpenPacket;
import talecraft.network.packets.PlayerNBTDataMergePacket;
import talecraft.network.packets.SoundsPacket;
import talecraft.network.packets.StringNBTCommandPacket;
import talecraft.network.packets.StringNBTCommandPacketClient;
import talecraft.network.packets.TriggerItemPacket;
import talecraft.network.packets.VoxelatorGuiPacket;
import talecraft.network.packets.VoxelatorPacket;
import talecraft.network.packets.WorkbenchCraftingPacket;

public class TaleCraftNetwork {

	private static int discriminator = 0;
	
	public static void preInit(){
		network = NetworkRegistry.INSTANCE.newSimpleChannel("TalecraftNet");

		register(PlayerNBTDataMergePacket.Handler.class, PlayerNBTDataMergePacket.class, CLIENT);
		register(VoxelatorGuiPacket.Handler.class, VoxelatorGuiPacket.class, CLIENT);
		register(DoorPacket.Handler.class, DoorPacket.class, CLIENT);
		register(VoxelatorPacket.Handler.class, VoxelatorPacket.class, SERVER);
		register(NPCDataPacket.Handler.class, NPCDataPacket.class, SERVER);
		register(NPCOpenPacket.Handler.class, NPCOpenPacket.class, CLIENT);
		register(NPCDataUpdatePacket.Handler.class, NPCDataUpdatePacket.class, CLIENT);
		register(SoundsPacket.Handler.class, SoundsPacket.class, CLIENT);
		register(FadePacket.Handler.class, FadePacket.class, CLIENT);
		register(GunReloadPacket.Handler.class, GunReloadPacket.class, SERVER);
		register(WorkbenchCraftingPacket.Handler.class, WorkbenchCraftingPacket.class, SERVER);
		register(StringNBTCommandPacket.Handler.class, StringNBTCommandPacket.class, SERVER);
		register(StringNBTCommandPacketClient.Handler.class, StringNBTCommandPacketClient.class, CLIENT);
		register(ForceF1Packet.Handler.class, ForceF1Packet.class, CLIENT);
		register(DecoratorPacket.Handler.class, DecoratorPacket.class, SERVER);
		register(DecoratorGuiPacket.Handler.class, DecoratorGuiPacket.class, CLIENT);
		register(TriggerItemPacket.Handler.class, TriggerItemPacket.class, SERVER);
		register(DialogueOpenPacket.Handler.class, DialogueOpenPacket.class, CLIENT);
		register(NPCDialogueOptionPacket.Handler.class, NPCDialogueOptionPacket.class, SERVER);
	}
	
	private static <REQ extends IMessage, REPLY extends IMessage> void register(Class<? extends IMessageHandler<REQ, REPLY>> handler, Class<REQ> packet, Side side){
		network.registerMessage(handler, packet, discriminator, side);
		discriminator++;
	}
	
}
