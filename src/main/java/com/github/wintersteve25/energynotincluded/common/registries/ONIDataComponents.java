package com.github.wintersteve25.energynotincluded.common.registries;

import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.modules.items.gadgets.blueprint.BlueprintItem;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ONIDataComponents {
    private static final DeferredRegister.DataComponents DEFERRED_REGISTER = DeferredRegister.createDataComponents(ONIUtils.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlueprintItem.ItemData>> BLUEPRINT_ITEM_DATA =
            DEFERRED_REGISTER.registerComponentType("blueprint_item", builder -> builder
                    .networkSynchronized(BlueprintItem.STREAM_CODEC)
                    .persistent(BlueprintItem.CODEC));

    public static void register(IEventBus bus) {
        DEFERRED_REGISTER.register(bus);
    }
}
