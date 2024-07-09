package com.github.wintersteve25.energynotincluded.common.data.saved_data.circuits;

import com.github.wintersteve25.energynotincluded.common.utils.SerializableMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import com.github.wintersteve25.energynotincluded.ONIUtils;

import java.util.ArrayList;
import java.util.List;

public class WorldCircuits extends SavedData {

    private static final String circuitsSerializationKey = "circuits";
    private static final String idSerializationKey = "circuitsID";

    private SerializableMap<Integer, Circuit> circuits;
    private int ID = 0;

    public WorldCircuits() {
        circuits = new SerializableMap<>(
                IntTag::valueOf,
                Circuit::write,
                t -> {
                    if (t instanceof IntTag intTag) {
                        return intTag.getAsInt();
                    }

                    ONIUtils.LOGGER.error("Failed to read circuit ID data from NBT");
                    return 0;
                },
                t -> Circuit.readFromNBT((CompoundTag) t),
                CompoundTag.TAG_INT,
                CompoundTag.TAG_COMPOUND
        );
    }

    public WorldCircuits(CompoundTag tag, HolderLookup.Provider pRegistries) {
        this();
        circuits.deserializeNBT(pRegistries, (CompoundTag) tag.get(circuitsSerializationKey));
        ID = tag.getInt(idSerializationKey);
    }

    public void addCircuits(Circuit... circuits) {
        for (Circuit circuit : circuits) {
            this.circuits.putIfAbsent(circuit.getId(), circuit);
        }
        setDirty();
    }

    public void replaceAndAddCircuits(Circuit... circuits) {
        for (Circuit circuit : circuits) {
            this.circuits.remove(circuit.getId());
            this.circuits.put(circuit.getId(), circuit);
        }
        setDirty();
    }

    public List<Circuit> getCircuits() {
        return new ArrayList<>(circuits.values());
    }

    public Circuit getCircuitByID(int ID) {
        return circuits.get(ID);
    }

    public void removeCircuitByID(int ID) {
        circuits.remove(ID);
        setDirty();
    }

    public Circuit getCircuitWithCableAtPos(BlockPos pos) {
        return circuits.values().stream()
                .filter((circuit) -> circuit.getCables().contains(pos))
                .findFirst()
                .get();
    }

    public int getNextID() {
        setDirty();
        return ID++;
    }

    @Override
    public CompoundTag save(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put(circuitsSerializationKey, circuits.serializeNBT(pRegistries));
        pTag.putInt(idSerializationKey, ID);
        return pTag;
    }

    public static WorldCircuits get(ServerLevel level) {
        DimensionDataStorage storage = level.getDataStorage();
        return storage.computeIfAbsent(new Factory<>(WorldCircuits::new, WorldCircuits::new), "oni_world_circuits");
    }
}
