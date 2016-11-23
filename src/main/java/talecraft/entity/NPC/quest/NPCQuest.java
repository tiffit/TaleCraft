package talecraft.entity.NPC.quest;

import net.minecraft.entity.player.EntityPlayerMP;
import talecraft.entity.NPC.EntityNPC;

public abstract class NPCQuest {

	private boolean finished;
	private String start_message;
	private String ongoing_message;
	private String end_message;
	
	public NPCQuest(String start_message, String ongoing_message, String end_message){
		finished = false;
		this.start_message = start_message;
		this.ongoing_message = ongoing_message;
		this.end_message = end_message;
	}
	
	public boolean isFinished(){
		return finished;
	}
	
	public void setFinished(boolean finished){
		this.finished = finished;
	}
	
	public String getStartMessage(){
		return start_message;
	}
	
	public String getOnGoingMessage(){
		return ongoing_message;
	}
	
	public String getEndMessage(){
		return end_message;
	}
	
	public abstract void update(EntityPlayerMP player, EntityNPC npc);
	
}
