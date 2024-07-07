package com.github.wintersteve25.energynotincluded.common.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import com.github.wintersteve25.energynotincluded.ONIUtils;

public class ONITags {
    public static final TagKey<Block> IRREPLACEABLE_BY_POCKET = TagKey.create(Registries.BLOCK, new ResourceLocation(ONIUtils.MODID, "irreplaceable_by_pocket"));

    public static void register() {
    }
}
