package dev.clairton.yuki.checks.impl.baritone;

import dev.clairton.yuki.checks.Check;
import dev.clairton.yuki.checks.CheckData;
import dev.clairton.yuki.checks.impl.aim.processor.AimProcessor;
import dev.clairton.yuki.checks.type.RotationCheck;
import dev.clairton.yuki.player.GrimPlayer;
import dev.clairton.yuki.utils.anticheat.update.RotationUpdate;
import dev.clairton.yuki.utils.data.HeadRotation;
import dev.clairton.yuki.utils.math.GrimMath;

// This check has been patched by Baritone for a long time and it also seems to false with cinematic camera now, so it is disabled.
@CheckData(name = "Baritone")
public class Baritone extends Check implements RotationCheck {
    public Baritone(GrimPlayer playerData) {
        super(playerData);
    }

    private int verbose;

    @Override
    public void process(final RotationUpdate rotationUpdate) {
        final HeadRotation from = rotationUpdate.getFrom();
        final HeadRotation to = rotationUpdate.getTo();

        final float deltaPitch = Math.abs(to.getPitch() - from.getPitch());

        // Baritone works with small degrees, limit to 1 degrees to pick up on baritone slightly moving aim to bypass anticheats
        if (rotationUpdate.getDeltaXRot() == 0 && deltaPitch > 0 && deltaPitch < 1 && Math.abs(to.getPitch()) != 90.0f) {
            if (rotationUpdate.getProcessor().divisorY < GrimMath.MINIMUM_DIVISOR) {
                verbose++;
                if (verbose > 8) {
                    flagAndAlert("Divisor " + AimProcessor.convertToSensitivity(rotationUpdate.getProcessor().divisorX));
                }
            } else {
                verbose = 0;
            }
        }
    }
}
