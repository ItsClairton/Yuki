package dev.clairton.yuki.checks.impl.scaffolding;

import dev.clairton.yuki.checks.CheckData;
import dev.clairton.yuki.checks.type.BlockPlaceCheck;
import dev.clairton.yuki.player.GrimPlayer;
import dev.clairton.yuki.utils.anticheat.update.BlockPlace;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;

@CheckData(name = "InvalidPlaceB")
public class InvalidPlaceB extends BlockPlaceCheck {
    public InvalidPlaceB(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onBlockPlace(final BlockPlace place) {
        if (place.getFaceId() == 255 && PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_8)) {
            return;
        }

        if (place.getFaceId() < 0 || place.getFaceId() > 5) {
            // ban
            if (flagAndAlert("direction=" + place.getFaceId()) && shouldModifyPackets() && shouldCancel()) {
                place.resync();
            }
        }
    }
}
