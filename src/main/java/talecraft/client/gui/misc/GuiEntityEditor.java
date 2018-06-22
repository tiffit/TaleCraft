package talecraft.client.gui.misc;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import com.google.common.collect.Lists;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants.NBT;
import talecraft.client.gui.qad.QADButton;
import talecraft.client.gui.qad.QADFACTORY;
import talecraft.client.gui.qad.QADGuiScreen;
import talecraft.client.gui.qad.QADNumberTextField;
import talecraft.client.gui.qad.QADPanel;
import talecraft.client.gui.qad.QADScrollPanel;
import talecraft.client.gui.qad.QADTextField;
import talecraft.client.gui.qad.QADTickBox;
import talecraft.client.gui.qad.QADNumberTextField.NumberType;
import talecraft.client.gui.qad.QADTextField.TextChangeListener;
import talecraft.client.gui.qad.QADTickBox.TickBoxModel;
import talecraft.client.gui.qad.model.nbtcompound.NBTBooleanTickBoxModel;
import talecraft.client.gui.qad.model.nbtcompound.NBTByteTextFieldModel;
import talecraft.client.gui.qad.model.nbtcompound.NBTDoubleTextFieldModel;
import talecraft.client.gui.qad.model.nbtcompound.NBTFloatTextFieldModel;
import talecraft.client.gui.qad.model.nbtcompound.NBTIntegerTextFieldModel;
import talecraft.client.gui.qad.model.nbtcompound.NBTShortTextFieldModel;
import talecraft.client.gui.qad.model.nbtcompound.NBTStringTextFieldModel;
import talecraft.util.MutableInteger;
import talecraft.util.NBTHelper;
import talecraft.util.RecursiveNBTIterator;
import talecraft.util.RecursiveNBTIterator.NBTTreeConsumer;

public class GuiEntityEditor extends QADGuiScreen {
	public interface RemoteEntityDataLink {
		void updateData(NBTTagCompound entityData);
	}

	RemoteEntityDataLink dataLink;
	final NBTTagCompound entityData;

	QADTickBox rawDataTickBox;
	QADButton buttonRefresh;
	QADButton buttonCancel;
	QADButton buttonApply;
	QADScrollPanel scrollPanel;

	List<QADPanel> panels;
	NumberFormat format;
	final int rightColumnOffset = 180;
	final int lineHeight = 20;
	boolean showRawData = false;

	public GuiEntityEditor(NBTTagCompound entity, RemoteEntityDataLink dataLink) {
		this.entityData = entity;
		this.dataLink = dataLink;

		this.entityData.removeTag("UUIDMost");
		this.entityData.removeTag("UUIDLeast");
		this.entityData.removeTag("Dimension");

		format = NumberFormat.getInstance(Locale.ENGLISH);
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(2);
		format.setMinimumIntegerDigits(1);
		format.setMaximumIntegerDigits(32);
		format.setGroupingUsed(true);
	}

	@Override
	public void buildGui() {
		{
			QADPanel panel = new QADPanel();
			panel.setPosition(0, 0);
			panel.setSize(9999, 22);
			panel.setBackgroundColor(0);
			addComponent(panel);
		}

		addComponent(QADFACTORY.createLabel("Editing Entity: " + entityData.getString("UUID"), 2, 2));
		addComponent(QADFACTORY.createLabel("Entity Type: " + entityData.getString("id"), 2, 2+10));

		// dont do anything, let the extended classes do their thing!
		buttonRefresh = new QADButton(0, 0, 20, "R");
		buttonCancel = new QADButton(0, 0, 40, "Cancel");
		buttonApply = new QADButton(0, 0, 40, "Merge");
		addComponent(buttonRefresh);
		addComponent(buttonCancel);
		addComponent(buttonApply);

		buttonApply.setAction(new Runnable() {
			@Override public void run() {
				dataLink.updateData(entityData);
				displayGuiScreen(null);
			}
		});

		buttonCancel.setAction(new Runnable() {
			@Override public void run() {
				displayGuiScreen(null);
			}
		});

		buttonRefresh.setAction(new Runnable() {
			@Override public void run() {
				generatePanels();
				relayout();
			}
		});

		rawDataTickBox = new QADTickBox(0, (20-14)/2, 14, 14);
		rawDataTickBox.setModel(new TickBoxModel() {
			@Override public void toggleState() {
				GuiEntityEditor.this.showRawData ^= true;
				generatePanels();
				relayout();
			}
			@Override public void setState(boolean newState) {
				GuiEntityEditor.this.showRawData = newState;
				generatePanels();
				relayout();
			}
			@Override public boolean getState() {
				return GuiEntityEditor.this.showRawData;
			}
		});
		rawDataTickBox.setTooltip("Show raw NBT data.");
		addComponent(rawDataTickBox);

		scrollPanel = new QADScrollPanel();
		scrollPanel.setPosition(0, 22);
		addComponent(scrollPanel);

		this.panels = Lists.newArrayList();
		generatePanels();
	}

	// Use entityData to generate panels!
	private void generatePanels() {
		// clear lists
		panels.clear();
		scrollPanel.removeAllComponents();

		{// General Data Panel
			generatePanels$General();
		}

		if(entityData.hasKey("HealF"))
		{ // Entity Living Base
			generatePanels$EntityLivingBase();
		}

		if(entityData.getTagList("Attributes", NBT.TAG_COMPOUND).tagCount() > 0)
		{ // Attributes
			generatePanels$Attributes();
		}

		{ // Inventory
			generatePanels$Inventory();
		}

		if(entityData.getString("id").equalsIgnoreCase("Villager"))
		{ // Entity Villager
			generatePanels$EntityVillager();
		}

		if(entityData.getString("id").equalsIgnoreCase("Creeper"))
		{ // Entity Creeper
			generatePanels$EntityCreeper();
		}

		if(entityData.getString("id").equalsIgnoreCase("Slime"))
		{ // Entity Slime
			generatePanels$EntitySlime();
		}

		if(entityData.getString("id").equalsIgnoreCase("Enderman"))
		{ // Entity Enderman
			generatePanels$EntityEnderman();
		}

		if(entityData.getString("id").equalsIgnoreCase("Sheep")) {
			generatePanels$EntitySheep();
		}

		if(entityData.getString("id").equalsIgnoreCase("Pig")) {
			generatePanels$EntityPig();
		}

		if(entityData.getString("id").equalsIgnoreCase("Ozelot")) {
			generatePanels$EntityOzelot();
		}

		if(entityData.getString("id").equalsIgnoreCase("Wolf")) {
			generatePanels$EntityWolf();
		}

		if(entityData.getString("id").equalsIgnoreCase("VillagerGolem")) {
			generatePanels$EntityVillagerGolem();
		}

		if(entityData.getString("id").equalsIgnoreCase("Zombie")) {
			generatePanels$EntityZombie();
		}

		if(entityData.getString("id").equalsIgnoreCase("PigZombie")) {
			generatePanels$EntityZombie();
			generatePanels$EntityPigZombie();
		}

		if(entityData.getString("id").equalsIgnoreCase("Guardian")) {
			generatePanels$EntityGuardian();
		}

		if(entityData.getString("id").equalsIgnoreCase("EntityHorse")) {
			generatePanels$EntityHorse();
		}

		if(entityData.hasKey("OwnerUUID"))
		{ // Pet
			generatePanels$Pet();
		}

		if(showRawData) { // NBT;
			generatePanels$NBT();
		}

		int yOff = 6;
		for(QADPanel panel : panels) {
			panel.setY(yOff);
			panel.setX(4);
			panel.setWidth(width);
			scrollPanel.addComponent(panel);
			yOff += panel.getHeight() + 8;
		}
		scrollPanel.setViewportHeight(yOff);
	}

	private void generatePanels$EntityHorse() {
		QADPanel panel = new QADPanel();
		panel.setBackgroundColor(1);
		panels.add(panel);
		int yOff = 2;

		panel.addComponent(QADFACTORY.createLabel(TextFormatting.GREEN+"Entity Horse", 2, yOff));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Is tamed?", 2, yOff));
		panel.addComponent(new QADTickBox(rightColumnOffset, yOff-3, new NBTBooleanTickBoxModel("Tame", entityData)));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Has reproduced?", 2, yOff));
		panel.addComponent(new QADTickBox(rightColumnOffset, yOff-3, new NBTBooleanTickBoxModel("HasReproduced", entityData)));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Is bred?", 2, yOff));
		panel.addComponent(new QADTickBox(rightColumnOffset, yOff-3, new NBTBooleanTickBoxModel("Bred", entityData)));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Is eating haystack?", 2, yOff));
		panel.addComponent(new QADTickBox(rightColumnOffset, yOff-3, new NBTBooleanTickBoxModel("EatingHaystack", entityData)));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Is chested?", 2, yOff));
		panel.addComponent(new QADTickBox(rightColumnOffset, yOff-3, new NBTBooleanTickBoxModel("ChestedHorse", entityData)));
		yOff += lineHeight;

		{
			panel.addComponent(QADFACTORY.createLabel("Variant", 2, yOff));
			Number value = entityData.getInteger("Variant");
			NumberType type = NumberType.INTEGER;
			QADNumberTextField tf = new QADNumberTextField(fontRenderer, rightColumnOffset, yOff - 3, 140, 14, value, type);
			tf.setModel(new NBTIntegerTextFieldModel("Variant", entityData));
			panel.addComponent(tf);
			yOff += lineHeight;
		}

		{
			panel.addComponent(QADFACTORY.createLabel("Type", 2, yOff));
			Number value = entityData.getInteger("Type");
			NumberType type = NumberType.INTEGER;
			QADNumberTextField tf = new QADNumberTextField(fontRenderer, rightColumnOffset, yOff - 3, 140, 14, value, type);
			tf.setModel(new NBTIntegerTextFieldModel("Type", entityData));
			panel.addComponent(tf);
			yOff += lineHeight;
		}

		{
			panel.addComponent(QADFACTORY.createLabel("Temper", 2, yOff));
			Number value = entityData.getShort("Temper");
			NumberType type = NumberType.INTEGER;
			QADNumberTextField tf = new QADNumberTextField(fontRenderer, rightColumnOffset, yOff - 3, 140, 14, value, type);
			tf.setModel(new NBTShortTextFieldModel("Temper", entityData));
			panel.addComponent(tf);
			yOff += lineHeight;
		}


		panel.setHeight(yOff);
	}

	private void generatePanels$EntityGuardian() {
		QADPanel panel = new QADPanel();
		panel.setBackgroundColor(1);
		panels.add(panel);
		int yOff = 2;

		panel.addComponent(QADFACTORY.createLabel(TextFormatting.GREEN+"Entity Guardian", 2, yOff));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Is elder?", 2, yOff));
		panel.addComponent(new QADTickBox(rightColumnOffset, yOff-3, new NBTBooleanTickBoxModel("Elder", entityData)));
		yOff += lineHeight;

		panel.setHeight(yOff);
	}

	private void generatePanels$EntityZombie() {
		QADPanel panel = new QADPanel();
		panel.setBackgroundColor(1);
		panels.add(panel);
		int yOff = 2;

		panel.addComponent(QADFACTORY.createLabel(TextFormatting.GREEN+"Entity Zombie", 2, yOff));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Can break doors?", 2, yOff));
		panel.addComponent(new QADTickBox(rightColumnOffset, yOff-3, new NBTBooleanTickBoxModel("CanBreakDoors", entityData)));
		yOff += lineHeight;

		panel.setHeight(yOff);
	}

	private void generatePanels$EntityPigZombie() {
		QADPanel panel = new QADPanel();
		panel.setBackgroundColor(1);
		panels.add(panel);
		int yOff = 2;

		panel.addComponent(QADFACTORY.createLabel(TextFormatting.GREEN+"Entity Pig Zombie", 2, yOff));
		yOff += lineHeight;

		{
			panel.addComponent(QADFACTORY.createLabel("Anger Time", 2, yOff));
			Number value = entityData.getShort("Anger");
			NumberType type = NumberType.INTEGER;
			QADNumberTextField tf = new QADNumberTextField(fontRenderer, rightColumnOffset, yOff - 3, 140, 14, value, type);
			tf.setModel(new NBTShortTextFieldModel("Anger", entityData));
			panel.addComponent(tf);
			yOff += lineHeight;
		}

		{
			panel.addComponent(QADFACTORY.createLabel("Conversion Time", 2, yOff));
			Number value = entityData.getInteger("ConversionTime");
			NumberType type = NumberType.INTEGER;
			QADNumberTextField tf = new QADNumberTextField(fontRenderer, rightColumnOffset, yOff - 3, 140, 14, value, type);
			tf.setModel(new NBTIntegerTextFieldModel("ConversionTime", entityData));
			panel.addComponent(tf);
			yOff += lineHeight;
		}

		panel.setHeight(yOff);
	}

	private void generatePanels$EntityVillagerGolem() {
		QADPanel panel = new QADPanel();
		panel.setBackgroundColor(1);
		panels.add(panel);
		int yOff = 2;

		panel.addComponent(QADFACTORY.createLabel(TextFormatting.GREEN+"Entity Villager Golem", 2, yOff));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Player created?", 2, yOff));
		panel.addComponent(new QADTickBox(rightColumnOffset, yOff-3, new NBTBooleanTickBoxModel("PlayerCreated", entityData)));
		yOff += lineHeight;

		panel.setHeight(yOff);
	}

	private void generatePanels$EntityWolf() {
		QADPanel panel = new QADPanel();
		panel.setBackgroundColor(1);
		panels.add(panel);
		int yOff = 2;

		panel.addComponent(QADFACTORY.createLabel(TextFormatting.GREEN+"Entity Wolf", 2, yOff));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Collar Color", 2, yOff));
		{
			Number value = entityData.getByte("CollarColor");
			NumberType type = NumberType.INTEGER;
			QADNumberTextField tf = new QADNumberTextField(fontRenderer, rightColumnOffset, yOff - 3, 140, 14, value, type);
			tf.setModel(new NBTByteTextFieldModel("CollarColor", entityData));
			panel.addComponent(tf);
		}
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Angry?", 2, yOff));
		panel.addComponent(new QADTickBox(rightColumnOffset, yOff-3, new NBTBooleanTickBoxModel("Angry", entityData)));
		yOff += lineHeight;

		panel.setHeight(yOff);
	}

	private void generatePanels$EntityOzelot() {
		QADPanel panel = new QADPanel();
		panel.setBackgroundColor(1);
		panels.add(panel);
		int yOff = 2;

		panel.addComponent(QADFACTORY.createLabel(TextFormatting.GREEN+"Entity Ozelot", 2, yOff));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Type", 2, yOff));
		{
			Number value = entityData.getByte("CatType");
			NumberType type = NumberType.INTEGER;
			QADNumberTextField tf = new QADNumberTextField(fontRenderer, rightColumnOffset, yOff - 3, 140, 14, value, type);
			tf.setModel(new NBTByteTextFieldModel("CatType", entityData));
			panel.addComponent(tf);
		}
		yOff += lineHeight;

		panel.setHeight(yOff);
	}

	private void generatePanels$Pet() {
		QADPanel panel = new QADPanel();
		panel.setBackgroundColor(1);
		panels.add(panel);

		int yOff = 2;

		panel.addComponent(QADFACTORY.createLabel(TextFormatting.GREEN+"Pet", 2, yOff));
		yOff += lineHeight;

		{
			panel.addComponent(QADFACTORY.createLabel("Owner UUID", 2, yOff));
			QADTextField textField = new QADTextField(fontRenderer, rightColumnOffset, yOff-3, 140, 14);
			textField.setModel(new NBTStringTextFieldModel("OwnerUUID", entityData));
			textField.textChangedListener = new TextChangeListener(){
				@Override public void call(QADTextField field, String text) {
					try {
						field.setTextColor(0xFFFFFFFF);
					} catch (IllegalArgumentException e) {
						field.setTextColor(0xFFFF0000);
					}
				}
			};
			panel.addComponent(textField);
			yOff += lineHeight;
		}

		if(entityData.hasKey("Sitting")) {
			panel.addComponent(QADFACTORY.createLabel("Sitting?", 2, yOff));
			panel.addComponent(new QADTickBox(rightColumnOffset, yOff-3, new NBTBooleanTickBoxModel("Sitting", entityData)));
			yOff += lineHeight;
		}



		panel.setHeight(yOff);
	}

	private void generatePanels$EntitySheep() {
		QADPanel panel = new QADPanel();
		panel.setBackgroundColor(1);
		panels.add(panel);
		int yOff = 2;

		panel.addComponent(QADFACTORY.createLabel(TextFormatting.GREEN+"Entity Sheep", 2, yOff));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Sheared?", 2, yOff));
		panel.addComponent(new QADTickBox(rightColumnOffset, yOff-3, new NBTBooleanTickBoxModel("Sheared", entityData)));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Color", 2, yOff));
		{
			Number value = entityData.getByte("Color");
			NumberType type = NumberType.INTEGER;
			QADNumberTextField tf = new QADNumberTextField(fontRenderer, rightColumnOffset, yOff - 3, 140, 14, value, type);
			tf.setModel(new NBTByteTextFieldModel("Color", entityData));
			panel.addComponent(tf);
		}
		yOff += lineHeight;

		panel.setHeight(yOff);
	}

	private void generatePanels$EntityPig() {
		QADPanel panel = new QADPanel();
		panel.setBackgroundColor(1);
		panels.add(panel);

		int lines = 5;
		panel.setHeight(lines*lineHeight+2);

		int yOff = 2;

		panel.addComponent(QADFACTORY.createLabel(TextFormatting.GREEN+"Entity Pig", 2, yOff));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Saddled?", 2, yOff));
		panel.addComponent(new QADTickBox(rightColumnOffset, yOff-3, new NBTBooleanTickBoxModel("Saddle", entityData)));

		yOff += lineHeight;

		panel.setHeight(yOff);
	}

	private void generatePanels$EntityEnderman() {
		QADPanel panel = new QADPanel();
		panel.setBackgroundColor(1);
		panels.add(panel);

		int lines = 5;
		panel.setHeight(lines*lineHeight+2);

		int yOff = 2;

		panel.addComponent(QADFACTORY.createLabel(TextFormatting.GREEN+"Entity Enderman", 2, yOff));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Carry", 2, yOff));
		panel.addComponent(QADFACTORY.createLabel(""+entityData.getShort("carried"), rightColumnOffset, yOff));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Carry Data", 2, yOff));
		panel.addComponent(QADFACTORY.createLabel(""+entityData.getShort("carriedData"), rightColumnOffset, yOff));
		yOff += lineHeight;

		panel.setHeight(yOff);
	}

	private void generatePanels$EntitySlime() {
		QADPanel panel = new QADPanel();
		panel.setBackgroundColor(1);
		panels.add(panel);

		int lines = 5;
		panel.setHeight(lines*lineHeight+2);

		int yOff = 2;

		panel.addComponent(QADFACTORY.createLabel(TextFormatting.GREEN+"Entity Slime", 2, yOff));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Size", 2, yOff));
		{
			Number value = entityData.getInteger("Size");
			NumberType type = NumberType.INTEGER;
			QADNumberTextField tf = new QADNumberTextField(fontRenderer, rightColumnOffset, yOff - 3, 140, 14, value, type);
			tf.setModel(new NBTIntegerTextFieldModel("Size", entityData));
			panel.addComponent(tf);
		}
		yOff += lineHeight;

		panel.setHeight(yOff);
	}

	private void generatePanels$EntityCreeper() {
		QADPanel panel = new QADPanel();
		panel.setBackgroundColor(1);
		panels.add(panel);

		int lines = 5;
		panel.setHeight(lines*lineHeight+2);

		int yOff = 2;

		panel.addComponent(QADFACTORY.createLabel(TextFormatting.GREEN+"Entity Creeper", 2, yOff));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Ignited?", 2, yOff));
		panel.addComponent(new QADTickBox(rightColumnOffset, yOff-3, new NBTBooleanTickBoxModel("ignited", entityData)));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Fuse", 2, yOff));
		{
			Number value = entityData.getShort("fuse");
			NumberType type = NumberType.INTEGER;
			QADNumberTextField tf = new QADNumberTextField(fontRenderer, rightColumnOffset, yOff - 3, 140, 14, value, type);
			tf.setModel(new NBTShortTextFieldModel("fuse", entityData));
			panel.addComponent(tf);
		}
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Explosion Radius", 2, yOff));
		{
			Number value = entityData.getShort("ExplosionRadius");
			NumberType type = NumberType.INTEGER;
			QADNumberTextField tf = new QADNumberTextField(fontRenderer, rightColumnOffset, yOff - 3, 140, 14, value, type);
			tf.setModel(new NBTShortTextFieldModel("ExplosionRadius", entityData));
			panel.addComponent(tf);
		}
		yOff += lineHeight;

		panel.setHeight(yOff);
	}

	private void generatePanels$General() {
		QADPanel panel = new QADPanel();
		panel.setBackgroundColor(1);
		panel.setName("panel.general");
		panels.add(panel);

		int lines = 5;
		panel.setHeight(lines*lineHeight+2);

		int yOff = 2;

		panel.addComponent(QADFACTORY.createLabel(TextFormatting.GREEN+"General Information", 2, yOff));
		yOff += lineHeight;

		{
			panel.addComponent(QADFACTORY.createLabel("Custom Name", 2, yOff));
			QADTextField textField = new QADTextField(fontRenderer, rightColumnOffset, yOff-3, 140, 14);
			textField.setModel(new NBTStringTextFieldModel("CustomName", entityData));
			panel.addComponent(textField);
			yOff += lineHeight;
		}

		panel.addComponent(QADFACTORY.createLabel("Type", 2, yOff));
		panel.addComponent(QADFACTORY.createLabel(entityData.getString("id"), rightColumnOffset, yOff));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("UUID", 2, yOff));
		panel.addComponent(QADFACTORY.createLabel(entityData.getString("UUID"), rightColumnOffset, yOff));
		yOff += lineHeight;

		{
			panel.addComponent(QADFACTORY.createLabel("Position (exact)", 2, yOff));
			StringBuilder builder = new StringBuilder();
			NBTTagList Pos = entityData.getTagList("Pos", NBT.TAG_DOUBLE);
			builder.append(format.format(Pos.getDoubleAt(0))).append(", ");
			builder.append(format.format(Pos.getDoubleAt(1))).append(", ");
			builder.append(format.format(Pos.getDoubleAt(2)));

			panel.addComponent(QADFACTORY.createLabel(builder.toString(), rightColumnOffset, yOff));
			yOff += lineHeight;
		}

		{
			panel.addComponent(QADFACTORY.createLabel("Position (block)", 2, yOff));
			StringBuilder builder = new StringBuilder();
			NBTTagList li = entityData.getTagList("Pos", NBT.TAG_DOUBLE);
			builder.append((int)Math.floor(li.getDoubleAt(0))).append(", ");
			builder.append((int)Math.floor(li.getDoubleAt(1))).append(", ");
			builder.append((int)Math.floor(li.getDoubleAt(2)));

			panel.addComponent(QADFACTORY.createLabel(builder.toString(), rightColumnOffset, yOff));
			yOff += lineHeight;
		}

		{
			panel.addComponent(QADFACTORY.createLabel("Rotation", 2, yOff));
			StringBuilder builder = new StringBuilder();
			NBTTagList li = entityData.getTagList("Rotation", NBT.TAG_DOUBLE);
			builder.append((int)Math.round(li.getDoubleAt(0))).append(", ");
			builder.append((int)Math.round(li.getDoubleAt(1)));

			panel.addComponent(QADFACTORY.createLabel(builder.toString(), rightColumnOffset, yOff));
			yOff += lineHeight;
		}

		{
			panel.addComponent(QADFACTORY.createLabel("Motion", 2, yOff));
			StringBuilder builder = new StringBuilder();
			NBTTagList Pos = entityData.getTagList("Motion", NBT.TAG_DOUBLE);
			builder.append(format.format(Pos.getDoubleAt(0))).append(", ");
			builder.append(format.format(Pos.getDoubleAt(1))).append(", ");
			builder.append(format.format(Pos.getDoubleAt(2)));

			panel.addComponent(QADFACTORY.createLabel(builder.toString(), rightColumnOffset, yOff));
			yOff += lineHeight;
		}

		panel.addComponent(QADFACTORY.createLabel("Persistence Required", 2, yOff));
		panel.addComponent(new QADTickBox(rightColumnOffset, yOff-3, new NBTBooleanTickBoxModel("PersistenceRequired", entityData)));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("No-Clip?", 2, yOff));
		panel.addComponent(new QADTickBox(rightColumnOffset, yOff-3, new NBTBooleanTickBoxModel("TC_NoClip", entityData)));
		yOff += lineHeight;

		/*
		{
			panel.addComponent(QADFACTORY.createLabel("[TC] Width", 2, yOff));
			Number value = entityData.getFloat("TC_Width");
			NumberType type = NumberType.DECIMAL;
			QADNumberTextField tf = new QADNumberTextField(fontRenderer, rightColumnOffset, yOff - 3, 140, 14, value, type);
			tf.setModel(new NBTFloatTextFieldModel("TC_Width", entityData));
			panel.addComponent(tf);
			yOff += lineHeight;
		}
		{
			panel.addComponent(QADFACTORY.createLabel("[TC] Height", 2, yOff));
			Number value = entityData.getFloat("TC_Height");
			NumberType type = NumberType.DECIMAL;
			QADNumberTextField tf = new QADNumberTextField(fontRenderer, rightColumnOffset, yOff - 3, 140, 14, value, type);
			tf.setModel(new NBTFloatTextFieldModel("TC_Height", entityData));
			panel.addComponent(tf);
			yOff += lineHeight;
		}
		//*/
		{
			panel.addComponent(QADFACTORY.createLabel("Step Height", 2, yOff));
			Number value = entityData.getFloat("TC_StepHeight");
			NumberType type = NumberType.DECIMAL;
			QADNumberTextField tf = new QADNumberTextField(fontRenderer, rightColumnOffset, yOff - 3, 140, 14, value, type);
			tf.setModel(new NBTFloatTextFieldModel("TC_StepHeight", entityData));
			panel.addComponent(tf);
			yOff += lineHeight;
		}

		panel.setHeight(yOff);
	}

	private void generatePanels$NBT() {
		final QADPanel panel = new QADPanel();
		panel.setBackgroundColor(1);
		panel.setName("panel.nbt");
		panels.add(panel);

		int yOff = 2;
		final int lineHeight = 14;

		panel.addComponent(QADFACTORY.createLabel(TextFormatting.GOLD +"Raw NBT", 2, yOff));
		yOff += lineHeight;

		{
			QADButton buttonAsJson = QADFACTORY.createButton("Copy to Clipboard as JSON", 2, yOff, rightColumnOffset);
			buttonAsJson.setAction(new Runnable() {
				@Override public void run() {
					NBTTagCompound compound = (NBTTagCompound) entityData.copy();
					compound.removeTag("Pos");
					compound.removeTag("Motion");
					compound.removeTag("Rotation");

					String nbtAsJson = NBTHelper.asJson(compound);

					if(nbtAsJson != null) {
						GuiScreen.setClipboardString(nbtAsJson);
					}
				}
			});
			buttonAsJson.setTooltip("Copies the entity data to the","clipboard as a string of JSON.");
			panel.addComponent(buttonAsJson);
		}
		yOff += lineHeight*2;

		//		{
		//			QADButton buttonEditRaw = QADFACTORY.createButton("Edit raw NBT", 2, yOff, rightColumnOffset);
		//			buttonEditRaw.setAction(new Runnable() {
		//				@Override public void run() {
		//					GuiNBTEditor nbtEditor = new GuiNBTEditor(entityData);
		//					nbtEditor.returnScreen = GuiEntityEditor.this;
		//					displayGuiScreen(nbtEditor);
		//				}
		//			});
		//			buttonEditRaw.setTooltip("Opens the NBT-Editor.");
		//			panel.addComponent(buttonEditRaw);
		//		}
		//		yOff += lineHeight*2;

		final MutableInteger yOffMut = new MutableInteger(yOff);
		RecursiveNBTIterator.iterate(entityData, new NBTTreeConsumer() {
			@Override public void consume(int depth, String name, NBTBase tag, NBTTagCompound parent) {
				int x = 6 + depth * 6;

				if(tag == null) {
					panel.addComponent(QADFACTORY.createLabel("---", x, yOffMut.get()-7));
					return;
				}

				if(tag instanceof NBTTagCompound) {
					panel.addComponent(QADFACTORY.createLabel(name, x, yOffMut.get()));
				} else if (tag instanceof NBTTagList) {
					NBTTagList list = (NBTTagList)tag;

					if(list.getTagType() == NBT.TAG_COMPOUND) {
						panel.addComponent(QADFACTORY.createLabel(name, x, yOffMut.get()));
					}

					if(list.getTagType() == NBT.TAG_DOUBLE) {
						StringBuilder builder = new StringBuilder();
						builder.append(list.tagCount());
						builder.append(" [");
						for(int i = 0; i < list.tagCount(); i++) {
							builder.append(TextFormatting.DARK_GRAY);
							builder.append(format.format(list.getDoubleAt(i)));
							builder.append(TextFormatting.WHITE);
							builder.append(", ");
						}
						builder.setLength(builder.length()-2);
						builder.append("]");

						panel.addComponent(QADFACTORY.createLabel(name + " = " + builder.toString(), x, yOffMut.get()));
					}

				} else {
					panel.addComponent(QADFACTORY.createLabel(name + " = " + tag.toString(), x, yOffMut.get()));
				}
				yOffMut.add(lineHeight);
			}
		});
		yOff = yOffMut.get();

		panel.setHeight(yOff);
	}

	private void generatePanels$Inventory() {
		NBTTagList inventory = null;
		{
			NBTTagList __e = entityData.getTagList("Equipment", NBT.TAG_COMPOUND);
			NBTTagList __i = entityData.getTagList("Inventory", NBT.TAG_COMPOUND);

			if(__e.tagCount() > 0) {
				inventory = __e;
			}
			if(__i.tagCount() > 0) {
				inventory = __i;
			}

			if(inventory == null)
				return;
		}

		QADPanel panel = new QADPanel();
		panel.setBackgroundColor(1);
		panel.setName("panel.inventory");

		int yOff = 2;

		panel.addComponent(QADFACTORY.createLabel(TextFormatting.BLUE+"Inventory", 2, yOff));
		yOff += lineHeight;

		NBTTagList list = inventory;
		for(int i = 0; i < list.tagCount(); i++) {
			final NBTTagCompound slot = list.getCompoundTagAt(i);

			if(slot.hasNoTags()) {
				continue;
			}

			int slotID = slot.hasKey("Slot") ? slot.getByte("Slot") : -1;

			panel.addComponent(QADFACTORY.createLabel("Item " + ((slotID == -1) ? i : slotID), 2, yOff));
			panel.addComponent(QADFACTORY.createButton(NBTItemToString(slot), rightColumnOffset, yOff, 200, new Runnable() {
				@Override public void run() {
					QADGuiScreen guiScreen = new GuiItemStackEditor(slot);
					guiScreen.setBehind(GuiEntityEditor.this);
					displayGuiScreen(guiScreen);
				}
			})).textAlignment = 0;

			yOff += lineHeight;
		}

		if(yOff <= lineHeight+3) {
			return;
		}

		panel.setHeight(yOff);
		panels.add(panel);
	}

	private void generatePanels$EntityVillager() {
		QADPanel panel = new QADPanel();
		panel.setBackgroundColor(1);
		panel.setName("panel.entity.villager");
		panels.add(panel);

		int yOff = 2;

		panel.addComponent(QADFACTORY.createLabel(TextFormatting.GREEN+"Entity Villager", 2, yOff));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Riches?", 2, yOff));
		panel.addComponent(new QADTickBox(rightColumnOffset, yOff-3, new NBTBooleanTickBoxModel("Riches", entityData)));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Willing?", 2, yOff));
		panel.addComponent(new QADTickBox(rightColumnOffset, yOff-3, new NBTBooleanTickBoxModel("Willing", entityData)));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Profession", 2, yOff));
		panel.addComponent(QADFACTORY.createLabel(""+entityData.getInteger("Profession"), rightColumnOffset, yOff));
		{
			Number value = entityData.getInteger("Profession");
			NumberType type = NumberType.INTEGER;
			QADNumberTextField tf = new QADNumberTextField(fontRenderer, rightColumnOffset, yOff - 3, 140, 14, value, type);
			tf.setModel(new NBTIntegerTextFieldModel("Profession", entityData));
			panel.addComponent(tf);
		}
		yOff += lineHeight;

		if(entityData.hasKey("Career")) {
			panel.addComponent(QADFACTORY.createLabel("Career Level", 2, yOff));
			{
				Number value = entityData.getInteger("CareerLevel");
				NumberType type = NumberType.INTEGER;
				QADNumberTextField tf = new QADNumberTextField(fontRenderer, rightColumnOffset, yOff - 3, 140, 14, value, type);
				tf.setModel(new NBTIntegerTextFieldModel("CareerLevel", entityData));
				panel.addComponent(tf);
			}
			yOff += lineHeight;
		}

		if(entityData.hasKey("Offers")) {
			panel.addComponent(QADFACTORY.createLabel(TextFormatting.YELLOW+"Offers", 2, yOff));
			yOff += lineHeight;

			final NBTTagCompound offers = entityData.getCompoundTag("Offers");
			final NBTTagList recipes = offers.getTagList("Recipes", NBT.TAG_COMPOUND);

			panel.addComponent(QADFACTORY.createButton("Add Recipe", 2, yOff, 100, new Runnable() {
				@Override public void run() {
					NBTTagCompound compound = new NBTTagCompound();
					compound.setInteger("uses", 0);
					compound.setInteger("maxUses", 1);
					compound.setBoolean("rewardExp", false);

					NBTTagCompound compoundBUY = new NBTTagCompound();
					compoundBUY.setShort("Damage", (short)0);
					compoundBUY.setByte("Count", (byte)1);
					compoundBUY.setString("id", "minecraft:emerald");

					NBTTagCompound compoundSELL = new NBTTagCompound();
					compoundSELL.setShort("Damage", (short)0);
					compoundSELL.setByte("Count", (byte)1);
					compoundSELL.setString("id", "minecraft:stone");

					compound.setTag("buy", compoundBUY);
					compound.setTag("sell", compoundSELL);

					recipes.appendTag(compound);;
					generatePanels();
					relayout();
				}
			}));
			yOff += lineHeight;

			for(int i = 0; i < recipes.tagCount(); i++) {
				final int index = i;
				final NBTTagCompound recipe = recipes.getCompoundTagAt(i);

				panel.addComponent(QADFACTORY.createLabel(TextFormatting.AQUA+"Recipe "+i, 2+8, yOff+6));
				panel.addComponent(QADFACTORY.createButton("Remove Recipe", rightColumnOffset, yOff, 100, new Runnable() {
					@Override public void run() {
						recipes.removeTag(index);
						generatePanels();
						relayout();
					}
				}));
				yOff += lineHeight + 8;

				panel.addComponent(QADFACTORY.createLabel("Times Used:", 2+8, yOff));
				{
					Number value = recipe.getInteger("uses");
					NumberType type = NumberType.INTEGER;
					QADNumberTextField tf = new QADNumberTextField(fontRenderer, rightColumnOffset, yOff - 3, 140, 14, value, type);
					tf.setModel(new NBTIntegerTextFieldModel("uses", recipe));
					panel.addComponent(tf);
				}
				yOff += lineHeight;

				panel.addComponent(QADFACTORY.createLabel("Maximum Uses:", 2+8, yOff));
				{
					Number value = recipe.getInteger("maxUses");
					NumberType type = NumberType.INTEGER;
					QADNumberTextField tf = new QADNumberTextField(fontRenderer, rightColumnOffset, yOff - 3, 140, 14, value, type);
					tf.setModel(new NBTIntegerTextFieldModel("maxUses", recipe));
					panel.addComponent(tf);
				}
				yOff += lineHeight;

				panel.addComponent(QADFACTORY.createLabel("Reward Experience:", 2+8, yOff));
				panel.addComponent(new QADTickBox(rightColumnOffset, yOff-3, new NBTBooleanTickBoxModel("rewardExp", recipe)));
				yOff += lineHeight;

				final NBTTagCompound slotBuy = recipe.getCompoundTag("buy");
				final NBTTagCompound slotSell = recipe.getCompoundTag("sell");

				panel.addComponent(QADFACTORY.createLabel("Buys:", 2+8, yOff));
				panel.addComponent(QADFACTORY.createButton(NBTItemToString(slotBuy), rightColumnOffset, yOff, 140, new Runnable() {
					@Override public void run() {
						QADGuiScreen guiScreen = new GuiItemStackEditor(slotBuy);
						guiScreen.setBehind(GuiEntityEditor.this);
						displayGuiScreen(guiScreen);
					}
				}));
				yOff += lineHeight;

				panel.addComponent(QADFACTORY.createLabel("Sells:", 2+8, yOff));
				panel.addComponent(QADFACTORY.createButton(NBTItemToString(slotSell), rightColumnOffset, yOff, 140, new Runnable() {
					@Override public void run() {
						QADGuiScreen guiScreen = new GuiItemStackEditor(slotSell);
						guiScreen.setBehind(GuiEntityEditor.this);
						displayGuiScreen(guiScreen);
					}
				}));

				yOff += lineHeight;
				yOff += lineHeight;
			}

		}

		panel.setHeight(yOff);
	}

	private void generatePanels$EntityLivingBase() {
		QADPanel panel = new QADPanel();
		panel.setBackgroundColor(1);
		panel.setName("panel.livingbase");
		panels.add(panel);

		int yOff = 2;

		panel.addComponent(QADFACTORY.createLabel(TextFormatting.GREEN+"Entity Living", 2, yOff));
		yOff += lineHeight;

		{
			panel.addComponent(QADFACTORY.createLabel("Health", 2, yOff));
			Number value = entityData.getFloat("HealF");
			NumberType type = NumberType.DECIMAL;
			QADNumberTextField tf = new QADNumberTextField(fontRenderer, rightColumnOffset, yOff - 3, 140, 14, value, type);
			tf.setModel(new NBTFloatTextFieldModel("HealF", entityData));
			panel.addComponent(tf);
			yOff += lineHeight;
		}

		panel.addComponent(QADFACTORY.createLabel("Air", 2, yOff));
		{
			Number value = entityData.getInteger("Air");
			NumberType type = NumberType.INTEGER;
			QADNumberTextField tf = new QADNumberTextField(fontRenderer, rightColumnOffset, yOff - 3, 140, 14, value, type);
			tf.setModel(new NBTIntegerTextFieldModel("Air", entityData));
			panel.addComponent(tf);
		}
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Fire", 2, yOff));
		{
			Number value = entityData.getInteger("Fire");
			NumberType type = NumberType.INTEGER;
			QADNumberTextField tf = new QADNumberTextField(fontRenderer, rightColumnOffset, yOff - 3, 140, 14, value, type);
			tf.setModel(new NBTIntegerTextFieldModel("Fire", entityData));
			panel.addComponent(tf);
		}
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Age", 2, yOff));
		{
			Number value = entityData.getInteger("Age");
			NumberType type = NumberType.INTEGER;
			QADNumberTextField tf = new QADNumberTextField(fontRenderer, rightColumnOffset, yOff - 3, 140, 14, value, type);
			tf.setModel(new NBTIntegerTextFieldModel("Age", entityData));
			panel.addComponent(tf);
		}
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("No AI?", 2, yOff));
		panel.addComponent(new QADTickBox(rightColumnOffset, yOff-3, new NBTBooleanTickBoxModel("NoAI", entityData)));

		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Invulnerable?", 2, yOff));
		panel.addComponent(new QADTickBox(rightColumnOffset, yOff-3, new NBTBooleanTickBoxModel("Invulnerable", entityData)));
		yOff += lineHeight;

		panel.addComponent(QADFACTORY.createLabel("Can pick up loot?", 2, yOff));
		panel.addComponent(new QADTickBox(rightColumnOffset, yOff-3, new NBTBooleanTickBoxModel("CanPickUpLoot", entityData)));
		yOff += lineHeight;

		panel.setHeight(yOff);
	}

	private void generatePanels$Attributes() {
		QADPanel panel = new QADPanel();
		panel.setBackgroundColor(1);
		panel.setName("panel.attributes");
		panels.add(panel);

		int yOff = 2;

		panel.addComponent(QADFACTORY.createLabel(TextFormatting.GRAY+"Attributes", 2, yOff));
		yOff += lineHeight;

		NBTTagList list = entityData.getTagList("Attributes", NBT.TAG_COMPOUND);
		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound attribute = list.getCompoundTagAt(i);

			{
				String name = attribute.getString("Name");
				panel.addComponent(QADFACTORY.createLabel(name, 2, yOff));
			}

			if(attribute.hasKey("Base")) {
				Number value = entityData.getFloat("Base");
				NumberType type = NumberType.DECIMAL;
				QADNumberTextField tf = new QADNumberTextField(fontRenderer, rightColumnOffset, yOff - 3, 140, 14, value, type);
				tf.setModel(new NBTDoubleTextFieldModel("Base", attribute));
				panel.addComponent(tf);
			}

			yOff += lineHeight-3;
		}

		panel.setHeight(yOff);
	}

	private static final String NBTItemToString(NBTTagCompound slot) {
		StringBuilder builder = new StringBuilder();
		builder.append(slot.getString("id")).append("/");
		builder.append(slot.getShort("Damage")).append(" x");
		builder.append(slot.getByte("Count"));
		return builder.toString();
	}

	@Override
	public void layoutGui() {
		buttonCancel.setX(width-40);
		buttonApply.setX(width-82);
		buttonRefresh.setX(width-104);
		rawDataTickBox.setX(width - 124);

		scrollPanel.setSize(width, height-22);

		boolean fit = scrollPanel.getDoesViewportFit();
		for(QADPanel panel : panels) {
			panel.setWidth(width-(fit?8:12));
		}
	}

}
