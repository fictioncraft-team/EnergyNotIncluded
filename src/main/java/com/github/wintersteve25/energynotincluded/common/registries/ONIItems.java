package com.github.wintersteve25.energynotincluded.common.registries;

import com.github.wintersteve25.energynotincluded.common.contents.modules.items.gadgets.blueprint.BlueprintItem;
import net.minecraft.world.item.Item;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.base.items.ONIBaseItem;
import com.github.wintersteve25.energynotincluded.common.contents.base.items.ONIIItem;
import com.github.wintersteve25.energynotincluded.common.contents.base.builders.ONIItemBuilder;
import com.github.wintersteve25.energynotincluded.common.contents.modules.items.gadgets.ONIToolItems;
import com.github.wintersteve25.energynotincluded.common.registration.item.ONIItemDeferredRegister;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ONIItems {

    public static final ONIItemDeferredRegister ITEMS = new ONIItemDeferredRegister(ONIUtils.MODID);

    public static class Gadgets {
        public static final DeferredHolder<Item, ONIBaseItem> WIRE_CUTTER = registerBuilder(ONIToolItems.WIRE_CUTTER);
        public static final DeferredHolder<Item, BlueprintItem> BLUEPRINT = registerBuilder(ONIToolItems.BLUEPRINT);

        private static void register() {
        }
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        Gadgets.register();
    }

    private static <T extends Item & ONIIItem> DeferredHolder<Item, T> registerBuilder(ONIItemBuilder<T> builder) {
        return ITEMS.register(builder.getRegName(), () -> builder.build().apply(null), builder.isDoModelGen(), builder.isDoLangGen(), builder.isIncludeInCreativeTab());
    }
}
