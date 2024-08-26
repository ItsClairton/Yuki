package dev.clairton.yuki.utils.anticheat;

import dev.clairton.yuki.Yuki;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.logging.Logger;

@UtilityClass
public class LogUtil {
    public void info(final String info) {
        getLogger().info(info);
    }

    public void warn(final String warn) {
        getLogger().warning(warn);
    }

    public void error(final String error) {
        getLogger().severe(error);
    }

    public Logger getLogger() {
        return Yuki.getInstance().getPlugin().getLogger();
    }

    public void console(final String info) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', info));
    }

}
