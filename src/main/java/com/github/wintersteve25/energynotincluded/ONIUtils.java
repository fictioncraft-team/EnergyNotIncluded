package com.github.wintersteve25.energynotincluded;

import com.github.wintersteve25.energynotincluded.common.registration.block.ONIBlockDeferredRegister;
import com.github.wintersteve25.energynotincluded.common.registration.block.ONIBlockRegistryData;
import com.github.wintersteve25.energynotincluded.common.registries.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.github.wintersteve25.energynotincluded.common.events.ONIServerEventsHandler;

@Mod(ONIUtils.MODID)
public class ONIUtils {
    public static final String MODID = "energynotincluded";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    private static final DeferredRegister<CreativeModeTab> TAB_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ONIUtils.MODID);
    private static final DeferredHolder<CreativeModeTab, CreativeModeTab> CUSTOM_TAB = TAB_REGISTER.register(MODID, () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + MODID))
            .icon(() -> new ItemStack(ONIBlocks.IGNEOUS_ROCK.blockItem()))
            .displayItems((displayParams, output) -> {
                for (DeferredHolder<Item, ? extends Item> i : ONIItems.ITEMS.getAllItems().keySet()) {
                    output.accept(i.get());
                }

                for (Tuple<ONIBlockDeferredRegister.DeferredBlock<?, ?>, ONIBlockRegistryData> b : ONIBlocks.BLOCKS.getAllBlocks()) {
                    output.accept(b.getA().blockItem().get());
                }
            })
            .build());

    public ONIUtils(final IEventBus eventBus) {
        final IEventBus forgeEventBus = NeoForge.EVENT_BUS;

        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.SERVER, ONIConfig.Server.SERVER_CONFIG);
        ONITags.register();

        ONIBlocks.register(eventBus);
        ONIItems.register(eventBus);
        ONISounds.register(eventBus);
        ONIRecipes.register(eventBus);
        ONIMenus.register(eventBus);
        ONIDataComponents.register(eventBus);
        TAB_REGISTER.register(eventBus);

        eventBus.addListener(ONIMenus::registerScreens);
        eventBus.addListener(ONIServerEventsHandler::commonSetup);
        eventBus.addListener(ONIServerEventsHandler::registerPayloads);
        forgeEventBus.addListener(ONIServerEventsHandler::command);

        ONIUtils.LOGGER.info("ONIUtils Registration Completed");
    }

    public static Item.Properties defaultProperties() {
        return new Item.Properties();
    }
}
