package talecraft.client.gui.misc;

import java.time.OffsetDateTime;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import talecraft.TaleCraft;
import talecraft.client.gui.qad.QADComponent;
import talecraft.client.gui.qad.QADDevider;
import talecraft.client.gui.qad.QADGuiScreen;
import talecraft.client.gui.qad.QADLabel;
import talecraft.client.gui.qad.QADPanel;
import talecraft.client.gui.vcui.VCUIRenderer;
import talecraft.network.packets.UndoPacket;

public class GuiUndo extends QADGuiScreen {
	
	private NBTTagCompound tag;
	
	public GuiUndo(NBTTagCompound tag) {
		this.tag = tag;
	}
	
	@Override
	public void buildGui() {
		QADLabel toolTitle = new QADLabel("Tool", width/8*1, 5, 0xff0000);
		toolTitle.setCentered();
		addComponent(toolTitle);
		
		QADLabel userTitle = new QADLabel("User", width/8*3, 5, 0xff0000);
		userTitle.setCentered();
		addComponent(userTitle);
		
		QADLabel changesTitle = new QADLabel("Blocks Changed", width/8*5, 5, 0xff0000);
		changesTitle.setCentered();
		addComponent(changesTitle);
		
		QADLabel timeTitle = new QADLabel("Time", width/8*7, 5, 0xff0000);
		timeTitle.setCentered();
		addComponent(timeTitle);
		
		NBTTagList list = tag.getTagList("list", (int)tag.getId());
		int spacing = 13;
		for(int i = 0; i < list.tagCount(); i++){
			QADPanel panel = new QADPanel();
			panel.setBackgroundColor(1);
			panel.setBounds(0, (8 + list.tagCount()*(spacing))-(i*(spacing)), getWidth(), spacing);
			NBTTagCompound comp = list.getCompoundTagAt(i);
			QADLabel tool = new QADLabel(comp.getString("tool"), width/8, 3);
			tool.setCentered();
			panel.addComponent(tool);
			
			int color = 0xFFFFFFFF;
			if(comp.getString("user").equals(mc.player.getName()))color = 0xffedd209;
			QADLabel user = new QADLabel(comp.getString("user"), width/8*3, 3, color);
			user.setCentered();
			panel.addComponent(user);
			
			QADLabel changes = new QADLabel(comp.getInteger("changes") + " blocks", width/8*5, 3);
			changes.setCentered();
			panel.addComponent(changes);
			
			QADLabel time = new QADLabel(getFormattedTime(comp.getString("time")), width/8*7, 3);
			time.setCentered();
			panel.addComponent(time);
			panel.setName(i + "");
			addComponent(panel);
		}
		
		addComponent(new QADDevider(width/4, true));
		addComponent(new QADDevider(width/4*2, true));
		addComponent(new QADDevider(width/4*3, true));
		addComponent(new QADDevider(20, false));
	}
	
	@Override
	public void drawCustom(int mouseX, int mouseY, float partialTicks, VCUIRenderer instance2) {
		for(QADComponent comp : getComponents()){
			if(comp instanceof QADPanel){
				QADPanel panel = (QADPanel) comp;
				if(comp.isPointInside(mouseX, mouseY)){
					panel.setBackgroundColor(0);
				}else{
					panel.setBackgroundColor(1);
				}
			}
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		for(QADComponent comp : getComponents()){
			if(comp instanceof QADPanel){
				if(comp.isPointInside(mouseX, mouseY)){
					System.out.println("loop");
					QADPanel panel = (QADPanel) comp;
					final int panelIndex = Integer.valueOf(panel.getName());
					GuiYesNoQuestion confirm = new GuiYesNoQuestion(this, "Are you sure you want to undo this action?", new Runnable() {
						
						@Override
						public void run() {
							TaleCraft.network.sendToServer(new UndoPacket(panelIndex));
							tag.getTagList("list", (int)tag.getId()).removeTag(panelIndex);
							forceRebuildAll();
							displayGuiScreen(null);
						}
					});
					displayGuiScreen(confirm);
				}
			}
		}
	}
	
	private String getFormattedTime(String preform){
		OffsetDateTime datetime = OffsetDateTime.parse(preform);
		OffsetDateTime now = OffsetDateTime.now();
		long second_diff = now.toEpochSecond() - datetime.toEpochSecond();
		if(second_diff < 60)return "< 1 minute ago";
		if(second_diff < 60*60){
			int minuteDiff = (int) (second_diff/60);
			String minuteText = " minutes";
			if(minuteDiff == 1) minuteText = " minute";
			return "~ " + minuteDiff + minuteText + " ago";
		}
		
		if(second_diff < 60*60*24){
			int hourDiff = (int) (second_diff/(60*60));
			String hourText = " hours";
			if(hourDiff == 1) hourText = " hour";
			return "~ " + hourDiff + hourText + " ago";
		}
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(datetime.getMonthValue());
		builder.append("/");
		
		builder.append(datetime.getDayOfMonth());
		builder.append("/");
		
		builder.append(datetime.getYear());
		builder.append(" ");
		
		builder.append(datetime.getHour());
		builder.append(":");
		builder.append(datetime.getMinute());
	
		
		return builder.toString();
	}
}
