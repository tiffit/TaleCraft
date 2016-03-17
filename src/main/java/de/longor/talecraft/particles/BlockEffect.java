package de.longor.talecraft.particles;

import java.lang.reflect.InvocationTargetException;

import de.longor.talecraft.Reference;
import de.longor.util.TCTextureAtlasSprite;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Barrier;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEffect extends Barrier {

	public BlockEffect(World worldIn, double x, double y, double z, Block block) {
		super(worldIn, x, y, z, Item.getItemFromBlock(block));
//		TextureAtlasSprite sprite = TCTextureAtlasSprite.create(new ResourceLocation(Reference.MOD_NAME.toLowerCase(), "textures/blocks/util/light.png"));
//		this.setParticleIcon(sprite);
		this.particleMaxAge = 1;
	}

}
