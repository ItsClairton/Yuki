package dev.clairton.yuki.checks.impl.badpackets;

import dev.clairton.yuki.checks.Check;
import dev.clairton.yuki.checks.CheckData;
import dev.clairton.yuki.checks.type.PacketCheck;
import dev.clairton.yuki.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerAbilities;

@CheckData(name = "BadPacketsI")
public class BadPacketsI extends Check implements PacketCheck {
    public BadPacketsI(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.PLAYER_ABILITIES) {
            if (new WrapperPlayClientPlayerAbilities(event).isFlying() && !player.canFly) {
                if (flagAndAlert() && shouldModifyPackets()) {
                    event.setCancelled(true);
                    player.onPacketCancel();
                }
            }
        }
    }
}
