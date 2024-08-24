package dev.clairton.yuki.checks.impl.badpackets;

import dev.clairton.yuki.checks.Check;
import dev.clairton.yuki.checks.CheckData;
import dev.clairton.yuki.checks.type.PacketCheck;
import dev.clairton.yuki.player.GrimPlayer;

@CheckData(name = "BadPacketsW", experimental = true)
public class BadPacketsW extends Check implements PacketCheck {
    public BadPacketsW(GrimPlayer player) {
        super(player);
    }
}
