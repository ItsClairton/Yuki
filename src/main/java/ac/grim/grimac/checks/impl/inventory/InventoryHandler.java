package ac.grim.grimac.checks.impl.inventory;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientStatus;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenHorseWindow;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenWindow;
import lombok.Getter;

public class InventoryHandler extends Check implements PacketCheck {

    private @Getter int windowId = -1;
    private final boolean legacyClient;

    public InventoryHandler(GrimPlayer player) {
        super(player);

        this.legacyClient = player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_11_1);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.CLIENT_STATUS) {
            final var wrapper = new WrapperPlayClientClientStatus(event);
            if (wrapper.getAction() == WrapperPlayClientClientStatus.Action.OPEN_INVENTORY_ACHIEVEMENT) {
                if (!legacyClient) {
                    return;
                }

                windowId = 0;
            }
        }

        if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW) {
            if (legacyClient) {
                return;
            }

            final var wrapper = new WrapperPlayClientClickWindow(event);

            windowId = wrapper.getWindowId();
            return;
        }

        if (event.getPacketType() == PacketType.Play.Client.CLOSE_WINDOW) {
            windowId = -1;
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.OPEN_WINDOW) {
            final var window = new WrapperPlayServerOpenWindow(event);

            // Thanks mojang, another disgraceful desync
            // when the player clicks on the "X" of the beacon
            // the client does not send a packet informing.
            if(window.getType() == 8 || "minecraft:beacon".equals(window.getLegacyType())) {
                return;
            }

            player.sendTransaction();
            player.latencyUtils.addRealTimeTask(player.lastTransactionSent.get(), () -> windowId = window.getContainerId());
            return;
        }

        if (event.getPacketType() == PacketType.Play.Server.OPEN_HORSE_WINDOW) {
            final var wrapper = new WrapperPlayServerOpenHorseWindow(event);

            player.sendTransaction();
            player.latencyUtils.addRealTimeTask(player.lastTransactionSent.get(), () -> windowId = wrapper.getWindowId());
        }

    }

    public void handleRespawn() {
        windowId = -1;
    }

}
