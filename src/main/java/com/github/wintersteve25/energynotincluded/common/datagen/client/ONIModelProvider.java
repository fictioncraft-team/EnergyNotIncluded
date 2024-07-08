package com.github.wintersteve25.energynotincluded.common.datagen.client;

import com.github.wintersteve25.energynotincluded.common.registration.block.ONIBlockDeferredRegister;
import net.minecraft.data.PackOutput;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.base.items.ONIIItem;
import com.github.wintersteve25.energynotincluded.common.registration.block.ONIBlockRegistryData;
import com.github.wintersteve25.energynotincluded.common.registration.block.ONIDirectionalBlockRegistryData;
import com.github.wintersteve25.energynotincluded.common.registration.item.ONIItemRegistryData;
import com.github.wintersteve25.energynotincluded.common.registries.ONIBlocks;
import com.github.wintersteve25.energynotincluded.common.registries.ONIItems;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ONIModelProvider extends ItemModelProvider {
    public ONIModelProvider(PackOutput generator, ExistingFileHelper existingFileHelper) {
        super(generator, ONIUtils.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        autoGenModels();
    }

    private ItemModelBuilder builder(ModelFile itemGenerated, String name) {
        return getBuilder(name).parent(itemGenerated).texture("layer0", "item/" + name);
    }

    private ItemModelBuilder builder(String path, String name) {
        return getBuilder(path).parent(getExistingFile(mcLoc("item/generated"))).texture("layer0", "item/" + name);
    }

    private void autoGenModels() {
        for (Tuple<ONIBlockDeferredRegister.DeferredBlock<?, ?>, ONIBlockRegistryData> b : ONIBlocks.BLOCKS.getAllBlocks()) {
            if (b.getB().isDoModelGen()) {
                String name = b.getA().block().getId().getPath();

                if (b.getB() instanceof ONIDirectionalBlockRegistryData directionalData) {
                    withExistingParent(name, directionalData.getModelFile().getLocation());
                } else {
                    withExistingParent(name, modLoc("block/" + name));
                }
            }
        }

        for (DeferredHolder<Item, ? extends Item> i : ONIItems.ITEMS.getAllItems().keySet()) {
            ONIItemRegistryData data = ONIItems.ITEMS.getAllItems().get(i);
            if (data.isDoModelGen()) {
                Item item = i.get();
                String name = i.getId().getPath();

                if (item instanceof ONIIItem) {
                    builder(name, ((ONIIItem) item).getONIItemCategory().getPathName() + name);
                } else {
                    builder(name, name);
                }
            }
        }
    }
}
