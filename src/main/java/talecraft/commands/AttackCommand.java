package talecraft.commands;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	private static List<String> damageSourceNames;
	private static Map<String, DamageSource> damageSources = new HashMap<String, DamageSource>();
	
	private static List<String> getDamageSourceNames(){
		if(damageSourceNames != null) return damageSourceNames;
		Field[] fields = DamageSource.class.getFields();
		damageSourceNames = new ArrayList<String>();
		for(Field field : fields){
			if(field.getType().isAssignableFrom(DamageSource.class)){
				damageSourceNames.add(field.getName());
			}
		}
		return damageSourceNames;
	}
	
	private static DamageSource getDamageSource(String name){
		if(damageSources.containsKey(name)) return damageSources.get(name);
		try{
			Field field = DamageSource.class.getField(name);
			DamageSource dmgsc = (DamageSource) field.get(null);
			damageSources.put(name, dmgsc);
			return dmgsc;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String getName() {
		return "tc_attack";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "<entity-selector> <damage-type> <damage-amount>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// check if we have all needed parameters
		// If not throw a syntax error.
		if(args.length != 3) {
			throw new SyntaxErrorException("Syntax: " + getUsage(sender));
		}

		// fetch
		String str_selector = args[0];
		String str_dmgtype = args[1];
		String str_dmgamount = args[2];

		// parse all parameters
		List<EntityLivingBase> entities = EntitySelector.matchEntities(sender, str_selector, EntityLivingBase.class);
		DamageSource damage_type = AttackCommand.getDamageSource(str_dmgtype);//AttackCommand.parseDamageType(str_dmgtype);
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

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		if(args.length == 1) {
			return getListOfStringsMatchingLastWord(args, new String[]{"@e","@a","@p","@r"});
		}
		if(args.length == 2) {
			return getListOfStringsMatchingLastWord(args, AttackCommand.getDamageSourceNames());
		}
		if(args.length == 3) {
			return getListOfStringsMatchingLastWord(args, new String[]{"1","0.5","2","2.5"});
		}

		return Collections.emptyList();
	}

}
