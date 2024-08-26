package dev.clairton.yuki.manager.init.load;

import dev.clairton.yuki.Yuki;
import dev.clairton.yuki.manager.init.Initable;
import dev.clairton.yuki.utils.anticheat.LogUtil;
import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;

public class PacketEventsInit implements Initable {
    @Override
    public void start() {
        LogUtil.info("Loading PacketEvents...");
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(Yuki.getInstance().getPlugin()));
        PacketEvents.getAPI().getSettings()
                .bStats(true)
                .fullStackTrace(true)
                .kickOnPacketException(true)
                .checkForUpdates(false)
                .debug(false);
        PacketEvents.getAPI().load();
    }
}
