package dev.clairton.yuki.checks.type;

import dev.clairton.yuki.api.AbstractCheck;
import dev.clairton.yuki.utils.anticheat.update.RotationUpdate;

public interface RotationCheck extends AbstractCheck {

    default void process(final RotationUpdate rotationUpdate) {
    }
}
