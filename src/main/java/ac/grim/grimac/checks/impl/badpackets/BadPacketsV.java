package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.Pair;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;

@CheckData(name = "BadPacketsV", experimental = true)
public class BadPacketsV extends Check implements PacketCheck {

    public BadPacketsV(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.INTERACT_ENTITY) {
            return;
        }

        if (!player.packetStateData.isSlowedByUsingItem()) {
            return;
        }

        final var packet = lastWrapper(event,
                WrapperPlayClientInteractEntity.class,
                () -> new WrapperPlayClientInteractEntity(event));

        if (packet.getAction() != WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
            return;
        }

        final var item = player.getInventory().getItemInHand(player.packetStateData.eatingHand);

        if(!flagAndAlert(new Pair<>("item-in-use", item.getType().getName().getKey()),
                new Pair<>("target-entity-id", packet.getEntityId()))) {
            return;
        }

        if (!shouldModifyPackets()) {
            return;
        }

        event.setCancelled(true);
    }

}
