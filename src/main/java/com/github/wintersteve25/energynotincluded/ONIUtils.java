package com.github.wintersteve25.energynotincluded;

import com.github.wintersteve25.energynotincluded.common.registration.block.ONIBlockDeferredRegister;
import com.github.wintersteve25.energynotincluded.common.registries.ONIBlocks;
import com.github.wintersteve25.energynotincluded.common.registries.ONIItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.github.wintersteve25.energynotincluded.common.registries.ONIConfig;
import com.github.wintersteve25.energynotincluded.common.events.ONIServerEventsHandler;
import com.github.wintersteve25.energynotincluded.common.registration.Registration;

@Mod(ONIUtils.MODID)
public class ONIUtils {
    public static final String MODID = "energynotincluded";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    private static final DeferredRegister<CreativeModeTab> TAB_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ONIUtils.MODID);
    private static final DeferredHolder<CreativeModeTab, CreativeModeTab> CUSTOM_TAB = TAB_REGISTER.register(MODID, () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + MODID))
            .icon(() -> new ItemStack(ONIBlocks.NonFunctionals.IGNEOUS_ROCK.blockItem()))
            .displayItems((displayParams, output) -> {
                for (DeferredHolder<Item, ? extends Item> i : ONIItems.ITEMS.getAllItems().keySet()) {
                    output.accept(i.get());
                }

                for (ONIBlockDeferredRegister.DeferredBlock<?, ?> b : ONIBlocks.BLOCKS.getAllBlocks().keySet()) {
                    output.accept(b.blockItem().get());
                }
            })
            .build());

    public ONIUtils(final IEventBus modEventBus) {
        final IEventBus forgeEventBus = NeoForge.EVENT_BUS;

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ONIConfig.Server.SERVER_CONFIG);
        Registration.init(modEventBus);
        TAB_REGISTER.register(modEventBus);

        modEventBus.addListener(ONIServerEventsHandler::commonSetup);
        forgeEventBus.addListener(ONIServerEventsHandler::command);
    }

    public static Item.Properties defaultProperties() {
        return new Item.Properties();
    }
}
