package talecraft.client.environment;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import talecraft.TaleCraft;
import talecraft.client.ClientRenderer;
import talecraft.client.environment.Environment.SkyLayer;
import talecraft.client.render.IRenderable;
import talecraft.proxy.ClientProxy;

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

		for(SkyLayer skyLayer : env.sky) {
			switch(skyLayer.blend) {
				default:
					GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					break;
			}

			GL11.glDepthMask(skyLayer.depth);

			if(skyLayer.cull)
				GL11.glEnable(GL11.GL_CULL_FACE);
			else
				GL11.glDisable(GL11.GL_CULL_FACE);

			Matrix4f skyMat = new Matrix4f();
			skyLayer.getMatrix(skyMat, world.getWorldTime()/20f+partialTicks);

			// TODO: Apply transformation matrix.

			SkyGeometry skyGeo = render_sky_geometry(skyLayer.shape);
			skyGeo.render(mc, TaleCraft.asClient(), partialTicks);
		}
	}

	private static void render_sky_blendFunc(String blendMode) {
	}

	private static SkyGeometry render_sky_geometry(String shape) {
		return new SkyGeometry_Null();
	}

	private static abstract class SkyGeometry {
		public abstract void render(Minecraft mc, ClientProxy clientProxy, double partialTicks);
	}
	// TODO: Implement various sky geometries and their renderers.
	// (null, plane, cube, dome, sphere, cylinder, pyramid)

	private static class SkyGeometry_Null extends SkyGeometry {
		@Override
		public void render(Minecraft mc, ClientProxy clientProxy, double partialTicks) {
			// Literally do nothing.
		}
	}
}
