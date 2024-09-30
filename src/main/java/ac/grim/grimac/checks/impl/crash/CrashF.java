package ac.grim.grimac.checks.impl.crash;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.Pair;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;

@CheckData(name = "CrashF")
public class CrashF extends Check implements PacketCheck {

    public CrashF(GrimPlayer playerData) {
        super(playerData);
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.CLICK_WINDOW) {
            return;
        }

        final var packet = lastWrapper(event,
                WrapperPlayClientClickWindow.class,
                () -> new WrapperPlayClientClickWindow(event));

        int clickType = packet.getWindowClickType().ordinal();
        int button = packet.getButton();
        int windowId = packet.getWindowId();
        int slot = packet.getSlot();

        if ((clickType == 1 || clickType == 2) && windowId >= 0 && button < 0) {
            if (flagAndAlert(new Pair<>("click-type", clickType), new Pair<>("button", button))) {
                event.setCancelled(true);
            }
        } else if (windowId >= 0 && clickType == 2 && slot < 0) {
            if (flagAndAlert(new Pair<>("click-type", clickType), new Pair<>("button", button), new Pair<>("slot", slot))) {
                event.setCancelled(true);
            }
        }
    }

}
