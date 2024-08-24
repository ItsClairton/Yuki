package dev.clairton.yuki.checks.impl.badpackets;

import dev.clairton.yuki.checks.Check;
import dev.clairton.yuki.checks.CheckData;
import dev.clairton.yuki.checks.type.PacketCheck;
import dev.clairton.yuki.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;

@CheckData(name = "BadPacketsE")
public class BadPacketsE extends Check implements PacketCheck {
    private int noReminderTicks;

    public BadPacketsE(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION ||
                event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION) {
            noReminderTicks = 0;
        } else if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            noReminderTicks++;
        } else if (event.getPacketType() == PacketType.Play.Client.STEER_VEHICLE) {
            noReminderTicks = 0; // Exempt vehicles
        }

        if (noReminderTicks > 20) {
            flagAndAlert("ticks=" + noReminderTicks); // ban?  I don't know how this would false
        }
    }

    public void handleRespawn() {
        noReminderTicks = 0;
    }
}
