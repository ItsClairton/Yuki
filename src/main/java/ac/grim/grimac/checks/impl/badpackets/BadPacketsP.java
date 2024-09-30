package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.Pair;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenWindow;

@CheckData(name = "BadPacketsP", experimental = true)
public class BadPacketsP extends Check implements PacketCheck {

    private int containerType = -1;

    public BadPacketsP(GrimPlayer playerData) {
        super(playerData);
    }

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.OPEN_WINDOW) {
            return;
        }

        final var packet = lastWrapper(event,
                WrapperPlayServerOpenWindow.class,
                () -> new WrapperPlayServerOpenWindow(event));

        this.containerType = packet.getType();
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.CLICK_WINDOW) {
            return;
        }

        final var packet = lastWrapper(event,
                WrapperPlayClientClickWindow.class,
                () -> new WrapperPlayClientClickWindow(event));

        int clickType = packet.getWindowClickType().ordinal();
        int button = packet.getButton();

        boolean flag = false;

        // TODO: Adjust for containers
        switch (clickType) {
            case 0:
            case 1:
            case 4:
                if (button != 0 && button != 1) flag = true;
                break;
            case 2:
                if ((button > 8 || button < 0) && button != 40) flag = true;
                break;
            case 3:
                if (button != 2) flag = true;
                break;
            case 5:
                if (button == 3 || button == 7 || button > 10 || button < 0) flag = true;
                break;
            case 6:
                if (button != 0) flag = true;
                break;
        }

        if (!flag) {
            return;
        }

        if (!flagAndAlert(
                new Pair<>("click-type", clickType),
                new Pair<>("button", button),
                new Pair<>("container-type", containerType))) {
            return;
        }

        if (!shouldModifyPackets()) {
            return;
        }

        event.setCancelled(true);
    }

}
