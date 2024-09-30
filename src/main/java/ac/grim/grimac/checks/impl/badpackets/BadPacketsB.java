package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.Pair;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerVehicle;

@CheckData(name = "BadPacketsB")
public class BadPacketsB extends Check implements PacketCheck {

    public BadPacketsB(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.STEER_VEHICLE) {
            return;
        }

        final var packet = lastWrapper(event,
                WrapperPlayClientSteerVehicle.class,
                () -> new WrapperPlayClientSteerVehicle(event));

        if (Math.abs(packet.getForward()) <= 0.98F && Math.abs(packet.getSideways()) <= 0.98F) {
            return;
        }

        if (!flagAndAlert(new Pair<>("forwards", packet.getForward()), new Pair<>("sideways", packet.getSideways()))) {
            return;
        }

        if (!shouldModifyPackets()) {
            return;
        }

        event.setCancelled(true);
    }

}
