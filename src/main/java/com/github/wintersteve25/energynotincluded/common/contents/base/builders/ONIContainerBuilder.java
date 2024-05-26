package com.github.wintersteve25.energynotincluded.common.contents.base.builders;

import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.util.Tuple;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import com.github.wintersteve25.energynotincluded.common.registries.ONIBlocks;
import com.github.wintersteve25.energynotincluded.common.utils.SlotArrangement;
import com.github.wintersteve25.energynotincluded.common.utils.helpers.MiscHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ONIContainerBuilder {

    private boolean shouldAddPlayerSlots = true;
    private boolean shouldTrackPower = false;
    private boolean shouldTrackPowerCapacity = false;
    private boolean shouldTrackWorking = false;
    private boolean shouldTrackProgress = false;
    private boolean shouldTrackTotalProgress = false;
    private boolean shouldAddInternalInventory = false;
    private List<SlotArrangement> internalSlotArrangement = new ArrayList<>();
    private Tuple<Integer, Integer> playerSlotStart = new Tuple<>(8, 88);

    private final String regName;
    private final ContainerTypeRegistryObject<ONIAbstractContainer> registryObject;

    public ONIContainerBuilder(String regName) {
        this.regName = regName;
        registryObject = buildContainerType();
    }

    public ONIContainerBuilder disablePlayerSlots() {
        this.shouldAddPlayerSlots = false;
        return this;
    }

    public ONIContainerBuilder trackPower() {
        this.shouldTrackPower = true;
        return this;
    }

    public ONIContainerBuilder trackPowerCapacity() {
        this.shouldTrackPowerCapacity = true;
        return this;
    }

    public ONIContainerBuilder trackWorking() {
        this.shouldTrackWorking = true;
        return this;
    }

    public ONIContainerBuilder trackProgress() {
        this.shouldTrackProgress = true;
        return this;
    }

    public ONIContainerBuilder trackTotalProgress() {
        this.shouldTrackTotalProgress = true;
        return this;
    }

    public ONIContainerBuilder addInternalInventory() {
        this.shouldAddInternalInventory = true;
        return this;
    }

    public ONIContainerBuilder setInternalSlotArrangement(SlotArrangement... internalSlotArrangement) {
        this.internalSlotArrangement = Arrays.asList(internalSlotArrangement);
        return this;
    }

    public ONIContainerBuilder setPlayerSlotStart(Tuple<Integer, Integer> playerSlotStart) {
        this.playerSlotStart = playerSlotStart;
        return this;
    }

    private ContainerTypeRegistryObject<ONIAbstractContainer> buildContainerType() {
        return ONIBlocks.MENUS.register(MiscHelper.langToReg(regName), () -> IForgeMenuType.create(buildFactory()));
    }

    public IContainerFactory<ONIAbstractContainer> buildFactory() {
        return (windowId, inv, data) -> buildNewInstance(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }

    public ONIAbstractContainer buildNewInstance(int windowID, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        return new ONIAbstractContainer(
                windowID,
                world,
                pos,
                playerInventory,
                player,
                registryObject.get(),
                shouldAddPlayerSlots,
                shouldTrackPower,
                shouldTrackPowerCapacity,
                shouldTrackWorking,
                shouldTrackProgress,
                shouldTrackTotalProgress,
                shouldAddInternalInventory,
                internalSlotArrangement,
                playerSlotStart
        );
    }

    public ContainerTypeRegistryObject<ONIAbstractContainer> getContainerTypeRegistryObject() {
        return registryObject;
    }
}
