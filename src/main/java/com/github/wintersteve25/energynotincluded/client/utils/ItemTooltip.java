package com.github.wintersteve25.energynotincluded.client.utils;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

public class ItemTooltip implements ClientTooltipComponent {

    private final ItemStack itemStack;

    public ItemTooltip(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        guiGraphics.renderItem(itemStack, x, y);
    }

    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public int getWidth(Font pFont) {
        return 16;
    }
}
