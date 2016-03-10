package de.longor.talecraft.util;

public final class Either<A,B> {
	private final A a;
	private final B b;
	
	public Either(A a, B b) {
		this.a = a;
		this.b = b;
	}
	
	public A getA() {
		return a;
	}
	
	public B getB() {
		return b;
	}
	
	public boolean issetA() {
		return a != null;
	}
	
	public boolean issetB() {
		return b != null;
	}
}
