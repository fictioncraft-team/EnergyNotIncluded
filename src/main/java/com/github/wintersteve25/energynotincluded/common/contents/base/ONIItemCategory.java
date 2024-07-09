package com.github.wintersteve25.energynotincluded.common.contents.base;

import net.minecraft.ChatFormatting;

public enum ONIItemCategory {
    GENERAL("", null),
    GADGETS("gadgets/", ChatFormatting.LIGHT_PURPLE),
    FURNITURE("furniture/", ChatFormatting.YELLOW),
    OXYGEN("oxygen/", ChatFormatting.AQUA),
    POWER("power/", ChatFormatting.RED),
    VENTILATION("ventilation/", ChatFormatting.GREEN);

    private final String pathName;
    private final ChatFormatting color;

    ONIItemCategory(String pathName, ChatFormatting color) {
        this.pathName = pathName;
        this.color = color;
    }

    public String getPathName() {
        return pathName;
    }

    public ChatFormatting getColor() {
        return color;
    }
}
