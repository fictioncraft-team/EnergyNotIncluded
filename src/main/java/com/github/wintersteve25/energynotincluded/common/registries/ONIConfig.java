package com.github.wintersteve25.energynotincluded.common.registries;

import com.github.wintersteve25.energynotincluded.common.contents.modules.blocks.power.coal.CoalGenTE;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ONIConfig {

    public static class Server {
        public static final String CAT_MACHINE = "machines";
        public static final String CAT_GAS = "gas";

        public static ModConfigSpec SERVER_CONFIG;
        
        public static final ModConfigSpec.IntValue COALGEN_PROCESS_TIME;

        static {
            ModConfigSpec.Builder SERVERBUILDER = new ModConfigSpec.Builder();

            SERVERBUILDER.comment("Machines Settings").push(CAT_MACHINE);
            COALGEN_PROCESS_TIME = SERVERBUILDER.defineInRange("coalgen_process_time", 100, 0, Integer.MAX_VALUE);
            SERVERBUILDER.pop();

            SERVERBUILDER.comment("Gas Settings").push(CAT_GAS);
            // gas configs
            SERVERBUILDER.pop();

            SERVER_CONFIG = SERVERBUILDER.build();
        }
    }

    public static class Client {
    }
}
