package com.github.wintersteve25.energynotincluded.common.compat.jei;

import com.github.wintersteve25.tau.menu.TauContainerScreen;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rect2i;

import java.util.List;

public class ONITabJEIHandler implements IGuiContainerHandler<TauContainerScreen> {
    @Override
    public List<Rect2i> getGuiExtraAreas(TauContainerScreen containerScreen) {
        return List.of();
    }
}
