package com.github.wintersteve25.energynotincluded.common.events;

import net.minecraft.client.player.Input;
import com.github.wintersteve25.energynotincluded.ONIUtils;
import com.github.wintersteve25.energynotincluded.common.network.ONINetworking;
import com.github.wintersteve25.energynotincluded.common.events.events.PlayerMovingEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;

@EventBusSubscriber(modid = ONIUtils.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class ForgeClientEventsHandler {
    @SubscribeEvent
    public static void userInput(MovementInputUpdateEvent event) {
        PlayerMovingEvent.MovementType type = null;

        Input input = event.getInput();

        if (input.down) {
            type = PlayerMovingEvent.MovementType.S;
        } else if (input.up) {
            type = PlayerMovingEvent.MovementType.W;
        } else if (input.jumping) {
            type = PlayerMovingEvent.MovementType.JUMP;
        } else if (input.left) {
            type = PlayerMovingEvent.MovementType.A;
        } else if (input.right) {
            type = PlayerMovingEvent.MovementType.D;
        } else if (input.shiftKeyDown) {
            type = PlayerMovingEvent.MovementType.SNEAK;
        }

        if (type == null) return;

        ONINetworking.sendToServer(new PacketTriggerPlayerMove(event.getEntity().getUUID(), type));
    }
}
