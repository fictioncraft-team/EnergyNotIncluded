package com.github.wintersteve25.energynotincluded.common.events;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.commands.SetGermAmountCommands;
import com.github.wintersteve25.energynotincluded.common.commands.SimpleCommands;
import com.github.wintersteve25.energynotincluded.common.network.ONINetworking;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class ONIServerEventsHandler {

    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        ONIUtils.LOGGER.info("Registering ONIUtils Networkings");
        ONINetworking.registerMessages(event);
    }

    public static void commonSetup(final FMLCommonSetupEvent evt) {
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
