package dev.clairton.yuki.checks.type;

import dev.clairton.yuki.api.AbstractCheck;
import dev.clairton.yuki.utils.anticheat.update.PositionUpdate;

public interface PositionCheck extends AbstractCheck {

    default void onPositionUpdate(final PositionUpdate positionUpdate) {
    }
}
