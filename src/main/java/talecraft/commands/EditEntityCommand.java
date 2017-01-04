package talecraft.commands;

import java.util.List;
import java.util.UUID;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import talecraft.TaleCraft;
import talecraft.network.packets.StringNBTCommandPacketClient;

public class EditEntityCommand extends CommandBase {

	@Override
	public String getName() {
		return "tc_editentity";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "<uuid|entity-selector|SELF>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length != 1) {
			throw new CommandException("Wrong number of parameters: " + args.length + " given, 1 needed.");
		}

		Entity senderEntity = sender.getCommandSenderEntity();

		if(senderEntity == null) {
			throw new CommandException("Command must be executed by a actual player. Sender has no entity.");
		}

		if(!(senderEntity instanceof EntityPlayerMP)) {
			throw new CommandException("Command must be executed by a actual player. Entity is not a player.");
		}

		EntityPlayerMP player = (EntityPlayerMP) senderEntity;
		World theWorld = sender.getEntityWorld();
		Entity theEntity = null;

		if(args[0].equalsIgnoreCase("self")) {
			theEntity = player;
		} else if(args[0].startsWith("@")) {
			List<Entity> entities = EntitySelector.matchEntities(player, args[0], Entity.class);
			if(!entities.isEmpty()) {
				theEntity = entities.get(0);
			}
		} else {
			UUID uuid = UUID.fromString(args[0]);

			for(Object entityObj : theWorld.loadedEntityList) {
				Entity entity = (Entity) entityObj;

				if(entity.getUniqueID().equals(uuid)) {
					theEntity = entity;
					break;
				}
			}
		}

		if(theEntity == null) {
			throw new CommandException("Entity not found: " + args[0]);
		}

		TaleCraft.logger.debug("Found entity to edit: " + theEntity);

		NBTTagCompound entityData = new NBTTagCompound();
		theEntity.writeToNBT(entityData);
		entityData.setString("id", theEntity instanceof EntityPlayerMP ? "Player" : EntityList.getEntityString(theEntity));

		NBTTagCompound commandData = new NBTTagCompound();
		commandData.setTag("entityData", entityData);
		commandData.setString("entityUUID", theEntity.getUniqueID().toString());

		StringNBTCommandPacketClient command = new StringNBTCommandPacketClient("client.gui.editor.entity", commandData);

		TaleCraft.logger.debug("Sending entity data for editing to player: " + player.getName());
		TaleCraft.network.sendTo(command, player);
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		return getListOfStringsMatchingLastWord(args, new String[]{"@e","self"});
	}

}
