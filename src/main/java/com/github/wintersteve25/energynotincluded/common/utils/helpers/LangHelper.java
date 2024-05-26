package com.github.wintersteve25.energynotincluded.common.utils.helpers;

import net.minecraft.network.chat.TranslatableComponent;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.player_data.api.SkillType;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.player_data.api.TraitType;

public class LangHelper {
    public static TranslatableComponent guiTitle(String name) {
        return new TranslatableComponent("oniutils.gui.titles." + MiscHelper.langToReg(name));
    }

    public static TranslatableComponent itemTooltip(String name) {
        return new TranslatableComponent("oniutils.tooltips.items." + MiscHelper.langToReg(name));
    }

    public static TranslatableComponent modificationToolTip(String name) {
        return itemTooltip("modification." + name);
    }

    public static TranslatableComponent germ(String name) {
        return new TranslatableComponent("germ.oniutils." + name);
    }

    public static TranslatableComponent skill(SkillType skill) {
        return new TranslatableComponent("skill.oniutils." + skill.name().toLowerCase());
    }

    public static TranslatableComponent trait(TraitType trait) {
        return new TranslatableComponent("trait.oniutils." + trait.name().toLowerCase());
    }

    public static TranslatableComponent curiosSlot(String name) {
        return new TranslatableComponent("curios.identifier." + name);
    }
}
