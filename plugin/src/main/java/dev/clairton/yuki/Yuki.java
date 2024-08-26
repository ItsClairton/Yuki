package dev.clairton.yuki;

import ac.grim.grimac.api.GrimAbstractAPI;
import dev.clairton.yuki.manager.AlertManagerImpl;
import dev.clairton.yuki.manager.ConfigManager;
import dev.clairton.yuki.manager.DiscordManager;
import dev.clairton.yuki.manager.InitManager;
import dev.clairton.yuki.manager.SpectateManager;
import dev.clairton.yuki.manager.TickManager;
import dev.clairton.yuki.utils.anticheat.PlayerDataManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Yuki {

  private static @Getter Yuki instance;

  private final AlertManagerImpl alertManager = new AlertManagerImpl();
  private final SpectateManager spectateManager = new SpectateManager();
  private final DiscordManager discordManager = new DiscordManager();
  private final PlayerDataManager playerDataManager = new PlayerDataManager();
  private final TickManager tickManager = new TickManager();
  private final InitManager initManager = new InitManager();
  private final ConfigManager configManager = new ConfigManager();

  private final GrimExternalAPI externalAPI = new GrimExternalAPI(this);

  private JavaPlugin plugin;

  public Yuki(final JavaPlugin plugin) {
    instance = this;

    this.plugin = plugin;
    initManager.load();
  }

  public void start() {
    initManager.start();

    Bukkit.getServicesManager().register(GrimAbstractAPI.class,
        externalAPI,
        plugin,
        ServicePriority.Normal);
  }

  public void stop() {
    initManager.stop();
  }

}
