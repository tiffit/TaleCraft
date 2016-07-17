package tiffit.talecraft.client.gui.npc;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADButton.ButtonModel;
import de.longor.talecraft.client.gui.qad.QADNumberTextField.NumberType;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.client.gui.qad.QADNumberTextField;
import de.longor.talecraft.client.gui.qad.QADSlider;
import de.longor.talecraft.client.gui.qad.QADSlider.SliderModel;
import de.longor.talecraft.client.gui.qad.QADTextField;
import de.longor.talecraft.client.gui.qad.QADTextField.TextFieldModel;
import de.longor.talecraft.client.gui.qad.QADTickBox;
import de.longor.talecraft.client.gui.qad.QADTickBox.TickBoxModel;
import de.longor.talecraft.client.gui.qad.model.AbstractButtonModel;
import de.longor.talecraft.client.gui.qad.model.DefaultTextFieldModel;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import tiffit.talecraft.entity.NPC.EntityNPC.NPCType;
import tiffit.talecraft.entity.NPC.EnumNPCModel;
import tiffit.talecraft.entity.NPC.EnumNPCSkin;
import tiffit.talecraft.entity.NPC.NPCData;
import tiffit.talecraft.entity.NPC.NPCInventoryData.NPCDrop;
import tiffit.talecraft.entity.NPC.NPCShop;
import tiffit.talecraft.packet.NPCDataPacket;

public class NPCEditorGui extends QADGuiScreen {

	NPCData data;
	String name;
	String message;
	boolean showname;
	boolean movable;
	boolean namemsg;
	boolean invulnerable;
	boolean boss;
	float pitch;
	float yaw;
	float damage;
	float health;
	double speed;
	boolean eyesfollow;
	UUID uuid;
	EnumNPCSkin skin;
	NPCType type;
	EnumNPCModel model;
	String interactScript;
	String updateScript;
	String deathScript;
	List<NPCDrop> drops;
	NPCShop shop;
	
	public NPCEditorGui(NPCData data, UUID uuid, String interactScript, String updateScript, String deathScript){
		this.data = data;
		this.name = data.getName();
		this.message = data.getMessage();
		this.showname = data.shouldShowName();
		this.movable = data.isMovable();
		this.namemsg = data.shouldIncludeNameInMessage();
		this.invulnerable = data.isInvulnerable();
		this.boss = data.isBoss();
		this.pitch = data.getPitch();
		this.yaw = data.getYaw();
		this.eyesfollow = data.doEyesFollow();
		this.uuid = uuid;
		this.skin = data.getSkin();
		this.model = data.getModel();
		this.health = data.getHealth();
		this.interactScript = interactScript;
		this.updateScript = updateScript;
		this.deathScript = deathScript;
		this.type = data.getType();
		this.damage = data.getDamage();
		this.speed = data.getSpeed();
		this.drops = data.getDrops();
		this.shop = data.getShop();
	}
	
	@Override
	public void updateGui() {
	}
	
	private void saveAndSend(){
		data.setInvulnerable(invulnerable);
		data.setName(name);
		data.setMessage(message);
		data.setShowName(showname);
		data.setNameInMessage(namemsg);
		data.setPitch(pitch);
		data.setYaw(yaw);
		data.setEyesFollow(eyesfollow);
		data.setSkin(skin);
		data.setType(type);
		data.setMovable(movable);
		data.setDamage(damage);
		data.setSpeed(speed);
		data.setModel(model);
		data.setDrops(drops);
		data.setBoss(boss);
		data.setHealth(health);
		data.setShop(shop);
		TaleCraft.network.sendToServer(new NPCDataPacket(uuid, data.toNBT(), interactScript, updateScript, deathScript));
		NPCEditorGui.this.mc.displayGuiScreen(null);
	}
	
	public void buildGui() {
		addComponent(new QADLabel("NPC: " + name, 2, 2));
		
		QADTickBox invulnerablebox = new QADTickBox(3, this.height - 23, new TickBoxModel(){

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
		addComponent(invulnerablebox.setTooltip("Is the NPC invulnerable?"));
		
		QADTickBox moveablebox = new QADTickBox(25, this.height - 23, new TickBoxModel(){

			@Override
			public void setState(boolean newState) {
				movable = newState;
			}

			@Override
			public boolean getState() {
				return movable;
			}

			@Override
			public void toggleState() {
				movable = !movable;
			}
			
		});
		addComponent(moveablebox.setTooltip("Can the NPC be collided with?"));
		
		QADTickBox bossbox = new QADTickBox(25 + 22, this.height - 23, new TickBoxModel(){

			@Override
			public void setState(boolean newState) {
				boss = newState;
			}

			@Override
			public boolean getState() {
				return boss;
			}

			@Override
			public void toggleState() {
				boss = !boss;
			}
			
		});
		addComponent(bossbox.setTooltip("Is the NPC a boss?"));
		
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
		
		QADTickBox namemsgbox = new QADTickBox(3, 15, new TickBoxModel(){

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
		
		QADTextField messageField = (QADTextField) QADFACTORY.createTextField(this.message, 20, 15, this.getWidth() - 20).setName("message");
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
		addComponent(messageField.setTooltip("The message to be sent to the player when right-clicked.", "Leave blank for no message."));
		
		QADButton save = (QADButton) QADFACTORY.createButton("", this.width - 80, this.height - 30, 75).setModel(new ButtonModel(){

			@Override
			public void onClick() {
				saveAndSend();
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
		
		QADTickBox eyesfollowbox = new QADTickBox(3, this.height - 70, new TickBoxModel(){

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
		
		
		QADButton skinselector = QADFACTORY.createButton("Skin Selector", this.width - 231 - 50, this.height - 30, 200).setModel(new ButtonModel(){
			@Override
			public void onClick() {
				displayGuiScreen(new NPCSkinSelector(NPCEditorGui.this));
			}

			@Override
			public String getText() {
				if(skin == null) return "Selected Skin:";
				return "Selected Skin: " + skin.toString();
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
		
		QADButton modelselector = QADFACTORY.createButton("Model Selector", this.width - 155, this.height - 58, 150).setModel(new AbstractButtonModel(""){
			@Override
			public void onClick() {
				displayGuiScreen(new NPCModelSelector(NPCEditorGui.this));
			}
			@Override
			public String getText() {
				return "Selected Model: " + model.toString();
			}
			
		});
		addComponent(modelselector);
		
		QADButton inventorybutton = QADFACTORY.createButton("Edit Inventory", this.width - 362, this.height - 30, 80).setModel(new ButtonModel(){
			@Override
			public void onClick() {
				displayGuiScreen(new NPCInventoryEditorGui(NPCEditorGui.this));
			}

			@Override
			public String getText() {
				return "Edit Inventory";
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
		addComponent(inventorybutton);
		
		QADButton typeselector = QADFACTORY.createButton("Type: " + type.name(), 3, this.height/2 - 50, 200).setModel(new ButtonModel(){
			@Override
			public void onClick() {
				int id = type.ordinal();
				id++;
				if(id >= NPCType.values().length) id = 0;
				type = NPCType.values()[id];
			}

			@Override
			public String getText() {
				return "Type: " + type.name();
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
		addComponent(typeselector);
		
		QADTextField updatescriptbox = new QADTextField(fontRendererObj, this.width - this.width/4 - 25, this.height/2 - 30, this.width/4, 20);
		updatescriptbox.setModel(new TextFieldModel(){
			private int color = 0xFFFFFFFF;;
			@Override
			public String getText() {
				return updateScript;
			}

			@Override
			public int getTextLength() {
				return updateScript.length();
			}

			@Override
			public char getCharAt(int i) {
				return updateScript.charAt(i);
			}

			@Override
			public void setText(String text) {
				updateScript = text;
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
		updatescriptbox.setTooltip("The script to be run every tick.", "Leave blank if there is not script.");
		addComponent(updatescriptbox);
		
		QADTextField deathscriptbox = new QADTextField(fontRendererObj, this.width - this.width/4 - 25, this.height/2 + 30, this.width/4, 20);
		deathscriptbox.setModel(new TextFieldModel(){
			private int color = 0xFFFFFFFF;;
			@Override
			public String getText() {
				return deathScript;
			}

			@Override
			public int getTextLength() {
				return deathScript.length();
			}

			@Override
			public char getCharAt(int i) {
				return deathScript.charAt(i);
			}

			@Override
			public void setText(String text) {
				deathScript = text;
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
		deathscriptbox.setTooltip("The script to be run upon death.", "Leave blank if there is not script.");
		addComponent(deathscriptbox);
		
		QADTextField interactscriptbox = new QADTextField(fontRendererObj, this.width - this.width/4 - 25, this.height/2, this.width/4, 20);
		interactscriptbox.setModel(new TextFieldModel(){
			private int color = 0xFFFFFFFF;;
			@Override
			public String getText() {
				return interactScript;
			}

			@Override
			public int getTextLength() {
				return interactScript.length();
			}

			@Override
			public char getCharAt(int i) {
				return interactScript.charAt(i);
			}

			@Override
			public void setText(String text) {
				interactScript = text;
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
		interactscriptbox.setTooltip("The script to be run on interaction.", "Leave blank if there is not script.");
		addComponent(interactscriptbox);
		
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
		yawSlider.setPosition(3, this.height - 100);
		yawSlider.setWidth(217);
		yawSlider.setSliderValue(yaw/360f);
		addComponent(yawSlider);
		
		QADSlider damageSlider = new QADSlider(new SliderModel<Float>(){
			float sliderValue;
			
			@Override
			public void setValue(Float value) {
				sliderValue = value;
				damage = formattedDamage();
			}

			@Override
			public Float getValue() {
				return sliderValue;
			}

			@Override
			public String getValueAsText() {
				return "DMG: " + (int) damage;
			}

			@Override
			public void setSliderValue(float sliderValue){
				this.sliderValue = sliderValue;
				damage = formattedDamage();
			}

			@Override
			public float getSliderValue() {
				return sliderValue;
			}
			
			private float formattedDamage(){
				return sliderValue*40f;
			}
			
		});
		damageSlider.setPosition(3, this.height/2-27);
		damageSlider.setWidth(80);
		damageSlider.setSliderValue(damage/40f);
		addComponent(damageSlider);
		QADNumberTextField maxHPField = new QADNumberTextField(fontRendererObj, 3, this.height/2 - 4, 60, 20, health, NumberType.DECIMAL);
		maxHPField.setModel(new DefaultTextFieldModel() {
			boolean empty = false;
			@Override
			public void setText(String text) {
				if(text.equals("")){
					empty = true;
					return;
				}
				empty = false;
				float hp = 0;
				try{
					hp = Float.valueOf(text);
				}catch(NumberFormatException e){
					return;
				}
				health = hp;
			}
			
			@Override
			public int getTextLength() {
				return getText().length();
			}
			
			@Override
			public String getText() {
				return empty ? "" : "" + health;
			}
			
			@Override
			public char getCharAt(int i) {
				return getText().charAt(i);
			}
		});
		addComponent(maxHPField.setTooltip("Max Health"));
		QADSlider speedSlider = new QADSlider(new SliderModel<Double>(){
			double sliderValue;
			
			@Override
			public void setValue(Double value) {
				sliderValue = value;
				speed = formattedSpeed();
			}

			@Override
			public Double getValue() {
				return sliderValue;
			}

			@Override
			public String getValueAsText() {
				return "Speed: " + speed;
			}

			@Override
			public void setSliderValue(float sliderValue){
				this.sliderValue = sliderValue;
				speed = formattedSpeed();
			}

			@Override
			public float getSliderValue() {
				return (float) sliderValue;
			}
			
			private double formattedSpeed(){
				double speed = sliderValue*2f;
				return new Double(ItemStack.DECIMALFORMAT.format(speed));
			}
			
		});
		speedSlider.setPosition(90, this.height/2-27);
		speedSlider.setWidth(80);
		speedSlider.setSliderValue((float) (speed/2f));
		addComponent(speedSlider);
		
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
		pitchSlider.setPosition(20, this.height - 70);
		pitchSlider.setWidth(200);
		pitchSlider.setSliderValue((pitch + 90.0f)/180.0f);
		addComponent(pitchSlider);
	}
	
	
	
}
