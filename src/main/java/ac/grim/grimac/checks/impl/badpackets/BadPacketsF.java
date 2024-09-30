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

@CheckData(name = "BadPacketsF")
public class BadPacketsF extends Check implements PacketCheck {

    public boolean lastSprinting;

    // Support 1.14+ clients starting on either true or false sprinting, we don't know
    public boolean exemptNext = true;

    public BadPacketsF(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.ENTITY_ACTION) {
            return;
        }

        final var packet = lastWrapper(event,
                WrapperPlayClientEntityAction.class,
                () -> new WrapperPlayClientEntityAction(event));

        final var action = packet.getAction();
        if (action != Action.START_SPRINTING && action != Action.STOP_SPRINTING) {
            return;
        }

        boolean state = action == Action.START_SPRINTING;
        if (state == lastSprinting) {
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

        lastSprinting = state;
    }

}
