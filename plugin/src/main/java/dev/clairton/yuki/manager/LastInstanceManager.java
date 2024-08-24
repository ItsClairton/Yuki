package dev.clairton.yuki.manager;

import dev.clairton.yuki.checks.Check;
import dev.clairton.yuki.checks.type.PostPredictionCheck;
import dev.clairton.yuki.player.GrimPlayer;
import dev.clairton.yuki.utils.anticheat.update.PredictionComplete;
import dev.clairton.yuki.utils.data.LastInstance;

import java.util.ArrayList;
import java.util.List;

public class LastInstanceManager extends Check implements PostPredictionCheck {
    private final List<LastInstance> instances = new ArrayList<>();

    public LastInstanceManager(GrimPlayer player) {
        super(player);
    }

    public void addInstance(LastInstance instance) {
        instances.add(instance);
    }

    @Override
    public void onPredictionComplete(final PredictionComplete predictionComplete) {
        for (LastInstance instance : instances) {
            instance.tick();
        }
    }
}
