package dev.clairton.yuki.checks.impl.aim;

import dev.clairton.yuki.checks.Check;
import dev.clairton.yuki.checks.CheckData;
import dev.clairton.yuki.checks.type.RotationCheck;
import dev.clairton.yuki.player.GrimPlayer;
import dev.clairton.yuki.utils.anticheat.update.RotationUpdate;

@CheckData(name = "AimDuplicateLook")
public class AimDuplicateLook extends Check implements RotationCheck {
    public AimDuplicateLook(GrimPlayer playerData) {
        super(playerData);
    }

    boolean exempt = false;

    @Override
    public void process(final RotationUpdate rotationUpdate) {
        if (player.packetStateData.lastPacketWasTeleport || player.packetStateData.lastPacketWasOnePointSeventeenDuplicate || player.compensatedEntities.getSelf().getRiding() != null) {
            exempt = true;
            return;
        }

        if (exempt) { // Exempt for a tick on teleport
            exempt = false;
            return;
        }

        if (rotationUpdate.getFrom().equals(rotationUpdate.getTo())) {
            flagAndAlert();
        }
    }
}
