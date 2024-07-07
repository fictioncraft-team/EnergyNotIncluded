package com.github.wintersteve25.energynotincluded.common.registries;

import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.data.plasma.api.IPlasma;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;

public class ONICapabilities {
    public static final BlockCapability<IPlasma, Direction> PLASMA = BlockCapability.createSided(
            new ResourceLocation(ONIUtils.MODID, "plasma"),
            IPlasma.class
    );
}
