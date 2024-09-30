package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.Pair;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity.InteractAction;

@CheckData(name = "BadPacketsM", experimental = true)
public class BadPacketsM extends Check implements PacketCheck {

    // 1.7 players do not send INTERACT_AT, so we cannot check them
    private final boolean legacy = player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_7_10);

    private boolean sentInteractAt;

    public BadPacketsM(final GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (legacy) {
            return;
        }

        if (event.getPacketType() != PacketType.Play.Client.INTERACT_ENTITY) {
            return;
        }

        final var packet = lastWrapper(event,
                WrapperPlayClientInteractEntity.class,
                () -> new WrapperPlayClientInteractEntity(event));

        final var action = packet.getAction();
        if (action != InteractAction.INTERACT && action != InteractAction.INTERACT_AT) {
            return;
        }

        final var entity = player.compensatedEntities.entityMap.get(packet.getEntityId());
        if (entity != null && entity.getType() == EntityTypes.ARMOR_STAND) {
            return;
        }

        final var requiredInteractAt = action == InteractAction.INTERACT_AT;
        if (requiredInteractAt != sentInteractAt) {
            this.sentInteractAt = requiredInteractAt;
            return;
        }

        if (!flagAndAlert(
                new Pair<>("sent-interact-at", sentInteractAt),
                new Pair<>("required-interact-at", requiredInteractAt))) {
            return;
        }

        if (!shouldModifyPackets()) {
            return;
        }

        event.setCancelled(true);
    }

}
