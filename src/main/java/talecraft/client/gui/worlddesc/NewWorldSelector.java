package talecraft.client.gui.worlddesc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListWorldSelectionEntry;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiWorldSelection;
import talecraft.util.ReflectionUtil;

import com.google.common.collect.Lists;

public class NewWorldSelector extends GuiWorldSelection {

	GuiButton readDescription;
	GuiListWorldSelectionEntry currently_selected_world;
	List<String> desc_lines;
	public NewWorldSelector() {
		super(new GuiMainMenu());
	}
	
    @Override
		public void postInit(){ //Used to add buttons
    	super.postInit();
        this.buttonList.add(this.readDescription = new GuiButton(6, this.width - 110, 2, 100, 20, "World Description"));
        this.readDescription.enabled = false;
    }
    
    @Override
		public void selectWorld(GuiListWorldSelectionEntry entry){
    	super.selectWorld(entry);
    	currently_selected_world = entry;
        boolean flag = entry != null;
        File folder = ReflectionUtil.getWorldFolderFromSelection(entry); //Pretty hacky way of getting the world file. I get the image for the world and then get the parent.
        if(folder == null){
        	readDescription.enabled = false;
        	return;
        }
        if(folder.getParentFile().getName().equals("saves")){ //Make sure I am in the saves folder
        	for(File f : folder.listFiles()){
        		if(f.getName().equals("desc.txt")){
        			try{
        				FileReader fr = new FileReader(f);
        				BufferedReader reader = new BufferedReader(fr);
        				List<String> lines = Lists.newArrayList();
        				String line;
        				while((line = reader.readLine()) != null){
        					lines.add(line);
        				}
        				desc_lines = lines;
        				readDescription.enabled = true;
        				fr.close();
        				reader.close();
        			}catch(Exception e){
        				e.printStackTrace();
        			}
    				return;
        		}
        	}
        }
        readDescription.enabled = false;
    }
    
    @Override
		public void actionPerformed(GuiButton button) throws IOException{
    	super.actionPerformed(button);
    	if(button.id == 6){
    		Minecraft.getMinecraft().displayGuiScreen(new WorldDescGui(desc_lines, ReflectionUtil.getWorldSummary(currently_selected_world).getDisplayName()));
    	}
    }

}
