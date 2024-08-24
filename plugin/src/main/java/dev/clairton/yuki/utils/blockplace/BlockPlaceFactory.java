package dev.clairton.yuki.utils.blockplace;

import dev.clairton.yuki.player.GrimPlayer;
import dev.clairton.yuki.utils.anticheat.update.BlockPlace;

public interface BlockPlaceFactory {
    void applyBlockPlaceToWorld(GrimPlayer player, BlockPlace place);
}
