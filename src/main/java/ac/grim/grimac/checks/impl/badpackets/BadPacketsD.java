package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.Pair;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;

@CheckData(name = "BadPacketsD")
public class BadPacketsD extends Check implements PacketCheck {

    public BadPacketsD(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (player.packetStateData.lastPacketWasTeleport) {
            return;
        }

        final var packetType = event.getPacketType();
        if (packetType != Client.PLAYER_ROTATION && packetType != Client.PLAYER_POSITION_AND_ROTATION) {
            return;
        }

        final var packet = lastWrapper(event,
                WrapperPlayClientPlayerFlying.class,
                () -> new WrapperPlayClientPlayerFlying(event));

        final var pitch = packet.getLocation().getPitch();
        if (pitch <= 90 && pitch >= -90) {
            return;
        }

        if (!flagAndAlert(new Pair<>("pitch", pitch)) || !shouldModifyPackets()) {
            return;
        }

        if (player.yRot > 90) {
            player.yRot = 90;
        }

        if (player.yRot < -90) {
            player.yRot = -90;
        }

        event.setCancelled(true);
    }

}
