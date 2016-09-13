package tiffit.talecraft.client.gui;

import java.util.ArrayList;
import java.util.List;

import de.longor.talecraft.TCSoundHandler.SoundEnum;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.ClientNetworkHandler;
import de.longor.talecraft.client.gui.qad.QADDropdownBox;
import de.longor.talecraft.client.gui.qad.QADDropdownBox.ListModel;
import de.longor.talecraft.client.gui.qad.QADDropdownBox.ListModelItem;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.client.gui.qad.QADNumberTextField;
import de.longor.talecraft.client.gui.qad.QADTickBox;
import de.longor.talecraft.client.gui.vcui.VCUIRenderer;
import de.longor.talecraft.network.StringNBTCommandPacket;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import tiffit.talecraft.tileentity.MusicBlockTileEntity;

public class GuiMusicBlock extends QADGuiScreen {
	MusicBlockTileEntity tileEntity;
	QADTickBox muteBox;
	QADDropdownBox soundBox;
	QADTickBox repeatBox;
	QADNumberTextField delayField;
	int repeat_delay;
	SoundEnum sound;

	public GuiMusicBlock(MusicBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
		sound = tileEntity.getSound();
	}

	@Override
	public void buildGui() {
		removeAllComponents();
		BlockPos position = tileEntity.getPosition();
		muteBox = new QADTickBox(120, 17);
		muteBox.getModel().setState(tileEntity.isMute());
		muteBox.setTooltip("Instead of playing a sound,","mute all sounds.");
		addComponent(muteBox);
		repeatBox = new QADTickBox(120, 44);
		repeatBox.getModel().setState(tileEntity.isRepeat());
		repeatBox.setTooltip("Repeat?");
		delayField = new QADNumberTextField(fontRendererObj, 10, 40, 75, 20, tileEntity.repeatDelay());
		delayField.setTooltip("Repeat Delay");
		addComponent(delayField);
		addComponent(repeatBox);
		addComponent(new QADLabel("Music Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
		soundBox = new QADDropdownBox(new MusicListModel(), new MusicModelItem(tileEntity.getSound()));
		soundBox.setBounds(10, 15, 100, 20);
		addComponent(soundBox);
		addComponent(QADFACTORY.createButton("Save", width - 60, height - 30, 50, new Runnable(){
			@Override
			public void run() {
				NBTTagCompound commandData = new NBTTagCompound();
				String commandString = ClientNetworkHandler.makeBlockCommand(tileEntity.getPosition());
				commandData.setString("command", "sound");
				commandData.setBoolean("mute", muteBox.getState());
				commandData.setString("sound", sound.toString());
				commandData.setBoolean("repeat", repeatBox.getState());
				commandData.setInteger("repeat_delay", delayField.getValue().intValue());
				TaleCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
				displayGuiScreen(null);
			}
			
		}));
	}
	
	private class MusicListModel implements ListModel{

		private final List<ListModelItem> items = new ArrayList<ListModelItem>();
		private final List<ListModelItem> filtered = new ArrayList<ListModelItem>();
		
		public MusicListModel() {
			for(SoundEnum sound : SoundEnum.values()){
				items.add(new MusicModelItem(sound));
			}
			filtered.addAll(filtered);
		}
		
		@Override
		public void onSelection(ListModelItem selected) {
			sound = ((MusicModelItem)selected).sound;
		}

		@Override
		public boolean hasItems() {return true;}

		@Override
		public int getItemCount() {
			return items.size();
		}

		@Override
		public List<ListModelItem> getItems() {
			return items;
		}

		@Override
		public void applyFilter(String filter) {
			filtered.clear();
			for(ListModelItem item : items){
				MusicModelItem sound = (MusicModelItem) item;
				if(sound.sound.toString().toLowerCase().contains(filter.toLowerCase())){
					filtered.add(item);
				}
			}
		}

		@Override
		public List<ListModelItem> getFilteredItems() {
			return filtered;
		}

		@Override
		public boolean hasIcons() {return false;}

		@Override
		public void drawIcon(VCUIRenderer renderer, float partialTicks, boolean light, ListModelItem item) {}
		
	}
	
	private class MusicModelItem implements ListModelItem{

		private SoundEnum sound;
		
		public MusicModelItem(SoundEnum sound){
			this.sound = sound;
		}
		@Override
		public String getText() {
			return sound.toString();
		}
		
		@Override
		public boolean equals(Object obj){
			if(obj instanceof MusicModelItem){
				MusicModelItem mmi = (MusicModelItem) obj;
				return mmi.sound == sound;
			}return false;
		}
		@Override
		public int hashCode() {
			return sound.hashCode();
		}
	}
	
}
