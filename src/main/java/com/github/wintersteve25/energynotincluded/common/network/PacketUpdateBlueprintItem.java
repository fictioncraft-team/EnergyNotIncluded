package com.github.wintersteve25.energynotincluded.common.network;

import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.modules.items.gadgets.blueprint.BlueprintItem;
import com.github.wintersteve25.energynotincluded.common.registries.ONIDataComponents;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketUpdateBlueprintItem(ResourceLocation recipe) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PacketUpdateBlueprintItem> TYPE = new CustomPacketPayload.Type<>(new ResourceLocation(ONIUtils.MODID, "sync_bp_item"));
    public static final StreamCodec<ByteBuf, PacketUpdateBlueprintItem> CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            PacketUpdateBlueprintItem::recipe,
            PacketUpdateBlueprintItem::new
    );
    
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    
    public static void handle(PacketUpdateBlueprintItem data, IPayloadContext context) {
        Player player = context.player();
        ItemStack item = player.getItemInHand(player.getUsedItemHand());
        if (!(item.getItem() instanceof BlueprintItem)) return;
        item.set(ONIDataComponents.BLUEPRINT_ITEM_DATA, new BlueprintItem.ItemData(data.recipe));
    }
}
