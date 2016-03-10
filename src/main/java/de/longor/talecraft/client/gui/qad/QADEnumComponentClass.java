package de.longor.talecraft.client.gui.qad;

public enum QADEnumComponentClass {
	CONTAINER(0xFFFF7F00),
	INPUT    (0xFF0050FF),
	ACTION   (0xFFFF5000),
	VISUAL   (0xFF005000);
	public final int color;
	QADEnumComponentClass(int color) {
		this.color = color;
	}
}
