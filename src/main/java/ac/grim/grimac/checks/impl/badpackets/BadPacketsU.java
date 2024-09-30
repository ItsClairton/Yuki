package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.Pair;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;

@CheckData(name = "BadPacketsU", experimental = true)
public class BadPacketsU extends Check implements PacketCheck {

    private final int expectedY = player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_8) ? 4095 : 255;
    private final boolean legacy = player.getClientVersion().isOlderThan(ClientVersion.V_1_9);

    public BadPacketsU(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
            return;
        }

        final var packet = lastWrapper(event,
                WrapperPlayClientPlayerBlockPlacement.class,
                () -> new WrapperPlayClientPlayerBlockPlacement(event));

        // Pre 1.9 USE_ITEM
        if (packet.getFace() != BlockFace.OTHER) {
            return;
        }

        final var failedItemCheck = legacy && packet.getItemStack().isPresent() && isEmpty(packet.getItemStack().get());
        final var pos = packet.getBlockPosition();
        final var cursor = packet.getCursorPosition();

        if (!(failedItemCheck || pos.x != -1 || pos.y != expectedY || pos.z != -1
                || cursor.x != 0 || cursor.y != 0 || cursor.z != 0
                || packet.getSequence() != 0)) {
            return;
        }

        if (!flagAndAlert(
                new Pair<>("x", pos.x),
                new Pair<>("y", pos.y),
                new Pair<>("z", pos.z),
                new Pair<>("cursor-x", cursor.x),
                new Pair<>("cursor-y", cursor.y),
                new Pair<>("cursor-z", cursor.z),
                new Pair<>("item", failedItemCheck),
                new Pair<>("sequence", packet.getSequence())) && shouldModifyPackets()) {
            return;
        }

        if (!shouldModifyPackets()) {
            return;
        }

        event.setCancelled(true);
    }

    private boolean isEmpty(ItemStack itemStack) {
        return itemStack.getType() == null || itemStack.getType() == ItemTypes.AIR;
    }

}
