package dev.clairton.yuki.manager.tick.impl;

import dev.clairton.yuki.Yuki;
import dev.clairton.yuki.manager.tick.Tickable;
import dev.clairton.yuki.player.GrimPlayer;

public class TickInventory implements Tickable {
    @Override
    public void tick() {
        for (GrimPlayer player : Yuki.getInstance().getPlayerDataManager().getEntries()) {
            player.getInventory().inventory.getInventoryStorage().tickWithBukkit();
        }
    }
}
