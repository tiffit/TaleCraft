package talecraft.commands;

import java.util.Collections;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class MountCommand extends TCCommandBase {

	@Override
	public String getName() {
		return "tc_mount";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "<rider> <mount>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length != 2) {
			throw new WrongUsageException(getUsage(sender), sender, args);
		}

		Entity rider = fetchEntity(sender, args[0]);
		Entity mount = fetchEntity(sender, args[1]);

		if(rider.getRidingEntity() != null) {
			throw new CommandException("Rider is already mounting another entity.", sender, rider, rider.getRidingEntity());
		}

		if(!mount.getPassengers().isEmpty()) {
			throw new CommandException("Mount is already mounted by another entity.", sender, mount, mount.getPassengers());
		}

		rider.startRiding(mount);
	}

	private Entity fetchEntity(ICommandSender sender, String string) throws CommandException {
		if(string.equalsIgnoreCase("this")) {
			if(sender.getCommandSenderEntity() != null) {
				return sender.getCommandSenderEntity();
			} else {
				throw new CommandException("CommandSender does not have a entity assigned.", sender, string);
			}
		}

		List<Entity> list = EntitySelector.matchEntities(sender, string, Entity.class);

		if(list.size() == 0) {
			throw new CommandException("Matched zero entitires: " + string, sender, string);
		}
		if(list.size() > 1) {
			throw new CommandException("Matched more than one ("+list.size()+") entity: " + string, sender, string);
		}

		Entity ent = list.get(0);

		if(ent == null) {
			throw new CommandException("Could not match one entity: " + string, sender, string);
		}

		return ent;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		if(args.length <= 1) {
			return getListOfStringsMatchingLastWord(args, new String[] {"this", "@a", "@p", "@r", "@e"});
		}

		if(args.length == 2) {
			return getListOfStringsMatchingLastWord(args, new String[] {"this", "@a", "@p", "@r", "@e"});
		}

		return Collections.emptyList();
	}

}
