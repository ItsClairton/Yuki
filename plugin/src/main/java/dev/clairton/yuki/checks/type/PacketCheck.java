package dev.clairton.yuki.checks.type;


import ac.grim.grimac.api.AbstractCheck;
import dev.clairton.yuki.utils.anticheat.update.PositionUpdate;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;

public interface PacketCheck extends AbstractCheck {

    default void onPacketReceive(final PacketReceiveEvent event) {
    }

    default void onPacketSend(final PacketSendEvent event) {
    }

    default void onPositionUpdate(final PositionUpdate positionUpdate) {
    }
}
