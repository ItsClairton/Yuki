package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;

@CheckData(name = "BadPacketsH")
public class BadPacketsH extends Check implements PacketCheck {

    // 1.9 packet order: INTERACT -> ANIMATION
    // 1.8 packet order: ANIMATION -> INTERACT
    // I personally think 1.8 made much more sense. You swing and THEN you hit!
    private boolean sentAnimation = player.getClientVersion().isNewerThan(ClientVersion.V_1_8);

    private final boolean exempt = player.getClientVersion().isOlderThan(ClientVersion.V_1_9)
            && PacketEvents.getAPI().getServerManager().getVersion().isNewerThan(ServerVersion.V_1_8_8);;

    public BadPacketsH(final GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (exempt) {
            return;
        }

        final var packetType = event.getPacketType();
        if (packetType == PacketType.Play.Client.ANIMATION) {
            sentAnimation = true;
            return;
        }

        if (packetType == PacketType.Play.Client.INTERACT_ENTITY) {
            final var packet = lastWrapper(event,
                    WrapperPlayClientInteractEntity.class,
                    () -> new WrapperPlayClientInteractEntity(event));

            if (packet.getAction() != WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                return;
            }

            if (!sentAnimation && flagAndAlert() && shouldModifyPackets()) {
                event.setCancelled(true);
                return;
            }

            sentAnimation = false;
        }
    }

}
