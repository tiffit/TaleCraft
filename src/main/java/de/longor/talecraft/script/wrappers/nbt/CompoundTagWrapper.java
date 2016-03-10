package de.longor.talecraft.script.wrappers.nbt;

import java.util.List;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.script.wrappers.IObjectWrapper;

public class CompoundTagWrapper implements IObjectWrapper {
	private NBTTagCompound comp;
	
	public CompoundTagWrapper() {
		comp = new NBTTagCompound();
	}
	
	public CompoundTagWrapper(NBTTagCompound in) {
		comp = in == null ? new NBTTagCompound() : in;
	}
	
	public CompoundTagWrapper(CompoundTagWrapper other) {
		comp = (NBTTagCompound) other.comp.copy();
	}
	
	@Override
	public NBTTagCompound internal() {
		return comp;
	}
	
	@Override
	public List<String> getOwnPropertyNames() {
		return TaleCraft.globalScriptManager.getOwnPropertyNames(this);
	}
	
	public CompoundTagWrapper clone() {
		return new CompoundTagWrapper(this);
	}
	
	public void setByte(String name, byte value) {
		comp.setByte(name, value);
	}
	
	public void setShort(String name, short value) {
		comp.setShort(name, value);
	}
	
	public void setInteger(String name, int value) {
		comp.setInteger(name, value);
	}
	
	public void setLong(String name, long value) {
		comp.setLong(name, value);
	}
	
	public void setFloat(String name, float value) {
		comp.setFloat(name, value);
	}
	
	public void setDouble(String name, double value) {
		comp.setDouble(name, value);
	}
	
	public void setString(String name, String value) {
		comp.setString(name, value);
	}
	
	public void setCompound(String name, CompoundTagWrapper wrap) {
		comp.setTag(name, wrap.comp);
	}
	
	public Set<String> getKeySet() {
		return comp.getKeySet();
	}
	
	public byte getByte(String name) {
		return comp.getByte(name);
	}
	
	public short getShort(String name) {
		return comp.getShort(name);
	}
	
	public int getInteger(String name) {
		return comp.getInteger(name);
	}
	
	public long getLong(String name) {
		return comp.getLong(name);
	}
	
	public float getFloat(String name) {
		return comp.getFloat(name);
	}
	
	public double getDouble(String name) {
		return comp.getDouble(name);
	}
	
	public CompoundTagWrapper getCompound(String name) {
		return new CompoundTagWrapper(comp.getCompoundTag(name));
	}
	
}
