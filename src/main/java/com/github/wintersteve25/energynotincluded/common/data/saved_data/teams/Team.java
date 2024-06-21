package com.github.wintersteve25.energynotincluded.common.data.saved_data.teams;

import com.github.wintersteve25.energynotincluded.common.data.saved_data.teams.research.Research;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Team implements INBTSerializable<CompoundTag> {

    private UUID uuid;
    private List<Research> researchesUnlocked;

    public Team() {
        uuid = UUID.randomUUID();
        researchesUnlocked = new ArrayList<>();
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        var tag = new CompoundTag();
        tag.putUUID("uuid", uuid);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        uuid = nbt.getUUID("uuid");
    }
}
