package com.github.wintersteve25.energynotincluded.common.contents.base.blocks.bounding;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;

import java.util.function.Function;

public record BoundingShapeDefinition(short[][][] shape, Vec3i controller) {
    public static final BoundingShapeDefinition EMPTY = new BoundingShapeDefinition(new short[0][0][0], Vec3i.ZERO);
    
    public void foreachBoundingLocation(Direction facing, BlockPos originPos, Function<BlockPos, Boolean> action) {
        short[][][] shape = shape();
        Vec3i controller = controller();

        for (int i = 0; i < shape.length; i++) {
            short[][] layer = shape[i];
            for (int j = 0; j < layer.length; j++) {
                short[] depths = layer[j];
                for (int k = 0; k < depths.length; k++) {
                    short solid = depths[k];
                    if (i == controller.getX() && j == controller.getY() && k == controller.getZ()) continue;
                    if (solid != 1) continue;

                    // controller.getX for y b/c shape is defined in layers so the first index is the y value
                    int up = i - controller.getX();
                    int away = j - controller.getY();
                    int right = k - controller.getZ();

                    if (action.apply(originPos.offset(offset(facing, up, right, -away)))) {
                        return;
                    }
                }
            }
        }
    }
    
    private Vec3i offset(Direction facing, int up, int right, int away) {
        return switch (facing) {
            case DOWN -> null;
            case UP -> null;
            case NORTH -> new Vec3i(right, up, -away);
            case SOUTH -> new Vec3i(-right, up, away);
            case WEST -> new Vec3i(-away, up, -right);
            case EAST -> new Vec3i(away, up, right);
        };
    }
}
