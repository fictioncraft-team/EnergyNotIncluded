package com.github.wintersteve25.energynotincluded.common.events;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.commands.SetGermAmountCommands;
import com.github.wintersteve25.energynotincluded.common.commands.SimpleCommands;
import com.github.wintersteve25.energynotincluded.common.compat.curios.CuriosCompat;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.germ.GermEventsHandler;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.player_data.PlayerDataEventsHandler;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.world_gas.WorldGasEventsHandler;
import com.github.wintersteve25.energynotincluded.common.network.ONINetworking;
import com.github.wintersteve25.energynotincluded.common.registries.ONIConfig;
import com.github.wintersteve25.energynotincluded.common.registries.worldgen.ONIDimensions;

public class ONIServerEventsHandler {

    public static void commonSetup(final FMLCommonSetupEvent evt) {

        evt.enqueueWork(() -> {
            ONIUtils.LOGGER.info("Registering ONIUtils Dimensions");
            ONIDimensions.register();
        });

        ONIUtils.LOGGER.info("Registering ONIUtils Networkings");
        ONINetworking.registerMessages();

        ONIUtils.LOGGER.info("Registering ONIUtils Capabilities");

        //germ events
        if (ONIConfig.Server.ENABLE_GERMS.get()) {
            ONIUtils.LOGGER.info("Registering Germs Capability");
            MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, GermEventsHandler::entityCapAttachEvent);
            MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, GermEventsHandler::teCapAttachEvent);
            MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, GermEventsHandler::itemCapAttachEvent);
            MinecraftForge.EVENT_BUS.addListener(GermEventsHandler::infectOnInteractEntitySpecific);
            MinecraftForge.EVENT_BUS.addListener(GermEventsHandler::infectOnPickItem);
            MinecraftForge.EVENT_BUS.addListener(GermEventsHandler::infectOnTossItem);
            MinecraftForge.EVENT_BUS.addListener(GermEventsHandler::infectOnTileInteract);
            MinecraftForge.EVENT_BUS.addListener(GermEventsHandler::playerTickEvent);
            MinecraftForge.EVENT_BUS.addListener(GermEventsHandler::keepGermWhilePlaced);
        }

        //player data
        if (ONIConfig.Server.ENABLE_TRAITS.get()) {
            ONIUtils.LOGGER.info("Registering Player Data");
            MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, PlayerDataEventsHandler::entityCapAttachEvent);
            MinecraftForge.EVENT_BUS.addListener(PlayerDataEventsHandler::onPlayerCloned);
            MinecraftForge.EVENT_BUS.addListener(PlayerDataEventsHandler::onPlayerLoggedIn);
            MinecraftForge.EVENT_BUS.addListener(PlayerDataEventsHandler::playerTickEvent);
            MinecraftForge.EVENT_BUS.addListener(PlayerDataEventsHandler::playerMove);
        }

        // World Gas
        ONIUtils.LOGGER.info("Registering World Gas");
        MinecraftForge.EVENT_BUS.addGenericListener(LevelChunk.class, WorldGasEventsHandler::chunkCapAttachEvent);
        MinecraftForge.EVENT_BUS.addListener(WorldGasEventsHandler::worldTick);

        //Misc Event Listeners
        if (ModList.get().isLoaded("curios")) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(CuriosCompat::register);
        }
    }

    public static void command(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        ONIUtils.LOGGER.info("Registering ONIUtils Commands");

        dispatcher.register(Commands.literal("oniutils")
                .requires((commandSource) -> commandSource.hasPermission(1))
                .then(Commands.literal("germs")
                        .then(SetGermAmountCommands.register(dispatcher))
                        .then(SimpleCommands.getGermCommand()))
                .then(Commands.literal("skills")
                        .then(SimpleCommands.setSkillLevelCommand())
                        .then(SimpleCommands.getSkillLevelCommand()))
                .then(Commands.literal("debug")
                        .then(SimpleCommands.teleportDimensionCommand())));
    }
}
