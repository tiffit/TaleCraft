package talecraft.commands;

import java.util.Collections;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class PlayerDataCommand extends TCCommandBase {

	@Override
	public String getName() {
		return "tc_playerdata";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "<selector> {merge-data}";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length < 2) {
			throw new WrongUsageException(getUsage(sender), sender, args);
		}
		List<EntityPlayerMP> list = EntitySelector.matchEntities(sender, args[0], EntityPlayerMP.class);

		String nbtS = "";
		for(int i = 1; i < args.length; i++){
			nbtS += args[i];
		}
		NBTTagCompound nbt;
		try {
			nbt = JsonToNBT.getTagFromJson(nbtS);
		} catch (NBTException e) {
			throw new CommandException("The NBT given is not in the json format!", sender, nbtS);
		}
		
		//Checks for blacklisted words
		if(nbt.hasKey("abilities"))throw new CommandException("The NBT given has the blacklisted tag: \"abilities\"", sender, nbtS);
		if(nbt.hasKey("Sleeping"))throw new CommandException("The NBT given has the blacklisted tag: \"Sleeping\"", sender, nbtS);
		if(nbt.hasKey("HurtTime"))throw new CommandException("The NBT given has the blacklisted tag: \"HurtTime\"", sender, nbtS);
		if(nbt.hasKey("HurtByTimestamp"))throw new CommandException("The NBT given has the blacklisted tag: \"HurtByTimestamp\"", sender, nbtS);
		if(nbt.hasKey("DeathTime"))throw new CommandException("The NBT given has the blacklisted tag: \"DeathTime\"", sender, nbtS);
		if(nbt.hasKey("FallFlying"))throw new CommandException("The NBT given has the blacklisted tag: \"FallFlying\"", sender, nbtS);
		if(nbt.hasKey("Pos"))throw new CommandException("The NBT given has the blacklisted tag: \"Pos\"", sender, nbtS);
		if(nbt.hasKey("Motion"))throw new CommandException("The NBT given has the blacklisted tag: \"Motion\"", sender, nbtS);
		if(nbt.hasKey("Rotation"))throw new CommandException("The NBT given has the blacklisted tag: \"Rotation\"", sender, nbtS);
		if(nbt.hasKey("FallDistance"))throw new CommandException("The NBT given has the blacklisted tag: \"FallDistance\"", sender, nbtS);
		if(nbt.hasKey("Fire"))throw new CommandException("The NBT given has the blacklisted tag: \"Fire\"", sender, nbtS);
		if(nbt.hasKey("Air"))throw new CommandException("The NBT given has the blacklisted tag: \"Air\"", sender, nbtS);
		if(nbt.hasKey("OnGround"))throw new CommandException("The NBT given has the blacklisted tag: \"OnGround\"", sender, nbtS);
		if(nbt.hasKey("Dimension"))throw new CommandException("The NBT given has the blacklisted tag: \"Dimension\"", sender, nbtS);
		if(nbt.hasKey("Invulnerable"))throw new CommandException("The NBT given has the blacklisted tag: \"Invulnerable\"", sender, nbtS);
		if(nbt.hasKey("PortalCooldown"))throw new CommandException("The NBT given has the blacklisted tag: \"PortalCooldown\"", sender, nbtS);
		if(nbt.hasKey("UUID"))throw new CommandException("The NBT given has the blacklisted tag: \"UUID\"", sender, nbtS);
		if(nbt.hasKey("CustomName"))throw new CommandException("The NBT given has the blacklisted tag: \"CustomName\"", sender, nbtS);
		if(nbt.hasKey("CustomNameVisible"))throw new CommandException("The NBT given has the blacklisted tag: \"CustomNameVisible\"", sender, nbtS);
		if(nbt.hasKey("Silent"))throw new CommandException("The NBT given has the blacklisted tag: \"Silent\"", sender, nbtS);
		if(nbt.hasKey("NoGravity"))throw new CommandException("The NBT given has the blacklisted tag: \"NoGravity\"", sender, nbtS);
		if(nbt.hasKey("Glowing"))throw new CommandException("The NBT given has the blacklisted tag: \"Glowing\"", sender, nbtS);
		
		for(EntityPlayerMP player : list){
			NBTTagCompound old = player.serializeNBT();
			old.merge(nbt);
			player.deserializeNBT(old);
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		if(args.length <= 1) {
			return getListOfStringsMatchingLastWord(args, new String[] {"@a", "@p", "@r", "@e"});
		}
		return Collections.emptyList();
	}

}
