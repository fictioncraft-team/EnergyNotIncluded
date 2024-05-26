package com.github.wintersteve25.energynotincluded.common.contents.base.items;

import com.github.wintersteve25.energynotincluded.common.contents.base.ONIItemCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import com.github.wintersteve25.energynotincluded.common.contents.base.interfaces.functional.IToolTipCondition;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class ONIBaseItem extends Item implements ONIIItem {

    // item builder stuff
    private Supplier<ChatFormatting> colorName;
    private Supplier<List<Component>> tooltips;
    private Supplier<IToolTipCondition> tooltipCondition = IToolTipCondition.DEFAULT;
    private ONIItemCategory itemCategory = ONIItemCategory.GENERAL;
    private boolean takeDurabilityDamage;

    public ONIBaseItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        if (getColorName() != null && getColorName().get() != null) {
            return super.getName(stack).plainCopy().withStyle(getColorName().get());
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public Supplier<ChatFormatting> getColorName() {
        if (colorName != null) {
            return colorName;
        }

        return ()->itemCategory.getColor();
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide && pState.getDestroySpeed(pLevel, pPos) != 0.0F && takeDurabilityDamage) {
            pStack.hurtAndBreak(1, pEntityLiving, (p_40992_) -> {
                p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }

        return true;
    }

    @Override
    public void setColorName(Supplier<ChatFormatting> colorName) {
        this.colorName = colorName;
    }

    @Override
    public Supplier<List<Component>> getTooltips() {
        return tooltips;
    }

    @Override
    public void setTooltips(Supplier<List<Component>> tooltips) {
        this.tooltips = tooltips;
    }

    @Override
    public Supplier<IToolTipCondition> getTooltipCondition() {
        return tooltipCondition;
    }

    @Override
    public void setTooltipCondition(Supplier<IToolTipCondition> condition) {
        this.tooltipCondition = condition;
    }

    @Override
    public ONIItemCategory getONIItemCategory() {
        return itemCategory;
    }

    @Override
    public void setONIItemCategory(ONIItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }

    public boolean isTakeDurabilityDamage() {
        return takeDurabilityDamage;
    }

    public void setTakeDurabilityDamage(boolean takeDurabilityDamage) {
        this.takeDurabilityDamage = takeDurabilityDamage;
    }
}
