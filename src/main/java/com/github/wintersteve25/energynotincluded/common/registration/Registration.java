package com.github.wintersteve25.energynotincluded.common.registration;

import com.github.wintersteve25.energynotincluded.common.registries.worldgen.ONIDensityFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.material.Fluid;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.registries.*;
import com.github.wintersteve25.energynotincluded.common.registries.worldgen.ONIFeatures;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Registration {
    public static final DeferredRegister<Fluid> FLUID = DeferredRegister.create(Registries.FLUID, ONIUtils.MODID);
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, ONIUtils.MODID);

    public static void init(IEventBus eventBus) {
        ONITags.register();

        ONIBlocks.register(eventBus);
        ONIItems.register(eventBus);
        ONIGases.register(eventBus);
        ONISounds.register(eventBus);
        ONICapabilities.register(eventBus);
        ONIRecipes.register(eventBus);

        ONIFeatures.register(eventBus);
        ONIDensityFunction.register(eventBus);

        EFFECTS.register(eventBus);
        FLUID.register(eventBus);

        ONIUtils.LOGGER.info("ONIUtils Registration Completed");
    }
}
