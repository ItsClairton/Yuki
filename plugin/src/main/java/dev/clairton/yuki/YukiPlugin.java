package dev.clairton.yuki;

import org.bukkit.plugin.java.JavaPlugin;

public final class YukiPlugin extends JavaPlugin {

  private Yuki yuki;

  @Override
  public void onLoad() {
    this.yuki = new Yuki(this);
  }

  @Override
  public void onEnable() {
    this.yuki.start();
  }

  @Override
  public void onDisable() {
    this.yuki.stop();
  }

}
