package com.github.wintersteve25.energynotincluded;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.github.wintersteve25.energynotincluded.common.registries.ONIBlocks;
import com.github.wintersteve25.energynotincluded.common.registries.ONIConfig;
import com.github.wintersteve25.energynotincluded.common.events.ONIServerEventsHandler;
import com.github.wintersteve25.energynotincluded.common.registration.Registration;
import org.jetbrains.annotations.NotNull;

@Mod(ONIUtils.MODID)
public class ONIUtils {
    public static final String MODID = "oniutils";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public ONIUtils(final IEventBus modEventBus) {
        final IEventBus forgeEventBus = NeoForge.EVENT_BUS;

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ONIConfig.Server.SERVER_CONFIG);
        Registration.init();

        modEventBus.addListener(ONIServerEventsHandler::commonSetup);
        forgeEventBus.addListener(ONIServerEventsHandler::command);
    }

    //item group
    public static final CreativeModeTab creativeTab = new CreativeModeTab("oniutils") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ONIBlocks.NonFunctionals.IGNEOUS_ROCK.asItem());
        }
    };

    public static Item.Properties defaultProperties() {
        return new Item.Properties();
    }

    //damage source
    public static final DamageSource oxygenDamage = new DamageSource("oniutils.oxygen").bypassArmor();
    public static final DamageSource gas = new DamageSource("oniutils.gas").bypassArmor();
    public static final DamageSource germDamage = new DamageSource("oniutils.germ").bypassArmor();
}
