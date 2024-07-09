package com.github.wintersteve25.energynotincluded.common.utils;

import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.bounding.BoundingShapeDefinition;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.bounding.ONIBoundingBlock;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.bounding.ONIBoundingTE;
import com.github.wintersteve25.energynotincluded.common.registries.ONIBlocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class MiscHelper {

    public static final Vec3 fromOrigin = new Vec3(-0.5, -0.5, -0.5);
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
        if (world == null) {
            return;
        }
        
        ONIBoundingBlock boundingBlock = ONIBlocks.BOUNDING_BLOCK.block().get();
        BlockState newState = boundingBlock.defaultBlockState();
        world.setBlock(boundingLocation, newState, 3);

        if (world.isClientSide()) return;
        if (world.getBlockEntity(boundingLocation) instanceof ONIBoundingTE tile) {
            tile.setMainLocation(orig);
        } else {
            ONIUtils.LOGGER.warn("Unable to find Bounding Block Tile at: {}", boundingLocation);
        }
    }
    
    public static BlockPos readBlockPos(Tag tag) {
        if (tag instanceof IntArrayTag intTags) {
            int[] aint = intTags.getAsIntArray();
            return aint.length == 3 ? new BlockPos(aint[0], aint[1], aint[2]) : null;
        }

        return null;
    }

    /**
     * Methods modified from <a href="https://github.com/mekanism/Mekanism/blob/1.20.6/src/main/java/mekanism/common/util/VoxelShapeUtils.java">...</a>
     */
    public static AABB rotate(AABB box, Direction side) {
        return switch (side) {
            case DOWN -> box;
            case UP -> new AABB(box.minX, -box.minY, -box.minZ, box.maxX, -box.maxY, -box.maxZ);
            case NORTH -> new AABB(box.minX, -box.minZ, box.minY, box.maxX, -box.maxZ, box.maxY);
            case SOUTH -> new AABB(-box.minX, -box.minZ, -box.minY, -box.maxX, -box.maxZ, -box.maxY);
            case WEST -> new AABB(box.minY, -box.minZ, -box.minX, box.maxY, -box.maxZ, -box.maxX);
            case EAST -> new AABB(-box.minY, -box.minZ, box.minX, -box.maxY, -box.maxZ, box.maxX);
        };
    }

    public static AABB rotate(AABB box, Rotation rotation) {
        return switch (rotation) {
            case NONE -> box;
            case CLOCKWISE_90 -> new AABB(-box.minZ, box.minY, box.minX, -box.maxZ, box.maxY, box.maxX);
            case CLOCKWISE_180 -> new AABB(-box.minX, box.minY, -box.minZ, -box.maxX, box.maxY, -box.maxZ);
            case COUNTERCLOCKWISE_90 -> new AABB(box.minZ, box.minY, -box.minX, box.maxZ, box.maxY, -box.maxX);
        };
    }

    public static AABB rotateHorizontal(AABB box, Direction side) {
        return switch (side) {
            case NORTH -> rotate(box, Rotation.NONE);
            case SOUTH -> rotate(box, Rotation.CLOCKWISE_180);
            case WEST -> rotate(box, Rotation.COUNTERCLOCKWISE_90);
            case EAST -> rotate(box, Rotation.CLOCKWISE_90);
            default -> box;
        };
    }

    public static VoxelShape rotate(VoxelShape shape, Direction side) {
        return rotate(shape, side, MiscHelper::rotate);
    }

    public static VoxelShape rotate(VoxelShape shape, Rotation rotation) {
        return rotate(shape, rotation, MiscHelper::rotate);
    }

    public static VoxelShape rotateHorizontal(VoxelShape shape, Direction side) {
        return rotate(shape, side, MiscHelper::rotateHorizontal);
    }

    public static VoxelShape rotate(VoxelShape shape, UnaryOperator<AABB> rotateFunction) {
        List<VoxelShape> rotatedPieces = new ArrayList<>();
        //Explode the voxel shape into bounding boxes
        List<AABB> sourceBoundingBoxes = shape.toAabbs();
        //Rotate them and convert them each back into a voxel shape
        for (AABB sourceBoundingBox : sourceBoundingBoxes) {
            //Make the bounding box be centered around the middle, and then move it back after rotating
            rotatedPieces.add(Shapes.create(rotateFunction.apply(sourceBoundingBox.move(fromOrigin.x, fromOrigin.y, fromOrigin.z))
                    .move(-fromOrigin.x, -fromOrigin.z, -fromOrigin.z)));
        }
        //return the recombined rotated voxel shape
        return combine(rotatedPieces);
    }

    public static <DATA> VoxelShape rotate(VoxelShape shape, DATA data, BiFunction<AABB, DATA, AABB> rotateFunction) {
        List<VoxelShape> rotatedPieces = new ArrayList<>();
        //Explode the voxel shape into bounding boxes
        List<AABB> sourceBoundingBoxes = shape.toAabbs();
        //Rotate them and convert them each back into a voxel shape
        for (AABB sourceBoundingBox : sourceBoundingBoxes) {
            //Make the bounding box be centered around the middle, and then move it back after rotating
            rotatedPieces.add(Shapes.create(rotateFunction.apply(sourceBoundingBox.move(fromOrigin.x, fromOrigin.y, fromOrigin.z), data)
                    .move(-fromOrigin.x, -fromOrigin.z, -fromOrigin.z)));
        }
        //return the recombined rotated voxel shape
        return combine(rotatedPieces);
    }

    public static VoxelShape combine(VoxelShape... shapes) {
        return batchCombine(Shapes.empty(), BooleanOp.OR, true, shapes);
    }

    public static VoxelShape combine(Collection<VoxelShape> shapes) {
        return batchCombine(Shapes.empty(), BooleanOp.OR, true, shapes);
    }

    public static VoxelShape exclude(VoxelShape... shapes) {
        return batchCombine(Shapes.block(), BooleanOp.ONLY_FIRST, true, shapes);
    }

    public static VoxelShape batchCombine(VoxelShape initial, BooleanOp function, boolean simplify, Collection<VoxelShape> shapes) {
        VoxelShape combinedShape = initial;
        for (VoxelShape shape : shapes) {
            combinedShape = Shapes.joinUnoptimized(combinedShape, shape, function);
        }
        return simplify ? combinedShape.optimize() : combinedShape;
    }

    public static VoxelShape batchCombine(VoxelShape initial, BooleanOp function, boolean simplify, VoxelShape... shapes) {
        VoxelShape combinedShape = initial;
        for (VoxelShape shape : shapes) {
            combinedShape = Shapes.joinUnoptimized(combinedShape, shape, function);
        }
        return simplify ? combinedShape.optimize() : combinedShape;
    }
}
