package com.github.wintersteve25.energynotincluded.common.contents.modules.blocks.power.coal;

import com.github.wintersteve25.energynotincluded.client.utils.ProgressArrow;
import com.github.wintersteve25.energynotincluded.common.contents.base.blocks.ONIBaseMenu;
import com.github.wintersteve25.tau.components.base.UIComponent;
import com.github.wintersteve25.tau.components.inventory.ItemSlot;
import com.github.wintersteve25.tau.components.inventory.PlayerInventory;
import com.github.wintersteve25.tau.components.layout.Align;
import com.github.wintersteve25.tau.components.layout.Row;
import com.github.wintersteve25.tau.components.layout.Stack;
import com.github.wintersteve25.tau.components.utils.Container;
import com.github.wintersteve25.tau.components.utils.Padding;
import com.github.wintersteve25.tau.layout.Layout;
import com.github.wintersteve25.tau.layout.LayoutSetting;
import com.github.wintersteve25.tau.menu.TauContainerMenu;
import com.github.wintersteve25.tau.menu.handlers.ItemSlotHandler;
import com.github.wintersteve25.tau.theme.Theme;
import com.github.wintersteve25.tau.utils.FlexSizeBehaviour;
import com.github.wintersteve25.tau.utils.Pad;
import com.github.wintersteve25.tau.utils.Variable;
import net.minecraft.world.inventory.DataSlot;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.Optional;

public class CoalGenGui extends ONIBaseMenu {
    
    private Variable<Float> progress;
    
    @Override
    public UIComponent build(Layout layout, Theme theme, TauContainerMenu tauContainerMenu) {
        Optional<CoalGenTE> te = tauContainerMenu.getBlockEntity(CoalGenTE.class);
        if (te.isEmpty()) return new Container.Builder();
        CoalGenTE coalGenTE = te.get();

        progress = new Variable<>(getProgress(coalGenTE));
        return new Container.Builder()
                .withChild(new Stack(
                        FlexSizeBehaviour.MAX,
                        new Align.Builder()
                                .withHorizontal(LayoutSetting.CENTER)
                                .withVertical(LayoutSetting.percentage(0.2f))
                                .build(row(coalGenTE.getItemHandler())),
                        new Align.Builder()
                                .withHorizontal(LayoutSetting.CENTER)
                                .withVertical(LayoutSetting.END)
                                .build(new Padding(new Pad(0, 16, 0, 0), new PlayerInventory()))
                ));
    }

    private UIComponent row(IItemHandler handler) {
        return new Row.Builder()
                .withSizeBehaviour(FlexSizeBehaviour.MIN)
                .withAlignment(LayoutSetting.CENTER)
                .withSpacing(8)
                .build(
                        new ItemSlot(new ItemSlotHandler(handler, 0)),
                        new ProgressArrow(progress)
                );
    }

    @Override
    public void tick(TauContainerMenu menu) {
        Optional<CoalGenTE> be = menu.getBlockEntity(CoalGenTE.class);
        be.ifPresent(te -> {
            progress.setValue(getProgress(te));
        });
    }

    @Override
    public void addDataSlots(TauContainerMenu menu) {
        Optional<CoalGenTE> te = menu.getBlockEntity(CoalGenTE.class);
        if (te.isEmpty()) return;
        CoalGenTE coalGenTE = te.get();
        
        menu.addDataSlot("progress", new DataSlot() {
            @Override
            public int get() {
                return coalGenTE.getProgress();
            }

            @Override
            public void set(int value) {
                coalGenTE.setProgress(value);
            }
        });
        
        menu.addDataSlot("totalProgress", new DataSlot() {
            @Override
            public int get() {
                return coalGenTE.getTotalProgress();
            }

            @Override
            public void set(int value) {
                coalGenTE.setTotalProgress(value);
            }
        });
    }
    
    private float getProgress(CoalGenTE te) {
        if (te.getProgress() == 0) return 0;
        return 1 - (te.getProgress() / (float) te.getTotalProgress());
    }
}
