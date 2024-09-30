package ac.grim.grimac.utils.latency;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

public class CompensatedFireworks extends Check implements PostPredictionCheck {

    // As this is sync to one player, this does not have to be concurrent
    private final IntSet activeFireworks = new IntOpenHashSet();
    private final IntSet fireworksToRemoveNextTick = new IntOpenHashSet();

    public CompensatedFireworks(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPredictionComplete(final PredictionComplete predictionComplete) {
        // Remove all the fireworks that were removed in the last tick
        // Remember to remove with an int not an Integer
        activeFireworks.removeAll(fireworksToRemoveNextTick);
        fireworksToRemoveNextTick.clear();
    }

    public boolean hasFirework(int entityId) {
        return activeFireworks.contains(entityId);
    }

    public void addNewFirework(int entityID) {
        activeFireworks.add(entityID);
    }

    public void removeFirework(int entityID) {
        fireworksToRemoveNextTick.add(entityID);
    }

    public int getMaxFireworksAppliedPossible() {
        return activeFireworks.size();
    }
}
