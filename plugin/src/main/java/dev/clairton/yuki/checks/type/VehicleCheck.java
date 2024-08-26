package dev.clairton.yuki.checks.type;

import ac.grim.grimac.api.AbstractCheck;
import dev.clairton.yuki.utils.anticheat.update.VehiclePositionUpdate;

public interface VehicleCheck extends AbstractCheck {

    void process(final VehiclePositionUpdate vehicleUpdate);
}
