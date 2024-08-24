package dev.clairton.yuki.predictionengine.movementtick;

import dev.clairton.yuki.player.GrimPlayer;
import dev.clairton.yuki.predictionengine.predictions.rideable.PredictionEngineRideableLava;
import dev.clairton.yuki.predictionengine.predictions.rideable.PredictionEngineRideableNormal;
import dev.clairton.yuki.predictionengine.predictions.rideable.PredictionEngineRideableWater;
import dev.clairton.yuki.predictionengine.predictions.rideable.PredictionEngineRideableWaterLegacy;
import dev.clairton.yuki.utils.nmsutil.BlockProperties;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import org.bukkit.util.Vector;

public class MovementTickerLivingVehicle extends MovementTicker {
    Vector movementInput = new Vector();

    public MovementTickerLivingVehicle(GrimPlayer player) {
        super(player);
    }

    @Override
    public void doWaterMove(float swimSpeed, boolean isFalling, float swimFriction) {
        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13)) {
            new PredictionEngineRideableWater(movementInput).guessBestMovement(swimSpeed, player, isFalling, player.gravity, swimFriction, player.lastY);
        } else {
            new PredictionEngineRideableWaterLegacy(movementInput).guessBestMovement(swimSpeed, player, player.gravity, swimFriction, player.lastY);
        }
    }

    @Override
    public void doLavaMove() {
        new PredictionEngineRideableLava(movementInput).guessBestMovement(0.02F, player);
    }

    @Override
    public void doNormalMove(float blockFriction) {
        new PredictionEngineRideableNormal(movementInput).guessBestMovement(BlockProperties.getFrictionInfluencedSpeed(blockFriction, player), player);
    }
}
