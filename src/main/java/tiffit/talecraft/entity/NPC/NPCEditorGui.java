package tiffit.talecraft.entity.NPC;

import java.util.UUID;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADButton.ButtonModel;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.client.gui.qad.QADSlider;
import de.longor.talecraft.client.gui.qad.QADSlider.SliderModel;
import de.longor.talecraft.client.gui.qad.QADTextField;
import de.longor.talecraft.client.gui.qad.QADTextField.TextFieldModel;
import de.longor.talecraft.client.gui.qad.QADTickBox;
import de.longor.talecraft.client.gui.qad.QADTickBox.TickBoxModel;
import net.minecraft.util.ResourceLocation;
import tiffit.talecraft.entity.NPC.NPCSkinEnum.NPCSkin;
import tiffit.talecraft.packet.NPCDataPacket;

public class NPCEditorGui extends QADGuiScreen {

	NPCData data;
	String name;
	String message;
	boolean showname;
	boolean movable;
	boolean namemsg;
	boolean invulnerable;
	float pitch;
	float yaw;
	boolean eyesfollow;
	UUID uuid;
	NPCSkin skin;
	
	public NPCEditorGui(NPCData data, UUID uuid){
		this.data = data;
		this.name = data.getName();
		this.message = data.getMessage();
		this.showname = data.shouldShowName();
		this.movable = data.isMovable();
		this.namemsg = data.shouldIncludeNameInMessage();
		this.invulnerable = data.isInvulnerable();
		this.pitch = data.getPitch();
		this.yaw = data.getYaw();
		this.eyesfollow = data.doEyesFollow();
		this.uuid = uuid;
		this.skin = data.getSkin();
	}
	
	public void buildGui() {
		addComponent(new QADLabel("NPC: " + name, 2, 2));
		
		QADTickBox invulnerablebox = new QADTickBox(3, 25, new TickBoxModel(){

			@Override
			public void setState(boolean newState) {
				invulnerable = newState;
			}

			@Override
			public boolean getState() {
				return invulnerable;
			}

			@Override
			public void toggleState() {
				invulnerable = !invulnerable;
			}
			
		});
		addComponent(invulnerablebox.setTooltip("Is the entity invulnerable?"));
		
		QADTickBox shownameBox = new QADTickBox(3, 50, new TickBoxModel(){

			@Override
			public void setState(boolean newState) {
				showname = newState;
			}

			@Override
			public boolean getState() {
				return showname;
			}

			@Override
			public void toggleState() {
				showname = !showname;
			}
			
		});
		addComponent(shownameBox.setTooltip("Should the name be shown?"));
		
		QADTextField nameField = (QADTextField) QADFACTORY.createTextField(this.name, 20, 50, 100).setName("name");
		nameField.setModel(new TextFieldModel(){
			
			int color = 0xFFFFFFFF;
			
			@Override
			public String getText() {
				return name;
			}

			@Override
			public int getTextLength() {
				return message.length();
			}

			@Override
			public char getCharAt(int i) {
				return name.charAt(i);
			}

			@Override
			public void setText(String text) {
				name = text;
			}

			@Override
			public void setTextColor(int color) {
				this.color = color;
			}

			@Override
			public int getTextColor() {
				return color;
			}
			
		});
		addComponent(nameField.setTooltip("The Name of the NPC"));
		
		QADTickBox namemsgbox = new QADTickBox(3, 75, new TickBoxModel(){

			@Override
			public void setState(boolean newState) {
				namemsg = newState;
			}

			@Override
			public boolean getState() {
				return namemsg;
			}

			@Override
			public void toggleState() {
				namemsg = !namemsg;
			}
			
		});
		addComponent(namemsgbox.setTooltip("Should the NPC's name be included in the message?"));
		
		QADTextField messageField = (QADTextField) QADFACTORY.createTextField(this.message, 20, 75, this.getWidth()/2 - 25).setName("message");
		messageField.setModel(new TextFieldModel(){
			
			int color = 0xFFFFFFFF;
			
			@Override
			public String getText() {
				return message;
			}

			@Override
			public int getTextLength() {
				return message.length();
			}

			@Override
			public char getCharAt(int i) {
				return message.charAt(i);
			}

			@Override
			public void setText(String text) {
				message = text;
			}

			@Override
			public void setTextColor(int color) {
				this.color = color;
			}

			@Override
			public int getTextColor() {
				return color;
			}
			
		});
		addComponent(messageField.setTooltip("The message to be sent to the player when right-clicked."));
		
		QADButton save = (QADButton) QADFACTORY.createButton("", this.width - 80, this.height - 30, 75).setModel(new ButtonModel(){

			@Override
			public void onClick() {
				data.setInvulnerable(invulnerable);
				data.setName(name);
				data.setMessage(message);
				data.setShowName(showname);
				data.setNameInMessage(namemsg);
				data.setPitch(pitch);
				data.setYaw(yaw);
				data.setEyesFollow(eyesfollow);
				data.setSkin(skin);
				TaleCraft.network.sendToServer(new NPCDataPacket(uuid, data.toNBT()));
				NPCEditorGui.this.mc.displayGuiScreen(null);
			}

			@Override
			public String getText() {
				return "Save";
			}

			@Override
			public ResourceLocation getIcon() {
				return null;
			}

			@Override
			public void setText(String newText) {
			}

			@Override
			public void setIcon(ResourceLocation newIcon) {
			}
		}).setTooltip("Apply Changes").setName("save");
		addComponent(save);
		
		QADTickBox eyesfollowbox = new QADTickBox(3, 100, new TickBoxModel(){

			@Override
			public void setState(boolean newState) {
				eyesfollow = newState;
			}

			@Override
			public boolean getState() {
				return eyesfollow;
			}

			@Override
			public void toggleState() {
				eyesfollow = !eyesfollow;
			}
			
		});
		addComponent(eyesfollowbox.setTooltip("Should the NPC's eyes follow the player?"));
		
		
		QADButton skinselector = QADFACTORY.createButton("Current Skin: " + skin.name(), this.width - 300, 100, 200).setModel(new ButtonModel(){
			@Override
			public void onClick() {
				displayGuiScreen(new NPCSkinSelector(NPCEditorGui.this));
			}

			@Override
			public String getText() {
				return "Selected Skin: " + skin.name();
			}

			@Override
			public ResourceLocation getIcon() {
				return null;
			}

			@Override
			public void setText(String newText) {
			}

			@Override
			public void setIcon(ResourceLocation newIcon) {
			}
			
		});
		addComponent(skinselector);
		
		QADSlider yawSlider = new QADSlider(new SliderModel<Float>(){
			float sliderValue;
			
			@Override
			public void setValue(Float value) {
				sliderValue = value;
				yaw = formattedYaw();
			}

			@Override
			public Float getValue() {
				return sliderValue;
			}

			@Override
			public String getValueAsText() {
				return "Yaw: " + (int) yaw;
			}

			@Override
			public void setSliderValue(float sliderValue){
				this.sliderValue = sliderValue;
				yaw = formattedYaw();
			}

			@Override
			public float getSliderValue() {
				return sliderValue;
			}
			
			private float formattedYaw(){
				return sliderValue*360f;
			}
			
		});
		yawSlider.setPosition(250, 100);
		yawSlider.setWidth(200);
		yawSlider.setSliderValue(yaw/360f);
		addComponent(yawSlider);
		
		QADSlider pitchSlider = new QADSlider(new SliderModel<Float>(){
			float sliderValue;
			
			@Override
			public void setValue(Float value) {
				sliderValue = value;
				pitch = formattedFloat();
			}

			@Override
			public Float getValue() {
				return sliderValue;
			}

			@Override
			public String getValueAsText() {
				return "Pitch: " + (int) pitch;
			}

			@Override
			public void setSliderValue(float sliderValue){
				this.sliderValue = sliderValue;
				pitch = formattedFloat();
			}

			@Override
			public float getSliderValue() {
				return sliderValue;
			}
			
			private float formattedFloat(){
				return sliderValue*180.0f - 90.0f;
			}
			
		});
		pitchSlider.setPosition(20, 100);
		pitchSlider.setWidth(200);
		pitchSlider.setSliderValue((pitch + 90.0f)/180.0f);
		addComponent(pitchSlider);
	}
	
	public void updateGui() {
		setPaused(true);
		
		setPaused(false);
	}
	
	
	
}
