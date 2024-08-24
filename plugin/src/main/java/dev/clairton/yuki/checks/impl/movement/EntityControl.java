package dev.clairton.yuki.checks.impl.movement;

import dev.clairton.yuki.checks.Check;
import dev.clairton.yuki.checks.CheckData;
import dev.clairton.yuki.checks.type.PostPredictionCheck;
import dev.clairton.yuki.player.GrimPlayer;

@CheckData(name = "Entity control", configName = "EntityControl")
public class EntityControl extends Check implements PostPredictionCheck {
    public EntityControl(GrimPlayer player) {
        super(player);
    }

    public void rewardPlayer() {
        reward();
    }
}
