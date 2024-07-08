package com.github.wintersteve25.energynotincluded.common.utils.helpers;

import net.minecraft.network.chat.Component;

public class LangHelper {
    public static Component guiTitle(String name) {
        return Component.translatable("oniutils.gui.titles." + MiscHelper.langToReg(name));
    }

    public static Component itemTooltip(String name) {
        return Component.translatable("oniutils.tooltips.items." + MiscHelper.langToReg(name));
    }

    public static Component modificationToolTip(String name) {
        return itemTooltip("modification." + name);
    }
}
