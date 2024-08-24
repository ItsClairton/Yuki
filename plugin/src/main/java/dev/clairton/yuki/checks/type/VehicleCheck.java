package dev.clairton.yuki.checks.type;

import dev.clairton.yuki.api.AbstractCheck;
import dev.clairton.yuki.utils.anticheat.update.VehiclePositionUpdate;

public interface VehicleCheck extends AbstractCheck {

    void process(final VehiclePositionUpdate vehicleUpdate);
}
