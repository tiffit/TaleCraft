package de.longor.talecraft.script.wrappers.world;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.entities.EntityPoint;
import de.longor.talecraft.script.wrappers.IObjectWrapper;
import de.longor.talecraft.script.wrappers.entity.EntityObjectWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.world.border.EnumBorderStatus;
import net.minecraft.world.border.WorldBorder;

public class WorldBorderObjectWrapper implements IObjectWrapper {
	private WorldBorder worldBorder;
	
	public WorldBorderObjectWrapper(WorldBorder worldBorder) {
		this.worldBorder = worldBorder;
	}
	
	@Override
	public WorldBorder internal() {
		return worldBorder;
	}
	
	@Override
	public List<String> getOwnPropertyNames() {
		return TaleCraft.globalScriptManager.getOwnPropertyNames(this);
	}
	
	public EnumBorderStatus getStatus() {
		return worldBorder.getStatus();
	}
	
	public int getSize() {
		return worldBorder.getSize();
	}
	
	public void setSize(int newSize) {
		worldBorder.setSize(newSize);
	}
	
	public double getDiameter() {
		return worldBorder.getDiameter();
	}
	
	public double getCenterX() {
		return worldBorder.getCenterX();
	}
	
	public double getCenterZ() {
		return worldBorder.getCenterZ();
	}
	
	public void setCenter(double x, double z) {
		worldBorder.setCenter(x, z);
	}
	
	public void setDamageBuffer(double size) {
		worldBorder.setDamageBuffer(size);
	}
	
	public void setDamageAmount(double amount) {
		worldBorder.setDamageAmount(amount);
	}
	
	public double getDamageBuffer() {
		return worldBorder.getDamageBuffer();
	}
	
	public double getDamageAmount() {
		return worldBorder.getDamageAmount();
	}
	
	public double getClosestDistance(double x, double z) {
		return worldBorder.getClosestDistance(x, z);
	}
	
	public double getClosestDistance(Entity entity) {
		return worldBorder.getClosestDistance(entity);
	}
	
	public double getClosestDistance(EntityObjectWrapper entity) {
		return worldBorder.getClosestDistance(entity.internal());
	}
	
}
