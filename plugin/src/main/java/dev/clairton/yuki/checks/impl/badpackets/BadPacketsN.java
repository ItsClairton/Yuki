package dev.clairton.yuki.checks.impl.badpackets;

import dev.clairton.yuki.checks.Check;
import dev.clairton.yuki.checks.CheckData;
import dev.clairton.yuki.checks.type.PacketCheck;
import dev.clairton.yuki.player.GrimPlayer;

@CheckData(name = "BadPacketsN")
public class BadPacketsN extends Check implements PacketCheck {
    public BadPacketsN(final GrimPlayer player) {
        super(player);
    }
}
