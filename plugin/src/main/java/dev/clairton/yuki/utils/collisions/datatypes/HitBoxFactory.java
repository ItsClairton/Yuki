package dev.clairton.yuki.utils.collisions.datatypes;

import dev.clairton.yuki.player.GrimPlayer;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;

public interface HitBoxFactory {
    CollisionBox fetch(GrimPlayer player, StateType heldItem, ClientVersion version, WrappedBlockState block, int x, int y, int z);
}
