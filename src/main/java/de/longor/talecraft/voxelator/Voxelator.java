package de.longor.talecraft.voxelator;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import scala.tools.nsc.settings.Final;

import com.google.common.collect.Lists;

import de.longor.talecraft.util.BlockRegion;
import de.longor.talecraft.util.MutableBlockPos;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;

// TODO: Create (de)serialization and a GUI for the VOXELATOR system.
// TODO: Create a VXAction that places a ClipboardItem into the world.
// TODO: Create a VXAction that repeats another VXAction multiple times.
// TODO: Create VXShape Capsule
// TODO: Create VXShape Cylinder
// TODO: Create VXShape HollowBox
// TODO: Create VXShape HollowSphere
// TODO: Create VXShape HollowCapsule
// TODO: Create VXShape HollowCylinder
// TODO: Create VXShape Expression

/**
 * A total rewrite of the VoxelBrush: Voxelator.
 **/
public class Voxelator {
	
	public static void apply(VXShape shape, VXPredicate predicate, VXAction action, World world) {
		
		final BlockPos center = shape.getCenter();
		final BlockRegion region = shape.getRegion();
		final MutableBlockPos offset = new MutableBlockPos(center);
		
		final List<BlockSnapshot> previous = Lists.newArrayList();
		final List<BlockSnapshot> changes = Lists.newArrayList();
		
		final CachedWorldDiff fworld = new CachedWorldDiff(world, previous, changes);
		
		for(final BlockPos pos : (Iterable<BlockPos>)BlockPos.getAllInBox(region.getMin(), region.getMax())) {
			offset.set(pos);
			offset.__sub(center);
			
			if(shape.test(pos, center, offset, fworld) && predicate.test(pos, center, offset, fworld)) {
				action.apply(pos, center, offset, fworld);
			}
		}
		
		fworld.applyChanges(true);
	}
	
}
