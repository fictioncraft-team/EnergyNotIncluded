package com.github.wintersteve25.energynotincluded.common.registries;

import mekanism.api.chemical.gas.Gas;
import mekanism.common.registration.impl.GasDeferredRegister;
import mekanism.common.registration.impl.GasRegistryObject;
import net.minecraftforge.eventbus.api.IEventBus;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.world_gas.api.chemistry.Element;

import java.util.ArrayList;
import java.util.List;

public class ONIGases {
    public static final GasDeferredRegister GASES = new GasDeferredRegister(ONIUtils.MODID);
    public static final List<GasRegistryObject<Gas>> ELEMENT_GASES = new ArrayList<>();

    public static void register(IEventBus bus) {
        GASES.register(bus);
        registerChemistryElements();
    }

    private static void registerChemistryElements() {
        for (Element element : Element.values()) {
            ELEMENT_GASES.add(GASES.register(element));
        }
    }
}
