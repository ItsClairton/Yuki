package dev.clairton.yuki.manager.init.stop;

import dev.clairton.yuki.manager.init.Initable;
import dev.clairton.yuki.utils.anticheat.LogUtil;
import com.github.retrooper.packetevents.PacketEvents;

public class TerminatePacketEvents implements Initable {
    @Override
    public void start() {
        LogUtil.info("Terminating PacketEvents...");
        PacketEvents.getAPI().terminate();
    }
}
