package talecraft.client.gui.qad.model;

import talecraft.client.gui.qad.QADTickBox.TickBoxModel;

public class DefaultTickBoxModel implements TickBoxModel {
	boolean state;
	
	public DefaultTickBoxModel(boolean initialState) {
		this.state = initialState;
	}
	
	public DefaultTickBoxModel() {
		this.state = false;
	}
	
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
