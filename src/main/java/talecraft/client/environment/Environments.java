package talecraft.client.environment;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import talecraft.TaleCraft;
import talecraft.client.ClientRenderer;
import talecraft.client.environment.Environment.SkyLayer;

public class Environments {
	private static final ResourceLocation environmentRes = new ResourceLocation("talecraft:environment.json");
	private static final Map<String, Environment> mapping = new HashMap<String, Environment>();
	private static final Gson gson = new Gson();
	private static String currentEnvironment = null;
	
	public static final Environment getCurrent() {
		Environment env = currentEnvironment == null ? null : mapping.get(currentEnvironment);
		if(env == null) {
			env = mapping.get("default");
		}
		return env;
	}
	
	public static final boolean isNonDefault() {
		return getCurrent() != null;
	}

	public static void reload(IResourceManager resourceManager) {
		TaleCraft.logger.info("Reloading environments...");
		
		InputStream input = null;
		try {
			input = resourceManager.getResource(environmentRes).getInputStream();
			Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
			Type typeOfT = new TypeToken<HashMap<String, Environment>>(){}.getType();
			
			Map<String, Environment> newMapping = gson.fromJson(reader, typeOfT);
			
			mapping.clear();
			mapping.putAll(newMapping);
			System.out.println(newMapping);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		TaleCraft.logger.info("Done reloading environments.");
	}

	public static void render_sky(ClientRenderer instance, float partialTicks, WorldClient world, Minecraft mc) {
		Environment env = getCurrent();
		if(env == null) return;
	}
	
}
