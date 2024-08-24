package dev.clairton.yuki.checks.impl.badpackets;

import dev.clairton.yuki.checks.Check;
import dev.clairton.yuki.checks.CheckData;
import dev.clairton.yuki.checks.type.PacketCheck;
import dev.clairton.yuki.player.GrimPlayer;
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
        if (event.getPacketType() == PacketType.Play.Client.STEER_VEHICLE) {
            final WrapperPlayClientSteerVehicle packet = new WrapperPlayClientSteerVehicle(event);

            if (Math.abs(packet.getForward()) > 0.98f || Math.abs(packet.getSideways()) > 0.98f) {
                if (flagAndAlert("forwards=" + packet.getForward() + ", sideways=" + packet.getSideways()) && shouldModifyPackets()) {
                    event.setCancelled(true);
                    player.onPacketCancel();
                }
            }
        }
    }
}
