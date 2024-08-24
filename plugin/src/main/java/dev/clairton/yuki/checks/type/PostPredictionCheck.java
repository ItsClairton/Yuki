package dev.clairton.yuki.checks.type;

import dev.clairton.yuki.utils.anticheat.update.PredictionComplete;

public interface PostPredictionCheck extends PacketCheck {

    default void onPredictionComplete(final PredictionComplete predictionComplete) {
    }
}
