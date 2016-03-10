package de.longor.talecraft.util;

public class MutableInteger {
	private int value;
	
	public MutableInteger(int initialValue) {
		this.value = initialValue;
	}
	
	public MutableInteger() {
		this.value = 0;
	}
	
	public void set(int newValue) {
		this.value = newValue;
	}
	
	public int get() {
		return this.value;
	}
	
	public int decrement() {
		return --value;
	}
	
	public int increment() {
		return ++value;
	}
	
	public void add(int v) {
		value += v;
	}
	
}
