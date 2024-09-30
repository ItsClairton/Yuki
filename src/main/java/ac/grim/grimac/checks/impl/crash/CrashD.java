package ac.grim.grimac.checks.impl.crash;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.Pair;
import ac.grim.grimac.utils.inventory.inventory.MenuType;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenWindow;

@CheckData(name = "CrashD")
public class CrashD extends Check implements PacketCheck {

    private final boolean supportedVersion = PacketEvents.getAPI()
            .getServerManager()
            .getVersion()
            .isNewerThanOrEquals(ServerVersion.V_1_14);

    private MenuType type = MenuType.UNKNOWN;
    private int lecternId = -1;

    public CrashD(GrimPlayer playerData) {
        super(playerData);
    }

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        if (!supportedVersion) {
            return;
        }

        if (event.getPacketType() != PacketType.Play.Server.OPEN_WINDOW) {
            return;
        }

        final var packet = lastWrapper(event,
                WrapperPlayServerOpenWindow.class,
                () -> new WrapperPlayServerOpenWindow(event));

        this.type = MenuType.getMenuType(packet.getType());
        if (type != MenuType.LECTERN) {
            return;
        }

        lecternId = packet.getContainerId();
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (!supportedVersion) {
            return;
        }

        if (event.getPacketType() != PacketType.Play.Client.CLICK_WINDOW) {
            return;
        }

        final var packet = lastWrapper(event,
                WrapperPlayClientClickWindow.class,
                () -> new WrapperPlayClientClickWindow(event));

        final var clickType = packet.getWindowClickType().ordinal();
        final var button = packet.getButton();
        final var windowId = packet.getWindowId();

        if (type == MenuType.LECTERN && windowId > 0 && windowId == lecternId) {
            if (flagAndAlert(new Pair<>("clickType", clickType), new Pair<>("button", button))) {
                event.setCancelled(true);
            }
        }
    }

}
