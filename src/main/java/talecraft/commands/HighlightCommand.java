package talecraft.commands;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import talecraft.TaleCraft;
import talecraft.network.packets.StringNBTCommandPacket;
import talecraft.util.CommandArgumentParser;

public class HighlightCommand extends TCCommandBase {

	@Override
	public String getName() {
		return "tc_highlight";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "< ? >";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		CommandArgumentParser parser = new CommandArgumentParser(args);
		parser.commandSenderPosition = sender.getPositionVector();

		String action = parser.consume_string("Couldn't parse highlight action!");

		if(action.equals("clear")) {
			// TODO: clear highlights!
			sender.sendMessage(new TextComponentString(TextFormatting.RED+"ERROR: highlight clearing not yet implemented."));
			return;
		}

		if(action.equals("entity")) {
			// highlight a entity/multiple entities
			double duration = parser.consume_double("Couldn't parse duration!", 0.0000000001d, 10d);
			String selector = parser.consume_string("Couldn't parse entity selector!");

			List<EntityPlayerSP> entities = EntitySelector.matchEntities(sender, selector, EntityPlayerSP.class);

			Potion potion = Potion.getPotionFromResourceLocation("minecraft:glow");
			PotionEffect effect = new PotionEffect(potion, (int) duration, 1);
			
			for(Entity ent : entities) {
				if(ent instanceof EntityLiving) {
					((EntityLiving) ent).addPotionEffect(effect);
				}
			}
			return;
		}

		if(action.equals("block")) {
			// highlight a block

			double duration = parser.consume_double("Couldn't parse duration!", 0.0000000001d, 10d);
			BlockPos blockPos = parser.consume_blockpos("Couldn't parse block position!");

			NBTTagCompound pktdata = new NBTTagCompound();
			pktdata.setString("type", "highlight.block");
			pktdata.setInteger("pos.x", blockPos.getX());
			pktdata.setInteger("pos.y", blockPos.getY());
			pktdata.setInteger("pos.z", blockPos.getZ());
			pktdata.setDouble("duration", duration);
			TaleCraft.network.sendToAll(new StringNBTCommandPacket("client.render.renderable.push", pktdata));
			return;
		}

	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		if(args.length <= 1) {
			return getListOfStringsMatchingLastWord(args, new String[]{"entity","block","clear"});
		}

		return Collections.emptyList();
	}

}
