package com.github.wintersteve25.energynotincluded.common.data.capabilities.player_data;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.ONICapabilityProvider;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.player_data.api.IMoraleProvider;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.player_data.api.IPlayerData;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.player_data.api.PlayerData;
import com.github.wintersteve25.energynotincluded.common.events.events.PlayerMovingEvent;
import com.github.wintersteve25.energynotincluded.common.registries.ONICapabilities;

public class PlayerDataEventsHandler {
    public static void entityCapAttachEvent(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (!(entity instanceof Player)) {
            return;
        }
        if (!entity.level.isClientSide()) {
            ONICapabilityProvider<IPlayerData> provider = new ONICapabilityProvider<>(PlayerData::new, ONICapabilities.PLAYER);
            event.addCapability(new ResourceLocation(ONIUtils.MODID, "player_data"), provider);
            event.addListener(provider::invalidate);
        }
    }

    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(ONICapabilities.PLAYER).ifPresent(oldStore -> {
                event.getPlayer().getCapability(ONICapabilities.PLAYER).ifPresent(newStore -> {
                    newStore.setSkills(oldStore.getSkills());
                    newStore.setMorale(oldStore.getMorale());
                    newStore.setBuildMoraleBonus(oldStore.getBuildMoraleBonus());
                    newStore.setTemperature(oldStore.getTemperature());
                });
            });
        }
    }

    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            if (!player.getCommandSenderWorld().isClientSide()) {

            }
        }
    }

    public static void playerTickEvent(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player != null) {
            Level world = player.getCommandSenderWorld();
            if (!world.isClientSide()) {
                player.getCapability(ONICapabilities.PLAYER).ifPresent(p -> {

                });
            }
        }
    }

    public static void playerMove(PlayerMovingEvent event) {
        Player player = event.getPlayer();
        Level world = player.getCommandSenderWorld();
        BlockPos pos = player.blockPosition();
        if (!world.isClientSide()) {
            if (event.getMovement() == PlayerMovingEvent.MovementType.JUMP && event.getMovement() == PlayerMovingEvent.MovementType.SNEAK)
                return;
            player.getCapability(ONICapabilities.PLAYER).ifPresent((cap) -> {
                //Reset and redo calculation when moved
                cap.setBuildMoraleBonus(0);
                for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-9, 0, -9), pos.offset(9, 3, 9))) {
                    Block block = world.getBlockState(blockpos).getBlock();
                    if (block instanceof IMoraleProvider) {
                        IMoraleProvider moraleProvider = (IMoraleProvider) block;
                        cap.setBuildMoraleBonus(cap.getBuildMoraleBonus() + moraleProvider.moraleModifier());
                    }
                }

//                int temperatureCached = cap.getTemperature();
//                cap.setTemperature(0);
//                AtomicInteger blockTemperatures = new AtomicInteger();
//                AtomicInteger amountTE = new AtomicInteger();
//
//                for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-9, 0, -9), pos.add(9, 3, 9))) {
//                    TileEntity te = world.getTileEntity(blockpos);
//                    if (te == null) return;
//                    te.getCapability(ONITEDataCapability.ONI_TE_CAP).ifPresent((teCap) -> {
//                        blockTemperatures.addAndGet(teCap.getTemperature());
//                        amountTE.getAndIncrement();
//                    });
//                }
//
//                blockTemperatures.set(blockTemperatures.get()/amountTE.get());

            });
        }
    }
}
