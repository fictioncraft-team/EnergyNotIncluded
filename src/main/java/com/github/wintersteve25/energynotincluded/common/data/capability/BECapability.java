package com.github.wintersteve25.energynotincluded.common.data.capability;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.logging.log4j.util.BiConsumer;

public record BECapability<B extends BlockEntity>(
        DeferredHolder<BlockEntityType<?>, BlockEntityType<B>> ibeType,
        BiConsumer<RegisterCapabilitiesEvent, BlockEntityType<B>> registrationFunc
) {
}
