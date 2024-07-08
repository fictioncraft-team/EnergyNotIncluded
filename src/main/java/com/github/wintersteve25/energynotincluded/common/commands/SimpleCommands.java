package com.github.wintersteve25.energynotincluded.common.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;

public class SimpleCommands {
    public static LiteralArgumentBuilder<CommandSourceStack> teleportDimensionCommand() {
        return Commands.literal("telDim")
                .then(Commands.argument("dimension", DimensionArgument.dimension())
                        .executes(cs -> {
                            var player = cs.getSource().getPlayerOrException();
                            player.teleportTo(DimensionArgument.getDimension(cs, "dimension"), player.getX(), player.getY(), player.getZ(), 0, 0);
                            return 1;
                        }));
    }
}
