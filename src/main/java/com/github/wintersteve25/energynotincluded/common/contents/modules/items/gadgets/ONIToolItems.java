package com.github.wintersteve25.energynotincluded.common.contents.modules.items.gadgets;

import com.github.wintersteve25.energynotincluded.common.contents.base.ONIItemCategory;
import com.github.wintersteve25.energynotincluded.common.contents.modules.items.gadgets.blueprint.ONIBlueprintItem;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.base.items.ONIBaseItem;
import com.github.wintersteve25.energynotincluded.common.contents.base.builders.ONIItemBuilder;

public class ONIToolItems {
    public static ONIItemBuilder<ONIBaseItem> WIRE_CUTTER = new ONIItemBuilder<>("wire_cutter", (b) -> new ONIBaseItem(ONIUtils.defaultProperties().durability(800)))
            .setCategory(ONIItemCategory.GADGETS)
            .defaultTooltip()
            .takeDurabilityDamage();
    
    public static ONIItemBuilder<ONIBlueprintItem> BLUEPRINT = new ONIItemBuilder<>("blueprint", (b) -> new ONIBlueprintItem())
            .setCategory(ONIItemCategory.GADGETS)
            .defaultTooltip();
}
