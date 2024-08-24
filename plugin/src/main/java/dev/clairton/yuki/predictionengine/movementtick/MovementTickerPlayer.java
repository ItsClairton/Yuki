package dev.clairton.yuki.predictionengine.movementtick;

import dev.clairton.yuki.player.GrimPlayer;
import dev.clairton.yuki.predictionengine.predictions.PredictionEngineLava;
import dev.clairton.yuki.predictionengine.predictions.PredictionEngineNormal;
import dev.clairton.yuki.predictionengine.predictions.PredictionEngineWater;
import dev.clairton.yuki.predictionengine.predictions.PredictionEngineWaterLegacy;
import dev.clairton.yuki.utils.nmsutil.BlockProperties;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;

public class MovementTickerPlayer extends MovementTicker {
    public MovementTickerPlayer(GrimPlayer player) {
        super(player);
    }

    @Override
    public void doWaterMove(float swimSpeed, boolean isFalling, float swimFriction) {
        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13)) {
            new PredictionEngineWater().guessBestMovement(swimSpeed, player, isFalling, player.gravity, swimFriction, player.lastY);
        } else {
            new PredictionEngineWaterLegacy().guessBestMovement(swimSpeed, player, player.gravity, swimFriction, player.lastY);
        }
    }

    @Override
    public void doLavaMove() {
        new PredictionEngineLava().guessBestMovement(0.02F, player);
    }

    @Override
    public void doNormalMove(float blockFriction) {
        new PredictionEngineNormal().guessBestMovement(BlockProperties.getFrictionInfluencedSpeed(blockFriction, player), player);
    }
}
