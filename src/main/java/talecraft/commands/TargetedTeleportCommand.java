package talecraft.commands;

import java.util.EnumSet;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketPlayerPosLook.EnumFlags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TargetedTeleportCommand extends TCCommandBase {

	@Override
	public String getName() {
		return "tc_tp";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "tc_tp <entity> <target>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// tc_tp self <target>
		// tc_tp self <x> <y> <z>
		// tc_tp <sl> <target>
		// tc_tp <sl> <x> <y> <z>
		// tc_tp <name> <target>
		// tc_tp <name> <x> <y> <z>

		if(args.length < 1) {
			throw new CommandException(getUsage(sender));
		}

		// Player to Entity teleport
		if(args.length == 1) {
			// name
			String name = args[0];

			// find entities wit that name
			List<Entity> targets = findNamedEntity(sender.getEntityWorld(), name);

			// is the list empty?
			if(targets.isEmpty()) {
				// failure
				throw new CommandException("Could not find any loaded entity named: " + name);
			}

			// we only need one, so take the first
			Entity target = targets.get(0);

			// now make sure the command sender has a entity wrapped
			Entity source = sender.getCommandSenderEntity();

			if(source == null) {
				throw new CommandException("Command-Sender is not a entity: Entity is null.");
			}

			// finally: Teleport
			teleport(source, target.getPositionVector());
			return;
		}

		final List<Entity> entitiesToTeleport = locateFunc(sender, args[0]);

		// TARGET SELECTION!
		Vec3d target = null;

		if(args.length == 2) {
			// <target>
			target = locateFunc(sender, args[1]).get(0).getPositionVector();

			if(target == null) {
				throw new CommandException("Target is null.");
			}
		} else if(args.length == 4) {
			final int min = -6000000;
			final int max = -6000000;

			final double baseX = sender.getPositionVector().x;
			final double baseY = sender.getPositionVector().y;
			final double baseZ = sender.getPositionVector().z;

			// <x> <y> <z>
			final double xCoord = CommandBase.parseDouble(baseX, args[1], min, max, true);
			final double yCoord = CommandBase.parseDouble(baseY, args[2], -16, 512, true);
			final double zCoord = CommandBase.parseDouble(baseZ, args[3], min, max, true);

			target = new Vec3d(xCoord, yCoord, zCoord);
		}

		if(target == null) {
			throw new CommandException("Target is null.");
		}

		// Finally: Teleport
		teleport(entitiesToTeleport, target);
	}

	/**This method either returns a list of entities with at least one entity, or it throws a {@link CommandException}.**/
	private List<Entity> locateFunc(ICommandSender sender, String funcStr) throws CommandException {
		List<Entity> entitiesToTeleport = null;

		// <SELECTOR>
		// $SELF
		// <NAME>

		if(funcStr.startsWith("@")) {
			// selector
			String selector = funcStr;
			entitiesToTeleport = EntitySelector.matchEntities(sender, selector, Entity.class);

			if(entitiesToTeleport.isEmpty()) {
				throw new CommandException("No entity found: Selector "+selector+" yielded no results.");
			}
		} else if(funcStr.equalsIgnoreCase("self")) {
			// self

			// make sure the command sender has a entity wrapped
			Entity source = sender.getCommandSenderEntity();

			if(source == null) {
				throw new CommandException("Command-Sender is not a entity: Entity is null.");
			}

			entitiesToTeleport = Lists.newArrayList(source);

			if(entitiesToTeleport.isEmpty()) {
				// THIS ERROR SHOULD NOT OCCUR.
				throw new CommandException("No entity found: CommandSender is not a entity. THIS ERROR SHOULD NOT OCCUR.");
			}
		} else {
			// name
			String name = funcStr;
			entitiesToTeleport = findNamedEntity(sender.getEntityWorld(), name);

			if(entitiesToTeleport.isEmpty()) {
				throw new CommandException("No entity found: There are no entities with the name '"+name+"'.");
			}
		}

		// by this point 'entitiesToTeleport' can not possibly be null.

		return entitiesToTeleport;
	}

	private List<Entity> findNamedEntity(final World world, final String name) {
		return world.getEntities(Entity.class, new Predicate<Entity>() {
			@Override public boolean apply(Entity input) {
				return name.equals(input.getName());
			}
		});
	}

	private void teleport(List<Entity> entitiesToTeleport, Vec3d target) throws CommandException {
		for(Entity entity : entitiesToTeleport) {
			teleport(entity, target);
		}
	}

	public void teleport(Entity entity, Vec3d target) throws CommandException {
		if(entity == null) {
			throw new CommandException("entity must not be NULL!");
		}

		if(target == null) {
			throw new CommandException("target must not be NULL!");
		}

		// XXX DISABLED FUNCTIONALITY: This doesn't work as expected.
		if(Boolean.FALSE.booleanValue() && entity instanceof EntityPlayerMP) {
			// special case code
			EnumSet<EnumFlags> enumset = EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class);
			float f = entity.rotationPitch;
			float f1 = entity.rotationYaw;

			entity.startRiding((Entity)null);
			((EntityPlayerMP)entity).connection.setPlayerLocation(target.x, target.y, target.z, f, f1, enumset);
			entity.velocityChanged = true;
			return;
		}

		entity.setPositionAndUpdate(target.x, target.y, target.z);
		entity.velocityChanged = true;
	}

}
