package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.Pair;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction.Action;

@CheckData(name = "BadPacketsG")
public class BadPacketsG extends Check implements PacketCheck {

    private boolean lastSneaking;
    private boolean exemptNext = true;

    public BadPacketsG(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (player.packetStateData.lastPacketWasTeleport) {
            exemptNext = true;
        }

        if (event.getPacketType() != PacketType.Play.Client.ENTITY_ACTION) {
            return;
        }

        final var packet = lastWrapper(event,
                WrapperPlayClientEntityAction.class,
                () -> new WrapperPlayClientEntityAction(event));

        final var action = packet.getAction();
        if (action != Action.START_SNEAKING && action != Action.STOP_SNEAKING) {
            return;
        }

        boolean state = action == Action.START_SNEAKING;
        if (state == lastSneaking) {
            if (exemptNext) {
                exemptNext = false;
                return;
            }

            if (!flagAndAlert(new Pair<>("state", state)) || !shouldModifyPackets()) {
                return;
            }

            event.setCancelled(true);
            return;
        }

        lastSneaking = state;
    }

}
