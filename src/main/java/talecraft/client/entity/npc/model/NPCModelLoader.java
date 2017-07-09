package talecraft.client.entity.npc.model;

import java.nio.file.Files;
import java.nio.file.Paths;

import talecraft.TaleCraft;

public class NPCModelLoader {

	public static NPCModelPattern loadModel(String model) {
		if (model.startsWith("internal:")) {
			return loadInternal(model.replace("internal:", ""));
		}
		return null;
	}

	private static NPCModelPattern loadInternal(String model) {
		try {
			String json = new String(Files.readAllBytes(Paths.get(NPCModelLoader.class.getResource("internal/" + model + ".json").toURI())));
			NPCModelPattern pat = TaleCraft.gson.fromJson(json, NPCModelPattern.class);
			pat.file = "internal:" + model;
			return pat;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
