package com.github.wintersteve25.energynotincluded.common.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.player_data.api.SkillType;
import com.github.wintersteve25.energynotincluded.common.registries.ONICapabilities;
import com.github.wintersteve25.energynotincluded.common.utils.helpers.LangHelper;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class SimpleCommands {
    public static LiteralArgumentBuilder<CommandSourceStack> getGermCommand() {
        return Commands.literal("get").executes((cs) -> {
            var player = cs.getSource().getPlayerOrException();
            player.getCapability(ONICapabilities.GERMS).ifPresent(cap -> {
                cs.getSource().sendSuccess(new TranslatableComponent("oniutils.commands.germs.get.success", Integer.toString(cap.getGermAmount()), LangHelper.germ(cap.getGermType().getName())), true);
            });
            return 1;
        });
    }

    public static LiteralArgumentBuilder<CommandSourceStack> teleportDimensionCommand() {
        return Commands.literal("telDim")
                .then(Commands.argument("dimension", DimensionArgument.dimension())
                        .executes(cs -> {
                            var player = cs.getSource().getPlayerOrException();
                            player.teleportTo(DimensionArgument.getDimension(cs, "dimension"), player.getX(), player.getY(), player.getZ(), 0, 0);
                            return 1;
                        }));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> setSkillLevelCommand() {
        Set<String> skillTypes = Arrays.stream(SkillType.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        return Commands.literal("set")
            .then(
                Commands.argument("skillType", StringArgumentType.string())
                    .suggests((ctx, sb) -> SharedSuggestionProvider.suggest(skillTypes, sb))
                    .then(
                        Commands.argument("level", IntegerArgumentType.integer())
                            .executes(cs -> {
                                var player = cs.getSource().getPlayerOrException();
                                player.getCapability(ONICapabilities.PLAYER).ifPresent(playerData -> {
                                    playerData.setLevel(
                                        SkillType.valueOf(StringArgumentType.getString(cs, "skillType")),
                                        IntegerArgumentType.getInteger(cs, "level")
                                    );
                                });
                                return 1;
                            }))
            );
    }

    public static LiteralArgumentBuilder<CommandSourceStack> getSkillLevelCommand() {
        return Commands.literal("get")
                .executes(cs -> {
                    var player = cs.getSource().getPlayerOrException();
                    player.getCapability(ONICapabilities.PLAYER).ifPresent(playerData -> {
                        cs.getSource().sendSuccess(new TextComponent(playerData.getSkills().toString()), true);
                    });
                    return 1;
                });
    }
}
