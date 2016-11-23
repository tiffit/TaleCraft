package talecraft;

import net.minecraft.world.GameRules;

public class TaleCraftGameRules {

	public static void registerGameRules(GameRules rules){
		rules.addGameRule("tc_playDefaultMusic", "true", GameRules.ValueType.BOOLEAN_VALUE);
		rules.addGameRule("tc_disableInvokeSystem", "false", GameRules.ValueType.BOOLEAN_VALUE);
		rules.addGameRule("tc_visualEventDebugging", "false", GameRules.ValueType.BOOLEAN_VALUE);
		rules.addGameRule("tc_disableTeleporter", "false", GameRules.ValueType.BOOLEAN_VALUE);
		rules.addGameRule("tc_disableWeather", "false", GameRules.ValueType.BOOLEAN_VALUE);
		
		rules.addGameRule("tc_disable.damage.*", "false", GameRules.ValueType.BOOLEAN_VALUE);
		rules.addGameRule("tc_disable.damage.fall", "false", GameRules.ValueType.BOOLEAN_VALUE);
		rules.addGameRule("tc_disable.damage.drown", "false", GameRules.ValueType.BOOLEAN_VALUE);
		rules.addGameRule("tc_disable.damage.lava", "false", GameRules.ValueType.BOOLEAN_VALUE);
		rules.addGameRule("tc_disable.damage.magic", "false", GameRules.ValueType.BOOLEAN_VALUE);
		rules.addGameRule("tc_disable.damage.fire", "false", GameRules.ValueType.BOOLEAN_VALUE);
		rules.addGameRule("tc_disable.damage.suffocate", "false", GameRules.ValueType.BOOLEAN_VALUE);
	}
	
}
