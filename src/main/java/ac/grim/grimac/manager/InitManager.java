package ac.grim.grimac.manager;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.GrimExternalAPI;
import ac.grim.grimac.manager.init.Initable;
import ac.grim.grimac.manager.init.load.CollisionLoader;
import ac.grim.grimac.manager.init.load.PacketEventsLoader;
import ac.grim.grimac.manager.init.start.*;
import ac.grim.grimac.manager.init.stop.TerminatePacketEvents;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;

public class InitManager {
    ClassToInstanceMap<Initable> initializersOnLoad;
    ClassToInstanceMap<Initable> initializersOnStart;
    ClassToInstanceMap<Initable> initializersOnStop;

    public InitManager() {
        initializersOnLoad = new ImmutableClassToInstanceMap.Builder<Initable>()
                .put(PacketEventsLoader.class, new PacketEventsLoader())
                .put(CollisionLoader.class, new CollisionLoader())
                .build();

        initializersOnStart = new ImmutableClassToInstanceMap.Builder<Initable>()
                .put(ExemptOnlinePlayers.class, new ExemptOnlinePlayers())
                .put(EventManager.class, new EventManager())
                .put(PacketManager.class, new PacketManager())
                .put(ViaBackwardsManager.class, new ViaBackwardsManager())
                .put(TickRunner.class, new TickRunner())
                .put(TickEndEvent.class, new TickEndEvent())
                .put(CommandRegister.class, new CommandRegister())
                .put(PacketLimiter.class, new PacketLimiter())
                .put(GrimExternalAPI.class, GrimAPI.INSTANCE.getExternalAPI())
                .put(ViaVersion.class, new ViaVersion())
                .build();

        initializersOnStop = new ImmutableClassToInstanceMap.Builder<Initable>()
                .put(TerminatePacketEvents.class, new TerminatePacketEvents())
                .build();
    }

    public void load() {
        for (Initable initable : initializersOnLoad.values()) {
            initable.start();
        }
    }

    public void start() {
        for (Initable initable : initializersOnStart.values()) {
            initable.start();
        }
    }

    public void stop() {
        for (Initable initable : initializersOnStop.values()) {
            initable.start();
        }
    }
}