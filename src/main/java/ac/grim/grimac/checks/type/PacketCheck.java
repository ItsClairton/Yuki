package ac.grim.grimac.checks.type;

import ac.grim.grimac.api.AbstractCheck;
import ac.grim.grimac.utils.anticheat.update.PositionUpdate;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.function.Supplier;

public interface PacketCheck extends AbstractCheck {

    default void onPacketReceive(final PacketReceiveEvent event) {
    }

    default void onPacketSend(final PacketSendEvent event) {
    }

    default void onPositionUpdate(final PositionUpdate positionUpdate) {
    }

    @SuppressWarnings("unchecked")
    default <T extends PacketWrapper<?>> T lastWrapper(ProtocolPacketEvent event, Class<T> wrapper, Supplier<T> supplier) {
        final var lastWrapper = event.getLastUsedWrapper();
        if (lastWrapper != null) {
            return (T) lastWrapper;
        }

        return supplier.get();
    }

}
