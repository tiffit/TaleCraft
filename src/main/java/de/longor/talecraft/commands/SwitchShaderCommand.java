package de.longor.talecraft.commands;

import java.util.Collections;
import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.network.StringNBTCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class SwitchShaderCommand extends TCCommandBase {

	@Override
	public String getCommandName() {
		return "tc_switchShader";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "<player> <shader>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(Boolean.TRUE.booleanValue()) {
			throw new CommandException("This command is not yet implemented. :(");
		}

		if(args.length != 2) {
			throw new CommandException("Wrong number of parameters: " + args.length + " given, 2 needed.");
		}

		List<EntityPlayerMP> players = EntitySelector.matchEntities(sender, args[0], EntityPlayerMP.class);
		String shaderName = args[1];

		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("shaderName", shaderName);
		StringNBTCommand pkt = new StringNBTCommand("switchShader", nbt);

		for(EntityPlayerMP entityPlayerMP : players) {
			TaleCraft.network.sendTo(pkt, entityPlayerMP);
		}

	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		return Collections.emptyList();
	}

}
