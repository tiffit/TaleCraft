package tiffit.talecraft.client.gui;

import de.longor.talecraft.TCSoundHandler.SoundEnum;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.ClientNetworkHandler;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.client.gui.qad.QADScrollPanel;
import de.longor.talecraft.client.gui.qad.QADTickBox;
import de.longor.talecraft.client.gui.qad.QADTickBox.TickBoxModel;
import de.longor.talecraft.network.StringNBTCommandPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import tiffit.talecraft.tileentity.MusicBlockTileEntity;

public class GuiMusicBlock extends QADGuiScreen {
	MusicBlockTileEntity tileEntity;
	
	boolean mute;
	SoundEnum sound;

	public GuiMusicBlock(MusicBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
		sound = tileEntity.getSound();
		mute = tileEntity.isMute();
	}

	@Override
	public void buildGui() {
		removeAllComponents();
		BlockPos position = tileEntity.getPosition();
		addComponent(new QADTickBox(120, 17, new TickBoxModel(){

			@Override
			public void setState(boolean newState) {
				mute = newState;
			}

			@Override
			public boolean getState() {
				return mute;
			}

			@Override
			public void toggleState() {
				mute = !mute;
			}
			
		}).setTooltip("Instead of playing a sound,","mute all sounds."));
		addComponent(new QADLabel("Song Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
		addComponent(QADFACTORY.createButton("Sound: " + sound.toString(), 10, 15, 100, new Runnable(){
			@Override
			public void run() {
				Minecraft.getMinecraft().displayGuiScreen(new MusicBlockSoundTypes(GuiMusicBlock.this));
			}
			
		}));
		addComponent(QADFACTORY.createButton("Save", width - 60, height - 30, 50, new Runnable(){
			@Override
			public void run() {
				NBTTagCompound commandData = new NBTTagCompound();
				String commandString = ClientNetworkHandler.makeBlockCommand(tileEntity.getPosition());
				commandData.setString("command", "sound");
				commandData.setBoolean("mute", mute);
				commandData.setString("sound", sound.toString());
				TaleCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
				displayGuiScreen(null);
			}
			
		}));
	}
	
	

	@Override
	public void layoutGui() {
	}
	
	public class MusicBlockSoundTypes extends QADGuiScreen {
		private QADScrollPanel panel;
		private GuiMusicBlock gui;
		public MusicBlockSoundTypes(GuiMusicBlock musicBlock) {
			this.setBehind(musicBlock);
			this.returnScreen = gui = musicBlock;
		}

		@Override
		public void buildGui() {
			panel = new QADScrollPanel();
			panel.setPosition(0, 0);
			panel.setSize(200, 200);
			this.addComponent(panel);

			final int rowHeight = 20;

			SoundEnum[] sounds = SoundEnum.values();
			panel.setViewportHeight(sounds.length * rowHeight + 2);
			panel.allowLeftMouseButtonScrolling = true;

			int yOff = 1;
			for(final SoundEnum sound : sounds) {
				final QADButton component = QADFACTORY.createButton(sound.name(), 2, yOff, 200 - 8, null);
				component.simplified = true;
				component.textAlignment = 0;

				component.setAction( new Runnable() {
					@Override public void run() {
						gui.sound = sound;
						((QADGuiScreen)getBehind()).buildGui();
						displayGuiScreen(getBehind());
					}
				});

				panel.addComponent(component);
				yOff += rowHeight;
			}
		}

		@Override
		public void layoutGui() {
			panel.setSize(this.width, this.height);
		}

	}



}
