package de.longor.talecraft.client.gui.qad.model;

import de.longor.talecraft.client.gui.qad.QADTickBox;
import de.longor.talecraft.client.gui.qad.QADTickBox.TickBoxModel;

public class DefaultTickBoxModel implements TickBoxModel {
	boolean state;
	@Override
	public void setState(boolean newState) {
		this.state = newState;
	}
	@Override
	public boolean getState() {
		return this.state;
	}
	@Override
	public void toggleState() {
		this.state = !this.state;
	}
}