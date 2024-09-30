package ac.grim.grimac.checks.impl.scaffolding;

import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.math.VectorUtils;
import com.github.retrooper.packetevents.protocol.attribute.Attributes;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import org.bukkit.util.Vector;

@CheckData(name = "FarPlace")
public class FarPlace extends BlockPlaceCheck {

    public FarPlace(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onBlockPlace(final BlockPlace place) {
        if (place.getMaterial() == StateTypes.SCAFFOLDING) {
            return;
        }

        final var blockPos = place.getPlacedAgainstBlockLocation();

        var min = Double.MAX_VALUE;
        for (final var d : player.getPossibleEyeHeights()) {
            final var box = new SimpleCollisionBox(blockPos);
            final var eyes = new Vector(player.x, player.y + d, player.z);
            final var best = VectorUtils.cutBoxToVector(eyes, box);
            min = Math.min(min, eyes.distanceSquared(best));
        }

        // getPickRange() determines this?
        // With 1.20.5+ the new attribute determines creative mode reach using a modifier
        var maxReach = player.compensatedEntities.getSelf().getAttributeValue(Attributes.PLAYER_BLOCK_INTERACTION_RANGE);
        final var threshold = player.getMovementThreshold();
        maxReach += Math.hypot(threshold, threshold);

        if (min > maxReach * maxReach) { // fail
            if (flagAndAlert() && shouldModifyPackets() && shouldCancel()) {
                place.resync();
            }
        }
    }

}
