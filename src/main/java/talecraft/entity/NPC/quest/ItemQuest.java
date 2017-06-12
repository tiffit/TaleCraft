package talecraft.entity.NPC.quest;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import talecraft.entity.NPC.EntityNPC;

public class ItemQuest extends NPCQuest {

	private ItemStack request;
	
	public ItemQuest(ItemStack request, String start_message, String ongoing_message, String end_message) {
		super(start_message, ongoing_message, end_message);
		this.request = request;
	}

	@Override
	public void update(EntityPlayerMP player, EntityNPC npc) {
		setFinished(player.inventory.hasItemStack(request));
	}

}
