package ac.grim.grimac.checks.impl.crash;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;

@CheckData(name = "CrashA")
public class CrashA extends Check implements PacketCheck {

    public CrashA(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (player.packetStateData.lastPacketWasTeleport) {
            return;
        }

        if (!WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            return;
        }

        final var packet = lastWrapper(event,
                WrapperPlayClientPlayerFlying.class,
                () -> new WrapperPlayClientPlayerFlying(event));

        if (!packet.hasPositionChanged()) {
            return;
        }

        final var HARD_CODED_BORDER = 2.9999999E7D;
        if (Math.abs(packet.getLocation().getX()) > HARD_CODED_BORDER
                || Math.abs(packet.getLocation().getZ()) > HARD_CODED_BORDER
                || Math.abs(packet.getLocation().getY()) > Integer.MAX_VALUE) {
            flagAndAlert(); // Ban
            player.getSetbackTeleportUtil().executeViolationSetback();
            event.setCancelled(true);
        }
    }

}
