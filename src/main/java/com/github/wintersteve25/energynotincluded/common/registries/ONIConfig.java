package com.github.wintersteve25.energynotincluded.common.registries;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ONIConfig {

    public static class Server {
        public static final String CAT_MACHINE = "machines";
        public static final String CAT_GAS = "gas";

        public static ModConfigSpec SERVER_CONFIG;

        static {
            ModConfigSpec.Builder SERVERBUILDER = new ModConfigSpec.Builder();

            SERVERBUILDER.comment("Machines Settings").push(CAT_MACHINE);
            // machine configs
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
