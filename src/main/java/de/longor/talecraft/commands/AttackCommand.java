package de.longor.talecraft.commands;

import java.util.Collections;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;

public class AttackCommand extends TCCommandBase {

	@Override
	public String getCommandName() {
		return "tc_attack";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "<entity-selector> <damage-type> <damage-amount>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// check if we have all needed parameters
		// If not throw a syntax error.
		if(args.length != 3) {
			throw new SyntaxErrorException("Syntax: " + getCommandUsage(sender));
		}

		// fetch
		String str_selector = args[0];
		String str_dmgtype = args[1];
		String str_dmgamount = args[2];

		// parse all parameters
		List<EntityLivingBase> entities = EntitySelector.matchEntities(sender, str_selector, EntityLivingBase.class);
		DamageSource damage_type = AttackCommand.parseDamageType(str_dmgtype);
		double damage_amount = CommandBase.parseDouble(str_dmgamount, 0, 1000);

		// check entities
		if(entities.size() == 0) {
			throw new CommandException("No entities found: " + str_selector);
		}

		// check damage type
		if(damage_type == null) {
			throw new CommandException("Unknown damage type: " + str_dmgtype);
		}

		// Attack the entities with the given damage type and amount.
		for(EntityLivingBase living : entities) {
			if(living instanceof EntityPlayerMP && ((EntityPlayerMP)living).capabilities.isCreativeMode) {
				continue;
			}

			living.attackEntityFrom(damage_type, (float) damage_amount);
		}
	}

	private static DamageSource parseDamageType(String str_dmgtype) {
		if(str_dmgtype.equalsIgnoreCase("magic")) {
			return DamageSource.magic;
		}

		if(str_dmgtype.equalsIgnoreCase("anvil")) {
			return DamageSource.anvil;
		}

		if(str_dmgtype.equalsIgnoreCase("cactus")) {
			return DamageSource.cactus;
		}

		if(str_dmgtype.equalsIgnoreCase("drown")) {
			return DamageSource.drown;
		}

		if(str_dmgtype.equalsIgnoreCase("fall")) {
			return DamageSource.fall;
		}

		if(str_dmgtype.equalsIgnoreCase("fallingblock")) {
			return DamageSource.fallingBlock;
		}

		if(str_dmgtype.equalsIgnoreCase("generic")) {
			return DamageSource.generic;
		}

		if(str_dmgtype.equalsIgnoreCase("fire")) {
			return DamageSource.inFire;
		}

		if(str_dmgtype.equalsIgnoreCase("wall")) {
			return DamageSource.inWall;
		}

		if(str_dmgtype.equalsIgnoreCase("lava")) {
			return DamageSource.lava;
		}

		if(str_dmgtype.equalsIgnoreCase("lightningbolt")) {
			return DamageSource.lightningBolt;
		}

		if(str_dmgtype.equalsIgnoreCase("onfire")) {
			return DamageSource.onFire;
		}

		if(str_dmgtype.equalsIgnoreCase("out_of_world")) {
			return DamageSource.outOfWorld;
		}

		if(str_dmgtype.equalsIgnoreCase("starve")) {
			return DamageSource.starve;
		}

		if(str_dmgtype.equalsIgnoreCase("wither")) {
			return DamageSource.wither;
		}

		return null;
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		if(args.length == 0) {
			return getListOfStringsMatchingLastWord(args, new String[]{"@e","@a","@p","@r"});
		}
		if(args.length == 1) {
			return getListOfStringsMatchingLastWord(args, new String[]{"magic","anvil","cactus","drown","fall","fallingblock","generic","fire","wall","lava","lightingbolt","onfire","out_of_world","starve","wither"}); // type
		}
		if(args.length == 2) {
			return getListOfStringsMatchingLastWord(args, new String[]{"1","0.5","2","2.5"});
		}

		return Collections.emptyList();
	}

}