package talecraft.commands;

import java.util.List;

import com.google.common.base.Predicates;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class ButcherCommand extends TCCommandBase {

	@Override
	public String getName() {
		return "tc_butcher";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "[filter]";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		String filter = null;

		if(args.length == 1) {
			filter = args[0];
		}

		List<Entity> entities = sender.getEntityWorld().getEntities(Entity.class, Predicates.alwaysTrue());

		if(filter == null) {
			for(Entity entity : entities) {
				if(entity instanceof EntityItem)
					entity.setDead();
				if(entity instanceof EntityLiving)
					entity.setDead();
			}
			return;
		}

		if(filter.equalsIgnoreCase("items")) {
			for(Entity entity : entities)
				if(entity instanceof EntityItem)
					entity.setDead();
			return;
		}

		if(filter.equalsIgnoreCase("livings")) {
			for(Entity entity : entities)
				if(entity instanceof EntityLiving)
					entity.setDead();
			return;
		}

		throw new CommandException("Unknown Filter: " + filter, filter);
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		return getListOfStringsMatchingLastWord(args, new String[]{"items", "livings"});
	}

}
