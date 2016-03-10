package de.longor.talecraft.invoke;

import net.minecraft.block.state.IBlockState;

public enum EnumTriggerState {
	ON(1,true), OFF(0,false), INVERT(-1,true), IGNORE(-2,true);
	
	private final int ordinal;
	private final boolean boolValue;
	private EnumTriggerState(int ord, boolean boolValue) {
		this.ordinal = ord;
		this.boolValue = boolValue;
	}
	
	public int getIntValue() {
		return this.ordinal;
	}
	
	public static final EnumTriggerState get(int ordinal) {
		switch(ordinal) {
		case 1: return ON;
		case 0: return OFF;
		case -1: return INVERT;
		case -2: return IGNORE;
		default: return ON;
		}
	}
	
	public EnumTriggerState override(EnumTriggerState triggerStateOverride) {
		switch (triggerStateOverride) {
		case ON: return ON;
		case OFF: return OFF;
		case IGNORE: return this;
		case INVERT: return this == ON ? OFF : ON;
		default: return ON;
		}
	}
	
	public EnumTriggerState invert() {
		switch (this) {
		case ON: return OFF;
		case OFF: return ON;
		case IGNORE: return ON;
		case INVERT: return ON;
		default: return ON;
		}
	}
	
	public boolean getBooleanValue() {
		return boolValue;
	}

	public static EnumTriggerState get(boolean flag) {
		return flag ? ON : OFF;
	}
	
}
