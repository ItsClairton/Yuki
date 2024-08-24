package dev.clairton.yuki.checks.impl.scaffolding;

import dev.clairton.yuki.checks.CheckData;
import dev.clairton.yuki.checks.type.BlockPlaceCheck;
import dev.clairton.yuki.player.GrimPlayer;
import dev.clairton.yuki.utils.anticheat.update.BlockPlace;
import dev.clairton.yuki.utils.nmsutil.Materials;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.util.Vector3i;

@CheckData(name = "AirLiquidPlace")
public class AirLiquidPlace extends BlockPlaceCheck {
    public AirLiquidPlace(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onBlockPlace(final BlockPlace place) {
        if (player.gamemode == GameMode.CREATIVE) return;
        Vector3i blockPos = place.getPlacedAgainstBlockLocation();
        StateType placeAgainst = player.compensatedWorld.getStateTypeAt(blockPos.getX(), blockPos.getY(), blockPos.getZ());

        if (placeAgainst.isAir() || Materials.isNoPlaceLiquid(placeAgainst)) { // fail
            if (flagAndAlert() && shouldModifyPackets() && shouldCancel()) {
                place.resync();
            }
        }
    }

    @Override
    public void reload() {
        super.reload();
        this.cancelVL = getConfig().getIntElse(getConfigName() + ".cancelVL", 0);
    }
}
