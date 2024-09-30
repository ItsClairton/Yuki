package ac.grim.grimac.checks.impl.movement;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;

@CheckData(name = "NoSlowB", setback = 5)
public class NoSlowB extends Check implements PacketCheck {

    public NoSlowB(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (player.canFly) {
            return;
        }

        if (!player.isSprinting) {
            return;
        }

        if (!WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            return;
        }

        if (player.food >= 6.0F) {
            return;
        }

        if (!flagAndAlert()) {
            return;
        }

        if (!shouldModifyPackets()) {
            return;
        }

        event.setCancelled(true);
        player.getSetbackTeleportUtil().executeNonSimulatingSetback();
    }

}