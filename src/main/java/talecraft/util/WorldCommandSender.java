package talecraft.util;

import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import talecraft.TaleCraft;

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
	public ITextComponent getDisplayName() {
		return new TextComponentString(getName());
	}

	@Override
	public void sendMessage(ITextComponent message) {
		TaleCraft.logger.info("WorldCommandSender :: " + message.getUnformattedText());
	}
	@Override
	public BlockPos getPosition() {
		return new MutableBlockPos(0,0,0);
	}

	@Override
	public Vec3d getPositionVector() {
		return new Vec3d(0, 0, 0);
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
	public boolean canUseCommand(int permLevel, String commandName) {
		return true;
	}

	@Override
	public MinecraftServer getServer() {
		return FMLCommonHandler.instance().getMinecraftServerInstance();
	}

}
