package com.github.wintersteve25.energynotincluded.common.registries;

import com.github.wintersteve25.energynotincluded.common.contents.modules.items.gadgets.blueprint.ONIBlueprintScreen;
import com.github.wintersteve25.tau.components.base.UIComponent;
import com.github.wintersteve25.tau.theme.MinecraftTheme;
import com.github.wintersteve25.tau.theme.Theme;
import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum ONIScreens implements StringRepresentable {
    BLUEPRINT(new ONIBlueprintScreen(), "blueprint");

    public static final Codec<ONIScreens> CODEC = StringRepresentable.fromEnum(ONIScreens::values);
    public static final Theme GUI_THEME = MinecraftTheme.INSTANCE;

    public final UIComponent ui;
    public final String name;

    ONIScreens(UIComponent ui, String name) {
        this.ui = ui;
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}
