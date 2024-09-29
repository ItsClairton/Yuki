package ac.grim.grimac.events.packets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChangeGameState;

public class PacketChangeGameState extends Check implements PacketCheck {

    public PacketChangeGameState(GrimPlayer playerData) {
        super(playerData);
    }

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.CHANGE_GAME_STATE) {
            return;
        }

        final var packet = new WrapperPlayServerChangeGameState(event);
        if (packet.getReason() != WrapperPlayServerChangeGameState.Reason.CHANGE_GAME_MODE) {
            return;
        }

        player.sendTransaction();
        player.latencyUtils.addRealTimeTask(player.lastTransactionSent.get(), () -> {
            final var mode = (int) packet.getValue();

            player.gamemode = mode < 0 || mode >= GameMode.values().length
                    ? GameMode.SURVIVAL
                    : GameMode.values()[mode];
        });
    }

}
