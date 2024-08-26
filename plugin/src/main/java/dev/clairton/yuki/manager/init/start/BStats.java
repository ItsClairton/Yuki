package dev.clairton.yuki.manager.init.start;

import dev.clairton.yuki.Yuki;
import dev.clairton.yuki.manager.init.Initable;

public class BStats implements Initable {
    @Override
    public void start() {
        int pluginId = 12820; // <-- Replace with the id of your plugin!
        try {
            new io.github.retrooper.packetevents.bstats.Metrics(Yuki.getInstance().getPlugin(), pluginId);
        } catch (Exception ignored) {
        }
    }
}
