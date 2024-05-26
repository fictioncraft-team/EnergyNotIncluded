package com.github.wintersteve25.energynotincluded.common.contents.base.items;

import com.github.wintersteve25.energynotincluded.common.contents.base.ONIItemCategory;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import com.github.wintersteve25.energynotincluded.common.contents.base.interfaces.functional.IPlacementCondition;
import com.github.wintersteve25.energynotincluded.common.contents.base.interfaces.functional.IToolTipCondition;
import com.github.wintersteve25.energynotincluded.common.utils.ONIConstants;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public interface ONIIItem {
    default Supplier<ChatFormatting> getColorName() {
        return null;
    }

    default void setColorName(Supplier<ChatFormatting> colorName) {

    }

    default Supplier<List<Component>> getTooltips() {
        return null;
    }

    default void setTooltips(Supplier<List<Component>> tooltips) {

    }

    default Supplier<IToolTipCondition> getTooltipCondition() {
        return IToolTipCondition.DEFAULT;
    }

    default void setTooltipCondition(Supplier<IToolTipCondition> condition) {

    }

    default IPlacementCondition getPlacementCondition() {
        return null;
    }

    default void setPlacementCondition(IPlacementCondition placementCondition) {
    }

    default ONIItemCategory getONIItemCategory() {
        return ONIItemCategory.GENERAL;
    }

    default void setONIItemCategory(ONIItemCategory itemCategory) {
    }

    default void tooltip(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (getTooltips() != null && getTooltips().get() != null && !getTooltips().get().isEmpty()) {
            IToolTipCondition condition = getTooltipCondition().get();
            if (condition == null) {
                tooltip.addAll(getTooltips().get());
            } else {
                if (condition == IToolTipCondition.DEFAULT.get()) {
                    if (condition.canShow(stack, worldIn, tooltip, flagIn)) {
                        tooltip.addAll(getTooltips().get());
                    } else {
                        tooltip.add(ONIConstants.LangKeys.HOLD_SHIFT);
                    }
                } else {
                    if (condition.canShow(stack, worldIn, tooltip, flagIn)) {
                        tooltip.addAll(getTooltips().get());
                    } else {
                        tooltip.add(condition.textWhenNotShown());
                    }
                }
            }
        }
    }
}
