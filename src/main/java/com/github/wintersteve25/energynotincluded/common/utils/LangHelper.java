package com.github.wintersteve25.energynotincluded.common.utils;

import com.github.wintersteve25.energynotincluded.ONIUtils;
import net.minecraft.network.chat.Component;

public class LangHelper {
    public static Component guiTitle(String name) {
        return Component.translatable(ONIUtils.MODID + ".gui.titles." + MiscHelper.langToReg(name));
    }

    public static Component itemTooltip(String name) {
        return Component.translatable(ONIUtils.MODID + ".tooltips.items." + MiscHelper.langToReg(name));
    }
}
