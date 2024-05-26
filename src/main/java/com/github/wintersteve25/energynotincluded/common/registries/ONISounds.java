package com.github.wintersteve25.energynotincluded.common.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ONISounds {
    public static final DeferredRegister<SoundEvent> SOUND = DeferredRegister.create(Registries.SOUND_EVENT, ONIUtils.MODID);

    public static DeferredHolder<SoundEvent, SoundEvent> COAL_GEN_SOUND = SOUND.register("coal_gen", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ONIUtils.MODID, "coal_gen")));

    public static void register(IEventBus bus) {
        SOUND.register(bus);
    }
}
