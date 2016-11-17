package talecraft.client.gui.qad.model.nbtlist;

import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import talecraft.client.gui.qad.QADTextField.TextFieldModel;

public class NBTListFloatTextFieldModel implements TextFieldModel {
	NBTTagList list;
	int index;
	String text;
	boolean valid;

	public NBTListFloatTextFieldModel(NBTTagList li, int ix) {
		this.list = li;
		this.index = ix;
		this.text = Float.toString(list.getFloatAt(index));
		valid = true;
	}

	@Override
	public void setText(String text) {
		this.text = text;

		try {
			float value = Float.parseFloat(text);
			list.set(index, new NBTTagFloat(value));
			valid = true;
		} catch(NumberFormatException ex) {
			valid = false;
		}
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public int getTextLength() {
		return text.length();
	}

	@Override
	public char getCharAt(int i) {
		return text.charAt(i);
	}

	@Override
	public void setTextColor(int color) {
		// nope
	}

	@Override
	public int getTextColor() {
		return valid ? 0xFFFFFFFF : 0xFFFF7070;
	}

}
