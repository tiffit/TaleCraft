package de.longor.talecraft.util;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.entities.EntityPoint;
import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class WorldCommandSender implements ICommandSender {
	private World world;
	
	public WorldCommandSender(World world) {
		this.world = world;
	}
	
	@Override
	public String getName() {
		return "World";
	}
	
	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentText(getName());
	}

	@Override
	public void addChatMessage(IChatComponent message) {
		TaleCraft.logger.info("WorldCommandSender :: " + message.getUnformattedText());
	}
	@Override
	public BlockPos getPosition() {
		return new MutableBlockPos(0,0,0);
	}

	@Override
	public Vec3 getPositionVector() {
		return new Vec3(0, 0, 0);
	}

	@Override
	public World getEntityWorld() {
		return world;
	}
	
	@Override
	public Entity getCommandSenderEntity() {
		return null;
	}
	
	@Override
	public boolean sendCommandFeedback() {
		return true;
	}

	@Override
	public void setCommandStat(Type type, int amount) {
		// no
	}

	@Override
	public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
		return true;
	}

}
