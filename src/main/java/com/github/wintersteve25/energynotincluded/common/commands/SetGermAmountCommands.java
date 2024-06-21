package com.github.wintersteve25.energynotincluded.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.TranslatableComponent;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.germ.api.EnumGermType;
import com.github.wintersteve25.energynotincluded.common.registries.ONICapabilities;
import com.github.wintersteve25.energynotincluded.common.utils.helpers.LangHelper;
import com.github.wintersteve25.energynotincluded.common.utils.helpers.MiscHelper;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class SetGermAmountCommands implements Command<CommandSourceStack> {

    private static final SetGermAmountCommands INSTANCE = new SetGermAmountCommands();

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        Set<String> germTypes = Arrays.stream(EnumGermType.values())
                .map((var) -> MiscHelper.langToReg(var.getName()))
                .collect(Collectors.toSet());

        return Commands.literal("set")
                .requires(cs -> cs.hasPermission(1))
                .then(Commands.argument("target", EntityArgument.entities())
                        .then(Commands.argument("germType", StringArgumentType.string()).suggests((ctx, sb) -> SharedSuggestionProvider.suggest(germTypes, sb))
                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes(INSTANCE))));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        int amount = IntegerArgumentType.getInteger(context, "amount");
        Entity target = EntityArgument.getEntity(context, "target");
        String name = StringArgumentType.getString(context, "germType");

        if (name.isEmpty()) {
            context.getSource().sendSuccess(() -> Component.translatable("oniutils.commands.germs.set.failed.typeIsNull"), true);
        }

        target.getCapability(ONICapabilities.GERMS).ifPresent(t -> {
            t.setGerm(EnumGermType.getGermFromName(name), amount);
        });

        context.getSource().sendSuccess(() -> Component.translatable("oniutils.commands.germs.set.success", amount, LangHelper.germ(name)), true);

        return 1;
    }
}
