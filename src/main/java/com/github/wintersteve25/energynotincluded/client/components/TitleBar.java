package com.github.wintersteve25.energynotincluded.client.components;

import com.github.wintersteve25.energynotincluded.client.utils.ONIUITheme;
import com.github.wintersteve25.tau.build.BuildContext;
import com.github.wintersteve25.tau.components.base.PrimitiveUIComponent;
import com.github.wintersteve25.tau.layout.Axis;
import com.github.wintersteve25.tau.layout.Layout;
import com.github.wintersteve25.tau.theme.Theme;
import com.github.wintersteve25.tau.utils.SimpleVec2i;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class UITitleBar implements PrimitiveUIComponent {
    private final Component text;

    public UITitleBar(Component text) {
        this.text = text;
    }

    public SimpleVec2i build(Layout layout, Theme theme, BuildContext context) {
        SimpleVec2i size = layout.getSize();
        BuildContext innerContext = new BuildContext();

        int width = size.x;
        int height = size.y;
        int x = layout.getPosition(Axis.HORIZONTAL, width);
        int y = layout.getPosition(Axis.VERTICAL, height);
        
        context.renderables().add((graphics, pMouseX, pMouseY, pPartialTicks) -> {
            graphics.blitWithBorder(ONIUITheme.TEXTURE, x, y, 0, 0, width, height, 90, 17, 1, 3, 1, 1);
            graphics.drawString(Minecraft.getInstance().font, text, (int) (x + width * 0.03f), (int) (y + height * 0.3f), theme.getTextColor().getAARRGGBB());
        });

        context.addAll(innerContext);
        return size;
    }
}
