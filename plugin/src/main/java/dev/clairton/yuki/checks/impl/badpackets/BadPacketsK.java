package dev.clairton.yuki.checks.impl.badpackets;

import dev.clairton.yuki.checks.Check;
import dev.clairton.yuki.checks.CheckData;
import dev.clairton.yuki.checks.type.PacketCheck;
import dev.clairton.yuki.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameMode;

@CheckData(name = "BadPacketsK")
public class BadPacketsK extends Check implements PacketCheck {
    public BadPacketsK(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.SPECTATE) {
            if (player.gamemode != GameMode.SPECTATOR) {
                if (flagAndAlert() && shouldModifyPackets()) {
                    event.setCancelled(true);
                    player.onPacketCancel();
                }
            }
        }
    }
}
