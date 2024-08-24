package dev.clairton.yuki.predictionengine.predictions.rideable;

import dev.clairton.yuki.player.GrimPlayer;
import dev.clairton.yuki.predictionengine.predictions.PredictionEngineNormal;
import dev.clairton.yuki.utils.data.VectorData;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;

public class PredictionEngineRideableNormal extends PredictionEngineNormal {

    Vector movementVector;

    public PredictionEngineRideableNormal(Vector movementVector) {
        this.movementVector = movementVector;
    }

    @Override
    public void addJumpsToPossibilities(GrimPlayer player, Set<VectorData> existingVelocities) {
        PredictionEngineRideableUtils.handleJumps(player, existingVelocities);
    }

    @Override
    public List<VectorData> applyInputsToVelocityPossibilities(GrimPlayer player, Set<VectorData> possibleVectors, float speed) {
        return PredictionEngineRideableUtils.applyInputsToVelocityPossibilities(movementVector, player, possibleVectors, speed);
    }
}
