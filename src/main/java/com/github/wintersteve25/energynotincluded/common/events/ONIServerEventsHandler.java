package com.github.wintersteve25.energynotincluded.common.events;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.commands.SetGermAmountCommands;
import com.github.wintersteve25.energynotincluded.common.commands.SimpleCommands;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.germ.GermEventsHandler;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.player_data.PlayerDataEventsHandler;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.world_gas.WorldGasEventsHandler;
import com.github.wintersteve25.energynotincluded.common.network.ONINetworking;
import com.github.wintersteve25.energynotincluded.common.registries.ONIConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class ONIServerEventsHandler {

    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        ONIUtils.LOGGER.info("Registering ONIUtils Networkings");
        ONINetworking.registerMessages(event);
    }

    public static void commonSetup(final FMLCommonSetupEvent evt) {
        ONIUtils.LOGGER.info("Registering ONIUtils Capabilities");

        //germ events
        if (ONIConfig.Server.ENABLE_GERMS.get()) {
            ONIUtils.LOGGER.info("Registering Germs Capability");
            NeoForge.EVENT_BUS.addGenericListener(Entity.class, GermEventsHandler::entityCapAttachEvent);
            NeoForge.EVENT_BUS.addGenericListener(BlockEntity.class, GermEventsHandler::teCapAttachEvent);
            NeoForge.EVENT_BUS.addGenericListener(ItemStack.class, GermEventsHandler::itemCapAttachEvent);
            NeoForge.EVENT_BUS.addListener(GermEventsHandler::infectOnInteractEntitySpecific);
            NeoForge.EVENT_BUS.addListener(GermEventsHandler::infectOnPickItem);
            NeoForge.EVENT_BUS.addListener(GermEventsHandler::infectOnTossItem);
            NeoForge.EVENT_BUS.addListener(GermEventsHandler::infectOnTileInteract);
            NeoForge.EVENT_BUS.addListener(GermEventsHandler::playerTickEvent);
            NeoForge.EVENT_BUS.addListener(GermEventsHandler::keepGermWhilePlaced);
        }

        //player data
        if (ONIConfig.Server.ENABLE_TRAITS.get()) {
            ONIUtils.LOGGER.info("Registering Player Data");
            NeoForge.EVENT_BUS.addGenericListener(Entity.class, PlayerDataEventsHandler::entityCapAttachEvent);
            NeoForge.EVENT_BUS.addListener(PlayerDataEventsHandler::onPlayerCloned);
            NeoForge.EVENT_BUS.addListener(PlayerDataEventsHandler::onPlayerLoggedIn);
            NeoForge.EVENT_BUS.addListener(PlayerDataEventsHandler::playerTickEvent);
            NeoForge.EVENT_BUS.addListener(PlayerDataEventsHandler::playerMove);
        }

        // World Gas
        ONIUtils.LOGGER.info("Registering World Gas");
        NeoForge.EVENT_BUS.addGenericListener(LevelChunk.class, WorldGasEventsHandler::chunkCapAttachEvent);
        NeoForge.EVENT_BUS.addListener(WorldGasEventsHandler::worldTick);

        //Misc Event Listeners
//        if (ModList.get().isLoaded("curios")) {
//            FMLJavaModLoadingContext.get().getModEventBus().addListener(CuriosCompat::register);
//        }
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
