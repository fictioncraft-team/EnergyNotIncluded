package com.github.wintersteve25.energynotincluded.common.utils.helpers;

import com.github.wintersteve25.energynotincluded.common.data.capabilities.player_data.api.SkillType;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.player_data.api.TraitType;
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

    public static Component germ(String name) {
        return Component.translatable("germ.oniutils." + name);
    }

    public static Component skill(SkillType skill) {
        return Component.translatable("skill.oniutils." + skill.name().toLowerCase());
    }

    public static Component trait(TraitType trait) {
        return Component.translatable("trait.oniutils." + trait.name().toLowerCase());
    }

    public static Component curiosSlot(String name) {
        return Component.translatable("curios.identifier." + name);
    }
}
