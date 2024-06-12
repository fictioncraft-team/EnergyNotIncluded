package com.github.wintersteve25.energynotincluded.common.registries;

import com.github.wintersteve25.energynotincluded.common.contents.modules.items.gadgets.blueprint.ONIBlueprintGui;
import com.github.wintersteve25.tau.components.base.UIComponent;
import com.github.wintersteve25.tau.theme.MinecraftTheme;
import com.github.wintersteve25.tau.theme.Theme;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum ONIGui implements StringRepresentable {
    BLUEPRINT(new ONIBlueprintGui(), "blueprint");

    public static final Codec<ONIGui> CODEC = StringRepresentable.fromEnum(ONIGui::values);
    public static final Theme GUI_THEME = MinecraftTheme.INSTANCE;

    public final UIComponent ui;
    public final String name;

    ONIGui(UIComponent ui, String name) {
        this.ui = ui;
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}
