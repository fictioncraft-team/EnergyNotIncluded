package com.github.wintersteve25.energynotincluded.common.registries;

import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.germ.api.IGerms;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.plasma.api.IPlasma;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.player_data.api.IPlayerData;
import com.github.wintersteve25.energynotincluded.common.data.capabilities.world_gas.api.IWorldGas;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class ONICapabilities {
    public static Capability<IGerms> GERMS;
    public static Capability<IPlayerData> PLAYER;
    public static Capability<IPlasma> PLASMA;
    public static Capability<IWorldGas> GAS;

    public static void register(IEventBus bus) {
        bus.addListener(ONICapabilities::registerCapabilities);
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        ONIUtils.LOGGER.info("Registering capabilities");
        event.register(IGerms.class);
        event.register(IPlayerData.class);
        event.register(IPlasma.class);
        event.register(IWorldGas.class);

        GERMS = CapabilityManager.get(new CapabilityToken<>(){});
        PLAYER = CapabilityManager.get(new CapabilityToken<>(){});
        PLASMA = CapabilityManager.get(new CapabilityToken<>(){});
        GAS = CapabilityManager.get(new CapabilityToken<>(){});
    }
}
