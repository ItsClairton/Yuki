package ac.grim.grimac.checks.impl.crash;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.Pair;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;

@CheckData(name = "CrashC")
public class CrashC extends Check implements PacketCheck {

    public CrashC(GrimPlayer playerData) {
        super(playerData);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (!WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            return;
        }

        final var packet = lastWrapper(event,
                WrapperPlayClientPlayerFlying.class,
                () -> new WrapperPlayClientPlayerFlying(event));

        if (!packet.hasPositionChanged()) {
            return;
        }

        final var pos = packet.getLocation();
        if (Double.isNaN(pos.getX()) || Double.isNaN(pos.getY()) || Double.isNaN(pos.getZ())
                || Double.isInfinite(pos.getX()) || Double.isInfinite(pos.getY()) || Double.isInfinite(pos.getZ()) ||
                Float.isNaN(pos.getYaw()) || Float.isNaN(pos.getPitch()) ||
                Float.isInfinite(pos.getYaw()) || Float.isInfinite(pos.getPitch())) {
            flagAndAlert(
                    new Pair<>("x", pos.getX()),
                    new Pair<>("y", pos.getY()),
                    new Pair<>("z", pos.getZ()),
                    new Pair<>("yaw", pos.getYaw()),
                    new Pair<>("pitch", pos.getPitch())
            );

            player.getSetbackTeleportUtil().executeViolationSetback();
            event.setCancelled(true);
        }
    }

}
