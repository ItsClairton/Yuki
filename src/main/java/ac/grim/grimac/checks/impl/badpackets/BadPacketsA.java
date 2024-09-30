package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.Pair;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;

@CheckData(name = "BadPacketsA")
public class BadPacketsA extends Check implements PacketCheck {

    private int lastSlot = -1;

    public BadPacketsA(final GrimPlayer player) {
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
        if (slot != lastSlot) {
            lastSlot = slot;
            return;
        }

        if (!flagAndAlert(new Pair<>("slot", slot)) || !shouldModifyPackets()) {
            return;
        }

        event.setCancelled(true);
    }

}
