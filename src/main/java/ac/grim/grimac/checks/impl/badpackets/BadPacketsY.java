package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.Pair;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;

/**
 * Checks for out of bounds slot changes
 */
@CheckData(name = "BadPacketsY")
public class BadPacketsY extends Check implements PacketCheck {

    public BadPacketsY(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.HELD_ITEM_CHANGE) {
            return;
        }

        final var packet = lastWrapper(event,
                WrapperPlayClientHeldItemChange.class,
                () -> new WrapperPlayClientHeldItemChange(event));

        final var slot = packet.getSlot();
        if (slot >= 0 && slot <= 8) {
            return;
        }

        if (!flagAndAlert(new Pair<>("slot", slot))) {
            return;
        }

        if (!shouldModifyPackets()) {
            return;
        }

        event.setCancelled(true);
    }

}
