package dev.clairton.yuki.checks.impl.scaffolding;

import dev.clairton.yuki.checks.CheckData;
import dev.clairton.yuki.checks.type.BlockPlaceCheck;
import dev.clairton.yuki.player.GrimPlayer;
import dev.clairton.yuki.utils.anticheat.update.BlockPlace;
import dev.clairton.yuki.utils.anticheat.update.RotationUpdate;

@CheckData(name = "DuplicateRotPlace", experimental = true)
public class DuplicateRotPlace extends BlockPlaceCheck {

    public DuplicateRotPlace(GrimPlayer player) {
        super(player);
    }

    private float deltaX, deltaY;

    private double deltaDotsX;
    private boolean rotated = false;

    @Override
    public void process(final RotationUpdate rotationUpdate) {
        deltaX = rotationUpdate.getDeltaXRotABS();
        deltaY = rotationUpdate.getDeltaYRotABS();
        deltaDotsX = rotationUpdate.getProcessor().deltaDotsX;
        rotated = true;
    }

    private float lastPlacedDeltaX;
    private double lastPlacedDeltaDotsX;

    public void onPostFlyingBlockPlace(BlockPlace place) {
        if (rotated) {
            if (deltaX > 2) {
                float xDiff = Math.abs(deltaX - lastPlacedDeltaX);
                double xDiffDots = Math.abs(deltaDotsX - lastPlacedDeltaDotsX);

                if (xDiff < 0.0001) {
                    flagAndAlert("x=" + xDiff + " xdots=" + xDiffDots + " y=" + deltaY);
                } else {
                    reward();
                }
            } else {
                reward();
            }
            this.lastPlacedDeltaX = deltaX;
            this.lastPlacedDeltaDotsX = deltaDotsX;
            rotated = false;
        }
    }


}
