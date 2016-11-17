package talecraft.particles;

import net.minecraft.block.Block;
import net.minecraft.client.particle.Barrier;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class BlockEffect extends Barrier {

	public BlockEffect(World worldIn, double x, double y, double z, Block block) {
		super(worldIn, x, y, z, Item.getItemFromBlock(block));
		//		TextureAtlasSprite sprite = TCTextureAtlasSprite.create(new ResourceLocation(Reference.MOD_NAME.toLowerCase(), "textures/blocks/util/light.png"));
		//		this.setParticleIcon(sprite);
		this.particleMaxAge = 1;
	}

}
