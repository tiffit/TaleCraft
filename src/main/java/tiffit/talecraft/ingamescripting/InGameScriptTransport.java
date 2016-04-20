package tiffit.talecraft.ingamescripting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

public class InGameScriptTransport{

	public String name;
	public List<String> content;
	
	public InGameScriptTransport(String name, List<String> content){
		this.name = name;
		this.content = content;
	}
	
	public NBTTagCompound getNBT(){
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("name", name);
		tag.setInteger("size", content.size());
		for(int i = 0; i < content.size(); i++){
			tag.setString("content_" + i, content.get(i));
		}
		return tag;
	}
	
	public static InGameScriptTransport fromNBT(NBTTagCompound tag){
		String name = tag.getString("name");
		List<String> content = new ArrayList<String>();
		for(int i = 0; i < tag.getInteger("size"); i++){
			content.add(tag.getString("content_" + i));
		}
		return new InGameScriptTransport(name, content);
	}
	
}
