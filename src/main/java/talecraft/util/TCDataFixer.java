package talecraft.util;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;
import talecraft.Reference;
import talecraft.TaleCraft;

// Adapted from: net.minecraft.util.datafix.fixes.TileEntityId
public class TCDataFixer implements IFixableData {
    private static final Map<String, String> OLD_TO_NEW_ID_MAP = Maps.newHashMap();
    
    static {
    	OLD_TO_NEW_ID_MAP.put("minecraft:tc_lockeddoorblock", Reference.MOD_ID + ":lockeddoorblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:tc_killblock", Reference.MOD_ID + ":killblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:tc_clockblock", Reference.MOD_ID + ":clockblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:tc_redstone_trigger", Reference.MOD_ID + ":redstone_trigger");
        OLD_TO_NEW_ID_MAP.put("minecraft:tc_redstone_activator", Reference.MOD_ID + ":redstone_activator");
        OLD_TO_NEW_ID_MAP.put("minecraft:tc_relayblock", Reference.MOD_ID + ":relayblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:tc_scriptblock", Reference.MOD_ID + ":scriptblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:tc_updatedetectorblock", Reference.MOD_ID + ":updatedetectorblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:tc_storageblock", Reference.MOD_ID + ":storageblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:tc_emitterblock", Reference.MOD_ID + ":emitterblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:tc_imagehologramblock", Reference.MOD_ID + ":imagehologramblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:tc_collisiontriggerblock", Reference.MOD_ID + ":collisiontriggerblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:tc_lightblock", Reference.MOD_ID + ":lightblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:tc_hiddenblock", Reference.MOD_ID + ":hiddenblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:tc_messageblock", Reference.MOD_ID + ":messageblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:tc_inverterblock", Reference.MOD_ID + ":inverterblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:tc_memoryblock", Reference.MOD_ID + ":memoryblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:tc_triggerfilterblock", Reference.MOD_ID + ":triggerfilterblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:tc_delayblock", Reference.MOD_ID + ":delayblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:tc_urlblock", Reference.MOD_ID + ":urlblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:tc_summonblock", Reference.MOD_ID + ":summonblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:tc_musicblock", Reference.MOD_ID + ":musicblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:tc_camerablock", Reference.MOD_ID + ":camerablock");
        
    	OLD_TO_NEW_ID_MAP.put("tc_lockeddoorblock", Reference.MOD_ID + ":lockeddoorblock");
        OLD_TO_NEW_ID_MAP.put("tc_killblock", Reference.MOD_ID + ":killblock");
        OLD_TO_NEW_ID_MAP.put("tc_clockblock", Reference.MOD_ID + ":clockblock");
        OLD_TO_NEW_ID_MAP.put("tc_redstone_trigger", Reference.MOD_ID + ":redstone_trigger");
        OLD_TO_NEW_ID_MAP.put("tc_redstonetrigger", Reference.MOD_ID + ":redstone_trigger");
        OLD_TO_NEW_ID_MAP.put("tc_redstone_activator", Reference.MOD_ID + ":redstone_activator");
        OLD_TO_NEW_ID_MAP.put("tc_relayblock", Reference.MOD_ID + ":relayblock");
        OLD_TO_NEW_ID_MAP.put("tc_scriptblock", Reference.MOD_ID + ":scriptblock");
        OLD_TO_NEW_ID_MAP.put("tc_updatedetectorblock", Reference.MOD_ID + ":updatedetectorblock");
        OLD_TO_NEW_ID_MAP.put("tc_storageblock", Reference.MOD_ID + ":storageblock");
        OLD_TO_NEW_ID_MAP.put("tc_emitterblock", Reference.MOD_ID + ":emitterblock");
        OLD_TO_NEW_ID_MAP.put("tc_imagehologramblock", Reference.MOD_ID + ":imagehologramblock");
        OLD_TO_NEW_ID_MAP.put("tc_collisiontriggerblock", Reference.MOD_ID + ":collisiontriggerblock");
        OLD_TO_NEW_ID_MAP.put("tc_lightblock", Reference.MOD_ID + ":lightblock");
        OLD_TO_NEW_ID_MAP.put("tc_hiddenblock", Reference.MOD_ID + ":hiddenblock");
        OLD_TO_NEW_ID_MAP.put("tc_messageblock", Reference.MOD_ID + ":messageblock");
        OLD_TO_NEW_ID_MAP.put("tc_inverterblock", Reference.MOD_ID + ":inverterblock");
        OLD_TO_NEW_ID_MAP.put("tc_memoryblock", Reference.MOD_ID + ":memoryblock");
        OLD_TO_NEW_ID_MAP.put("tc_triggerfilterblock", Reference.MOD_ID + ":triggerfilterblock");
        OLD_TO_NEW_ID_MAP.put("tc_delayblock", Reference.MOD_ID + ":delayblock");
        OLD_TO_NEW_ID_MAP.put(":tc_urlblock", Reference.MOD_ID + ":urlblock");
        OLD_TO_NEW_ID_MAP.put("tc_summonblock", Reference.MOD_ID + ":summonblock");
        OLD_TO_NEW_ID_MAP.put("tc_musicblock", Reference.MOD_ID + ":musicblock");
        OLD_TO_NEW_ID_MAP.put("tc_camerablock", Reference.MOD_ID + ":camerablock");
    }

    public int getFixVersion() {
        return 1;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
    	String oldID = compound.getString("id");
        String newID = OLD_TO_NEW_ID_MAP.get(oldID);

        if(newID != null) {
        	TaleCraft.logger.info("Converted tile entity ID. Old ID: " + oldID + ", new ID: " + newID);
            compound.setString("id", newID);
        }

        return compound;
    }
}