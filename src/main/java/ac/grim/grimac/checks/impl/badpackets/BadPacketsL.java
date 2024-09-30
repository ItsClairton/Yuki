package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.Pair;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;

// checks for impossible dig packets
@CheckData(name = "BadPacketsL")
public class BadPacketsL extends Check implements PacketCheck {

    private final boolean legacy = player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_7_10);

    public BadPacketsL(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.PLAYER_DIGGING) {
            return;
        }

        final var packet = lastWrapper(event,
                WrapperPlayClientPlayerDigging.class,
                () -> new WrapperPlayClientPlayerDigging(event));

        final var action = packet.getAction();
        if (action == DiggingAction.START_DIGGING
                || action == DiggingAction.FINISHED_DIGGING
                || action == DiggingAction.CANCELLED_DIGGING) {
            return;
        }

        final var expectedFace = legacy && action == DiggingAction.RELEASE_USE_ITEM
                ? BlockFace.SOUTH
                : BlockFace.DOWN;

        final var blockPosition = packet.getBlockPosition();
        if (packet.getBlockFace() == expectedFace
                && blockPosition.getX() == 0
                && blockPosition.getY() == 0
                && blockPosition.getZ() == 0
                && packet.getSequence() == 0) {
            return;
        }

        if (!flagAndAlert(
                new Pair<>("blockPos", blockPosition),
                new Pair<>("face", packet.getBlockFace()),
                new Pair<>("sequence", packet.getSequence()),
                new Pair<>("action", action)
        )) {
            return;
        }

        if (!shouldModifyPackets() || action == DiggingAction.RELEASE_USE_ITEM) {
            return;
        }

        event.setCancelled(true);
    }

}
