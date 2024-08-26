package dev.clairton.yuki.manager.init.start;

import dev.clairton.yuki.Yuki;
import dev.clairton.yuki.manager.init.Initable;
import dev.clairton.yuki.utils.anticheat.LogUtil;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import org.bukkit.Bukkit;

public class TickRunner implements Initable {
    @Override
    public void start() {
        LogUtil.info("Registering tick schedulers...");

        if (FoliaScheduler.isFolia()) {
            FoliaScheduler.getAsyncScheduler().runAtFixedRate(Yuki.getInstance().getPlugin(), (dummy) -> {
                Yuki.getInstance().getTickManager().tickSync();
                Yuki.getInstance().getTickManager().tickAsync();
            }, 1, 1);
        } else {
            Bukkit.getScheduler().runTaskTimer(
                Yuki.getInstance().getPlugin(), () -> Yuki.getInstance().getTickManager().tickSync(), 0, 1);
            Bukkit.getScheduler().runTaskTimerAsynchronously(
                Yuki.getInstance().getPlugin(), () -> Yuki.getInstance().getTickManager().tickAsync(), 0, 1);
        }
    }
}
