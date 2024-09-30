package ac.grim.grimac.checks.impl.scaffolding;

import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
import ac.grim.grimac.utils.nmsutil.Materials;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;

@CheckData(name = "FabricatedPlace")
public class FabricatedPlace extends BlockPlaceCheck {
    public FabricatedPlace(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onBlockPlace(final BlockPlace place) {
        final var cursor = place.getCursor();
        if (cursor == null) {
            return;
        }

        final var allowed = Materials.isShapeExceedsCube(place.getPlacedAgainstMaterial()) || place.getPlacedAgainstMaterial() == StateTypes.LECTERN ? 1.5 : 1;
        final var minAllowed = 1 - allowed;

        if (cursor.getX() < minAllowed
                || cursor.getY() < minAllowed
                || cursor.getZ() < minAllowed
                || cursor.getX() > allowed
                || cursor.getY() > allowed
                || cursor.getZ() > allowed) {
            if (flagAndAlert() && shouldModifyPackets() && shouldCancel()) {
                place.resync();
            }
        }
    }
}
