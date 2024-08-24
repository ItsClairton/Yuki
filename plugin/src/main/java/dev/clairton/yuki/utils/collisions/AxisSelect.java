package dev.clairton.yuki.utils.collisions;

import dev.clairton.yuki.utils.collisions.datatypes.SimpleCollisionBox;

public interface AxisSelect {
    SimpleCollisionBox modify(SimpleCollisionBox box);
}