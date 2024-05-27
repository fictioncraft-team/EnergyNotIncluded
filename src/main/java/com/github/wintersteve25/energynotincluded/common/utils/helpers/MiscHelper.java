package com.github.wintersteve25.energynotincluded.common.utils.helpers;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.bounding.ONIBoundingBlock;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.bounding.ONIBoundingTE;
import com.github.wintersteve25.energynotincluded.common.registries.ONIBlocks;

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

    public static <K, V> HashMap<K, V> newHashmap(List<K> keys, List<V> values) {
        HashMap<K, V> map = new HashMap<>();

        for (int i = 0; i < keys.size(); i++) {
            try {
                map.put(keys.get(i), values.get(i));
            } catch (ArrayIndexOutOfBoundsException e) {
                return map;
            }
        }

        return map;
    }

    /**
     * @return returns a new list that contains all the object in the first list that isn't in the second list
     */
    public static <E> List<E> filterList(Collection<E> list, Collection<E> other) {
        return list.stream().filter(obj -> !other.contains(obj)).collect(Collectors.toList());
    }

    public static <T, E> List<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static double distEuclidean(BlockPos pos1, BlockPos pos2) {
        return Math.sqrt(
                Math.pow(pos2.getX() - pos1.getX(), 2) + Math.pow(pos2.getY() - pos1.getY(), 2) + Math.pow(pos2.getZ() - pos1.getZ(), 2)
        );
    }

    public static boolean chanceHandling(float chance) {
        return chanceHandling(new Random(), chance);
    }

    public static boolean chanceHandling(Random rand, float chance) {
        return rand.nextDouble() < chance;
    }
}
