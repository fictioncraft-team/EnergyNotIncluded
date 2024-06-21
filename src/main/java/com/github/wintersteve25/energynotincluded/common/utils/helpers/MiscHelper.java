package com.github.wintersteve25.energynotincluded.common.utils.helpers;

import com.mojang.math.Axis;
import net.minecraft.core.Direction;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.bounding.ONIBoundingBlock;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.bounding.ONIBoundingTE;
import com.github.wintersteve25.energynotincluded.common.registries.ONIBlocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Quaterniond;
import org.joml.Vector3d;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class MiscHelper {

    public static final double ONEPIXEL = 1D / 16;

    public static String langToReg(String lang) {
        return lang.toLowerCase().replace(' ', '_').replace('-', '_');
    }

    public static double randomInRange(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }

    public static float randomInRange(float min, float max) {
        return (float) ((Math.random() * (max - min)) + min);
    }

    public static int randomInRange(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    /**
     * Method modified from <a href="https://github.com/mekanism/Mekanism/blob/1.16.x/src/main/java/mekanism/common/util/WorldUtils.java#L537">...</a>
     */
    public static void makeBoundingBlock(@Nullable LevelAccessor world, BlockPos boundingLocation, BlockPos orig) {
        if (world != null) {
            ONIBoundingBlock boundingBlock = ONIBlocks.Misc.BOUNDING_BLOCK.block().get();
            BlockState newState = boundingBlock.defaultBlockState();
            world.setBlock(boundingLocation, newState, 3);

            if (world.isClientSide()) return;
            if (world.getBlockEntity(boundingLocation) instanceof ONIBoundingTE tile) {
                tile.setMainLocation(orig);
            } else {
                ONIUtils.LOGGER.warn("Unable to find Bounding Block Tile at: {}", boundingLocation);
            }
        }
    }

    public static BlockPos readBlockPos(Tag tag) {
        if (tag instanceof IntArrayTag intTags) {
            int[] aint = intTags.getAsIntArray();
            return aint.length == 3 ? new BlockPos(aint[0], aint[1], aint[2]) : null;
        }

        return null;
    }

    public static VoxelShape rotateShape(VoxelShape shape, double degrees, Vector3d axis) {
        VoxelShape result = Shapes.empty();

        AABB boundingBox = shape.bounds();
        Vector3d minbb = new Vector3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        Vector3d maxbb = new Vector3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        Vector3d center = maxbb.sub(minbb).div(2);

        Quaterniond rot = new Quaterniond()
                .rotateAxis(org.joml.Math.toRadians(degrees), axis);

        for (AABB aabb : shape.toAabbs()) {
            Vector3d min = new Vector3d(aabb.minX, aabb.minY, aabb.minZ);
            Vector3d max = new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ);

            min.sub(center) // center to origin
                .rotate(rot) // rotate
                .add(center); // move back

            max.sub(center).rotate(rot).add(center);

            VoxelShape rotated = Shapes.box(min.x, min.y, min.z, max.x, max.y, max.z);
            result = Shapes.or(result, rotated);
        }

        return result.optimize();
    }
}
