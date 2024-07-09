package com.github.wintersteve25.energynotincluded.common.registries;

import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.modules.blocks.power.coal.CoalGenGui;
import com.github.wintersteve25.energynotincluded.common.contents.modules.blocks.power.coal.CoalGenTE;
import com.github.wintersteve25.tau.menu.TauMenuHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ONIMenus {
    private static final DeferredRegister<MenuType<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.MENU, ONIUtils.MODID);
    
    public static final TauMenuHolder COALGEN_UI = new TauMenuHolder(DEFERRED_REGISTER, CoalGenGui::new, CoalGenTE.ID, FeatureFlagSet.of());
    
    public static void register(IEventBus eventBus) {
        DEFERRED_REGISTER.register(eventBus);
    }
    
    public static void registerScreens(RegisterMenuScreensEvent event) {
        COALGEN_UI.registerScreen(event);
    }
}
