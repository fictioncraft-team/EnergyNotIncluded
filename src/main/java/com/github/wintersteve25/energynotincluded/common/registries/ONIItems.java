package com.github.wintersteve25.energynotincluded.common.registries;

import com.github.wintersteve25.energynotincluded.common.contents.modules.items.gadgets.blueprint.ONIBlueprintItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.base.items.ONIBaseItem;
import com.github.wintersteve25.energynotincluded.common.contents.base.items.ONIBaseItemArmor;
import com.github.wintersteve25.energynotincluded.common.contents.base.items.ONIIItem;
import com.github.wintersteve25.energynotincluded.common.contents.base.builders.ONIItemBuilder;
import com.github.wintersteve25.energynotincluded.common.contents.base.enums.EnumModifications;
import com.github.wintersteve25.energynotincluded.common.contents.modules.items.gadgets.ONIToolItems;
import com.github.wintersteve25.energynotincluded.common.contents.modules.items.modifications.ONIModificationItem;
import com.github.wintersteve25.energynotincluded.common.registration.item.ONIItemDeferredRegister;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ONIItems {

    public static final ONIItemDeferredRegister ITEMS = new ONIItemDeferredRegister(ONIUtils.MODID);

    public static class Gadgets {
        public static final DeferredHolder<Item, ONIBaseItemArmor> GAS_GOGGLES = registerBuilder(ONIToolItems.GAS_GOGGLES);
        public static final DeferredHolder<Item, ONIBaseItem> WIRE_CUTTER = registerBuilder(ONIToolItems.WIRE_CUTTER);
        public static final DeferredHolder<Item, ONIBlueprintItem> BLUEPRINT = registerBuilder(ONIToolItems.BLUEPRINT);

        private static void register() {
        }
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        Gadgets.register();
        registerModifications();
    }

    private static void registerModifications() {
        for (EnumModifications modifications : EnumModifications.values()) {
            for (int i = 1; i < modifications.getTiers()+1; i++) {
                registerModification("modification_" + modifications.getName() + "_tier_" + i, modifications.getColor(), modifications.getBonusEachTier() * i, modifications, modifications.getTooltips());
            }
        }
    }

    private static void registerModification(String regName, ChatFormatting color, int maxBonus, EnumModifications modType, Component... tooltips) {
        ITEMS.register(regName, () -> new ONIModificationItem(ONIUtils.defaultProperties().stacksTo(1).setNoRepair(), color, maxBonus, modType, tooltips));
    }

    private static <T extends Item & ONIIItem> DeferredHolder<Item, T> registerBuilder(ONIItemBuilder<T> builder) {
        return ITEMS.register(builder.getRegName(), () -> builder.build().apply(null), builder.isDoModelGen(), builder.isDoLangGen());
    }
}
