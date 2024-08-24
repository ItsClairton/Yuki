package dev.clairton.yuki.checks.impl.movement;

import dev.clairton.yuki.checks.Check;
import dev.clairton.yuki.checks.type.VehicleCheck;
import dev.clairton.yuki.player.GrimPlayer;
import dev.clairton.yuki.utils.anticheat.update.PositionUpdate;
import dev.clairton.yuki.utils.anticheat.update.VehiclePositionUpdate;

public class VehiclePredictionRunner extends Check implements VehicleCheck {
    public VehiclePredictionRunner(GrimPlayer playerData) {
        super(playerData);
    }

    @Override
    public void process(final VehiclePositionUpdate vehicleUpdate) {
        // Vehicle onGround = false always
        // We don't do vehicle setbacks because vehicle netcode sucks.
        player.movementCheckRunner.processAndCheckMovementPacket(new PositionUpdate(vehicleUpdate.getFrom(), vehicleUpdate.getTo(), false, null, null, vehicleUpdate.isTeleport()));
    }
}
