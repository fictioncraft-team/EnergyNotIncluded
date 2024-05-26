package com.github.wintersteve25.energynotincluded.common.contents.base.interfaces;

import com.github.wintersteve25.energynotincluded.common.contents.modules.items.modifications.ModificationHandler;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.player_data.api.SkillType;

import java.util.HashMap;

/**
 * Should be implemented on a {@link net.minecraft.world.level.block.entity.BlockEntity}
 */
public interface ONIIRequireSkillToInteract extends ONIIModifiable {
    HashMap<SkillType, Integer> requiredSkill();

    ModificationHandler modHandler();

    default int getRequiredLevel(SkillType skill) {
        int levelReq = requiredSkill().get(skill);
        if (modHandler() == null) return levelReq;
        return modHandler().getRequiredSkillLevel(levelReq);
    }
}
