package dev.clairton.yuki.manager.init.start;

import dev.clairton.yuki.Yuki;
import dev.clairton.yuki.events.bukkit.PistonEvent;
import dev.clairton.yuki.manager.init.Initable;
import dev.clairton.yuki.utils.anticheat.LogUtil;
import org.bukkit.Bukkit;

public class EventManager implements Initable {
    public void start() {
        LogUtil.info("Registering singular bukkit event... (PistonEvent)");

        Bukkit.getPluginManager().registerEvents(new PistonEvent(), Yuki.getInstance().getPlugin());
    }
}
