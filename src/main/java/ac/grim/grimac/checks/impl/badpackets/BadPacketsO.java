package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.Pair;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientKeepAlive;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerKeepAlive;
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;

@CheckData(name = "BadPacketsO")
public class BadPacketsO extends Check implements PacketCheck {

    private final LongArrayFIFOQueue queue = new LongArrayFIFOQueue();

    public BadPacketsO(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.KEEP_ALIVE) {
            return;
        }

        final var packet = lastWrapper(event,
                WrapperPlayServerKeepAlive.class,
                () -> new WrapperPlayServerKeepAlive(event));

        queue.enqueue(packet.getId());
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.KEEP_ALIVE) {
            return;
        }

        final var packet = lastWrapper(event,
                WrapperPlayClientKeepAlive.class,
                () -> new WrapperPlayClientKeepAlive(event));

        final var expectedId = queue.isEmpty()
                ? -1
                : queue.dequeueLong();

        if (packet.getId() == expectedId) {
            return;
        }

        if (expectedId != -1) {
            queue.enqueueFirst(expectedId);
        }

        if (shouldModifyPackets()) {
            event.setCancelled(true);
        }

        if (System.currentTimeMillis() - player.joinTime > 5000) {
            return;
        }

        flagAndAlert(new Pair<>("id", packet.getId()), new Pair<>("expected-id", expectedId));
    }

}
