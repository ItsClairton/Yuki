package dev.clairton.yuki.manager.tick.impl;

import dev.clairton.yuki.GrimAPI;
import dev.clairton.yuki.manager.tick.Tickable;
import dev.clairton.yuki.player.GrimPlayer;

public class ClientVersionSetter implements Tickable {
    @Override
    public void tick() {
        for (GrimPlayer player : GrimAPI.INSTANCE.getPlayerDataManager().getEntries()) {
            player.pollData();
        }
    }
}
