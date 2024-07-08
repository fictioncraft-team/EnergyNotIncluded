package com.github.wintersteve25.energynotincluded.client.utils;

import com.github.wintersteve25.energynotincluded.common.contents.modules.recipes.CountedIngredient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

public class IngredientTooltip implements ClientTooltipComponent {

    private final CountedIngredient ingredient;
    private final ItemStack[] itemStacks;
    private ItemStack itemStackShown;
    private int shownIndex;
    private float countDown;

    public IngredientTooltip(CountedIngredient ingredient) {
        this.ingredient = ingredient;
        itemStacks = ingredient.ingredient().getItems();
        shownIndex = 0;
        itemStackShown = itemStacks.length == 0 ? ItemStack.EMPTY : itemStacks[0];
    }

    @Override
    public void renderImage(Font pFont, int x, int y, GuiGraphics guiGraphics) {
        guiGraphics.renderItem(itemStackShown, x, y);

        countDown += Minecraft.getInstance().getDeltaFrameTime();
        if (countDown >= 30) {
            countDown = 0;
            shownIndex += 1;
            shownIndex %= itemStacks.length;
            itemStackShown = itemStacks[shownIndex];
        }
    }

    @Override
    public void renderText(Font font, int mouseX, int mouseY, Matrix4f matrix, MultiBufferSource.BufferSource bufferSource) {
        Component itemName = itemStackShown.getHoverName();
        String text = " x" + ingredient.count();
        font.drawInBatch(itemName, (float)mouseX + 18, (float)mouseY + 4f, -1, true, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
        font.drawInBatch(text, (float)mouseX + 18 + font.width(itemName), (float)mouseY + (getHeight() - 9) / 2f, -1, true, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
    }

    @Override
    public int getHeight() {
        return 17;
    }

    @Override
    public int getWidth(Font pFont) {
        return 18 + pFont.width(itemStackShown.getHoverName()) + pFont.width(" x" + ingredient.count());
    }
}
