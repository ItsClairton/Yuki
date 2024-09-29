package ac.grim.grimac.manager.init.load;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.manager.init.Initable;
import ac.grim.grimac.utils.anticheat.LogUtil;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.PEVersions;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;

public class PacketEventsInit implements Initable {

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void start() {
        LogUtil.info("Loading PacketEvents v" + PEVersions.RAW + "...");

        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(GrimAPI.INSTANCE.getPlugin()));
        PacketEvents.getAPI().getSettings()
                .fullStackTrace(true)
                .kickOnPacketException(true)
                .checkForUpdates(false)
                .debug(false);

        PacketEvents.getAPI().load();
    }

}
