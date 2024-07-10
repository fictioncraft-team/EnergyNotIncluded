package com.github.wintersteve25.energynotincluded.common.registries;

import com.github.wintersteve25.energynotincluded.common.contents.modules.items.gadgets.blueprint.BlueprintScreen;
import com.github.wintersteve25.tau.components.base.UIComponent;
import com.github.wintersteve25.tau.theme.MinecraftTheme;
import com.github.wintersteve25.tau.theme.Theme;
import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public enum ONIScreen implements StringRepresentable {
    BLUEPRINT(() -> BlueprintScreen::new, "blueprint");

    public static final Codec<ONIScreen> CODEC = StringRepresentable.fromEnum(ONIScreen::values);
    public static final Theme GUI_THEME = MinecraftTheme.INSTANCE;

    public final Supplier<Supplier<UIComponent>> ui;
    public final String name;

    ONIScreen(Supplier<Supplier<UIComponent>> ui, String name) {
        this.ui = ui;
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}
