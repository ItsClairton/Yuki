package dev.clairton.yuki.checks.type;

import ac.grim.grimac.api.AbstractCheck;
import dev.clairton.yuki.utils.anticheat.update.RotationUpdate;

public interface RotationCheck extends AbstractCheck {

    default void process(final RotationUpdate rotationUpdate) {
    }
}
