package talecraft.client.gui.qad.model;

import talecraft.client.gui.qad.QADLabel.LabelModel;

public class DefaultLabelModel implements LabelModel {
	private final String text;
	private final int color;

	public DefaultLabelModel(String text) {
		this.text = text;
		this.color = 0xFFFFFFFF;
	}

	public DefaultLabelModel(String text, int color) {
		this.text = text;
		this.color = color;
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
	public int getColor() {
		return color;
	}
}
