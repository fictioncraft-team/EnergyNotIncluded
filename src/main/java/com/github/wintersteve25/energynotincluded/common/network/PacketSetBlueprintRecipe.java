package com.github.wintersteve25.energynotincluded.common.network;

import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.contents.modules.items.gadgets.blueprint.ONIBlueprintItem;
import com.github.wintersteve25.energynotincluded.common.contents.modules.recipes.blueprints.BlueprintRecipe;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketSetBlueprintRecipe(ResourceLocation recipe) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PacketSetBlueprintRecipe> TYPE = new CustomPacketPayload.Type<>(new ResourceLocation(ONIUtils.MODID, "setBlueprintRecipe"));
    public static final StreamCodec<ByteBuf, PacketSetBlueprintRecipe> CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            PacketSetBlueprintRecipe::recipe,
            PacketSetBlueprintRecipe::new
    );

    public static void handle(PacketSetBlueprintRecipe data, IPayloadContext context) {
        context.enqueueWork(() -> ONIBlueprintItem.setRecipe((ServerPlayer) context.player(), data.recipe));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
