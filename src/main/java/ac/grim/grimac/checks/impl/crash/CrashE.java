package ac.grim.grimac.checks.impl.crash;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.Pair;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSettings;

@CheckData(name = "CrashE")
public class CrashE extends Check implements PacketCheck {

    public CrashE(GrimPlayer playerData) {
        super(playerData);
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.CLIENT_SETTINGS) {
            return;
        }

        final var packet = lastWrapper(event,
                WrapperPlayClientSettings.class,
                () -> new WrapperPlayClientSettings(event));

        final var viewDistance = packet.getViewDistance();
        if (viewDistance >= 2) {
            return;
        }

        flagAndAlert(new Pair<>("view-distance", viewDistance));
        packet.setViewDistance(2);
        event.markForReEncode(true);
    }

}
