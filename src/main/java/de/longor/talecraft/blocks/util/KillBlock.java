package de.longor.talecraft.blocks.util;

import java.util.List;

import de.longor.talecraft.blocks.TCBlock;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class KillBlock extends TCBlock {
    public static final PropertyInteger KILLTYPE = PropertyInteger.create("ktype", 0, 6);
    
	public KillBlock() {
		super();
        this.setDefaultState(this.blockState.getBaseState().withProperty(KILLTYPE, Integer.valueOf(0)));
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if(entityIn instanceof EntityPlayerSP) {
			return;
		}
    	
		if(entityIn instanceof EntityPlayerMP) {
			EntityPlayerMP p = (EntityPlayerMP) entityIn;
			if(p.capabilities.isCreativeMode)
				return;
		}
		
		int type = ((Integer)state.getValue(KILLTYPE)).intValue();
    	
		/*
			0 "all",
			1 "npc",
			2 "items",
			3 "living",
			4 "player",
			5 "monster",
			6 "xor_player"
		 */
		
    	switch(type) {
    		case 1: if(entityIn instanceof EntityVillager) {
					entityIn.setPosition(entityIn.posX, -1024, entityIn.posZ);
    				entityIn.attackEntityFrom(DamageSource.outOfWorld, 999999999F);
    		} break;
    		
    		case 2: if(entityIn instanceof EntityItem) {
				entityIn.setPosition(entityIn.posX, -1024, entityIn.posZ);
				entityIn.attackEntityFrom(DamageSource.outOfWorld, 999999999F);
    		} break;
    		
    		case 3: if(entityIn instanceof EntityLivingBase) {
				entityIn.setPosition(entityIn.posX, -1024, entityIn.posZ);
				entityIn.attackEntityFrom(DamageSource.outOfWorld, 999999999F);
    		} break;
    		
    		case 4: if(entityIn instanceof EntityPlayer) {
				entityIn.setPosition(entityIn.posX, -1024, entityIn.posZ);
				entityIn.attackEntityFrom(DamageSource.outOfWorld, 999999999F);
    		} break;
    		
    		case 5: if(entityIn instanceof EntityMob) {
				entityIn.setPosition(entityIn.posX, -1024, entityIn.posZ);
				entityIn.attackEntityFrom(DamageSource.outOfWorld, 999999999F);
    		} break;
    		
    		case 6: if(!(entityIn instanceof EntityPlayer)) {
				entityIn.setPosition(entityIn.posX, -1024, entityIn.posZ);
				entityIn.attackEntityFrom(DamageSource.outOfWorld, 999999999F);
    		} break;
    		
    		case 0: default: {
				entityIn.setPosition(entityIn.posX, -1024, entityIn.posZ);
				entityIn.attackEntityFrom(DamageSource.outOfWorld, 999999999F);
    		} break;
    	}
    	
	}
	
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return false;
    }
	
	@Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn) {
    	
    }
	
	@Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        float f = 0.45f;
        return new AxisAlignedBB(
        		(double) (pos.getX() + f),
        		(double) (pos.getY() + f),
        		(double) (pos.getZ() + f),
        		(double)((pos.getX() + 1) - f),
        		(double)((pos.getY() + 1) - f),
        		(double)((pos.getZ() + 1) - f)
        );
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        for (int j = 0; j < 7; ++j)
        {
            list.add(new ItemStack(itemIn, 1, j));
        }
    }
	
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(KILLTYPE, Integer.valueOf(meta));
    }
	
	@Override
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(KILLTYPE)).intValue();
    }
	
	@Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {KILLTYPE});
    }
	
	@Override
    public boolean isOpaqueCube()
    {
        return true;
    }
	
}
