package dev.clairton.yuki.checks.impl.movement;

import dev.clairton.yuki.checks.CheckData;
import dev.clairton.yuki.checks.type.PostPredictionCheck;
import dev.clairton.yuki.player.GrimPlayer;
import dev.clairton.yuki.utils.anticheat.update.PredictionComplete;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;

@CheckData(name = "NegativeTimer", configName = "NegativeTimer", setback = 10, experimental = true)
public class NegativeTimerCheck extends TimerCheck implements PostPredictionCheck {

    public NegativeTimerCheck(GrimPlayer player) {
        super(player);
        timerBalanceRealTime = System.nanoTime() + clockDrift;
    }

    @Override
    public void onPredictionComplete(final PredictionComplete predictionComplete) {
        // We can't negative timer check a 1.9+ player who is standing still.
        if (!player.canThePlayerBeCloseToZeroMovement(2) || !predictionComplete.isChecked()) {
            timerBalanceRealTime = System.nanoTime() + clockDrift;
        }

        if (timerBalanceRealTime < lastMovementPlayerClock - clockDrift) {
            int lostMS = (int) ((System.nanoTime() - timerBalanceRealTime) / 1e6);
            flagAndAlert("-" + lostMS);
            timerBalanceRealTime += 50e6;
        }
    }

    @Override
    public void doCheck(final PacketReceiveEvent event) {
        // We don't know if the player is ticking stable, therefore we must wait until prediction
        // determines this.  Do nothing here!
    }

    @Override
    public void reload() {
        super.reload();
        clockDrift = (long) (getConfig().getDoubleElse(getConfigName() + ".drift", 1200.0) * 1e6);
    }
}
