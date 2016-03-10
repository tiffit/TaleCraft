package de.longor.talecraft.client.gui.qad.model.nbtcompound;

import net.minecraft.nbt.NBTTagCompound;
import de.longor.talecraft.client.gui.qad.QADTextField.TextFieldModel;

public final class NBTStringTextFieldModel implements TextFieldModel {
	String text;
	String tagKey;
	NBTTagCompound tagParent;
	
	public NBTStringTextFieldModel(String tagKey, NBTTagCompound tagParent) {
		this.tagKey = tagKey;
		this.tagParent = tagParent;
		this.text = tagParent.getString(tagKey);
	}
	
	@Override public void setText(String newText) {
		text = newText;
		
		if(newText.isEmpty()) {
			tagParent.removeTag(tagKey);
		} else {
			tagParent.setString(tagKey, newText);
		}
	}
	
	@Override public int getTextLength() {
		return text.length();
	}

	@Override public String getText() {
		return text;
	}

	@Override public char getCharAt(int i) {
		return text.charAt(i);
	}

	@Override
	public void setTextColor(int color) {
		// nope
	}

	@Override
	public int getTextColor() {
		return 0xFFFFFFFF;
	}
}