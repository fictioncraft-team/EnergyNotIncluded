package com.github.wintersteve25.energynotincluded.common.datagen.client;

import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.registration.block.ONIBlockDeferredRegister;
import com.github.wintersteve25.energynotincluded.common.registration.block.ONIBlockRegistryData;
import com.github.wintersteve25.energynotincluded.common.registration.item.ONIItemRegistryData;
import com.github.wintersteve25.energynotincluded.common.registries.ONIBlocks;
import com.github.wintersteve25.energynotincluded.common.registries.ONIItems;
import net.minecraft.data.PackOutput;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.commons.lang3.text.WordUtils;

public class ONIEngLangProvider extends LanguageProvider {
    public ONIEngLangProvider(PackOutput gen) {
        super(gen, ONIUtils.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        autoGenLang();

        //item groups
        add("itemGroup." + ONIUtils.MODID, "Energy Not Included");
    }

    private void autoGenLang() {
        for (Tuple<ONIBlockDeferredRegister.DeferredBlock<?, ?>, ONIBlockRegistryData> b : ONIBlocks.BLOCKS.getAllBlocks()) {
            if (b.getB().isDoLangGen()) {
                String name = b.getA().block().getId().getPath();
                add("block." + ONIUtils.MODID + "." + name, WordUtils.capitalizeFully(name.replace("_", " ")));
            }
        }

        for (DeferredHolder<Item, ?> i : ONIItems.ITEMS.getAllItems().keySet()) {
            ONIItemRegistryData data = ONIItems.ITEMS.getAllItems().get(i);

            if (data.isDoLangGen()) {
                String name = i.getId().getPath();
                add("item." + ONIUtils.MODID + "." + name, WordUtils.capitalizeFully(name.replace("_", " ")));
            }
        }
    }
}
