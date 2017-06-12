package talecraft.entity.NPC.dialogue;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import talecraft.entity.NPC.EntityNPC;

public class NPCDialogue {

	private String title;
	private String text;
	private final String name;
	private List<NPCDialogueOption> options;
	
	public static class NPCDialogueOption{
		public String option;
		public String destination;
		public String action;
		
		public NPCDialogueOption(String option){
			this.option = option;
			destination = null;
			this.action = "";
		}
		
		public NPCDialogueOption(String option, String destination){
			this(option);
			this.destination = destination;
		}
		
		public NPCDialogueOption(String option, String destination, String action){
			this(option, destination);
			this.action = action;
		}
		
		public NBTTagCompound getNBT(){
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("option", option);
			if(destination != null)tag.setString("destination", destination);
			tag.setString("action", action);
			return tag;
		}

		//Should ONLY be run on the server side!!
		public void handleClickServer(EntityNPC npc){
			if(action.startsWith("action.nbt=")){
				String[] equalsplit = action.split("=");
				String nbt_json = "";
				for(int i = 1; i < equalsplit.length; i++){
					nbt_json += equalsplit[i];
				}
				NBTTagCompound newNBT;
				try {
					newNBT = JsonToNBT.getTagFromJson(nbt_json);
				} catch (NBTException e) {
					e.printStackTrace();
					return;
				}
				npc.getScriptData().merge(newNBT);
			}
		}
		
		public static NPCDialogueOption fromNBT(NBTTagCompound tag){
			return new NPCDialogueOption(tag.getString("option"), tag.hasKey("destination") ? tag.getString("destination") : null, tag.getString("action"));
		}
		
	}
	
	public NPCDialogue(String name){
		this.name = name;
		this.title = "";
		this.text = "";
		options = new ArrayList<NPCDialogueOption>();
	}

	public String getName(){
		return name;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getText(){
		return text;
	}
	
	public List<NPCDialogueOption> getOptions(){
		return options;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setText(String text){
		this.text = text;
	}
	
	public void addOption(String option, String destination){
		options.add(new NPCDialogueOption(option, destination));
	}
	
	public void addOption(String option){
		options.add(new NPCDialogueOption(option));
	}
	
	public void addOption(String option, String destrination, String action){
		options.add(new NPCDialogueOption(option, destrination, action));
	}
	
	private void addOption(NBTTagCompound comp){
		options.add(NPCDialogueOption.fromNBT(comp));
	}
	
	public NBTTagCompound getNBT(){
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("name", name);
		tag.setString("title", title);
		tag.setString("text", text);
		NBTTagList list = new NBTTagList();
		for(NPCDialogueOption option : options){
			list.appendTag(option.getNBT());
		}
		tag.setTag("options", list);
		return tag;
	}
	
	public static NPCDialogue fromNBT(NBTTagCompound tag){
		NPCDialogue dialogue = new NPCDialogue(tag.getString("name"));
		dialogue.setTitle(tag.getString("title"));
		dialogue.setText(tag.getString("text"));
		NBTTagList list = tag.getTagList("options", 10);
		for(int i = 0; i < list.tagCount(); i++){
			NBTTagCompound comp = list.getCompoundTagAt(i);
			dialogue.addOption(comp);
		}
		return dialogue;
	}
	
}
