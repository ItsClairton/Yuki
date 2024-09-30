package ac.grim.grimac.checks.impl.crash;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUseItem;

@CheckData(name = "CrashG")
public class CrashG extends Check implements PacketCheck {

    private final boolean supportedVersion = player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_19)
            && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_19);

    public CrashG(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (!supportedVersion) {
            return;
        }

        final var packetType = event.getPacketType();
        if (packetType != PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT
                && packetType != PacketType.Play.Client.PLAYER_DIGGING
                && packetType != PacketType.Play.Client.USE_ITEM) {
            return;
        }

        if (packetType == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
            final var packet = lastWrapper(event,
                    WrapperPlayClientPlayerBlockPlacement.class,
                    () -> new WrapperPlayClientPlayerBlockPlacement(event));


            if (packet.getSequence() >= 0) {
                return;
            }
        }

        if (packetType == PacketType.Play.Client.PLAYER_DIGGING) {
            final var packet = lastWrapper(event,
                    WrapperPlayClientPlayerDigging.class,
                    () -> new WrapperPlayClientPlayerDigging(event));

            if (packet.getSequence() >= 0) {
                return;
            }
        }

        if (packetType == PacketType.Play.Client.USE_ITEM) {
            final var packet = lastWrapper(event,
                    WrapperPlayClientUseItem.class,
                    () -> new WrapperPlayClientUseItem(event));

            if (packet.getSequence() >= 0) {
                return;
            }
        }

        flagAndAlert();
        event.setCancelled(true);
    }

}
