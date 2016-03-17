package de.longor.util;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

public class TCTextureAtlasSprite extends TextureAtlasSprite {

	protected TCTextureAtlasSprite(String spriteName) {
		super(spriteName);
	}
	
	public static TextureAtlasSprite create(ResourceLocation spriteResourceLocation){
		return new TCTextureAtlasSprite(spriteResourceLocation.toString());
	}

}
